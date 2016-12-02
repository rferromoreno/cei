package estructuras.ast.encadenados;

import java.util.HashMap;
import java.util.LinkedList;

import estructuras.Token;
import estructuras.ast.expresiones.NodoExpresion;
import estructuras.ts.Metodo;
import estructuras.ts.TablaSimbolos;
import estructuras.ts.tipos.TipoMetodo;
import estructuras.ts.variables.VarParametro;
import excepciones.semanticas.ExcepcionMetodoInexistente;
import excepciones.semanticas.ExcepcionParametroNoConforma;
import excepciones.semanticas.ExcepcionParametrosNoCoinciden;
import excepciones.semanticas.ExcepcionReceptorPrimitivo;
import excepciones.semanticas.ExcepcionReceptorVoid;
import excepciones.semanticas.ExcepcionSemantica;
import modulos.GenCod;

public class NodoLlamadaEncadenada extends NodoEncadenado {

	private LinkedList<NodoExpresion> paramActuales;
	
	public NodoLlamadaEncadenada(Token id, NodoEncadenado cad) {
		super(id,cad);
		paramActuales = new LinkedList<NodoExpresion>();
	}
	
	public NodoLlamadaEncadenada(Token id, NodoEncadenado cad, LinkedList<NodoExpresion> listaParam) {
		this(id,cad);
		paramActuales = listaParam;
	}
	
	public NodoLlamadaEncadenada(Token id, LinkedList<NodoExpresion> listaParam) {
		super(id,null);
		paramActuales = listaParam;
	}
	
	public LinkedList<NodoExpresion> getParamActuales() {
		return paramActuales;
	}

	public void setParamActuales(LinkedList<NodoExpresion> paramActuales) {
		this.paramActuales = paramActuales;
	}

	public boolean terminaEnVar() {
		if (this.getCadena()==null)
			return false;
		else return this.getCadena().terminaEnVar();
	}

	public boolean terminaEnLlamada() {
		if (this.getCadena()==null)
			return true;
		else return this.getCadena().terminaEnLlamada();
	}

	@Override
	public TipoMetodo chequear(TipoMetodo receptor) throws ExcepcionSemantica {
		if (receptor.esTipoPrimitivo())
			throw new ExcepcionReceptorPrimitivo(this.getToken().getLinea());
		else if (receptor.esVoid())
			throw new ExcepcionReceptorVoid(this.getToken().getLinea());
		else {
			String claseReceptora = receptor.getNombreClase();
			String metodoActual = this.getToken().getLexema();
			HashMap<String, Metodo> tablaMetodos = TablaSimbolos.clases.get(claseReceptora).getMetodos();
			if (!tablaMetodos.containsKey(metodoActual))
				throw new ExcepcionMetodoInexistente(this.getToken().getLinea(),metodoActual,claseReceptora);
			else {
				Metodo metLlamado = tablaMetodos.get(metodoActual);
				TipoMetodo rec = metLlamado.getTipoRetorno();
				
				if (!rec.esVoid()) { // tiene valor de retorno
					GenCod.gen("RMEM 1","Estoy por hacer llamada encadenada, reservo para el valor de retorno");
					GenCod.gen("SWAP","Bajo el this");
				}
				
				LinkedList<VarParametro> formalArgs = metLlamado.getListaParametros();
				if (formalArgs.size()!=paramActuales.size())
					throw new ExcepcionParametrosNoCoinciden(this.getToken().getLinea());
				VarParametro argForm = null;
				NodoExpresion expr = null;
				TipoMetodo tipoExpr = null;
				for (int i=0; i<paramActuales.size();i++) {
					argForm = formalArgs.get(i);
					expr = paramActuales.get(i);
					tipoExpr = expr.chequear();
					if (!metLlamado.getEsEstatico())
						GenCod.gen("SWAP","Intercambio el this con lo evaluado por el parametro actual");
					if (!tipoExpr.conforma(argForm.getTipoVar()))
						throw new ExcepcionParametroNoConforma(metodoActual,this.getToken().getLinea());
				}
				
				// Accedo a VT
				GenCod.gen("DUP","Duplico this");
				GenCod.gen("LOADREF 0");
				GenCod.gen("LOADREF "+metLlamado.getOffset(),"Cargo el offset del metodo "+metLlamado.getToken().getLexema());
				GenCod.gen("CALL");
				
				if (this.getCadena()!=null) {
					this.getCadena().setEsLadoIzq(this.getEsLadoIzq());
					return this.getCadena().chequear(rec);
				}
				else
					return rec;
			
			}		
		}
	}

}
