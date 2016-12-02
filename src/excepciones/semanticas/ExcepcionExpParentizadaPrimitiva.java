package excepciones.semanticas;

@SuppressWarnings("serial")
public class ExcepcionExpParentizadaPrimitiva extends ExcepcionSemantica {

	public ExcepcionExpParentizadaPrimitiva(int linea) {
		super(linea);
		System.out.println("[Error Semantico] La expresion parentizada no puede ser de un tipo primitivo.");
	}


}
