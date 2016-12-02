package excepciones.semanticas;

@SuppressWarnings("serial")
public class ExcepcionHerenciaCircular extends ExcepcionSemantica {
	
	public ExcepcionHerenciaCircular(String nom, int linea) {
		super(linea);
		System.out.println("[Error Semantico] La clase '"+nom+"' tiene herencia circular.");
	}
}
