package estructuras.ast.sentencias;

import estructuras.Token;
import estructuras.ast.expresiones.NodoExpresion;
import estructuras.ts.tipos.TipoMetodo;
import excepciones.semanticas.ExcepcionSemantica;
import excepciones.semanticas.ExcepcionSemanticaPersonalizada;
import modulos.GenCod;

public class NodoIf extends NodoSentencia {
	
	private Token identificador;
	private NodoExpresion condicion;
	private NodoSentencia sentencia;
	private NodoSentencia sentenciaElse;

	
	public NodoIf(Token id, NodoExpresion expr, NodoSentencia sent, NodoSentencia sentElse) {
		identificador = id;
		condicion = expr;
		sentencia = sent;
		sentenciaElse = sentElse;
	}

	public NodoExpresion getCondicion() {
		return condicion;
	}

	public void setCondicion(NodoExpresion condicion) {
		this.condicion = condicion;
	}

	public NodoSentencia getSentencia() {
		return sentencia;
	}

	public void setSentencia(NodoSentencia sentencia) {
		this.sentencia = sentencia;
	}

	public NodoSentencia getSentenciaElse() {
		return sentenciaElse;
	}

	public void setSentenciaElse(NodoSentencia sentenciaElse) {
		this.sentenciaElse = sentenciaElse;
	}

	public void chequear() throws ExcepcionSemantica {
		String str = GenCod.generarEtiqueta();
		GenCod.comment("Comienzo del IF_"+str);
		TipoMetodo te = null;
		if (sentenciaElse!=null) {		// if (E) S1 else S2
			te = condicion.chequear();			// Traduccion de E
			if (!te.getNombreClase().equals("boolean"))
				throw new ExcepcionSemanticaPersonalizada(identificador.getLinea(),"La condicion del If no es una expresion logica");
			GenCod.gen("BF IF_L1_"+str);
			sentencia.chequear();				// Traduccion de S1
			GenCod.gen("JUMP IF_L2_"+str);
			GenCod.gen("IF_L1_"+str,"NOP","");
			sentenciaElse.chequear();			// Traduccion de S2
			GenCod.gen("IF_L2_"+str,"NOP","");
		} else {						// if (E) S1
			te = condicion.chequear();			// Traduccion de E
			if (!te.getNombreClase().equals("boolean"))
				throw new ExcepcionSemanticaPersonalizada(identificador.getLinea(),"La condicion del If no es una expresion logica");
			GenCod.gen("BF IF_L_"+str);
			sentencia.chequear();				// Traduccion de S1
			GenCod.gen("IF_L_"+str,"NOP","");
		}
		GenCod.comment("Fin del IF_"+str);
				
	}

}
