package zeson.simpleC;

public class PrintVisitor implements IActionVisitor {

	@Override
	public Value visit(BinOpExpr bin) {
		System.out.print("(");
		bin.left.accept(this);
		System.out.print(bin.op);
		bin.right.accept(this);
		System.out.print(")");
		return null;
	}

	@Override
	public Value visit(IntegerExpr integer) {
		System.out.print(integer.val);
		return null;
	}

	@Override
	public Value visit(IdentExpr identExpr) {
		System.out.print(identExpr.name);
		return null;
	}

	@Override
	public Value visit(CallExpr callExpr) {
		callExpr.ident.accept(this);
		System.out.print("(");
		System.out.print(callExpr.params);
		System.out.print(")");
		return null;
	}

	@Override
	public void visit(ExprStmt exprStmt) {
		exprStmt.exp.accept(this);
		System.out.print(";");

	}

	@Override
	public void visit(FunctionStmt functionStmt) {
		System.out.print(functionStmt.returnType);
		System.out.print(" " + functionStmt.name.name);
		System.out.print("(");
		System.out.print(functionStmt.arguments);
		System.out.print(")");
		System.out.println("");
		functionStmt.body.accept(this);
	}

	@Override
	public void visit(BlockStmt blockStmt) {
		System.out.println("{");
		for (Stmt st : blockStmt.sts) {
			st.accept(this);
			System.out.println();
		}
		System.out.println("}");
	}

	@Override
	public void visit(VariableDeclStmt variableDeclStmt) {
		System.out.print(variableDeclStmt.type + " ");
		variableDeclStmt.ident.accept(this);
		if (variableDeclStmt.initalValueExpr != null) {
			System.out.print(" = ");
			variableDeclStmt.initalValueExpr.accept(this);

		}

		System.out.print(";");

	}

	@Override
	public void visit(ReturnStmt returnStmt) {
		System.out.print("return ");
		returnStmt.returnedExpr.accept(this);
		System.out.print(";");
	}

	@Override
	public void visit(ForLoopStmt forLoopStmt) {
		System.out.print("for(");
		if (forLoopStmt.one != null) {
			forLoopStmt.one.accept(this);
		} else {
			System.out.print(";");
		}

		if (forLoopStmt.two != null)
			forLoopStmt.two.accept(this);
		System.out.print(";");
		if (forLoopStmt.three != null)
			forLoopStmt.three.accept(this);
		System.out.print(")");
		System.out.println();
		forLoopStmt.body.accept(this);
	}

	@Override
	public void visit(IfStmt ifStmt) {
		System.out.print("if(");
		ifStmt.prediction.accept(this);
		System.out.print(")");
		ifStmt.body.accept(this);

		if (ifStmt.elseStmt != null) {
			System.out.print("else");
			ifStmt.elseStmt.accept(this);
		}
	}

	@Override
	public Value visit(StringLiteralExpr stringLiteralExpr) {
		System.out.print(stringLiteralExpr.val);
		return null;
	}

}
