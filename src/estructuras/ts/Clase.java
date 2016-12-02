package estructuras.ts;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import estructuras.Token;
import estructuras.ast.sentencias.NodoBloque;
import estructuras.ts.variables.VarInstancia;
import excepciones.semanticas.ExcepcionAtributoDuplicado;
import excepciones.semanticas.ExcepcionClaseNoDeclarada;
import excepciones.semanticas.ExcepcionConstructorDuplicado;
import excepciones.semanticas.ExcepcionConstructorMalNombrado;
import excepciones.semanticas.ExcepcionHerenciaPropia;
import excepciones.semanticas.ExcepcionMetodoDuplicado;
import excepciones.semanticas.ExcepcionMetodoMalHeredado;
import excepciones.semanticas.ExcepcionSemantica;
import modulos.GenCod;

public class Clase {
	
	private Token identificador;
	private String herencia;
	private Constructor constructor;
	private HashMap<String,Metodo> metodos;
	private HashMap<String,VarInstancia> atributos;
	private boolean estaActualizada;
	private int cantAtrib;
	private int cantMet;
	private int id;
	
	public Clase(Token iden, String padre) {
		identificador = iden;
		herencia = padre;
		constructor = null;
		metodos = new HashMap<String,Metodo>();
		atributos = new HashMap<String,VarInstancia>();
		estaActualizada = false;
		cantAtrib = 0;
		cantMet = 0;
		id = GenCod.generarNumClase();
	}
	
	public Clase(Token iden) {
		this(iden,"Object");
	}
	
	public void agregarAtributo(VarInstancia v) throws ExcepcionSemantica{
		String nombreAtrib = v.getToken().getLexema();
		if (atributos.containsKey(nombreAtrib)) {
			throw new ExcepcionAtributoDuplicado(nombreAtrib,v.getToken().getLinea());
		} else
			atributos.put(nombreAtrib, v);
	}
	
	public void agregarMetodo(Metodo m) throws ExcepcionSemantica {
		String nombreMet = m.getToken().getLexema();
		if (metodos.containsKey(nombreMet)) {
			throw new ExcepcionMetodoDuplicado(nombreMet,m.getToken().getLinea());
		} else
			metodos.put(nombreMet, m);
	}
	
	public Constructor getConstructor() {
		return constructor;
	}

	public void setConstructor(Constructor constructor) throws ExcepcionSemantica{
		// Realizo el chequeo que el constructor no haya sido declarado
		// Ademas, que el nombre del constructor sea el mismo de la clase
		if (constructor.getToken().getLexema().compareTo(identificador.getLexema())==0) {
			if (this.constructor!=null)
				throw new ExcepcionConstructorDuplicado(constructor.getToken().getLexema(),constructor.getToken().getLinea());
			else
				this.constructor = constructor;
		} else {
			throw new ExcepcionConstructorMalNombrado(constructor.getToken().getLexema(),constructor.getToken().getLinea());
		}		
	}

	public HashMap<String, Metodo> getMetodos() {
		return metodos;
	}

	public HashMap<String, VarInstancia> getAtributos() {
		return atributos;
	}

	public boolean estaActualizada() {
		return estaActualizada;
	}

	public void setEstaActualizada(boolean estaActualizada) {
		this.estaActualizada = estaActualizada;
	}

	public void setPadre(String padre) throws ExcepcionSemantica{
		if (identificador.getLexema().compareTo(padre)==0)
			throw new ExcepcionHerenciaPropia(padre,identificador.getLinea());
		else
			herencia = padre;
	}
	
	public String getNombreClase() {
		return identificador.getLexema();
	}
	
	public String getNombreClasePadre() {
		return herencia;
	}
	
	public Token getToken() {
		return identificador;
	}
	
