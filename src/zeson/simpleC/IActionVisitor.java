package zeson.simpleC;

public interface IActionVisitor {

	public Value visit(BinOpExpr bin);

	public Value visit(IntegerExpr integer);

	public Value visit(IdentExpr identExpr);

	public void visit(ExprStmt exprStmt);

	public Value visit(CallExpr callExpr);

	public void visit(FunctionStmt functionStmt);

	public void visit(BlockStmt blockStmt);

	public void visit(VariableDeclStmt variableDeclStmt);

	public void visit(ReturnStmt returnStmt);

	public void visit(ForLoopStmt forLoopStmt);

	public void visit(IfStmt ifStmt);

	public Value visit(StringLiteralExpr stringLiteralExpr);

}
