package estructuras.ts.tipos;


public abstract class Tipo extends TipoMetodo {

	public abstract boolean esTipoPrimitivo();
	
	public abstract String getNombreClase();
	
	public boolean esVoid() {
		return false;
	}
	
}
