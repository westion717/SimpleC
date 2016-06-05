package zeson.simpleC;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.Vector;

import org.junit.Assume;

public class Environment {

	Stack<Map<String, Value>> symbols = new Stack<>();
	Map<String, FunctionStmt> funcs = new HashMap<String, FunctionStmt>();

	Value returnValue;

	boolean isReturn;

	public Environment() {
		this.symbols.add(new HashMap<String, Value>());
		initalBuiltinFunc();
	}

	private void initalBuiltinFunc() {
		Vector<VariableDeclStmt> println_args = new Vector<>();
		println_args.add(new VariableDeclStmt(Type.Int, new IdentExpr("num",
				null), null));
		addFunction("println", new FunctionStmt(Type.Void, new IdentExpr(
				"println", null), println_args, null));
	}

	public Value getSymbol(String name) {

		for (int i = symbols.size() - 1; i >= 0; i--) {
			Value v = symbols.get(i).get(name);
			if (v != null)
				return v;
		}
		return null;
	}

	public FunctionStmt getFunction(String name) {

		return funcs.get(name);
	}

	public boolean addSymbol(String name, Value v) {

		Assume.assumeNotNull(symbols.lastElement());
		symbols.lastElement().put(name, v);
		return true;
	}

	public boolean addFunction(String name, FunctionStmt f) {

		funcs.put(name, f);
		return true;
	}

	public void enterScope() {
		symbols.push(new HashMap<String, Value>());

	}

	public void outScope() {
		symbols.pop();

	}

	@Override
	public String toString() {
		return "Environment [symbols=" + symbols + ", funcs=" + funcs
				+ ", returnValue=" + returnValue + ", isReturn=" + isReturn
				+ "]";
	}

}
