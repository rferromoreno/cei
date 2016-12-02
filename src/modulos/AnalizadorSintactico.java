package modulos;

import java.io.BufferedReader;
import java.util.LinkedList;

import estructuras.*;
import estructuras.ast.encadenados.*;
import estructuras.ast.expresiones.*;
import estructuras.ast.sentencias.*;
import estructuras.ts.*;
import estructuras.ts.tipos.*;
import estructuras.ts.variables.*;
import excepciones.semanticas.*;
import excepciones.sintacticas.*;

public class AnalizadorSintactico {
	
	private Token lookAhead;
	private Token lookBehind;
	private AnalizadorLexico alexico;
	private TablaSimbolos TS;

	public AnalizadorSintactico(BufferedReader br) throws Exception {
		alexico = new AnalizadorLexico(br);
		lookAhead = alexico.getToken();
		lookBehind = null;
		TS = new TablaSimbolos();
		cargarTabla();
	}
	
	@SuppressWarnings("static-access")
	private void cargarTabla() throws ExcepcionSemantica {
		// Cargo clase Object
		Token tok = new Token("PR_Class","Object",0);
		Clase cla = new Clase(tok,null);
		cla.chequearConstructor(); // hago que se cree un constructor vacio
		TS.agregarClase("Object",cla);
		// Cargo clase System
		tok = new Token("PR_Class","System",0);
		cla = new Clase(tok,"Object");
		cla.chequearConstructor(); // hago que se cree un constructor vacio
		TS.agregarClase("System",cla);
		TS.claseActual = cla;
		// Cargo metodos de System
		// static int read();
		Metodo met = new Metodo(new Token("idMetVar","read",0),true,new TipoInt(),TS.claseActual);
		met.setCodigo(new NodoBloqueSystem("read"));
		TS.claseActual.agregarMetodo(met);
		// static void printB(boolean b);
		LinkedList<VarParametro> listaPar = new LinkedList<VarParametro>();
		VarParametro varP = new VarParametro(new Token("idMetVar","b",0),new TipoBoolean());
		listaPar.add(varP);
		met = new Metodo(new Token("idMetVar","printB",0),listaPar,true,new TipoVoid(),TS.claseActual);	
		met.setCodigo(new NodoBloqueSystem("printB"));
		TS.claseActual.agregarMetodo(met);
		// static void printC(char c);
		listaPar = new LinkedList<VarParametro>();
		varP = new VarParametro(new Token("idMetVar","c",0),new TipoChar());
		listaPar.add(varP);
		met = new Metodo(new Token("idMetVar","printC",0),listaPar,true,new TipoVoid(),TS.claseActual);	
		met.setCodigo(new NodoBloqueSystem("printC"));
		TS.claseActual.agregarMetodo(met);
		// static void printI(int i);
		listaPar = new LinkedList<VarParametro>();
		varP = new VarParametro(new Token("idMetVar","i",0),new TipoInt());
		listaPar.add(varP);
		met = new Metodo(new Token("idMetVar","printI",0),listaPar,true,new TipoVoid(),TS.claseActual);	
		met.setCodigo(new NodoBloqueSystem("printI"));
		TS.claseActual.agregarMetodo(met);
		// static void printS(String s);
		listaPar = new LinkedList<VarParametro>();
		varP = new VarParametro(new Token("idMetVar","s",0),new TipoString());
		listaPar.add(varP);
		met = new Metodo(new Token("idMetVar","printS",0),listaPar,true,new TipoVoid(),TS.claseActual);	
		met.setCodigo(new NodoBloqueSystem("printS"));
		TS.claseActual.agregarMetodo(met);
		// static void println();
		met = new Metodo(new Token("idMetVar","println",0),true,new TipoVoid(),TS.claseActual);	
		met.setCodigo(new NodoBloqueSystem("println"));
		TS.claseActual.agregarMetodo(met);
		// static void printBln(boolean b);
		listaPar = new LinkedList<VarParametro>();
		varP = new VarParametro(new Token("idMetVar","b",0),new TipoBoolean());
		listaPar.add(varP);
		met = new Metodo(new Token("idMetVar","printBln",0),listaPar,true,new TipoVoid(),TS.claseActual);	
		met.setCodigo(new NodoBloqueSystem("printBln"));
		TS.claseActual.agregarMetodo(met);
		// static void printCln(char c);
		listaPar = new LinkedList<VarParametro>();
		varP = new VarParametro(new Token("idMetVar","c",0),new TipoChar());
		listaPar.add(varP);
		met = new Metodo(new Token("idMetVar","printCln",0),listaPar,true,new TipoVoid(),TS.claseActual);	
		met.setCodigo(new NodoBloqueSystem("printCln"));
		TS.claseActual.agregarMetodo(met);
		// static void printIln(int i);
		listaPar = new LinkedList<VarParametro>();
		varP = new VarParametro(new Token("idMetVar","i",0),new TipoInt());
		listaPar.add(varP);
		met = new Metodo(new Token("idMetVar","printIln",0),listaPar,true,new TipoVoid(),TS.claseActual);	
		met.setCodigo(new NodoBloqueSystem("printIln"));
		TS.claseActual.agregarMetodo(met);
		// static void printSln(String s);
		listaPar = new LinkedList<VarParametro>();
		varP = new VarParametro(new Token("idMetVar","s",0),new TipoString());
		listaPar.add(varP);
		met = new Metodo(new Token("idMetVar","printSln",0),listaPar,true,new TipoVoid(),TS.claseActual);	
		met.setCodigo(new NodoBloqueSystem("printSln"));
		TS.claseActual.agregarMetodo(met);	
	}
	
