package estructuras.ts;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;

import estructuras.Token;
import estructuras.ts.variables.VarParametro;
import estructuras.ts.variables.Variable;
import excepciones.semanticas.ExcepcionClaseNoDeclarada;
import excepciones.semanticas.ExcepcionSemantica;
import modulos.GenCod;

public class Constructor extends Unidad {
	
	public Constructor(Token id) {
		super(id);
	}

	public void chequearDeclaraciones() throws ExcepcionSemantica {
		// Variables del Constructor
		for (Variable var : this.getTablaVariables().values()) {
			// TODO: Reemplazar por la nueva implementacion de esValido. Revisar que pasa con void
			if (!var.getTipoVar().esTipoPrimitivo()) {
				if (!TablaSimbolos.clases.containsKey(var.getTipoVar().getNombreClase())) {
					throw new ExcepcionClaseNoDeclarada(var.getTipoVar().getNombreClase(),var.getToken().getLinea());
				}
			}				
		}
	}
	
	public boolean esConstructor() {
		return true;
	}
	
	public void chequearSentencias() throws ExcepcionSemantica, IOException {
		LinkedList<VarParametro> listaParam = new LinkedList<VarParametro>(this.getListaParametros());
		Collections.sort(listaParam);
		int cantParametros = this.getListaParametros().size();
		for (int i=0; i<listaParam.size(); i++) {
				listaParam.get(i).setOffset(cantParametros+3-i);
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

		GenCod.gen("RET "+(cantParametros+1),"Retorna de la llamada, liberando parametros");
	}
	

}
