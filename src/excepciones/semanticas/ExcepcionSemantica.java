package excepciones.semanticas;

@SuppressWarnings("serial")
public class ExcepcionSemantica extends Exception {
	
	public ExcepcionSemantica () {
	}
		
	public ExcepcionSemantica (String msg) {
		System.out.println(msg);
	}
	
	public ExcepcionSemantica (int linea) {
		System.out.println("[Error Semantico] Error en la linea "+linea);
	}


}
