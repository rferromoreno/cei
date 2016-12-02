package estructuras.ast.sentencias;

import java.util.HashMap;
import java.util.LinkedList;

import estructuras.ts.TablaSimbolos;
import estructuras.ts.variables.VarLocal;
import estructuras.ts.variables.Variable;
import excepciones.semanticas.ExcepcionSemantica;
import excepciones.semanticas.ExcepcionVarLocalDuplicada;
import modulos.GenCod;

public class NodoBloque extends NodoSentencia {

	private LinkedList<NodoSentencia> sentencias;
	private HashMap<String, Variable> varsBloque;
	
	public NodoBloque() {
		sentencias = new LinkedList<NodoSentencia>();
		varsBloque = new HashMap<String, Variable>();
	}

	public NodoBloque(LinkedList<NodoSentencia> listaSent) {
		sentencias = listaSent;
		varsBloque = new HashMap<String, Variable>();
	}
	
	public NodoBloque(LinkedList<NodoSentencia> listaSent, HashMap<String,Variable> variables) {
		sentencias = listaSent;
		varsBloque = new HashMap<String, Variable>(variables);
	}
	
	public LinkedList<NodoSentencia> getSentencias() {
		return sentencias;
	}

	public void setSentencias(LinkedList<NodoSentencia> sentencias) {
		this.sentencias = sentencias;
	}
	
	public HashMap<String, Variable> getVariables() {
		return varsBloque;
	}
	
	public void setVariables(HashMap<String,Variable> varsBloque) {
		this.varsBloque = varsBloque;
	}
	
	public void agregarVariable(VarLocal var) throws ExcepcionSemantica {
		if (varsBloque.containsKey(var.getToken().getLexema()))
			throw new ExcepcionVarLocalDuplicada(var.getToken().getLexema(),var.getToken().getLinea());
		else {
			varsBloque.put(var.getToken().getLexema(), var);
		}
	}

	public void chequear() throws ExcepcionSemantica {
		NodoBloque anidado = TablaSimbolos.bloqueActual;
		TablaSimbolos.bloqueActual = this;

		for (NodoSentencia sent : sentencias) {
			sent.chequear();
		}
		
		HashMap<String, Variable> tablaVariables = TablaSimbolos.metodoActual.getTablaVariables();
		for (String nombreVar : varsBloque.keySet()) {
			tablaVariables.remove(nombreVar);
		}
		
		int cantVarsLocales = varsBloque.size();
		if (cantVarsLocales >0)
			GenCod.gen("FMEM "+cantVarsLocales,"Libero las variables locales declaradas dentro de mi bloque");

		TablaSimbolos.bloqueActual = anidado;
	}

}
