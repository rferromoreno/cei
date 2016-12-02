package excepciones.semanticas;

@SuppressWarnings("serial")
public class ExcepcionMetodoMalHeredado extends ExcepcionSemantica {

	public ExcepcionMetodoMalHeredado(String nom, int linea) {
		super(linea);
		System.out.println("[Error Semantico] El metodo '"+nom+"' no posee la misma signatura que su ancestro.");
	}


}
