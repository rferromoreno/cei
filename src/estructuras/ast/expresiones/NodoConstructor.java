package estructuras.ast.expresiones;

import java.util.LinkedList;

import estructuras.Token;
import estructuras.ast.encadenados.NodoEncadenado;
import estructuras.ts.Clase;
import estructuras.ts.Constructor;
import estructuras.ts.TablaSimbolos;
import estructuras.ts.tipos.Tipo;
import estructuras.ts.tipos.TipoClase;
import estructuras.ts.tipos.TipoMetodo;
import estructuras.ts.variables.VarParametro;
import excepciones.semanticas.ExcepcionClaseNoDeclarada;
import excepciones.semanticas.ExcepcionParametroNoConforma;
import excepciones.semanticas.ExcepcionParametrosNoCoinciden;
import excepciones.semanticas.ExcepcionSemantica;
import modulos.GenCod;

public class NodoConstructor extends NodoPrimario {

	private Token identificador;
	private LinkedList<NodoExpresion> actualArgs;
	
	public NodoConstructor(Token id, LinkedList<NodoExpresion> listaParams, NodoEncadenado cad) {
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
		if (!TablaSimbolos.clases.containsKey(identificador.getLexema()))
			throw new ExcepcionClaseNoDeclarada(identificador.getLexema(), identificador.getLinea());
		else {
			Clase clase = TablaSimbolos.clases.get(identificador.getLexema());
			Constructor ctor = clase.getConstructor();
			
			GenCod.gen("RMEM 1","Reservo espacio para la referencia al objeto del Ctor");
			GenCod.gen("PUSH "+(clase.getCantAtrib()+2),"Apilo el tamanio del CIR");
			GenCod.gen("PUSH simple_malloc","Preparo llamada a malloc");
			GenCod.gen("CALL","Invoco al malloc");
			// Hasta aca ya cree el CIR segun la cantidad de atrib +2 (ref a vt e id de clase)
			GenCod.gen("DUP","Duplico la referencia al CIR");
			GenCod.gen("PUSH VT_"+clase.getLabel(),"Guardo la etiqueta de la VT en el CIR");
			GenCod.gen("STOREREF 0");
			GenCod.gen("DUP");
			GenCod.gen("PUSH "+clase.getId(),"Guardo el ID de la clase en el CIR");
			GenCod.gen("STOREREF 1");
			GenCod.gen("DUP");
			
			LinkedList<VarParametro> formalArgs = ctor.getListaParametros();
			if (formalArgs.size()!=actualArgs.size())
				throw new ExcepcionParametrosNoCoinciden(identificador.getLinea());
			VarParametro argForm = null;
			NodoExpresion expr = null;
			TipoMetodo tipoExpr = null;
			for (int i=0; i<actualArgs.size();i++) {
				argForm = formalArgs.get(i);
				expr = actualArgs.get(i);
				tipoExpr = expr.chequear();
				GenCod.gen("SWAP","Intercambio this por expresion");	// Los Ctores los considero dinamicos.
				if (!tipoExpr.conforma(argForm.getTipoVar()))
					throw new ExcepcionParametroNoConforma(ctor.getToken().getLexema(),identificador.getLinea());
			}
			Tipo receptor = new TipoClase(identificador);
			
			GenCod.gen("PUSH "+ctor.getLabel(),"Llamo al constructor (estoy en un new)");
			GenCod.gen("CALL");
			
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
