package excepciones.lexicas;

@SuppressWarnings("serial")
public class ExcepcionCharMalFormado extends ExcepcionLexica {

	public ExcepcionCharMalFormado (int linea) {
		super("[Error Lexico] Literal caracter mal formado en la linea "+linea);
	}
}
