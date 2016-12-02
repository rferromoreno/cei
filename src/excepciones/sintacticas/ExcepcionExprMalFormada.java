package excepciones.sintacticas;

@SuppressWarnings("serial")
public class ExcepcionExprMalFormada extends ExcepcionSintactica {

	public ExcepcionExprMalFormada (int linea) {
		super("[Error Sintactico] Expresion mal formada en la linea "+linea);
	}
	
}
