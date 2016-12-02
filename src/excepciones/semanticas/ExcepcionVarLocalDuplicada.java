package excepciones.semanticas;

@SuppressWarnings("serial")
public class ExcepcionVarLocalDuplicada extends ExcepcionSemantica {

	public ExcepcionVarLocalDuplicada(String nom, int linea) {
		super(linea);
		System.out.println("[Error Semantico] La variable local '"+nom+"' ya fue declarada.");
	}


}
