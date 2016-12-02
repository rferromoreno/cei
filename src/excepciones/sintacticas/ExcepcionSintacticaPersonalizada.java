package excepciones.sintacticas;

@SuppressWarnings("serial")
public class ExcepcionSintacticaPersonalizada extends ExcepcionSintactica {
	
	public ExcepcionSintacticaPersonalizada (int linea, String msj) {
		super(linea);
		System.out.println("[Error Sintactico] "+msj);
	}

	public ExcepcionSintacticaPersonalizada (int linea, String msj, String encontrado) {
		super(linea);
		System.out.println("[Error Sintactico] "+msj+". Se encontro \""+encontrado+"\"");
	}


}
