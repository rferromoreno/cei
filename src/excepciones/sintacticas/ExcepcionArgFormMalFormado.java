package excepciones.sintacticas;

@SuppressWarnings("serial")
public class ExcepcionArgFormMalFormado extends ExcepcionSintactica {

	public ExcepcionArgFormMalFormado (int linea) {
		super("[Error Sintactico] Expresion mal formada en la linea "+linea);
	}
	
	public ExcepcionArgFormMalFormado (int linea, String msj) {
		super(linea,msj);
		System.out.println("[Error Sintactico] Argumento formal mal formado.");
	}
	
}
