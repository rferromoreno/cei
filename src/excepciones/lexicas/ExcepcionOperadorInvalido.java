package excepciones.lexicas;

@SuppressWarnings("serial")
public class ExcepcionOperadorInvalido extends ExcepcionLexica {

	public ExcepcionOperadorInvalido(int linea, char chr) {
		super("[Error Lexico] Operador invalido '"+chr+"' encontrado en la linea "+linea);
	}
	
}