	private void  match(String tipoToken) throws Exception {
		if (lookAhead.comparar(tipoToken)) {
			lookBehind = lookAhead;
			lookAhead = alexico.getToken();
		} else { 
			if (tipoToken.equals("idMetVar")) {
				throw new ExcepcionSintactica(lookAhead.getLinea(),"identificador de metodo o variable",lookAhead.getLexema());
			} else if (tipoToken.equals("idClase")) {
				throw new ExcepcionSintactica(lookAhead.getLinea(),"identificador de clase",lookAhead.getLexema());
			} else  {
				TablaTokens tabla = new TablaTokens(true); // creo una tabla de tokens invertida
				throw new ExcepcionSintactica(lookAhead.getLinea(),tabla.obtenerTipo(tipoToken),lookAhead.getLexema());
			}
		}
	}
	
	public void analizar() throws Exception {
		inicial();
		TS.chequearDeclaraciones();
		TS.generarCodigo();	// Creo la primera parte del codigo.
		TS.chequearSentencias();
		// Si hasta este punto no se lanzaron excepciones, es porque esta todo ok.
		GenCod.close();
		//TS.imprimirEstado();
		System.out.println("El analisis lexico, sintactico y semantico se realizo de forma exitosa");
	}
	
	@SuppressWarnings("static-access")
	private void clase() throws Exception{
		match("PR_Class");
		match("idClase");
		TS.claseActual = new Clase(lookBehind);
		TS.agregarClase(TS.claseActual.getNombreClase(),TS.claseActual);
		if (lookAhead.comparar("PR_Extends")) {
			herencia();
		} else {
			TS.claseActual.setPadre("Object");
		}			
		match("P_Llave_A");
		listaMiembro();
		match("P_Llave_C");
		TS.claseActual.chequearConstructor(); // agrego uno por default si no tiene
	}
	
	private void inicialPrimo() throws Exception{
		if (!lookAhead.comparar("EOF"))
			inicial();	// Se supone que hay otra clase. Arranco de nuevo...
	}
	
	private void inicial() throws Exception{
		clase();
		inicialPrimo();
	}
	
	@SuppressWarnings("static-access")
	private void herencia() throws Exception{
		match("PR_Extends");
		match("idClase");
		TS.claseActual.setPadre(lookBehind.getLexema());
	}
	
	private void listaMiembro() throws Exception{
		switch (lookAhead.getNombre()) {
			case "PR_Public":
			case "PR_Private":
			case "idClase":
			case "PR_Static":
			case "PR_Dynamic": 	miembro();
								listaMiembro();
								break;
			case "P_Llave_C": break;
			default: throw new ExcepcionSintactica(lookAhead.getLinea(),lookAhead.getLexema());
		}
	}
	
