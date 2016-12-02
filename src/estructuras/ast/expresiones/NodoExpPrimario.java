package estructuras.ast.expresiones;

public abstract class NodoExpPrimario extends NodoExpresion {
	
	private boolean esLadoIzq;
	
	public void setEsLadoIzq(boolean esLadoIzq) {
		this.esLadoIzq = esLadoIzq;
	}
	
	public boolean getEsLadoIzq() {
		return esLadoIzq;
	}
	
}
