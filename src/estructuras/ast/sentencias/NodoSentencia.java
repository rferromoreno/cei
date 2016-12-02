package estructuras.ast.sentencias;

import excepciones.semanticas.ExcepcionSemantica;

public abstract class NodoSentencia {
	
	public abstract void chequear() throws ExcepcionSemantica;

}
