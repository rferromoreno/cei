package excepciones.semanticas;

@SuppressWarnings("serial")
public class ExcepcionAtributoYaNombrado extends ExcepcionSemantica {

	public ExcepcionAtributoYaNombrado(String nom, int linea) {
		super(linea);
		System.out.println("[Error Semantico] El nombre de atributo '"+nom+"' es el mismo que el de algun metodo o el de clase.");
	}


}
