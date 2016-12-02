package excepciones.semanticas;

@SuppressWarnings("serial")
public class ExcepcionConstructorMalNombrado extends ExcepcionSemantica {

	public ExcepcionConstructorMalNombrado(String nom, int linea) {
		super(linea);
		System.out.println("[Error Semantico] El nombre del constructor '"+nom+"' no corresponde al nombre de clase.");
	}


}
