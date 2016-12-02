package estructuras.ast.encadenados;

import java.util.HashMap;

import estructuras.Token;
import estructuras.ts.TablaSimbolos;
import estructuras.ts.tipos.Tipo;
import estructuras.ts.tipos.TipoMetodo;
import estructuras.ts.variables.VarInstancia;
import excepciones.semanticas.ExcepcionAtributoInexistente;
import excepciones.semanticas.ExcepcionAtributoNoVisible;
import excepciones.semanticas.ExcepcionReceptorPrimitivo;
import excepciones.semanticas.ExcepcionReceptorVoid;
import excepciones.semanticas.ExcepcionSemantica;
import modulos.GenCod;

public class NodoIdEncadenado extends NodoEncadenado {
	
	public NodoIdEncadenado(Token id, NodoEncadenado cad) {
		super(id,cad);
	}
	
	public NodoIdEncadenado(Token id) {
		this(id,null);
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

	@Override
	public TipoMetodo chequear(TipoMetodo receptor) throws ExcepcionSemantica {
		if (receptor.esTipoPrimitivo())
			throw new ExcepcionReceptorPrimitivo(this.getToken().getLinea());
		else if (receptor.esVoid())
			throw new ExcepcionReceptorVoid(this.getToken().getLinea());
		else {
			HashMap<String, VarInstancia> tablaAtributos = TablaSimbolos.clases.get(receptor.getNombreClase()).getAtributos();
			if (!tablaAtributos.containsKey(this.getToken().getLexema()))
				throw new ExcepcionAtributoInexistente(this.getToken().getLexema(),this.getToken().getLinea());
			else {
				VarInstancia vi = tablaAtributos.get(this.getToken().getLexema());
				if (!vi.getEsPublico())
					throw new ExcepcionAtributoNoVisible(this.getToken().getLexema(),this.getToken().getLinea());				
				
				Tipo tipo = vi.getTipoVar();
				if (this.getEsLadoIzq() && this.getCadena()==null) {
					GenCod.gen("SWAP");
					GenCod.gen("STOREREF "+vi.getOffset());
				} else {
					GenCod.gen("LOADREF "+vi.getOffset());
				}
				
				if (this.getCadena()!=null) {
					this.getCadena().setEsLadoIzq(this.getEsLadoIzq());
					return this.getCadena().chequear(tipo); 
				}
				else
					return tipo;
			}		
		}
	}

}
