package excepciones.semanticas;

@SuppressWarnings("serial")
public class ExcepcionParametroNoConforma extends ExcepcionSemantica {

	public ExcepcionParametroNoConforma(String nom, int linea) {
		super(linea);
		System.out.println("[Error Semantico] Un parametro actual en la llamada del metodo '"+nom+"' no conforma con el formal");
	}


}
