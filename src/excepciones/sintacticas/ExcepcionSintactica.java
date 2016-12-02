package excepciones.sintacticas;

@SuppressWarnings("serial")
public class ExcepcionSintactica extends Exception {
	
	public ExcepcionSintactica (int linea, String esperado, String encontrado) {
		this(linea);
		System.out.println("[Error Sintactico] Se esperaba un "+esperado+" y se encontro un \""+encontrado+"\"");
	}
	
	public ExcepcionSintactica (String msg) {
		System.out.println(msg);
	}
	
	public ExcepcionSintactica (int linea) {
		System.out.println("[Error Sintactico] Error en la linea "+linea);
	}
	
	public ExcepcionSintactica (int linea, String msj) {
		this(linea);
		System.out.println("[Error Sintactico] Se encontro algo invalido: \""+msj+"\"");
	}



}