	private void miembro() throws Exception{
		switch (lookAhead.getNombre()) {
			case "PR_Public":
			case "PR_Private": 	atributo();
								break;
			case "idClase": ctor();
							break;
			case "PR_Static":
			case "PR_Dynamic": metodo();
							   break;
		}
	}

	@SuppressWarnings("static-access")
	private void metodo() throws Exception{
		boolean esEstatico = formaMetodo();
		TipoMetodo tipo = tipoMetodo();
		match("idMetVar");
		Metodo met = new Metodo(lookBehind,esEstatico,tipo,TablaSimbolos.claseActual);
		TS.claseActual.agregarMetodo(met);
		TS.metodoActual = met;
		argsFormales();
		NodoBloque bloq = bloque();
		TS.metodoActual.setCodigo(bloq);
	}

	@SuppressWarnings("static-access")
	private void ctor() throws Exception{
		match("idClase");
		Constructor ctor = new Constructor(lookBehind);
		TS.claseActual.setConstructor(ctor);
		TS.metodoActual = ctor;
		argsFormales();
		NodoBloque bloq = bloque();
		TS.metodoActual.setCodigo(bloq);
	}
	
	private void argsFormales() throws Exception{
		match("P_Parentesis_A");
		LAForm();
	}
	
	private void LAForm() throws Exception{
		if (lookAhead.comparar("P_Parentesis_C")) {
			match("P_Parentesis_C");
		} else switch (lookAhead.getNombre()) {
			case "idClase":
			case "PR_Boolean":
			case "PR_Char":
			case "PR_Int":
			case "PR_String":	listaArgsFormales();
								match("P_Parentesis_C");
								break;
			default: throw new ExcepcionArgFormMalFormado(lookAhead.getLinea(),lookAhead.getLexema());
		}
	}
	
	private void listaArgsFormales() throws Exception{
		argFormal();
		restoLAF();
	}
	
	@SuppressWarnings("static-access")
	private void argFormal() throws Exception{
		 switch (lookAhead.getNombre()) {
			case "idClase":
			case "PR_Boolean":
			case "PR_Char":
			case "PR_Int":
			case "PR_String":	Tipo tipo = tipo();
								match("idMetVar");
								VarParametro var = new VarParametro(lookBehind,tipo);
								TS.metodoActual.agregarParametro(var);
								break;
			default: throw new ExcepcionArgFormMalFormado(lookAhead.getLinea(),lookAhead.getLexema());
		 }
	}
	
	private void restoLAF() throws Exception{
		if (lookAhead.comparar("P_Coma")) {
			match("P_Coma");
			listaArgsFormales();
		} else if (!lookAhead.comparar("P_Parentesis_C"))
			throw new ExcepcionArgFormMalFormado(lookAhead.getLinea(),lookAhead.getLexema());
	}
	
	private boolean formaMetodo() throws Exception{
		boolean esEstatico = true;
		if (lookAhead.comparar("PR_Static")) 
			match("PR_Static");
		else if (lookAhead.comparar("PR_Dynamic")) {
				match("PR_Dynamic");
				esEstatico = false;
		} else throw new ExcepcionSintactica(lookAhead.getLinea());  // no deberia llegar a este punto
		return esEstatico;
	}
	
	private TipoMetodo tipoMetodo() throws Exception{
		if (lookAhead.comparar("PR_Void")) {
			match("PR_Void");
			return new TipoVoid();
		}
		else return tipo();	// encuentra el error cuando ve los otros tipos
	}

	private NodoBloque bloque() throws Exception{
		match("P_Llave_A");
		LinkedList<NodoSentencia> listaSent = listaSentencias();
		NodoBloque bloq = new NodoBloque(listaSent);
		match("P_Llave_C");
		return bloq;
	}
	
