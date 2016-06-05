package zeson.simpleC;

class SimpleCEvalError extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SimpleCEvalError(String message) {
		super(message);
	}

}

public class EvalVisitor implements IActionVisitor {

	Environment env;

	public EvalVisitor(Environment env) {
		super();
		this.env = env;
	}

	@Override
	public Value visit(BinOpExpr bin) {

		Value left = bin.left.accept(this);

		Value right = bin.right.accept(this);

		assert left != null;
		assert right != null;

		switch (bin.op) {
		case "+":
			return left.add(right);
		case "-":
			return left.sub(right);
		case "*":
			return left.mul(right);
		case "/":
			return left.div(right);
		case "<":
			return left.less(right);
		case ">":
			return left.greater(right);
		case "==":
			return left.eq(right);
		case "=":
			return left.assign(right);
		default:
			assert false;
			break;
		}

		return null;
	}

	@Override
	public Value visit(IntegerExpr integer) {
		return new IntegerValue(integer.val);
	}

	@Override
	public Value visit(IdentExpr identExpr) {
		Value v = env.getSymbol(identExpr.name);
		assert v != null;

		return v;

	}

	@Override
	public void visit(ExprStmt exprStmt) {
		exprStmt.exp.accept(this);
	}

	private Value handleBuiltInFunc(CallExpr callExpr) {
		switch (callExpr.ident.name) {
		case "println":
			assert callExpr.params.size() == 1;
			System.out.println(callExpr.params.get(0).accept(this));
			return new VoidValue();

		default:
			return null;
		}
	}

	@Override
	public Value visit(CallExpr callExpr) {
		Value builtinFuncV = handleBuiltInFunc(callExpr);
		if (builtinFuncV != null)
			return builtinFuncV;
		FunctionStmt f = env.getFunction(callExpr.ident.name);
		assert f != null;
		this.env.enterScope();
		assert callExpr.params.size() == f.arguments.size();

		for (int i = 0; i < callExpr.params.size(); i++) {

			Value value = callExpr.params.get(i).accept(this);

			assert f.arguments.get(i).type.equals(value.getValueType());

			this.env.addSymbol(f.arguments.get(i).ident.name, value);
		}

		env.isReturn = false;
		for (Stmt st : f.body.sts) {
			if (env.isReturn)
				break;
			st.accept(this);

		}

		this.env.outScope();
		assert this.env.returnValue != null;
		assert f.returnType.equals(this.env.returnValue.getValueType());
		env.isReturn = false;
		return this.env.returnValue;
	}

	@Override
	public void visit(FunctionStmt functionStmt) {
		FunctionStmt f = env.getFunction(functionStmt.name.name);
		System.out.println(f);
		// assert f == null;
		// env.addFunction(functionStmt.name.name, functionStmt);
	}

	@Override
	public void visit(BlockStmt blockStmt) {
		this.env.enterScope();
		for (Stmt st : blockStmt.sts) {
			if (env.isReturn)
				break;
			st.accept(this);
		}
		this.env.outScope();

	}

	@Override
	public void visit(VariableDeclStmt variableDeclStmt) {
		assert this.env.getSymbol(variableDeclStmt.ident.name) == null;

		if (variableDeclStmt.type.equals(Type.Int)) {
			this.env.addSymbol(variableDeclStmt.ident.name, new IntegerValue());
		} else {
			assert false;
		}
		if (variableDeclStmt.initalValueExpr != null) {
			Value initalValue = variableDeclStmt.initalValueExpr.accept(this);
			assert variableDeclStmt.type.equals(initalValue.getValueType());
			this.env.getSymbol(variableDeclStmt.ident.name).assign(initalValue);
		}

	}

	@Override
	public void visit(ReturnStmt returnStmt) {

		if (returnStmt.returnedExpr != null)
			this.env.returnValue = returnStmt.returnedExpr.accept(this);
		this.env.isReturn = true;
	}

	@Override
	public void visit(ForLoopStmt forLoopStmt) {
		this.env.enterScope();
		if (forLoopStmt.one != null)
			forLoopStmt.one.accept(this);

		forst: while (forLoopStmt.two == null
				|| ((BooleanValue) forLoopStmt.two.accept(this)).val) {
			for (Stmt st : forLoopStmt.body.sts) {
				if (env.isReturn)
					break forst;
				st.accept(this);

			}

			if (forLoopStmt.three != null)
				forLoopStmt.three.accept(this);

			// System.out.println(this.env);
		}

		this.env.enterScope();

	}

	@Override
	public void visit(IfStmt ifStmt) {
		assert ifStmt.prediction != null;

		Value predictionValue = ifStmt.prediction.accept(this);
		assert predictionValue.getClass() == BooleanValue.class;
		BooleanValue bool = (BooleanValue) predictionValue;
		if (bool.val) {
			assert ifStmt.body != null;
			ifStmt.body.accept(this);
		} else {
			if (ifStmt.elseStmt != null)
				ifStmt.elseStmt.accept(this);
		}
	}

	@Override
	public Value visit(StringLiteralExpr stringLiteralExpr) {

		return new StringValue(stringLiteralExpr.val);
	}
}