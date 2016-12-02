package estructuras.ast.expresiones;

import estructuras.Token;
import estructuras.ts.TablaSimbolos;
import estructuras.ts.tipos.Tipo;
import estructuras.ts.tipos.TipoBoolean;
import estructuras.ts.tipos.TipoClase;
import estructuras.ts.tipos.TipoInt;
import estructuras.ts.tipos.TipoMetodo;
import excepciones.semanticas.ExcepcionExpUnMalFormada;
import excepciones.semanticas.ExcepcionSemantica;
import excepciones.semanticas.ExcepcionSemanticaPersonalizada;
import modulos.GenCod;

public class NodoExpresionUnaria extends NodoExpresion {
	
	private NodoExpresion ladoDer;
	private Token simbolo;
	
	public NodoExpresionUnaria(Token temp, NodoExpresion lder) {
		simbolo = temp;
		ladoDer = lder;
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

	public TipoMetodo chequear() throws ExcepcionSemantica {
		TipoMetodo td = ladoDer.chequear();
		switch (simbolo.getLexema()) {
			case "+":
			case "-": 	if (simbolo.getLexema().equals("-"))
							GenCod.gen("NEG");
						if (td.getNombreClase().equals("int"))
							return new TipoInt();
						else throw new ExcepcionExpUnMalFormada(simbolo.getLinea(),simbolo.getLexema(),"enteros");
			case "!":	GenCod.gen("NOT");
						if (td.getNombreClase().equals("boolean"))
							return new TipoBoolean();
						else throw new ExcepcionExpUnMalFormada(simbolo.getLinea(),simbolo.getLexema(),"booleanos");
			default: // es un casteo, tiene la forma [idClase] <expr>
						if (td.esVoid())
							throw new ExcepcionSemanticaPersonalizada(simbolo.getLinea(),"La expresion del casteo no puede ser de tipo void.");
						Tipo tip = new TipoClase(simbolo);
						if (!tip.conforma(td)) 
							throw new ExcepcionSemanticaPersonalizada(simbolo.getLinea(),"Los tipos del casteo no conforman");
						GenCod.gen("DUP","Duplico la referencia al objeto");
						GenCod.gen("LOADREF 1","Cargo el ID desde el CIR de la expresion analizada (der)");
						int id = TablaSimbolos.clases.get(simbolo.getLexema()).getId();
						GenCod.gen("PUSH "+id,"Cargo el ID de la clase a castear (izq)");
						GenCod.gen("EQ","Comparo los IDs");
						GenCod.gen("BF error_cast","Casteo incorrecto. Lanzo excepcion");
						return tip;	
		}
	}

}