	private LinkedList<NodoSentencia> listaSentencias() throws Exception{
		LinkedList<NodoSentencia> listaSent = new LinkedList<NodoSentencia>();
		switch (lookAhead.getNombre()) {
			case "P_Llave_C": 	break; // siguiente(listaSentencias)
			case "P_Llave_A":	
			case "P_Puntocoma":		
			case "PR_Boolean": 
			case "PR_Char":	
			case "PR_Int":
			case "PR_String":
			case "idClase":
			case "PR_If":
			case "PR_While":
			case "PR_Return":
			case "PR_Llave_A": 
			case "P_Parentesis_A":
			case "PR_This":
			case "idMetVar":
			case "PR_New": 	NodoSentencia sent = sentencia();
							listaSent = listaSentencias();
							listaSent.addFirst(sent);
							return listaSent;
			default: throw new ExcepcionSintacticaPersonalizada(lookAhead.getLinea(),"Error leyendo lista de sentencias"); 
		}
		return listaSent;
	}
	
	private NodoSentencia sentencia() throws Exception{
		NodoSentencia sent = null;
		NodoExpresion expr = null;
		Token temp = null;
		switch (lookAhead.getNombre()) {
			case "P_Puntocoma":		match("P_Puntocoma");
									return new NodoSentenciaVacia();
			case "PR_Boolean": 
			case "PR_Char":	
			case "PR_Int":
			case "PR_String":
			case "idClase":			Tipo tipo = tipo();
									LinkedList<Token> listaTokens = listaDecVars();
									LinkedList<VarLocal> listaVars = new LinkedList<VarLocal>();
									for (Token var : listaTokens) {
										VarLocal varLoc = new VarLocal(var,tipo);
										listaVars.add(varLoc);
									}
									match("P_Puntocoma");
									return new NodoDeclaracion(tipo,listaVars);
									
			case "PR_If":			match("PR_If");
									temp = lookBehind;
									match("P_Parentesis_A");
									expr = expresion();
									match("P_Parentesis_C");
									sent = sentencia();
									NodoSentencia sentElse = null;
									if (lookAhead.comparar("PR_Else")) 
										sentElse = elseOpc();
									return new NodoIf(temp,expr,sent,sentElse);
									
			case "PR_While":		match("PR_While");
									temp = lookBehind;
									match("P_Parentesis_A");
									expr = expresion();
									match("P_Parentesis_C");
									sent = sentencia();
									return new NodoWhile(temp,expr,sent);
									
			case "PR_Return":		match("PR_Return");
									temp = lookBehind;
									expr = expresionPrimo();
									return new NodoReturn(temp,expr);
									
			case "P_Llave_A": 		return bloque();
									
			case "P_Parentesis_A":	
			case "PR_This":			
			case "idMetVar":
			case "PR_New": 			NodoPrimario pri = primario();
									sent = asigOSentencia(pri);
									match("P_Puntocoma");
									return sent;
			default: throw new ExcepcionSintacticaPersonalizada(lookAhead.getLinea(),"Sentencia mal formada"); 
		}
	}
	
	private NodoSentencia elseOpc() throws Exception{
		match("PR_Else");
		return sentencia();
	}

	private NodoExpresion expresionPrimo() throws Exception{
		NodoExpresion expr = null;
		if (lookAhead.comparar("P_Puntocoma")) {
			match("P_Puntocoma");
		} else {
			expr = expresion();
			match("P_Puntocoma");
		}
		return expr;
	}

	private NodoSentencia asigOSentencia(NodoPrimario pri) throws Exception{
		Token temp = null;
		if (lookAhead.comparar("O_Asignacion")) {
			match("O_Asignacion");
			temp = lookBehind;
			NodoExpresion expr = expresion();
			return new NodoAsignacion(pri,expr,temp);
		} else if (!lookAhead.comparar("P_Puntocoma"))
			throw new ExcepcionSintacticaPersonalizada(lookAhead.getLinea(),"Se esperaba una asignacion o una sentencia");
		temp = lookBehind;
		return new NodoSentenciaLlamada(temp,pri);
	}

