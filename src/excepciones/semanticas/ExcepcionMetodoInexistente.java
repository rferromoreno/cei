package excepciones.semanticas;

@SuppressWarnings("serial")
public class ExcepcionMetodoInexistente extends ExcepcionSemantica {

	public ExcepcionMetodoInexistente(int linea, String metodoActual, String claseReceptora) {
		super(linea);
		System.out.println("[Error Semantico] El metodo '"+metodoActual+"' no existe para la clase '"+claseReceptora+"'.");
	}


}
