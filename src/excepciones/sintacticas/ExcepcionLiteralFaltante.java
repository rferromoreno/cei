package excepciones.sintacticas;

@SuppressWarnings("serial")
public class ExcepcionLiteralFaltante extends ExcepcionSintactica {

	public ExcepcionLiteralFaltante (int linea) {
		super("[Error Sintactico] Expresion mal formada en la linea "+linea);
	}
	
	public ExcepcionLiteralFaltante (int linea, String msj) {
		super(linea,msj);
		System.out.println("[Error Sintactico] Se esperaba un literal y se encontro '"+msj+"'.");
	}
	
}
