package estructuras.ast.encadenados;

import estructuras.Token;
import estructuras.ts.tipos.TipoMetodo;
import excepciones.semanticas.ExcepcionSemantica;

public abstract class NodoEncadenado {

	private Token identificador;
	private NodoEncadenado cadena;
	private boolean esLadoIzq;
	
	public NodoEncadenado(Token id, NodoEncadenado cad) {
		identificador = id;
		cadena = cad;
		esLadoIzq = false;
	}
	
	public NodoEncadenado(Token id) {
		this(id,null);
	}
	
	public Token getToken() {
		return identificador;
	}

	public void setIdentificador(Token identificador) {
		this.identificador = identificador;
	}

	public NodoEncadenado getCadena() {
		return cadena;
	}

	public void setCadena(NodoEncadenado cadena) {
		this.cadena = cadena;
	}
	
	public void setEsLadoIzq(boolean esLadoIzq) {
		this.esLadoIzq = esLadoIzq;
	}
	
	public boolean getEsLadoIzq() {
		return esLadoIzq;
	}

	public abstract boolean terminaEnVar();
	public abstract boolean terminaEnLlamada();
	
	public abstract TipoMetodo chequear(TipoMetodo receptor) throws ExcepcionSemantica;
}