	private NodoPrimario primario() throws Exception{
		NodoEncadenado cad = null;
		Token temp = null;
		switch (lookAhead.getNombre()) {						
			case "P_Parentesis_A": 		match("P_Parentesis_A");
										return primarioUno();
								
			case "PR_This":				match("PR_This");
										NodoThis nodo = new NodoThis(lookBehind);
										cad = listaLOI();
										nodo.setCadena(cad);
										return nodo;
										
			case "idMetVar":			match("idMetVar");	// revisar si es metodo o variable
										temp = lookBehind;
										return primarioDos(temp);
										
			case "PR_New": 			match("PR_New");
									match("idClase");
									temp = lookBehind;
									LinkedList<NodoExpresion> listaParams = argsActuales();
									cad = listaLOI();
									return new NodoConstructor(temp,listaParams,cad);
			default: throw new ExcepcionSintacticaPersonalizada(lookAhead.getLinea(),"Se esperaba un literal");  
		}
	}
	
	private NodoPrimario primarioDos(Token tok) throws Exception{
		NodoEncadenado resto = null;
		if (lookAhead.comparar("P_Parentesis_A")) { // es llamada a metodo
			LinkedList<NodoExpresion> listaParam = argsActuales();
			resto = listaLOI();
			return new NodoLlamadaDirecta(tok,listaParam,resto); 
		} else if (lookAhead.comparar("P_Punto")) { // sigue llamando en cadena
			resto = listaLOI();
		} 
		return new NodoVariable(tok,resto); 
	}

	private NodoEncadenado listaLOI() throws Exception{
		if (lookAhead.comparar("P_Punto")) {
			NodoEncadenado nodo = llamadaOId();
			NodoEncadenado cad = listaLOI();
			nodo.setCadena(cad);
			return nodo;
		} else return null;
	}
	
	private NodoEncadenado llamadaOId() throws Exception{
		match("P_Punto");
		match("idMetVar");
		Token temp = lookBehind;
		LinkedList<NodoExpresion> listaArgs = restoLOI();
		if (listaArgs!=null) 
			return new NodoLlamadaEncadenada(temp,null,listaArgs);
		else return new NodoIdEncadenado(temp,null);
	}
	
	private LinkedList<NodoExpresion> restoLOI() throws Exception{
		LinkedList<NodoExpresion> listaArgs = null;
		if (lookAhead.comparar("P_Parentesis_A")) 
			listaArgs = argsActuales();
		return listaArgs;
	}

	private LinkedList<NodoExpresion> argsActuales() throws Exception{
		LinkedList<NodoExpresion> args = null;
		match("P_Parentesis_A");
		args = listaExpsOpc();
		match("P_Parentesis_C");
		return args;
	}
	
	private LinkedList<NodoExpresion> listaExpsOpc() throws Exception{
		if (!lookAhead.comparar("P_Parentesis_C")) 
			return listaExps();
		return new LinkedList<NodoExpresion>();
	}
	
	private LinkedList<NodoExpresion> listaExps() throws Exception{
		LinkedList<NodoExpresion> listaExpr;
		NodoExpresion expr = expresion();
		listaExpr = restoListaExps();
		listaExpr.addFirst(expr);
		return listaExpr;
	}
	
	private LinkedList<NodoExpresion> restoListaExps() throws Exception{
		LinkedList<NodoExpresion> listaExpr = new LinkedList<NodoExpresion>();
		if (lookAhead.comparar("P_Coma")) {
			match("P_Coma");
			listaExpr = listaExps();
		} else if (!lookAhead.comparar("P_Parentesis_C"))
			throw new ExcepcionSintacticaPersonalizada(lookBehind.getLinea(),"Lista de expresiones mal formada"); 
		return listaExpr;
	}
	
	private NodoPrimario primarioUno() throws Exception{
		NodoEncadenado cad;
		Token temp = null;
		switch(lookAhead.getNombre()) {
			case "idClase":		match("idClase");
								Token idc = lookBehind;
								match("P_Punto");
								match("idMetVar");
								temp = lookBehind;
								LinkedList<NodoExpresion> listaParam = argsActuales();
								match("P_Parentesis_C");
								cad = listaLOI();
								return new NodoLlamadaEstatica(temp,idc,listaParam,cad);
			
			case "P_Corchetes_A":	
			case "PR_Null":
			case "PR_False":
			case "PR_True":
			case "L_Entero":
			case "L_Caracter":
			case "L_String":
			case "P_Parentesis_A":
			case "PR_This":
			case "idMetVar":
			case "PR_New":	
			case "O_Not":
			case "O_Suma":
			case "O_Resta":		NodoExpresion expr = expresion();
								match("P_Parentesis_C");
								temp = lookBehind;
								cad = listaLOI();
								return new NodoExpParentizada(temp,expr,cad);
			default: throw new ExcepcionSintacticaPersonalizada(lookAhead.getLinea(),"Expresion mal formada"); 
		}
	}
	