	public void chequearDeclaraciones() throws ExcepcionSemantica {
		// Si la clase padre no fue declarada
		if (!this.getNombreClase().equals("Object")){
			if (!TablaSimbolos.clases.containsKey(herencia)) {
				throw new ExcepcionClaseNoDeclarada(herencia,identificador.getLinea());
			}
		}
		// Chequeo de Variables de instancia
		for (VarInstancia atrib : atributos.values()) {
			if (!atrib.getTipoVar().esTipoPrimitivo()) {
				if (!TablaSimbolos.clases.containsKey(atrib.getTipoVar().getNombreClase())) {
					throw new ExcepcionClaseNoDeclarada(atrib.getTipoVar().getNombreClase(),atrib.getToken().getLinea());
				}
			}				
		}
		// Chequeo de Metodos
		List<Metodo> listaDinamicos = new LinkedList<Metodo>();
		for (Metodo met : metodos.values()) {
			met.chequearDeclaraciones();
			if (!met.getEsEstatico())
				listaDinamicos.add(met);
		}
		if (constructor!=null) // constructor por ahora es null para System y para Object
			constructor.chequearDeclaraciones();  
		
		// Ordeno la lista de metodos dinamicos segun offset
		Collections.sort(listaDinamicos);
		// Genero la VT para la clase (con los met dinamicos ordenados)
		GenCod.data();
		if (listaDinamicos.isEmpty()) {
			GenCod.gen("VT_"+getLabel(),"NOP","VT de la clase "+getNombreClase());
		} else {
			GenCod.label("VT_"+getLabel());
			for (Metodo met: listaDinamicos) {
				GenCod.gen("DW "+met.getLabel(), "Metodo "+met.getToken().getLexema()+" cuyo offset es "+met.getOffset());
			}
		}
	}
	
	public void chequearSentencias() throws ExcepcionSemantica, IOException {
		TablaSimbolos.metodoActual = constructor;
		constructor.chequearSentencias();
		for (Metodo met: metodos.values()) {
			TablaSimbolos.metodoActual = met;
			// Reviso solo los metodos implementados por la clase, no los heredados
			if (met.getClaseImplementadora().getNombreClase().equals(TablaSimbolos.claseActual.getNombreClase()))
				met.chequearSentencias();
		}
	}
	
	public void chequearConstructor() throws ExcepcionSemantica {
		if (constructor==null) {
			constructor = new Constructor(new Token("idClase",this.getNombreClase(),0));
			constructor.setCodigo(new NodoBloque()); // le asigno un bloque de sentencias vacio 
		}
	}
	
	public void actualizar() throws ExcepcionSemantica {
		if (!estaActualizada) {
			if (!identificador.getLexema().equals("Object")) {
				Clase padre = TablaSimbolos.clases.get(herencia);
				padre.actualizar();
				
				// Seteo offset para atributos de instancia propios
				cantAtrib = padre.getCantAtrib();
				for (VarInstancia vi: atributos.values()) {
					vi.setOffset(cantAtrib+2);	// Para las var instancia offset+2 (por ref a VT y a ID Clase en CIR)
					cantAtrib++;
				}
				
				// Atributos de instancia heredados
				for (VarInstancia atrPadre: padre.getAtributos().values()) {
					String nombreAtrib = atrPadre.getToken().getLexema();
					if (atributos.containsKey(nombreAtrib) || !atrPadre.getEsPublico()) { 
						// atributo de instancia redefinido o no se puede heredar porque es privado
						nombreAtrib = "@" + nombreAtrib;
					}
					atributos.put(nombreAtrib, atrPadre);
				}
				
				// Metodos heredados
				for (Metodo metPadre : padre.getMetodos().values()) {
					if (metodos.containsKey(metPadre.getToken().getLexema())) { // padre e hijo tienen mismo metodo
						Metodo metHijo = metodos.get(metPadre.getToken().getLexema());
						if (!metHijo.comparar(metPadre))
							throw new ExcepcionMetodoMalHeredado(metHijo.getToken().getLexema(),metHijo.getToken().getLinea());
						metHijo.setOffset(metPadre.getOffset());	// "tapo" a mi metodo padre
					} else {
						metodos.put(metPadre.getToken().getLexema(), metPadre);
					}
				}
				
				// Seteo offset para metodos propios (dinamicos y que no hayan sido chequeados antes)
				cantMet = padre.getCantMet();
				for (Metodo met: metodos.values()) {
					if (!met.getEsEstatico() && !met.estaChequeado()) {	
						met.setOffset(cantMet++);
					}
				}

				estaActualizada = true;
			}
		}
	}
	
	public int getCantAtrib() {
		return cantAtrib;
	}

	public void setCantAtrib(int cantAtrib) {
		this.cantAtrib = cantAtrib;
	}

	public int getCantMet() {
		return cantMet;
	}

	public void setCantMet(int cantMet) {
		this.cantMet = cantMet;
	}
	
	public String getLabel() {
		return identificador.getLexema()+"_"+id;
	}
	
	public int getId() {
		return id;
	}

}
