package estructuras.ts.tipos;

public abstract class TipoPrimitivo extends Tipo {

	public boolean esTipoPrimitivo() {
		return true;
	}
	
	public boolean esValido() {
		return true;
	}
	
	public boolean conforma(TipoMetodo t) {
		return t.getNombreClase().equals(this.getNombreClase());
	}
	
	public boolean esTipoClase() {
		return false;
	}
	
}
