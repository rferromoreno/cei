package estructuras.ts.variables;

import estructuras.Token;
import estructuras.ts.tipos.Tipo;

public abstract class Variable implements Comparable<Variable> {
	
	private Tipo tipoVar;
	private Token identificador;
	private int offset;
	
	public Variable(Token id, Tipo tipo) {
		identificador = id;
		tipoVar = tipo;
	}
	
	public Tipo getTipoVar() {
		return tipoVar;
	}
	public void setTipoVar(Tipo tipoVar) {
		this.tipoVar = tipoVar;
	}
	public Token getToken() {
		return identificador;
	}
	public void setIdentificador(Token identificador) {
		this.identificador = identificador;
	}
	public int getOffset() {
		return offset;
	}
	public void setOffset(int offset) {
		this.offset = offset;
	}
	
	@Override
	public int compareTo(Variable var) {
		return new Integer(this.getOffset()).compareTo(new Integer(var.getOffset()));
	}
	

}
