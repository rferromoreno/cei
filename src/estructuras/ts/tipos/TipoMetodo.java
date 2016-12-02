package estructuras.ts.tipos;

public abstract class TipoMetodo {
	
	public abstract String getNombreClase();
	
	public abstract boolean esValido();
	
	public abstract boolean esVoid();
	
	public abstract boolean esTipoPrimitivo();
	
	public abstract boolean esTipoClase();
	
	public abstract boolean conforma(TipoMetodo tipo);
	
	public boolean equals(TipoMetodo tipo) {
		return this.getNombreClase().equals(tipo.getNombreClase());
	}
	
}
