package estructuras.ts;

import java.io.IOException;
import java.util.HashMap;

import estructuras.ast.sentencias.NodoBloque;
import estructuras.ts.variables.VarInstancia;
import estructuras.ts.variables.Variable;
import excepciones.semanticas.ExcepcionAtributoYaNombrado;
import excepciones.semanticas.ExcepcionClaseDeclarada;
import excepciones.semanticas.ExcepcionClaseNoDeclarada;
import excepciones.semanticas.ExcepcionFaltaMain;
import excepciones.semanticas.ExcepcionHerenciaCircular;
import excepciones.semanticas.ExcepcionSemantica;
import modulos.GenCod;

public class TablaSimbolos {
	
	public static Clase claseActual;
	public static Unidad metodoActual;
	public static NodoBloque bloqueActual;
	public static HashMap<String,Clase> clases;
	public static Metodo main;
	
	public TablaSimbolos(){
		claseActual = null;
		metodoActual = null;
		bloqueActual = null;
		clases = new HashMap<String,Clase>();
	}
	
	public void agregarClase(String nom, Clase c) throws ExcepcionSemantica{
		if (nom!=null) {
			if (!estaClase(nom)) 
				clases.put(nom, c);
			else throw new ExcepcionClaseDeclarada(nom,c.getToken().getLinea());
		}
	}
	
	public boolean estaClase(String nombreClase){
		if (nombreClase != null)
			return clases.containsKey(nombreClase);
		else return false;
	}
	
	public void chequearDeclaraciones() throws ExcepcionSemantica {
		// Chequeo que las variables de instancia no tengan mismo nombre que clase o metodos
		chequearAtributos();
		// Chequeo que alguna clase tenga main. Guardo la referencia.
		main = chequearMain();
		// Chequeo que no haya herencia circular
		chequearHerenciaCircular();
		
		// Actualizar tabla de metodos
		actualizarMetodos();
		
		// Chequeo que los nombres de clases usados esten declarados
		for (Clase cla : clases.values()) {
			cla.chequearDeclaraciones();
		}
	}
	
	private void actualizarMetodos() throws ExcepcionSemantica {
		for (Clase cla: clases.values()) {
			cla.actualizar();
		}
	}
	
	public void chequearSentencias() throws ExcepcionSemantica, IOException {
		for (Clase cla : clases.values()) {
			claseActual = cla;
			cla.chequearSentencias();
		}
	}
	
	/* Metodo para chequear que al menos alguna clase tenga un main estatico y sin parametros */
	private Metodo chequearMain() throws ExcepcionSemantica {
		int cantMains = 0;
		String nombreClase = null;
		Metodo main = null;
		for (Clase cla : clases.values()) {
			HashMap<String,Metodo> listaMets = cla.getMetodos();
			if (listaMets.containsKey("main")) {
				Metodo met = listaMets.get("main");
				if (met.getEsEstatico()) {
					if (met.getListaParametros()!=null) {
						if (met.getListaParametros().size()==0) {
							cantMains++;
							if (main==null) {
								main = met;
								nombreClase = cla.getNombreClase();
							}
						}
					} else {
						// lista de parametros nula. no deberia pasar, pero si pasa asumo que no tiene parametros
						cantMains++;
						if (main==null) {
							main = met;
							nombreClase = cla.getNombreClase();
						}
					}
				}
			}
		}
		if (cantMains==0) {	// no tiene main
			throw new ExcepcionFaltaMain();
		} else if (cantMains>1) { // tiene mas de un main
			System.out.println("[Advertencia] Se encontraron varias clases con main.");
			System.out.println("[Advertencia] Se utilizara el main de la clase '"+nombreClase+"'.");
		}
		return main;
	}
	
	/* Metodo para chequear la herencia circular */
	private void chequearHerenciaCircular() throws ExcepcionSemantica {
		// Ineficiente, se podria mejorar marcando los nodos ya visitados
		for (String nomClase : clases.keySet()) {
			chequearHC(nomClase,clases.get(nomClase).getNombreClasePadre());
		}
	}
	
