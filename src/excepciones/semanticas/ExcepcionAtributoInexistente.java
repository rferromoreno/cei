package excepciones.semanticas;

@SuppressWarnings("serial")
public class ExcepcionAtributoInexistente extends ExcepcionSemantica {

	public ExcepcionAtributoInexistente(String nom, int linea) {
		super(linea);
		System.out.println("[Error Semantico] La variable de instancia '"+nom+"' no existe.");
	}


}
