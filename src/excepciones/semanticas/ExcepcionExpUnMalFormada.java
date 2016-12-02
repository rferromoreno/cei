package excepciones.semanticas;

@SuppressWarnings("serial")
public class ExcepcionExpUnMalFormada extends ExcepcionSemantica {

	public ExcepcionExpUnMalFormada (int linea, String msj) {
		super(linea);
		System.out.println("[Error Semantico] "+msj);
	}

	public ExcepcionExpUnMalFormada (int linea, String op, String permitidos) {
		super(linea);
		System.out.println("[Error Semantico] El operador unario "+op+" solo permite "+permitidos+".");
	}


}
