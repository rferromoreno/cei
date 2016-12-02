package estructuras.ast.expresiones;

import estructuras.Token;
import estructuras.ts.tipos.Tipo;
import estructuras.ts.tipos.TipoBoolean;
import estructuras.ts.tipos.TipoChar;
import estructuras.ts.tipos.TipoInt;
import estructuras.ts.tipos.TipoNull;
import estructuras.ts.tipos.TipoString;
import excepciones.semanticas.ExcepcionSemantica;
import modulos.GenCod;

public class NodoLiteral extends NodoExpPrimario {
	
	private Token literal;
	
	public NodoLiteral(Token token) {
		literal = token;
		setEsLadoIzq(false);
	}

	public Token getToken() {
		return literal;
	}

	public void setToken(Token literal) {
		this.literal = literal;
	}

	public Tipo chequear() throws ExcepcionSemantica {
		switch(literal.getNombre()){
			case "L_Entero":
				GenCod.gen("PUSH "+literal.getLexema(),"Apilo un "+literal.getLexema());
				return new TipoInt();
			case "L_Caracter":
				char letra = '0';	// Parche. Letra despues va a tomar un valor.
				if (literal.getLexema().length()==1)
					letra = literal.getLexema().charAt(0);
				else {
					switch (literal.getLexema().charAt(1)) {
						case 't': letra = '\t'; break;
						case 'n': letra = '\n'; break;
					}
				}
				GenCod.gen("PUSH "+(int)letra,"Apilo un caracter "+literal.getLexema());
				return new TipoChar();
			case "PR_True":
				GenCod.gen("PUSH 1", "Apilo TRUE");
				return new TipoBoolean();
			case "PR_False":
				GenCod.gen("PUSH 0", "Apilo FALSE");
				return new TipoBoolean();
			case "L_String":
				String label = "str_" + GenCod.generarEtiqueta();
				GenCod.data();
				GenCod.gen(label,"DW \""+literal.getLexema()+"\",0","Guardo un string");
				GenCod.code();
				GenCod.gen("PUSH "+label,"Apilo un string");
				return new TipoString();
			case "PR_Null":
				GenCod.gen("PUSH 0", "Apilo NULL");
				return new TipoNull(literal);
		}
		return null;
	}

}
