package estructuras.ts;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import estructuras.Token;
import estructuras.ast.sentencias.NodoBloque;
import estructuras.ts.variables.VarLocal;
import estructuras.ts.variables.VarParametro;
import estructuras.ts.variables.Variable;
import excepciones.semanticas.ExcepcionParametroDuplicado;
import excepciones.semanticas.ExcepcionSemantica;
import excepciones.semanticas.ExcepcionVarLocalDuplicada;
import modulos.GenCod;

public abstract class Unidad {
	
	private Token identificador;
	private NodoBloque codigo;
	private LinkedList<VarParametro> listaParametros;
	private HashMap<String,Variable> tablaVariables;
	private int offset;
	private String label;
	private boolean chequeado;
	
	public Unidad(Token id, LinkedList<VarParametro> listaPar, HashMap<String,Variable> tablaVar) {
		identificador = id;
		listaParametros = listaPar;
		tablaVariables = tablaVar;
		codigo = new NodoBloque();
		offset = -1;
		label = "met_"+id.getLexema()+"_"+GenCod.generarEtiqueta();
		chequeado = false;
	}
	
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Unidad(Token id) {
		this(id,null,null);
		listaParametros = new LinkedList<VarParametro>();
		tablaVariables = new HashMap<String,Variable>();
	}
	
	public Unidad(Token id, LinkedList<VarParametro> listaPar) {
		this(id);
		setListaParametros(listaPar);
	}
	
	public void agregarParametro(VarParametro var) throws ExcepcionSemantica {
		if (tablaVariables.containsKey(var.getToken().getLexema()))
			throw new ExcepcionParametroDuplicado(var.getToken().getLexema(),var.getToken().getLinea());
		else {
			tablaVariables.put(var.getToken().getLexema(), var);
			listaParametros.addLast(var);
		}
	}
	
	public void agregarVariable(VarLocal var) throws ExcepcionSemantica {
		if (tablaVariables.containsKey(var.getToken().getLexema()))
			throw new ExcepcionVarLocalDuplicada(var.getToken().getLexema(),var.getToken().getLinea());
		else {
			tablaVariables.put(var.getToken().getLexema(), var);
			int cantVarsLocales = tablaVariables.size() - listaParametros.size();
			var.setOffset(-(cantVarsLocales-1));
			// TODO Revisar
		}
	}
	
	public abstract void chequearDeclaraciones() throws ExcepcionSemantica, IOException;
	public abstract void chequearSentencias() throws ExcepcionSemantica, IOException;
	
	public Token getToken() {
		return identificador;
	}

	public void setIdentificador(Token identificador) {
		this.identificador = identificador;
	}

	public NodoBloque getCodigo() {
		return codigo;
	}

	public void setCodigo(NodoBloque codigo) {
		this.codigo = codigo;
	}

	public LinkedList<VarParametro> getListaParametros() {
		return listaParametros;
	}

	public void setListaParametros(LinkedList<VarParametro> listaParametros) {
		this.listaParametros = listaParametros;
	}

	public HashMap<String, Variable> getTablaVariables() {
		return tablaVariables;
	}

	public void setTablaVariables(HashMap<String, Variable> tablaVariables) {
		this.tablaVariables = tablaVariables;
	}	
	
	public abstract boolean esConstructor();
	
	public int getOffset() {
		return offset;
	}
	
	public void setOffset(int offset) { 
		this.offset = offset;
		chequeado = true;
	}
	
	public boolean estaChequeado() {
		return chequeado;
	}
	
}
