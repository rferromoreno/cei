package excepciones.semanticas;

@SuppressWarnings("serial")
public class ExcepcionClaseDeclarada extends ExcepcionSemantica {

	public ExcepcionClaseDeclarada(String nom, int linea) {
		super(linea);
		System.out.println("[Error Semantico] La clase '"+nom+"' ya esta declarada.");
	}


}