	private NodoExpresion expresion() throws Exception{
		switch(lookAhead.getNombre()) {
			case "P_Corchetes_A":	
			case "PR_Null":
			case "PR_False":
			case "PR_True":
			case "L_Entero":
			case "L_Caracter":
			case "L_String": 	
			case "P_Parentesis_A":
			case "PR_This":
			case "idMetVar":
			case "PR_New":		
			case "O_Suma":
			case "O_Resta":
			case "O_Not":		return expOr();
			default: throw new ExcepcionSintacticaPersonalizada(lookAhead.getLinea(),"Error en la expresion",lookAhead.getLexema()); 
		}
	}
	
	private NodoExpresion expOr() throws Exception{
		NodoExpresion expr = expAnd();
		return expOrPrimo(expr);
	}
	
	private NodoExpresion expOrPrimo(NodoExpresion expr) throws Exception{
		if (lookAhead.comparar("O_Or")){
			match("O_Or");
			Token temp = lookBehind;
			NodoExpresion ladoDer = expAnd();
			NodoExpresionBinaria neb = new NodoExpresionBinaria(temp,expr,ladoDer);
			return expOrPrimo(neb);
		} else switch (lookAhead.getNombre()) { // siguientes(expOrPrimo)
			case "P_Coma": // lista de expresiones
			case "P_Puntocoma":
			case "P_Parentesis_C": return expr;
			default: throw new ExcepcionExprMalFormada(lookBehind.getLinea());   // error formando una expresion
		}
	}
	
	private NodoExpresion expAnd() throws Exception{
		NodoExpresion expr = expIg();
		return expAndPrimo(expr);
	}
	
	private NodoExpresion expAndPrimo(NodoExpresion expr) throws Exception{
		if (lookAhead.comparar("O_And")){
			match("O_And");
			Token temp = lookBehind;
			NodoExpresion ladoDer = expIg();
			NodoExpresionBinaria neb = new NodoExpresionBinaria(temp,expr,ladoDer);
			return expAndPrimo(neb);
		} else switch (lookAhead.getNombre()) { // siguientes(expAndPrimo)
			case "P_Coma":	// lista de expresiones
			case "P_Puntocoma":
			case "P_Parentesis_C": 
			case "O_Or":	 return expr;
			default: throw new ExcepcionExprMalFormada(lookBehind.getLinea()); 
		}
	}
	
	private NodoExpresion expIg() throws Exception{
		NodoExpresion expr = expComp();
		return expIgPrimo(expr);
	}
	
	private NodoExpresion expIgPrimo(NodoExpresion expr) throws Exception{
		switch (lookAhead.getNombre()) {
			case "O_Comparacion":
			case "O_Distinto": 		opIg();
									Token temp = lookBehind;
									NodoExpresion ladoDer = expComp();
									NodoExpresionBinaria neb = new NodoExpresionBinaria(temp,expr,ladoDer);
									return expIgPrimo(neb);
			case "P_Coma":	// lista de expresiones
			case "P_Puntocoma":
			case "P_Parentesis_C": 
			case "O_Or": 
			case "O_And": 			return expr;
			default: throw new ExcepcionExprMalFormada(lookBehind.getLinea()); 
		}
	}
	
	private NodoExpresion expComp() throws Exception{
		NodoExpresion expr = expAd();
		return restoExpComp(expr);
	}
	
