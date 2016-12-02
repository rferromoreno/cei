package excepciones.semanticas;

@SuppressWarnings("serial")
public class ExcepcionSemanticaPersonalizada extends ExcepcionSemantica {

	public ExcepcionSemanticaPersonalizada (int linea, String msj) {
		super(linea);
		System.out.println("[Error Semantico] "+msj);
	}

	public ExcepcionSemanticaPersonalizada (int linea, String msj, String encontrado) {
		super(linea);
		System.out.println("[Error Semantico] "+msj+". Se encontro \""+encontrado+"\"");
	}


}