	/* Metodo auxiliar para comprobar que no haya herencia circular */
	private void chequearHC(String claseBase, String ancestro) throws ExcepcionSemantica {
		if (ancestro!=null){
			if (ancestro.equals("Object")) {
				if (claseBase.equals(ancestro))
					throw new ExcepcionHerenciaCircular(claseBase,clases.get(claseBase).getToken().getLinea());
			} else {
				if (claseBase.equals(ancestro))
					throw new ExcepcionHerenciaCircular(claseBase,clases.get(claseBase).getToken().getLinea());
				else {
					if (!clases.containsKey(ancestro))
						throw new ExcepcionClaseNoDeclarada(ancestro,clases.get(claseBase).getToken().getLinea());
					else
						chequearHC(claseBase,clases.get(ancestro).getNombreClasePadre());
				}
					
			}
		}
	}
	
	/* Metodo para comprobar que las variables de instancia no tengan el mismo nombre
	 * que la clase o que alguno de los metodos
	 */
	private void chequearAtributos() throws ExcepcionSemantica {
		for (Clase cla : clases.values()) {
			for (VarInstancia atrib : cla.getAtributos().values()) {
				String nombre = atrib.getToken().getLexema();
				if (cla.getMetodos().containsKey(nombre) || cla.getConstructor().getToken().comparar(nombre) || cla.getNombreClase().equals(nombre)) {
					throw new ExcepcionAtributoYaNombrado(nombre,atrib.getToken().getLinea());
				}
			}
		}
	}
	
	/* Metodo auxiliar para debuggear el programa cuando surja un error */
	public static void imprimirEstado() {
		for (Clase cla : clases.values()) {
			System.out.println("Clase "+cla.getNombreClase()+" hereda de "+cla.getNombreClasePadre());
			System.out.println("Atributos de clase: ");
			for (VarInstancia varsInst : cla.getAtributos().values())
				System.out.println("Variable de instancia: "+varsInst.getToken().getLexema()+" (offset "+varsInst.getOffset()+") ");
			for (Unidad met : cla.getMetodos().values()) {
				System.out.println("Metodo: "+met.getToken().getLexema()+" (offset "+met.getOffset()+")");
				for (Variable varlocal : met.getTablaVariables().values()) {
					System.out.println("Var local: "+varlocal.getToken().getLexema()+" (offset "+varlocal.getOffset()+") ");
				}
			}
			System.out.println("----------------");
		}
	}
	
	public void generarCodigo() throws IOException {
		// TODO
		codigoArranque();
	}

	/* Contiene el codigo de arranque + Heap y Malloc provisto por la catedra */
	private void codigoArranque() throws IOException {	
		GenCod.code();
		GenCod.gen("PUSH simple_heap_init");
		GenCod.gen("CALL");
		GenCod.gen("PUSH "+main.getLabel());
		GenCod.gen("CALL");
		GenCod.gen("HALT");	

		GenCod.gen("simple_heap_init","RET 0","Retorna inmediatamente");
		GenCod.gen("simple_malloc","LOADFP","Inicialización unidad");
		GenCod.gen("LOADSP");
		GenCod.gen("STOREFP","Finaliza inicialización del RA");
		GenCod.gen("LOADHL","hl");
		GenCod.gen("DUP","hl");
		GenCod.gen("PUSH 1","1");
		GenCod.gen("ADD","hl+1");
		GenCod.gen("STORE 4","Guarda el resultado (un puntero a la primer celda de la región de memoria)");
		GenCod.gen("LOAD 3","Carga la cantidad de celdas a alojar (parámetro que debe ser positivo)");
		GenCod.gen("ADD");
		GenCod.gen("STOREHL","Mueve el heap limit (hl). Expande el heap");
		GenCod.gen("STOREFP");
		GenCod.gen("RET 1","Retorna eliminando el parámetro");
		
		
		GenCod.data();
		GenCod.gen("mensaje_error_cast","DW \"[Excepcion] Se produjo error al intentar castear\",0","Mensaje para casteos erroneos");
		GenCod.code();
		GenCod.gen("error_cast","PUSH mensaje_error_cast","Apilo msj de error");
		GenCod.gen("SPRINT");
		GenCod.gen("HALT");
		
	}
	
	
}
