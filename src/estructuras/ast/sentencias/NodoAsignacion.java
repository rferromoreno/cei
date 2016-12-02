package estructuras.ast.sentencias;

import estructuras.ast.expresiones.NodoExpPrimario;
import estructuras.ast.expresiones.NodoExpresion;
import estructuras.ast.expresiones.NodoPrimario;
import estructuras.ts.tipos.TipoMetodo;
import excepciones.semanticas.ExcepcionSemantica;
import excepciones.semanticas.ExcepcionSemanticaPersonalizada;
import estructuras.Token;


public class NodoAsignacion extends NodoSentencia {
	
	private NodoExpPrimario ladoIzq;
	private NodoExpresion ladoDer;
	private Token simbolo;
	
	public NodoAsignacion(NodoExpPrimario lizq, NodoExpresion lder, Token simb) {
		simbolo = simb;
		ladoDer = lder;
		ladoIzq = lizq;
	}
	
	public NodoExpPrimario getLadoIzq() {
		return ladoIzq;
	}

	public void setLadoIzq(NodoExpPrimario ladoIzq) {
		this.ladoIzq = ladoIzq;
	}

	public NodoExpresion getLadoDer() {
		return ladoDer;
	}

	public void setLadoDer(NodoExpresion ladoDer) {
		this.ladoDer = ladoDer;
	}

	public Token getSimbolo() {
		return simbolo;
	}

	public void setSimbolo(Token simbolo) {
		this.simbolo = simbolo;
	}

	public void chequear() throws ExcepcionSemantica {
		TipoMetodo td = ladoDer.chequear();
		ladoIzq.setEsLadoIzq(true);
		TipoMetodo ti = ladoIzq.chequear();
		if (td.esVoid())
			throw new ExcepcionSemanticaPersonalizada(simbolo.getLinea(),"La expresion debe devolver un tipo valido. El metodo llamado es void");
		NodoPrimario np = (NodoPrimario)ladoIzq;
		if (!np.terminaEnVar())
			throw new ExcepcionSemanticaPersonalizada(simbolo.getLinea(),"Asignacion incorrecta. El lado izquierdo debe terminar en una variable.");
		if (np.terminaEnLlamada())
			throw new ExcepcionSemanticaPersonalizada(simbolo.getLinea(),"Asignacion incorrecta. El lado izquierdo no puede terminar en una llamada.");
		if (!td.conforma(ti)) 
			throw new ExcepcionSemanticaPersonalizada(simbolo.getLinea(),"Asignacion incorrecta. No se puede asignar algo de tipo "+td.getNombreClase()+" a algo de tipo "+ti.getNombreClase());;
	}

}
