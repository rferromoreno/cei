package estructuras;

import java.util.HashMap;

public final class TablaTokens {

	private HashMap<String,String> tabla;
	private static final char EOF = (char)-1;
	
	/* Constructor de la tabla de tokens comun */
	public TablaTokens() {
		this(false); 
	}
	
	/* Constructor para generar la tabla invertida. Si el argumento es true, la table es (value,key) */
	public TablaTokens(boolean invertida) {
		// Inicializo la tabla
		tabla = new HashMap<String,String>();
		if (invertida) {
			// Palabras clave
			tabla.put("PR_Class","class");
			tabla.put("PR_Extends", "extends");
			tabla.put("PR_Static", "static");
			tabla.put("PR_Dynamic", "dynamic");
			tabla.put("PR_Public", "public");
			tabla.put("PR_Private", "private");
			tabla.put("PR_Void", "void");
			tabla.put("PR_Boolean", "boolean");
			tabla.put("PR_Char", "char");
			tabla.put("PR_Int", "int");
			tabla.put("PR_String", "String");
			tabla.put("PR_If", "if");
			tabla.put("PR_Else", "else");
			tabla.put("PR_While", "while");
			tabla.put("PR_Return", "return");
			tabla.put("PR_Instanceof", "instanceof");
			tabla.put("PR_This", "this");
			tabla.put("PR_New", "new");
			tabla.put("PR_Null", "null");
			tabla.put("PR_True", "true");
			tabla.put("PR_False", "false");		
			// Operadores y puntuacion
			tabla.put("P_Parentesis_A", "(");
			tabla.put("P_Parentesis_C", ")");
			tabla.put("P_Llave_A", "{");
			tabla.put("P_Llave_C", "}");
			tabla.put("P_Corchetes_A", "[");
			tabla.put("P_Corchetes_C", "]");
			tabla.put("P_Punto", ".");
			tabla.put("P_Puntocoma", ";");
			tabla.put("P_Coma", ",");
			tabla.put("O_Mayor", ">");
			tabla.put("O_Menor", "<");
			tabla.put("O_Not", "!");
			tabla.put("O_Asignacion","=");
			tabla.put("O_Comparacion","==");
			tabla.put("O_Mayorigual",">=");
			tabla.put("O_Menorigual","<=");
			tabla.put("O_Distinto","!=");
			tabla.put("O_Suma","+");
			tabla.put("O_Resta","-");
			tabla.put("O_Mult","*");
			tabla.put("O_Div","/");
			tabla.put("O_Mod","%");
			tabla.put("O_Or","||");
			tabla.put("O_And","&&");
			// Caso especial: EOF
			tabla.put("EOF", "EOF");
		} else {
			// Palabras clave
						tabla.put("class","PR_Class");
						tabla.put("extends", "PR_Extends");
						tabla.put("static", "PR_Static");
						tabla.put("dynamic", "PR_Dynamic");
						tabla.put("public", "PR_Public");
						tabla.put("private", "PR_Private");
						tabla.put("void", "PR_Void");
						tabla.put("boolean", "PR_Boolean");
						tabla.put("char", "PR_Char");
						tabla.put("int", "PR_Int");
						tabla.put("String", "PR_String");
						tabla.put("if", "PR_If");
						tabla.put("else", "PR_Else");
						tabla.put("while", "PR_While");
						tabla.put("return", "PR_Return");
						tabla.put("instanceof", "PR_Instanceof");
						tabla.put("this", "PR_This");
						tabla.put("new", "PR_New");
						tabla.put("null", "PR_Null");
						tabla.put("true", "PR_True");
						tabla.put("false", "PR_False");		
						// Operadores y puntuacion
						tabla.put("(", "P_Parentesis_A");
						tabla.put(")", "P_Parentesis_C");
						tabla.put("{", "P_Llave_A");
						tabla.put("}", "P_Llave_C");
						tabla.put("[", "P_Corchetes_A");
						tabla.put("]", "P_Corchetes_C");
						tabla.put(".", "P_Punto");
						tabla.put(";", "P_Puntocoma");
						tabla.put(",", "P_Coma");
						tabla.put(">", "O_Mayor");
						tabla.put("<", "O_Menor");
						tabla.put("!", "O_Not");
						tabla.put("=", "O_Asignacion");
						tabla.put("==", "O_Comparacion");
						tabla.put(">=", "O_Mayorigual");
						tabla.put("<=", "O_Menorigual");
						tabla.put("!=", "O_Distinto");
						tabla.put("+", "O_Suma");
						tabla.put("-", "O_Resta");
						tabla.put("*", "O_Mult");
						tabla.put("/", "O_Div");
						tabla.put("%", "O_Mod");
						tabla.put("||", "O_Or");
						tabla.put("&&", "O_And");
						// Caso especial: EOF
						String aux = "" + EOF;
						tabla.put(aux, "EOF");
		}
	}
	
	/* Metodo que se fija si un identificador es una palabra reservada */
	public boolean esPalabraReservada(String str){
		return tabla.containsKey(str);
	}
	
	/* Metodo para devolver el tipo de palabra reservada.
	 * Devuelve NULL en caso de que no sea palabra reservada. */
	public String obtenerTipo(String str){
		return tabla.get(str);
	}
	
}
