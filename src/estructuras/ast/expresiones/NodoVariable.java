package estructuras.ast.expresiones;

import java.util.HashMap;

import estructuras.Token;
import estructuras.ast.encadenados.NodoEncadenado;
import estructuras.ts.Metodo;
import estructuras.ts.TablaSimbolos;
import estructuras.ts.tipos.Tipo;
import estructuras.ts.tipos.TipoMetodo;
import estructuras.ts.variables.VarInstancia;
import estructuras.ts.variables.Variable;
import excepciones.semanticas.ExcepcionSemantica;
import excepciones.semanticas.ExcepcionSemanticaPersonalizada;
import modulos.GenCod;

public class NodoVariable extends NodoPrimario {
	
	private Token identificador;
	
	public NodoVariable(Token t) {
		identificador = t;
	}
	
	public NodoVariable(Token t, NodoEncadenado cad) {
		super(cad);
		identificador = t;
	}	
	
	public Token getToken() {
		return identificador;
	}

	public void setIdentificador(Token identificador) {
		this.identificador = identificador;
	}

	@Override
	public TipoMetodo chequear() throws ExcepcionSemantica {
		HashMap<String, VarInstancia> atributosInstancia = TablaSimbolos.claseActual.getAtributos();
		HashMap<String, Variable> atributosLocales = TablaSimbolos.metodoActual.getTablaVariables();
		String idVar = identificador.getLexema();
		Variable var = null;
		boolean esVarInstancia = false;
		if (atributosLocales.containsKey(idVar)) {
			var = atributosLocales.get(idVar);
		} else {
			if (!atributosInstancia.containsKey(idVar))
				throw new ExcepcionSemanticaPersonalizada(identificador.getLinea(),"La variable '"+idVar+"' no existe.");
			else {
				if (!TablaSimbolos.metodoActual.esConstructor()) {
					Metodo met = (Metodo)TablaSimbolos.metodoActual;
					if (met.getEsEstatico())
						throw new ExcepcionSemanticaPersonalizada(identificador.getLinea(),"No se puede usar la variable de instancia '"+idVar+"' dentro de un metodo estatico.");
				}
				var = atributosInstancia.get(idVar);
				esVarInstancia = true;
			}
		}
		if (esVarInstancia) {
			if (this.getEsLadoIzq()) {
				if (this.getCadena()!=null) {
					GenCod.gen("LOAD 3","Cargo referencia al 'this'");
					GenCod.gen("LOADREF "+var.getOffset(),"Cargo variable de instancia "+var.getToken().getLexema());
				} else {
					GenCod.gen("LOAD 3","Cargo referencia al 'this'");
					GenCod.gen("SWAP","Cambio valor del this por el valor a guardar");
					GenCod.gen("STOREREF "+var.getOffset(),"Actualizo el valor de "+var.getToken().getLexema());
				}
			} else { 	// es lado der
				GenCod.gen("LOAD 3","Cargo referencia al 'this'");
				GenCod.gen("LOADREF "+var.getOffset(),"Cargo variable de instancia "+var.getOffset());
			}	
		} else {	// es param o var local
			if (this.getEsLadoIzq()) {
				if (this.getCadena()!=null) {
					GenCod.gen("LOAD "+var.getOffset(),"Cargo parametro/var local "+var.getToken().getLexema());
				} else {
					GenCod.gen("STORE "+var.getOffset(),"Guardo parametro/var local "+var.getToken().getLexema());
				}
			} else {	// es lado der
				GenCod.gen("LOAD "+var.getOffset(),"Cargo parametro/var local "+var.getToken().getLexema());
			}
		}
		Tipo tipo = var.getTipoVar();
		if (this.getCadena()!=null) {
			this.getCadena().setEsLadoIzq(this.getEsLadoIzq());
			return this.getCadena().chequear(tipo);
		} else return tipo;
	}

	public boolean terminaEnVar() {
		if (this.getCadena()==null)
			return true;
		else return this.getCadena().terminaEnVar();
	}

	public boolean terminaEnLlamada() {
		if (this.getCadena()==null)
			return false;
		else return this.getCadena().terminaEnLlamada();
	}

}
