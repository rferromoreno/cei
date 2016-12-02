package estructuras.ast.expresiones;

import java.util.HashMap;
import java.util.LinkedList;

import estructuras.Token;
import estructuras.ast.encadenados.NodoEncadenado;
import estructuras.ts.Metodo;
import estructuras.ts.TablaSimbolos;
import estructuras.ts.tipos.TipoMetodo;
import estructuras.ts.variables.VarParametro;
import excepciones.semanticas.ExcepcionMetodoInexistente;
import excepciones.semanticas.ExcepcionParametroNoConforma;
import excepciones.semanticas.ExcepcionParametrosNoCoinciden;
import excepciones.semanticas.ExcepcionSemantica;
import excepciones.semanticas.ExcepcionSemanticaPersonalizada;
import modulos.GenCod;

public class NodoLlamadaDirecta extends NodoPrimario {

	private Token identificador;
	private LinkedList<NodoExpresion> actualArgs;
	
	public NodoLlamadaDirecta(Token id, LinkedList<NodoExpresion> listaParams, NodoEncadenado cad) {
		super(cad);
		identificador = id;
		actualArgs = listaParams;
	}

	public Token getToken() {
		return identificador;
	}

	public void setIdentificador(Token identificador) {
		this.identificador = identificador;
	}

	public LinkedList<NodoExpresion> getActualArgs() {
		return actualArgs;
	}

	public void setActualArgs(LinkedList<NodoExpresion> actualArgs) {
		this.actualArgs = actualArgs;
	}
	
	
	@Override
	public TipoMetodo chequear() throws ExcepcionSemantica {
		HashMap<String, Metodo> tablaMetodos = TablaSimbolos.claseActual.getMetodos();
		if (!tablaMetodos.containsKey(identificador.getLexema()))
			throw new ExcepcionMetodoInexistente(identificador.getLinea(),identificador.getLexema(),TablaSimbolos.claseActual.getNombreClase());
		else {
			Metodo metLlamado = tablaMetodos.get(identificador.getLexema());
			if (!TablaSimbolos.metodoActual.esConstructor()) {
				Metodo metActual = (Metodo) TablaSimbolos.metodoActual;
				if (metActual.getEsEstatico() && !metLlamado.getEsEstatico()) 
					throw new ExcepcionSemanticaPersonalizada(identificador.getLinea(),"El metodo '"+identificador.getLexema()+"' no es estatico y esta siendo invocado desde un metodo estatico.");
			}
			
			TipoMetodo receptor = metLlamado.getTipoRetorno();
			if (!metLlamado.getEsEstatico())
				GenCod.gen("LOAD 3","Apilo 'this'");
			if (!receptor.esVoid())  // tiene valor de retorno
				GenCod.gen("RMEM 1","Reservo lugar para el valor de retorno");
			if (!metLlamado.getEsEstatico() && !receptor.esVoid())
				GenCod.gen("SWAP","Bajo el this, subo el lugar que reserve (si el metodo no es void)");
			
			LinkedList<VarParametro> formalArgs = metLlamado.getListaParametros();
			if (formalArgs.size()!=actualArgs.size())
				throw new ExcepcionParametrosNoCoinciden(identificador.getLinea());
			VarParametro argForm = null;
			NodoExpresion expr = null;
			TipoMetodo tipoExpr = null;
			for (int i=0; i<actualArgs.size();i++) {
				argForm = formalArgs.get(i);
				expr = actualArgs.get(i);
				tipoExpr = expr.chequear();
				if (!metLlamado.getEsEstatico())
					GenCod.gen("SWAP","Intercambio el this con lo evaluado por el parametro actual (Llamada directa)");
				if (!tipoExpr.conforma(argForm.getTipoVar()))
					throw new ExcepcionParametroNoConforma(identificador.getLexema(),identificador.getLinea());
			}
			
			if (!metLlamado.getEsEstatico()) {
				// Accedo a VT
				GenCod.gen("DUP","Duplico this");
				GenCod.gen("LOADREF 0");
				GenCod.gen("LOADREF "+metLlamado.getOffset(),"Cargo el offset del metodo "+metLlamado.getToken().getLexema());
				GenCod.gen("CALL");
			} else {
				GenCod.gen("PUSH "+metLlamado.getLabel());
				GenCod.gen("CALL");
			}
			if (this.getCadena()!=null) {
				this.getCadena().setEsLadoIzq(this.getEsLadoIzq());
				return this.getCadena().chequear(receptor);
			} else
				return receptor;
		}
	}

	@Override
	public boolean terminaEnVar() {
		if (this.getCadena()==null)
			return false;
		else return this.getCadena().terminaEnVar();
	}

	@Override
	public boolean terminaEnLlamada() {
		if (this.getCadena()==null)
			return true;
		else return this.getCadena().terminaEnLlamada();
	}

}
