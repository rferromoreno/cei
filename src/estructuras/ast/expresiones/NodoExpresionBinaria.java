package estructuras.ast.expresiones;


import estructuras.Token;
import estructuras.ts.TablaSimbolos;
import estructuras.ts.tipos.TipoBoolean;
import estructuras.ts.tipos.TipoClase;
import estructuras.ts.tipos.TipoInt;
import estructuras.ts.tipos.TipoMetodo;
import excepciones.semanticas.ExcepcionExpBinMalFormada;
import excepciones.semanticas.ExcepcionSemantica;
import excepciones.semanticas.ExcepcionSemanticaPersonalizada;
import modulos.GenCod;

public class NodoExpresionBinaria extends NodoExpresion {
	
	private NodoExpresion ladoIzq;
	private NodoExpresion ladoDer;
	private Token simbolo;

	public NodoExpresionBinaria(Token temp, NodoExpresion lizq, NodoExpresion lder) {
		simbolo = temp;
		ladoIzq = lizq;
		ladoDer = lder;
	}

	public NodoExpresion getLadoIzq() {
		return ladoIzq;
	}

	public void setLadoIzq(NodoExpresion ladoIzq) {
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

	public TipoMetodo chequear() throws ExcepcionSemantica {
		TipoMetodo ti = ladoIzq.chequear();
		TipoMetodo td = null;
		// Solamente reviso el lado derecho si no es un instance of
		if (!simbolo.getLexema().equals("instanceof"))
			td = ladoDer.chequear();
		switch (simbolo.getLexema()) {
			case "+":
			case "-":
			case "*":
			case "/":
			case "%": 	switch (simbolo.getLexema()) {
							case "+": GenCod.gen("ADD"); break;
							case "-": GenCod.gen("SUB"); break;
							case "*": GenCod.gen("MUL"); break;
							case "/": GenCod.gen("DIV"); break;
							case "%": GenCod.gen("MOD"); break;
						}
						if (ti.getNombreClase().equals("int") && td.getNombreClase().equals("int"))  
							return new TipoInt();
						else throw new ExcepcionExpBinMalFormada(simbolo.getLinea(),simbolo.getLexema(),"enteros");
			case "&&":
			case "||":	switch (simbolo.getLexema()) {
							case "&&": GenCod.gen("AND"); break;
							case "||": GenCod.gen("OR"); break;
						}
						if (ti.getNombreClase().equals("boolean") && td.getNombreClase().equals("boolean")) 
							return new TipoBoolean();
						else throw new ExcepcionExpBinMalFormada(simbolo.getLinea(),simbolo.getLexema(),"booleanos");
			case "!=":
			case "==":	switch (simbolo.getLexema()) {
							case "!=": GenCod.gen("NE"); break;
							case "==": GenCod.gen("EQ"); break;
						}
						if (ti.conforma(td) || td.conforma(ti)) 
							return new TipoBoolean();
						else throw new ExcepcionExpBinMalFormada(simbolo.getLinea(),simbolo.getLexema(),"tipos que conformen");
			case ">":
			case "<":
			case ">=":
			case "<=":	switch (simbolo.getLexema()) {
							case ">": GenCod.gen("GT"); break;
							case "<": GenCod.gen("LT"); break;
							case ">=": GenCod.gen("GE"); break;
							case "<=": GenCod.gen("LE"); break;
						}
						if (ti.getNombreClase().equals("int") && td.getNombreClase().equals("int")) 
							return new TipoBoolean();
						else throw new ExcepcionExpBinMalFormada(simbolo.getLinea(),simbolo.getLexema(),"enteros");
			case "instanceof": 		// ( <expr> instanceof <idClase> )
									// La expresion de la izquierda ya esta chequeada (al comienzo del metodo)
									td = new TipoClase(((NodoLiteral)ladoDer).getToken());
									if (!td.esValido()) {
										// Error, el tipo de TD no existe.
										throw new ExcepcionSemanticaPersonalizada(simbolo.getLinea(),"El tipo '"+td.getNombreClase()+"' es invalido.");	
									} else if (!td.conforma(ti)) { 
										// Error, el tipo no conforma
										throw new ExcepcionSemanticaPersonalizada(simbolo.getLinea(),"El tipo '"+ti.getNombreClase()+"' no conforma con '"+td.getNombreClase()+"'.");
									} else {
										int id = TablaSimbolos.clases.get(td.getNombreClase()).getId();
										GenCod.gen("LOADREF 1","Cargo el ID desde el CIR de la expresion analizada (izq)");
										GenCod.gen("PUSH "+id,"Cargo el ID de la Clase (der)");
										GenCod.gen("EQ","Comparo los IDs. El resultado queda en el tope de la pila");
										return new TipoBoolean();
									}
		}
		return null;
	}

}
