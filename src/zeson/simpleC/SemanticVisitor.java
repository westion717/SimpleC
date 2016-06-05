package zeson.simpleC;

import org.junit.Assume;

class SimpleCSemanticError extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SimpleCSemanticError(String message) {
		super(message);
	}

	public SimpleCSemanticError(String message, SourceLoc sourceLoc) {
		super(message + " at " + "line: " + sourceLoc.getLine() + ", column: "
				+ sourceLoc.getColumn());
	}

}

public class SemanticVisitor implements ITypeVisitor {

	Environment env;

	public SemanticVisitor(Environment env) {
		super();
		this.env = env;
	}

	@Override
	public Type visit(BinOpExpr bin) {

		Type left = bin.left.accept(this);

		Type right = bin.right.accept(this);

		Assume.assumeNotNull(left, right);

		if (!left.equals(right)) {
			throw new SimpleCSemanticError(
					"different types can not calculate, you try to " + left
							+ bin.op + right + " at" + bin.getSourceLoc());
		}

		switch (bin.op) {
		case "<":
		case ">":
		case "==":
			return Type.Bool;

		default:
			break;
		}

		return left;
	}

	@Override
	public Type visit(IntegerExpr integer) {
		return Type.Int;
	}

	@Override
	public Type visit(IdentExpr identExpr) {

		Value v = env.getSymbol(identExpr.name);
		if (v == null) {
			System.out.println(this.env);
			throw new SimpleCSemanticError("can not find the symbol "
					+ identExpr.name + " at " + identExpr.getSourceLoc());
		}

		return v.getValueType();

	}

	private Type handleBuiltIn(CallExpr callExpr) {

		switch (callExpr.ident.name) {
		case "println":
			Assume.assumeTrue(callExpr.params.size() == 1);
			callExpr.params.get(0).accept(this);
			return Type.Void;

		default:
			break;
		}
		return null;
	}

	@Override
	public Type visit(CallExpr callExpr) {
		Type builtInReturnType = handleBuiltIn(callExpr);
		if (builtInReturnType != null)
			return builtInReturnType;

		FunctionStmt f = env.funcs.get(callExpr.ident.name);

		if (f == null)
			throw new SimpleCSemanticError("can not find function called "
					+ callExpr.ident.name + " at "
					+ callExpr.ident.getSourceLoc());

		if (f.arguments.size() != callExpr.params.size())
			throw new SimpleCSemanticError(callExpr.ident.name
					+ " have different params size , defination is at"
					+ f.getSourceLoc() + callExpr.ident.getSourceLoc());

		for (int i = 0; i < callExpr.params.size(); i++) {

			Type type = callExpr.params.get(i).accept(this);
			if (!type.equals(f.arguments.get(i).type)) {
				throw new SimpleCSemanticError("function "
						+ callExpr.ident.name
						+ "'s parameter type is not match, expected type "
						+ f.arguments.get(i).type + " but actual type is "
						+ type, callExpr.params.get(i).getSourceLoc());
			}
		}

		return f.returnType;
	}

	@Override
	public void visit(ExprStmt exprStmt) {
		exprStmt.exp.accept(this);
	}

