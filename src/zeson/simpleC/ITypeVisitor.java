package zeson.simpleC;

public interface ITypeVisitor {

	public Type visit(BinOpExpr bin);

	public Type visit(IntegerExpr integer);

	public Type visit(IdentExpr identExpr);

	public void visit(VariableDeclStmt intDeclStmt);

	public void visit(ExprStmt exprStmt);

	public Type visit(CallExpr callExpr);

	public void visit(FunctionStmt functionStmt);

	public void visit(BlockStmt blockStmt);

	public void visit(ReturnStmt returnStmt);

	public void visit(ForLoopStmt forLoopStmt);

	public void visit(IfStmt ifStmt);

	public Type visit(StringLiteralExpr stringLiteralExpr);

}
