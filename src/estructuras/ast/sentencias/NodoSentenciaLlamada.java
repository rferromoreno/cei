package estructuras.ast.sentencias;

import estructuras.Token;
import estructuras.ast.expresiones.NodoExpPrimario;
import estructuras.ast.expresiones.NodoPrimario;
import estructuras.ts.tipos.TipoMetodo;
import excepciones.semanticas.ExcepcionSemantica;
import excepciones.semanticas.ExcepcionSemanticaPersonalizada;
import modulos.GenCod;

public class NodoSentenciaLlamada extends NodoSentencia {
	
	private Token identificador;
	private NodoExpPrimario llamada;
	
	public NodoSentenciaLlamada(Token id, NodoExpPrimario llamada) {
		this.llamada = llamada;
		identificador = id;
	}

	public NodoExpPrimario getLlamada() {
		return llamada;
	}

	public void setLlamada(NodoExpPrimario llamada) {
		this.llamada = llamada;
	}

	public void chequear() throws ExcepcionSemantica {
		TipoMetodo tipoRetorno = llamada.chequear();
		NodoPrimario np = (NodoPrimario)llamada;
		if (!np.terminaEnLlamada())
			throw new ExcepcionSemanticaPersonalizada(identificador.getLinea(),"La sentencia no termina en una llamada");
		// Si hago una llamada a algo que no es void me va a quedar en el tope de la pila el retorno
		if (!tipoRetorno.esVoid())
			GenCod.gen("POP","Desapilo el tope de la pila luego de una sentencia llamada");
	}

}
