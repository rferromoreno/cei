package estructuras.ast.sentencias;

import estructuras.Token;
import estructuras.ast.expresiones.NodoExpresion;
import estructuras.ts.Metodo;
import estructuras.ts.TablaSimbolos;
import estructuras.ts.tipos.TipoMetodo;
import excepciones.semanticas.ExcepcionSemantica;
import excepciones.semanticas.ExcepcionSemanticaPersonalizada;
import modulos.GenCod;

public class NodoReturn extends NodoSentencia {
	
	private Token identificador;
	private NodoExpresion expresion;
	
	public NodoReturn(Token id, NodoExpresion expr) {
		identificador = id;
		expresion = expr;
	}

	public NodoExpresion getExpresion() {
		return expresion;
	}

	public void setExpresion(NodoExpresion expresion) {
		this.expresion = expresion;
	}

	@Override
	public void chequear() throws ExcepcionSemantica {
		if (TablaSimbolos.metodoActual.esConstructor()) 
			throw new ExcepcionSemanticaPersonalizada(identificador.getLinea(),"El constructor no puede contener un return");
		Metodo m = (Metodo)TablaSimbolos.metodoActual;
		int cantParam = m.getListaParametros().size();
		int cantVarLocales = m.getTablaVariables().size() - cantParam;
		int dirRetorno = cantParam+3;
		if (!m.getEsEstatico()) 
			dirRetorno++;
		if (expresion==null) {	
			// es return ;
			TipoMetodo ret = m.getTipoRetorno();
			if (!ret.esVoid()) 
				throw new ExcepcionSemanticaPersonalizada(identificador.getLinea(),"El metodo no puede contener un return vacio");			
		} else {				
			// es return <expr>;
			TipoMetodo te = expresion.chequear();
			TipoMetodo ret = m.getTipoRetorno();
			if (ret.esVoid()) {
				throw new ExcepcionSemanticaPersonalizada(identificador.getLinea(),"El metodo es void y no puede contener un return con una expresion");
			} else {
				if (!te.conforma(ret)) {
					throw new ExcepcionSemanticaPersonalizada(identificador.getLinea(),"El tipo de retorno de la expresion no conforma con el tipo de retorno del metodo");
				}
			}
				GenCod.gen("STORE "+dirRetorno);
		}
		GenCod.gen("FMEM "+cantVarLocales,"Libero "+cantVarLocales+" variables locales");
		GenCod.gen("STOREFP");
		GenCod.gen("RET "+(dirRetorno-3));
		//TablaSimbolos.imprimirEstado();
	}

}
