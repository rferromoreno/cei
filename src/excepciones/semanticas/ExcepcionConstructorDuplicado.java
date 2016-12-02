package excepciones.semanticas;

@SuppressWarnings("serial")
public class ExcepcionConstructorDuplicado extends ExcepcionSemantica {

	public ExcepcionConstructorDuplicado(String nom, int linea) {
		super(linea);
		System.out.println("[Error Semantico] El constructor '"+nom+"' ya esta declarado.");
	}


}
