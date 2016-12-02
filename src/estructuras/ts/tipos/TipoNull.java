package estructuras.ts.tipos;

import estructuras.Token;

public class TipoNull extends TipoClase {

	public TipoNull(Token id) {
		super(id);
	}

	public String getNombreClase() {
		return "null";
	}
	
	public boolean conforma(TipoMetodo t) {
		return t.esTipoClase();
	}
	
}
