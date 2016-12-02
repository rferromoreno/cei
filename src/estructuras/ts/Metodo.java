package estructuras.ts;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;

import estructuras.Token;
import estructuras.ts.tipos.TipoMetodo;
import estructuras.ts.variables.VarParametro;
import estructuras.ts.variables.Variable;
import excepciones.semanticas.ExcepcionClaseNoDeclarada;
import excepciones.semanticas.ExcepcionSemantica;
import modulos.GenCod;

public class Metodo extends Unidad implements Comparable<Metodo> {
	
	private boolean esEstatico;
	private TipoMetodo tipoRetorno;
	private Clase claseImplementadora;
		
	public Metodo(Token id, boolean estatico, TipoMetodo retorno, Clase cla){
		super(id);
		esEstatico = estatico;
		tipoRetorno = retorno;
		setClaseImplementadora(cla);
	}
	
	public Metodo(Token id, LinkedList<VarParametro> listaPar, boolean estatico, TipoMetodo retorno, Clase cla){
		super(id,listaPar);
		esEstatico = estatico;
		tipoRetorno = retorno;
		setClaseImplementadora(cla);
	}
	
	public boolean getEsEstatico(){
		return esEstatico;
	}

	public void setEsEstatico(boolean esEstatico) {
		this.esEstatico = esEstatico;
	}

	public TipoMetodo getTipoRetorno() {
		return tipoRetorno;
	}

	public void setTipoRetorno(TipoMetodo tipoRetorno) {
		this.tipoRetorno = tipoRetorno;
	}
	
	public boolean esConstructor() {
		return false;
	}

	public void chequearDeclaraciones() throws ExcepcionSemantica {
		// Chequeo que el tipo de retorno sea valido
		if (!tipoRetorno.esVoid()) { 
			if (!tipoRetorno.esValido()) {
				TipoMetodo ret = tipoRetorno;
				throw new ExcepcionClaseNoDeclarada(ret.getNombreClase(),this.getToken().getLinea());
			}
		}
		// Chequeo que los args y vars locales sean validos
		for (Variable var : this.getTablaVariables().values()) {
			// TODO: Reemplazar por la nueva implementacion de esValido. Revisar que pasa con void
			if (!var.getTipoVar().esTipoPrimitivo()) {
				if (!TablaSimbolos.clases.containsKey(var.getTipoVar().getNombreClase())) {
					throw new ExcepcionClaseNoDeclarada(var.getTipoVar().getNombreClase(),var.getToken().getLinea());
				}
			}				
		}
		
	}

	/* Metodo auxiliar para comparar que un metodo sea igual a otro,
	 * Tanto en cuanto a signatura: forma, tipo de retorno, nombre
	 * Cantidad y tipo de argumentos formales.
	 */
	public boolean comparar(Metodo met) {
		if (!this.getToken().getLexema().equals(met.getToken().getLexema())) // nombre. redundante porque solo se va a invocar cuando tengan mismo nombre
			return false; 
		else  if (esEstatico!=met.getEsEstatico()) // forma
			return false; 
		else if (!tipoRetorno.equals(met.getTipoRetorno())) // tipo de retorno
			return false; 
		else if (this.getListaParametros().size()!=met.getListaParametros().size()) // cant parametros
			return false;
		else {	// para cada uno de los argumentos formales reviso si el tipo es igual
			LinkedList<VarParametro> listaArgsMet = met.getListaParametros();
			LinkedList<VarParametro> listaArgsThis = this.getListaParametros();
			VarParametro argMet, argThis = null;
			for (int i = 0; i < listaArgsMet.size(); i++) {
				argMet = listaArgsMet.get(i);
				argThis = listaArgsThis.get(i);
				if (!argThis.getTipoVar().equals(argMet.getTipoVar()))
					return false;
			}
		}
		return true;
	}

	public Clase getClaseImplementadora() {
		return claseImplementadora;
	}

	public void setClaseImplementadora(Clase claseImplementadora) {
		this.claseImplementadora = claseImplementadora;
	}
	
	public void chequearSentencias() throws ExcepcionSemantica, IOException {
		
		LinkedList<VarParametro> listaParam = new LinkedList<VarParametro>(this.getListaParametros());
		Collections.sort(listaParam);
		int cantParametros = this.getListaParametros().size();
		for (int i=0; i<listaParam.size(); i++) {
			if (esEstatico) {
				listaParam.get(i).setOffset(cantParametros+2-i);
			} else {
				listaParam.get(i).setOffset(cantParametros+3-i);
			}
		}
		
		GenCod.gen(getLabel(),"LOADFP", "Guardar enlace dinamico");
		GenCod.gen("LOADSP", "Inicializar el FP");
		GenCod.gen("STOREFP", "");

		int cantVarsLocales = this.getTablaVariables().size() - this.getListaParametros().size();
		if (cantVarsLocales > 0)
			GenCod.gen("RMEM "+cantVarsLocales, "Reservar espacio para variables locales");

		// Se genera todo el codigo del bloque
		TablaSimbolos.bloqueActual = this.getCodigo();
		TablaSimbolos.bloqueActual.chequear();
		
		if (cantVarsLocales > 0)
			GenCod.gen("FMEM "+cantVarsLocales, "Liberar espacio para variables locales");

		GenCod.gen("STOREFP", "Reestablece el contexto");
		
		if (!esEstatico)
			cantParametros++; // si el metodo es dinamico, cuento al "this"
		
		GenCod.gen("RET "+cantParametros,"Retorna de la llamada, liberando parametros");
	
	}

	@Override
	public int compareTo(Metodo met) {
		return new Integer(this.getOffset()).compareTo(new Integer(met.getOffset()));
	}
	
}
