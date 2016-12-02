package excepciones.lexicas;

@SuppressWarnings("serial")
public class ExcepcionCharInvalido extends ExcepcionLexica {

	public ExcepcionCharInvalido(int linea,char chr) {
		super("[Error Lexico] Caracter invalido '"+chr+"' encontrado en la linea "+linea);
	}
	
}
