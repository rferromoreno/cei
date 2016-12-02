package excepciones.lexicas;

@SuppressWarnings("serial")
public class ExcepcionLexica extends Exception {
	
	public ExcepcionLexica (int linea) {
		System.out.println("[Error Lexico] Error en la linea "+linea);
	}
	
	public ExcepcionLexica (String msg) {
		System.out.println(msg);
	}

}
