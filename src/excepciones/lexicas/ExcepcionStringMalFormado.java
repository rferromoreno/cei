package excepciones.lexicas;

@SuppressWarnings("serial")
public class ExcepcionStringMalFormado extends ExcepcionLexica {

	public ExcepcionStringMalFormado (int linea) {
		super("[Error Lexico] Literal String mal formado en la linea "+linea);
	}
	
}
