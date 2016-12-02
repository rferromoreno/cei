package excepciones.semanticas;

@SuppressWarnings("serial")
public class ExcepcionParametrosNoCoinciden extends ExcepcionSemantica {

	public ExcepcionParametrosNoCoinciden(int linea) {
		super(linea);
		System.out.println("[Error Semantico] La cantidad de parametros no coincide.");
	}


}