	@Override
	public void visit(FunctionStmt functionStmt) {

		FunctionStmt function = env.getFunction(functionStmt.name.name);
		/*
		 * if(function != null) throw new SimpleCSemanticError(
		 * "you have define the same name function" + functionStmt.name.name +
		 * "at" + functionStmt.getSourceLoc() + " previous defination is at " +
		 * function.getSourceLoc());
		 */
		int size = this.env.symbols.size();
		this.env.enterScope();

		for (VariableDeclStmt v : function.arguments) {

			if (v.type.equals(Type.Int)) {
				this.env.addSymbol(v.ident.name, new IntegerValue());
			} else {
				Assume.assumeTrue(false);
			}

		}
		ReturnStmt returnSt = null;
		Type actualType = null;
		for (Stmt st : function.body.sts) {
			if (st.getClass() == ReturnStmt.class) {
				returnSt = (ReturnStmt) st;
				actualType = returnSt.returnedExpr.accept(this);
			}
			st.accept(this);

		}
		this.env.outScope();
		Assume.assumeTrue(size == this.env.symbols.size());

		if (returnSt == null && !function.returnType.equals(Type.Void))
			throw new SimpleCSemanticError("the return type expected "
					+ function.returnType + " defined at"
					+ function.getSourceLoc()
					+ ". but there is no return statment in function body"
					+ function.body.getSourceLoc());

		if (!actualType.equals(function.returnType))
			throw new SimpleCSemanticError("the return type expected "
					+ function.returnType + " defined at"
					+ function.getSourceLoc() + ". but actual type is"
					+ actualType, returnSt.getSourceLoc());

		// env.addFunction(functionStmt.name.name, functionStmt);
	}

	@Override
	public void visit(BlockStmt blockStmt) {
		int size = this.env.symbols.size();
		this.env.enterScope();
		for (Stmt st : blockStmt.sts) {
			st.accept(this);
		}
		this.env.outScope();
		Assume.assumeTrue(size == this.env.symbols.size());
	}

	@Override
	public void visit(VariableDeclStmt variableDeclStmt) {
		Value value = this.env.symbols.lastElement().get(
				variableDeclStmt.ident.name);
		if (value != null)
			throw new SimpleCSemanticError(
					"you have define the same name variable"
							+ variableDeclStmt.ident.name + "at"
							+ variableDeclStmt.ident.getSourceLoc());
		if (variableDeclStmt.initalValueExpr != null) {
			Type expType = variableDeclStmt.initalValueExpr.accept(this);

			if (!variableDeclStmt.type.equals(expType)) {
				throw new SimpleCSemanticError("You can not intial"
						+ variableDeclStmt.ident.name + "with type" + expType,
						variableDeclStmt.getSourceLoc());
			}
		}

		if (variableDeclStmt.type.equals(Type.Int)) {
			this.env.addSymbol(variableDeclStmt.ident.name, new IntegerValue());
		} else {
			Assume.assumeTrue(false);
		}

	}

	@Override
	public void visit(ReturnStmt returnStmt) {
		returnStmt.returnedExpr.accept(this);

	}

	@Override
	public void visit(ForLoopStmt forLoopStmt) {
		int size = this.env.symbols.size();
		this.env.enterScope();
		if (forLoopStmt.one != null)
			forLoopStmt.one.accept(this);
		if (forLoopStmt.two != null) {
			Type isTrue = forLoopStmt.two.accept(this);
			if (!isTrue.equals(Type.Bool)) {
				throw new SimpleCSemanticError(
						"the second exp in for statment must be a bool exp",
						forLoopStmt.two.getSourceLoc());
			}
		}
		if (forLoopStmt.three != null)
			forLoopStmt.three.accept(this);

		for (Stmt st : forLoopStmt.body.sts) {
			st.accept(this);
		}
		this.env.outScope();
		Assume.assumeTrue(size == this.env.symbols.size());
	}

	@Override
	public void visit(IfStmt ifStmt) {
		Type isTrue = ifStmt.prediction.accept(this);
		if (!isTrue.equals(Type.Bool)) {
			throw new SimpleCSemanticError(
					"the exp in if() must be a bool exp",
					ifStmt.prediction.getSourceLoc());
		}
		ifStmt.body.accept(this);

		if (ifStmt.elseStmt != null) {

			if (!(ifStmt.elseStmt.getClass() == IfStmt.class || ifStmt.elseStmt
					.getClass() == BlockStmt.class)) {
				throw new SimpleCSemanticError(
						"there must be a {} enclosing the statment after `else`",
						ifStmt.elseStmt.getSourceLoc());
			}
			ifStmt.elseStmt.accept(this);
		}
	}

	@Override
	public Type visit(StringLiteralExpr stringLiteralExpr) {

		return Type.String;
	}
}