	private NodoExpresion restoExpComp(NodoExpresion expr) throws Exception{
		switch (lookAhead.getNombre()) {
			case "O_Mayor":
			case "O_Menor":
			case "O_Mayorigual":
			case "O_Menorigual":	opComp();
									Token temp = lookBehind;
									NodoExpresion ladoDer = expAd();
									return new NodoExpresionBinaria(temp,expr,ladoDer);
			case "PR_Instanceof":	match("PR_Instanceof");
									Token aux = lookBehind;
									match("idClase");
									return new NodoExpresionBinaria(aux,expr,new NodoLiteral(lookBehind));
			case "P_Coma":
			case "P_Puntocoma":
			case "P_Parentesis_C": 
			case "O_Or": 
			case "O_And": 
			case "O_Comparacion":
			case "O_Distinto": 		return expr;
			default: throw new ExcepcionExprMalFormada(lookBehind.getLinea());
		}
	}
	
	private NodoExpresion expAd() throws Exception{
		NodoExpresion expr = expMul();
		return expAdPrimo(expr);
	}
	
	private NodoExpresion expAdPrimo(NodoExpresion expr)  throws Exception{
		switch (lookAhead.getNombre()) {
			case "O_Suma":
			case "O_Resta": 		opAd();
									Token temp = lookBehind;
									NodoExpresion ladoDer = expMul();
									NodoExpresionBinaria neb = new NodoExpresionBinaria(temp,expr,ladoDer);
									return expAdPrimo(neb);
			case "P_Coma":
			case "P_Puntocoma":
			case "P_Parentesis_C": 
			case "O_Or": 
			case "O_And": 
			case "O_Comparacion":
			case "O_Distinto": 		
			case "PR_Instanceof":
			case "O_Mayor":
			case "O_Mayorigual":
			case "O_Menor":
			case "O_Menorigual": return expr;
			default: throw new ExcepcionExprMalFormada(lookBehind.getLinea()); 
		}
	}
	
	private NodoExpresion expMul() throws Exception{
		NodoExpresion expr = expUn();
		return expMulPrimo(expr);
	}
	
	private NodoExpresion expMulPrimo(NodoExpresion expr)  throws Exception{
		switch (lookAhead.getNombre()) {
			case "O_Mult":
			case "O_Div":
			case "O_Mod":			opMul();
									Token temp = lookBehind;
									NodoExpresion ladoDer = expUn();
									NodoExpresionBinaria neb = new NodoExpresionBinaria(temp,expr,ladoDer); 
									return expMulPrimo(neb);
			case "P_Coma":
			case "P_Puntocoma":
			case "P_Parentesis_C": 
			case "O_Or": 
			case "O_And": 
			case "O_Comparacion":
			case "O_Distinto": 		
			case "PR_Instanceof":
			case "O_Mayor":
			case "O_Mayorigual":
			case "O_Menor":
			case "O_Menorigual":
			case "O_Suma":
			case "O_Resta":	return expr;
			default: throw new ExcepcionExprMalFormada(lookBehind.getLinea());  // error formando una expresion
		}
	}
	
	private NodoExpresion expUn() throws Exception{
		switch(lookAhead.getNombre()) {
			case "O_Suma":
			case "O_Resta":
			case "O_Not":	opUn();
							Token temp = lookBehind;
							NodoExpresion ladoDer = expUn();
							return new NodoExpresionUnaria(temp,ladoDer);
			default: return expCast();
		}
	}
	
	
	private NodoExpresion expCast() throws Exception{
		switch (lookAhead.getNombre()) {
			case "P_Corchetes_A": 	match("P_Corchetes_A");
									match("idClase");
									Token temp = lookBehind;
									match("P_Corchetes_C");
									NodoExpPrimario nop = operando();
									return new NodoExpresionUnaria(temp,nop);
			case "PR_Null":
			case "PR_False":
			case "PR_True":
			case "L_Entero":
			case "L_Caracter":
			case "L_String": 	
			case "P_Parentesis_A":
			case "PR_This":
			case "idMetVar":
			case "PR_New":		return operando();	
			default: throw new ExcepcionExprMalFormada(lookBehind.getLinea()); 
		}
	}
	
	private NodoExpPrimario operando() throws Exception{
		switch (lookAhead.getNombre()) {
			case "PR_Null":
			case "PR_False":
			case "PR_True":
			case "L_Entero":
			case "L_Caracter":
			case "L_String": 	return literal();
			case "P_Parentesis_A":
			case "PR_This":
			case "idMetVar":
			case "PR_New":		return primario();
			default: throw new ExcepcionSintacticaPersonalizada(lookAhead.getLinea(),"Operando invalido"); 	
		}
	}	
	
