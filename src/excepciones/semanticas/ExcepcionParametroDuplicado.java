package excepciones.semanticas;

@SuppressWarnings("serial")
public class ExcepcionParametroDuplicado extends ExcepcionSemantica {

	public ExcepcionParametroDuplicado(String nom, int linea) {
		super(linea);
		System.out.println("[Error Semantico] Ya existe un parametro con el nombre '"+nom+"'.");
	}


}
