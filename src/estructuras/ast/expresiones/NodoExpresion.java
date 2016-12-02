package estructuras.ast.expresiones;


import estructuras.ts.tipos.TipoMetodo;
import excepciones.semanticas.ExcepcionSemantica;

public abstract class NodoExpresion {

	public abstract TipoMetodo chequear() throws ExcepcionSemantica;
	
}
