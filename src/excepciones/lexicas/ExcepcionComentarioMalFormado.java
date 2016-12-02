package excepciones.lexicas;

@SuppressWarnings("serial")
public class ExcepcionComentarioMalFormado extends ExcepcionLexica {

	public ExcepcionComentarioMalFormado (int linea) {
		super("[Error Lexico] Comentario mal formado en la linea "+linea);
	}
	
}
