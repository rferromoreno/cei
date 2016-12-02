package excepciones.semanticas;

@SuppressWarnings("serial")
public class ExcepcionClaseNoDeclarada extends ExcepcionSemantica {

	public ExcepcionClaseNoDeclarada(String nom, int linea) {
		super(linea);
		System.out.println("[Error Semantico] La clase '"+nom+"' no esta declarada.");
	}


}
