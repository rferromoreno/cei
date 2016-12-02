package estructuras.ts.tipos;

public class TipoVoid extends TipoMetodo {

	public boolean esValido() {
		return false;
	}
	
	public boolean esVoid() { 
		return true;
	}
	
	public boolean esTipoPrimitivo() {
		return false;
	}
	
	public String getNombreClase() {
		return "void";
	}
	
	public boolean conforma(TipoMetodo t) {
		return false;
	}
	
	public boolean esTipoClase() {
		return false;
	}
	
}
