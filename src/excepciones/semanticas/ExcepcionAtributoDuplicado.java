package excepciones.semanticas;

@SuppressWarnings("serial")
public class ExcepcionAtributoDuplicado extends ExcepcionSemantica {

	public ExcepcionAtributoDuplicado(String nom, int linea) {
		super(linea);
		System.out.println("[Error Semantico] El atributo '"+nom+"' ya esta declarado.");
	}


}
