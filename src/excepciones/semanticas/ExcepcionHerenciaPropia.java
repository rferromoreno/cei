package excepciones.semanticas;

@SuppressWarnings("serial")
public class ExcepcionHerenciaPropia extends ExcepcionSemantica {
	
	public ExcepcionHerenciaPropia(String nom, int linea) {
		super(linea);
		System.out.println("[Error Semantico] La clase '"+nom+"' no puede heredar de si misma.");
	}
}
