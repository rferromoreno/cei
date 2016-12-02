package estructuras;

public class Token {
	
	private String nombre;
	private String lexema;
	private int nroLinea;
	
	public Token(String nombre, String lexema, int nroLinea) {
		this.nombre = nombre;
		this.lexema = lexema;
		this.nroLinea = nroLinea;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public String getLexema() {
		return lexema;
	}
	
	public int getLinea() {
		return nroLinea;
	}
	
	public boolean comparar(String msg){
		return nombre.equals(msg);
	}

}
