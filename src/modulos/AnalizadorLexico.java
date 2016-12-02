package modulos;

import java.io.BufferedReader;
import java.io.IOException;

import estructuras.*;
import excepciones.lexicas.ExcepcionCharInvalido;
import excepciones.lexicas.ExcepcionCharMalFormado;
import excepciones.lexicas.ExcepcionComentarioMalFormado;
import excepciones.lexicas.ExcepcionLexica;
import excepciones.lexicas.ExcepcionOperadorInvalido;
import excepciones.lexicas.ExcepcionStringMalFormado;

public class AnalizadorLexico {

	private static final char EOF = (char)-1;
	private int nroLinea;
	private char actual;
	private BufferedReader buffer;
	private TablaTokens tabla;

	public AnalizadorLexico(BufferedReader br) throws IOException {
		tabla = new TablaTokens();
		buffer = br;
		nroLinea = 1;
		actual = dameChar();
	}

	public Token getToken() throws ExcepcionLexica {	
		String lexema = new String();
		Token token = null;		
		// Consumo separadores;
		while (Helper.esSeparador(actual)) {
			revisarEnter();
			actual = dameChar();
		}	
		// Comienzo a identificar el token.
		if (Helper.esLetra(actual)) { // comienza con una letra?
			while (Helper.esIdentificador(actual)) {
				lexema += actual;
				actual = dameChar();
			}
			if (tabla.esPalabraReservada(lexema)) {
				token = new Token(tabla.obtenerTipo(lexema),lexema,nroLinea);
			} else if (Helper.esMayuscula(lexema.charAt(0))) { // diferencio si es identificador de Clase o de variable
				token = new Token("idClase",lexema,nroLinea);
			} else {
				token = new Token("idMetVar",lexema,nroLinea);
			}
		} else if (Helper.esDigito(actual)) { // comienza con un digito?
			while(Helper.esDigito(actual)) {
				lexema += actual;
				actual = dameChar();
			}
			token = new Token("L_Entero",lexema,nroLinea);
		} else switch (actual)  { // comienza con un simbolo
			case EOF:
			case '{' : 
			case '}' : 
			case '[' :
			case ']' : 
			case '(' : 
			case ')' : 
			case '.' : 
			case ',' : 
			case ';' :
			case '+' : 
			case '-' : 
			case '*' : 
			case '%' : 
				lexema += actual;
				token = new Token(tabla.obtenerTipo(lexema),lexema,nroLinea);
				actual = dameChar(); // consumo char;
				break;
			// Casos especiales con un '=' a continuacion (>=, <=, ==, !=)
			case '>' :  
			case '<' : 
			case '!' : 
			case '=' : 
				lexema += actual;
				actual = dameChar();
				if (actual=='=') { 
					lexema += actual;
					token = new Token(tabla.obtenerTipo(lexema),lexema,nroLinea);
					actual = dameChar();
				} else {
					token = new Token(tabla.obtenerTipo(lexema),lexema,nroLinea);
				}
				break;
			case '|' : // necesita aparecer otro | o &, sino es error
			case '&' :
				lexema += actual;
				char aux = actual; // guardo el | o &
				actual = dameChar();
				if (actual != aux) {
					throw new ExcepcionOperadorInvalido(nroLinea,aux);
				} else {
					lexema += actual;
					token = new Token(tabla.obtenerTipo(lexema),lexema,nroLinea);
					actual = dameChar();
				}
				break;
			// Literales caracteres y Strings
			case '"' : // necesita cerrar comillas
				actual = '-'; // fix para saltearme las comillas actuales.
				while (!Helper.esComillas(actual) && !Helper.esEOF(actual) && !Helper.esEnter(actual)) {
					actual = dameChar();
					lexema += actual;
				} 
				if (Helper.esEOF(actual) || Helper.esEnter(actual)) {
					throw new ExcepcionStringMalFormado(nroLinea);
				} else {
					lexema = lexema.substring(0, lexema.length()-1); // saco las comillas del final
					token = new Token("L_String",lexema,nroLinea);
					actual = dameChar();
				}
				break;
			case '\'' : // necesita cerrar apostrofo 
				actual = dameChar();	boolean huboBarra = false;
				if (Helper.esApostrofo(actual) || Helper.esEnter(actual) || Helper.esEOF(actual)) {
					throw new ExcepcionCharMalFormado(nroLinea);
				} else if (actual=='\\') { // es barra invertida								
					actual = dameChar();	
					huboBarra = true;
					if (Helper.esEOF(actual) || Helper.esEnter(actual)) 
						throw new ExcepcionCharMalFormado(nroLinea);
				} 
				if ((actual=='n' || actual=='t') && huboBarra) {
					lexema = "\\" + actual;
				} else {
					lexema += "" + actual;
				}
				actual = dameChar(); // deberia ser apostrofo, casos 'x', '\x' e incluso '\''
				if (Helper.esApostrofo(actual)) {
					token = new Token("L_Caracter",lexema,nroLinea);
				} else {
					throw new ExcepcionCharMalFormado(nroLinea);
				}
				actual = dameChar(); // consumo un char para lo que venga despues!
				break;		
			// Caso especial '/': Puede ser operador, comentario simple o multilinea
			case '/' : // necesito ver si es comentario
				lexema += actual;
				actual = dameChar();
				if (actual=='/') { // Es comentario simple
					do {
						actual = dameChar();
						revisarEnter();
					} while (!Helper.esEnter(actual) && !Helper.esEOF(actual));
					if (Helper.esEOF(actual)) {
						lexema = "" + actual;
						token = new Token(tabla.obtenerTipo(lexema),lexema,nroLinea);
					} else { // Es un enter, ya consumi el comentario.
						actual = dameChar();
						token = this.getToken(); // llamada recursiva para obtener el token post-comentario
					}
				} else if (actual=='*') { // Es comentario multilinea
					char anterior;
					actual = dameChar();
					if (Helper.esEOF(actual)) {
						throw new ExcepcionComentarioMalFormado(nroLinea);
					} else { 
						revisarEnter(); // por si es un enter.
					}
					do {
						anterior = actual;
						actual = dameChar();
						revisarEnter();
					} while (!((anterior=='*')&&(actual=='/')) && !Helper.esEOF(actual));
					if (Helper.esEOF(actual)) {
						throw new ExcepcionComentarioMalFormado(nroLinea);
					} else { // Termine de consumir el comentario
						actual = dameChar();
						token = this.getToken(); // llamada recursiva para obtener el token post-comentario
					}
				} else { // Es una barra simple
					token = new Token(tabla.obtenerTipo(lexema),lexema,nroLinea);
				}
				break;
			default: throw new ExcepcionCharInvalido(nroLinea,actual);
			} 	
		return token;
	}

	/* Busca un caracter en el buffer de lectura. Devuelve el caracter correspondiente
	 * al entero -1 si se llego al final del buffer (EOF) */
	private char dameChar() {
		try {
			return (char) buffer.read();
		} catch (IOException e) {
			System.out.println("Error leyendo del archivo de entrada");
		}
		return EOF;
	}

	/* Revisa si el caracter actual es un "Enter" para incrementar el numero de linea */
	private void revisarEnter() {
		if (Helper.esEnter(actual)) 
			nroLinea++;
	}		
}