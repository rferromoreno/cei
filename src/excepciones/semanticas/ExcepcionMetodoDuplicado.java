package excepciones.semanticas;

@SuppressWarnings("serial")
public class ExcepcionMetodoDuplicado extends ExcepcionSemantica {

	public ExcepcionMetodoDuplicado(String nom, int linea) {
		super(linea);
		System.out.println("[Error Semantico] El metodo '"+nom+"' ya esta declarado.");
	}


}
