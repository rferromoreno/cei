package estructuras.ast.expresiones;

import java.util.LinkedList;

import estructuras.Token;
import estructuras.ast.encadenados.NodoEncadenado;
import estructuras.ts.Clase;
import estructuras.ts.Metodo;
import estructuras.ts.TablaSimbolos;
import estructuras.ts.tipos.TipoMetodo;
import estructuras.ts.variables.VarParametro;
import excepciones.semanticas.ExcepcionClaseNoDeclarada;
import excepciones.semanticas.ExcepcionMetodoInexistente;
import excepciones.semanticas.ExcepcionParametroNoConforma;
import excepciones.semanticas.ExcepcionParametrosNoCoinciden;
import excepciones.semanticas.ExcepcionSemantica;
import excepciones.semanticas.ExcepcionSemanticaPersonalizada;
import modulos.GenCod;

public class NodoLlamadaEstatica extends NodoPrimario {

	private Token idClase;
	private Token identificador;
	private LinkedList<NodoExpresion> actualArgs;
	
	public NodoLlamadaEstatica(Token id, Token idclase, LinkedList<NodoExpresion> listaParams, NodoEncadenado cad) {
		super(cad);
		identificador = id;
		actualArgs = listaParams;
		this.idClase = idclase;
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
	
	public Token getIdClase() {
		return idClase;
	}

	public void setIdClase(Token idClase) {
		this.idClase = idClase;
	}

	@Override
	public TipoMetodo chequear() throws ExcepcionSemantica {	
		if (!TablaSimbolos.clases.containsKey(idClase.getLexema()))
			throw new ExcepcionClaseNoDeclarada(idClase.getLexema(),identificador.getLinea());
		else {
			Clase cla = TablaSimbolos.clases.get(idClase.getLexema());
			if (!cla.getMetodos().containsKey(identificador.getLexema())) 
				throw new ExcepcionMetodoInexistente(identificador.getLinea(),identificador.getLexema(),idClase.getLexema());
			else { 
				Metodo met = cla.getMetodos().get(identificador.getLexema());
				if (!met.getEsEstatico())
					throw new ExcepcionSemanticaPersonalizada(identificador.getLinea(),"El metodo '"+identificador.getLexema()+"' debe ser un metodo estatico.");
				
				TipoMetodo receptor = met.getTipoRetorno();		
				if (!receptor.esVoid()) 
					GenCod.gen("RMEM 1","Voy a llamar a un metodo estatico (no void), reservo lugar para lo que devuelva");
				
				LinkedList<VarParametro> formalArgs = met.getListaParametros();
				if (formalArgs.size()!=actualArgs.size())
					throw new ExcepcionParametrosNoCoinciden(identificador.getLinea());
				VarParametro argForm = null;
				NodoExpresion expr = null;
				TipoMetodo tipoExpr = null;
				for (int i=0; i<actualArgs.size();i++) {
					argForm = formalArgs.get(i);
					expr = actualArgs.get(i);
					tipoExpr = expr.chequear();
					//SWAP?
					if (!tipoExpr.conforma(argForm.getTipoVar()))
						throw new ExcepcionParametroNoConforma(identificador.getLexema(),identificador.getLinea());
				}
				
				GenCod.gen("PUSH "+met.getLabel(),"Preparo para llamar al metodo "+idClase.getLexema()+"."+identificador.getLexema());
				GenCod.gen("CALL");			
				if (this.getCadena()!=null) {
					this.getCadena().setEsLadoIzq(this.getEsLadoIzq());
					return this.getCadena().chequear(receptor);
				} else
					return receptor;	
			}
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
