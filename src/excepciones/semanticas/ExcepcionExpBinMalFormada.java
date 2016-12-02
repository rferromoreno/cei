package excepciones.semanticas;

@SuppressWarnings("serial")
public class ExcepcionExpBinMalFormada extends ExcepcionSemantica {

	public ExcepcionExpBinMalFormada (int linea, String msj) {
		super(linea);
		System.out.println("[Error Semantico] "+msj);
	}

	public ExcepcionExpBinMalFormada (int linea, String op, String permitidos) {
		super(linea);
		System.out.println("[Error Semantico] El operador binario "+op+" solo permite "+permitidos+".");
	}


}