	private NodoLiteral literal() throws Exception{
		switch (lookAhead.getNombre()) {
			case "PR_Null":
			case "PR_False":
			case "PR_True":
			case "L_Entero":
			case "L_Caracter":
			case "L_String": 	match(lookAhead.getNombre());
								return new NodoLiteral(lookBehind);
			default: throw new ExcepcionLiteralFaltante(lookAhead.getLinea(),lookAhead.getLexema()); 
		}
	}	
		
	@SuppressWarnings("static-access")
	private void atributo() throws Exception{
		boolean esPublico = visibilidad();
		Tipo tipoAtrib = tipo();
		LinkedList<Token> listaVars = listaDecVars();
		for (Token var : listaVars) {
			VarInstancia varInst = new VarInstancia(var,tipoAtrib,esPublico);
			TS.claseActual.agregarAtributo(varInst);
		}
		match("P_Puntocoma");
	}
	
	private boolean visibilidad() throws Exception{
		boolean esPublico = true;
		if (lookAhead.comparar("PR_Public")) 
			match("PR_Public");
		else if (lookAhead.comparar("PR_Private")) {
			match("PR_Private"); 
			esPublico = false;
		}
		return esPublico;
	}
	
	private Tipo tipo() throws Exception{
		if (lookAhead.comparar("idClase")) {
			match("idClase");
			return new TipoClase(lookBehind);
		}
		else return tipoPrimitivo(); // postergo la deteccion cuando veo que no era ni un tipo primitivo
	}
	
	private TipoPrimitivo tipoPrimitivo() throws Exception{
		switch (lookAhead.getNombre()) {
			case "PR_Boolean": 	match(lookAhead.getNombre());
								return new TipoBoolean();
			case "PR_Int":		match(lookAhead.getNombre());
								return new TipoInt();
			case "PR_Char":		match(lookAhead.getNombre());
								return new TipoChar();
			case "PR_String":	match(lookAhead.getNombre());
								return new TipoString();
			default: throw new ExcepcionSintacticaPersonalizada(lookAhead.getLinea(),"Tipo invalido"); 
		}
	}
	
	private LinkedList<Token> listaDecVars() throws Exception{
		LinkedList<Token> listaVars;
		match("idMetVar");
		Token var = lookBehind;
		listaVars = restoLDV();
		listaVars.addFirst(var);
		return listaVars;
	}
	
	private LinkedList<Token> restoLDV() throws Exception{
		LinkedList<Token> listaVars = new LinkedList<Token>();
		if (lookAhead.comparar("P_Coma")) {
			match("P_Coma");
			listaVars = listaDecVars();
		} else if (!lookAhead.comparar("P_Puntocoma")) 
			 throw new ExcepcionSintacticaPersonalizada(lookBehind.getLinea(),"Error en la declaracion de variables");
		return listaVars;
	}
	
	private void opIg() throws Exception{
		switch (lookAhead.getNombre()) {
			case "O_Comparacion":
			case "O_Distinto":	match(lookAhead.getNombre());
								break;
		}
	}
	
	private void opComp() throws Exception{
		switch (lookAhead.getNombre()) {
			case "O_Menor":
			case "O_Mayor":
			case "O_Menorigual":
			case "O_Mayorigual": 	match(lookAhead.getNombre());
									break;
		}
	}
	
	private void opAd() throws Exception{
		switch (lookAhead.getNombre()) {
			case "O_Suma":
			case "O_Resta": match(lookAhead.getNombre());
						  	break;
		}
	}
	
	private void opUn() throws Exception{
		switch (lookAhead.getNombre()) {
			case "O_Suma":
			case "O_Resta":
			case "O_Not": match(lookAhead.getNombre());
						  break;
		}
	}
	
	private void opMul() throws Exception{
		switch (lookAhead.getNombre()) {
			case "O_Mult":
			case "O_Div":
			case "O_Mod": match(lookAhead.getNombre());
						  break;
		}
	}
	
}
