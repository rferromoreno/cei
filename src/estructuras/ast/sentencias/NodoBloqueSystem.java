package estructuras.ast.sentencias;

import excepciones.semanticas.ExcepcionSemantica;
import modulos.GenCod;

public class NodoBloqueSystem extends NodoBloque {
	
	private String tipoBloque;

	public NodoBloqueSystem(String tipo) {
		tipoBloque = tipo;
	}
	
	public void chequear() throws ExcepcionSemantica {
		switch (tipoBloque) {
			case "read":		GenCod.gen("READ");
								GenCod.gen("STORE 3");
								break;
			case "printB":
			case "printBln":	GenCod.gen("LOAD 3");
								GenCod.gen("BPRINT");
								break;
			case "printI":
			case "printIln":	GenCod.gen("LOAD 3");
								GenCod.gen("IPRINT");
								break;
			case "printC":
			case "printCln":	GenCod.gen("LOAD 3");
								GenCod.gen("CPRINT");
								break;
			case "printS":
			case "printSln":	GenCod.gen("LOAD 3");
								GenCod.gen("SPRINT");
								break;
		}
		switch (tipoBloque) {
			case "printBln":
			case "printIln":
			case "printCln":
			case "printSln":
			case "println":		GenCod.gen("PRNLN");
								break;
		}
		
	}

}
