package excepciones.semanticas;

@SuppressWarnings("serial")
public class ExcepcionReceptorVoid extends ExcepcionSemantica {

	public ExcepcionReceptorVoid(int linea) {
		super(linea);
		System.out.println("[Error Semantico] El receptor de la llamada debe ser un tipo clase (y no tipo void)");
	}


}
