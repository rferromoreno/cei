package excepciones.semanticas;

@SuppressWarnings("serial")
public class ExcepcionAtributoNoVisible extends ExcepcionSemantica {

	public ExcepcionAtributoNoVisible(String nom, int linea) {
		super(linea);
		System.out.println("[Error Semantico] La variable de instancia '"+nom+"' no es visible.");
	}


}
