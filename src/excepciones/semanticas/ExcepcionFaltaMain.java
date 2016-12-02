package excepciones.semanticas;

@SuppressWarnings("serial")
public class ExcepcionFaltaMain extends ExcepcionSemantica {

	public ExcepcionFaltaMain() {
		System.out.println("[Error Semantico] Alguna clase debe tener el metodo estatico 'main' (sin parametros)");
	}

	
}
