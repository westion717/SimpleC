package zeson.simpleC;

import java.util.Vector;

import org.junit.Assume;

class SourceLoc {
	private int line;
	private int column;

	public SourceLoc(int line, int column) {
		super();
		this.line = line;
		this.column = column;
	}

	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	@Override
	public String toString() {
		return "SourceLoc [line=" + line + ", column=" + column + "]";
	}

}

enum Type {
	Int, Double, String, Bool, Void
}

abstract class AST {
	public abstract SourceLoc getSourceLoc();
}

abstract class Expr extends AST {
	public abstract Value accept(IActionVisitor v);

	public abstract Type accept(ITypeVisitor v);
}

class BinOpExpr extends Expr {

	Expr left;
	Expr right;
	String op;

	public BinOpExpr(Expr left, Expr right, String op) {
		super();
		this.left = left;
		this.right = right;
		this.op = op;
	}

	@Override
	public Value accept(IActionVisitor v) {
		return v.visit(this);
	}

	@Override
	public SourceLoc getSourceLoc() {
		return left.getSourceLoc();
	}

	@Override
	public Type accept(ITypeVisitor v) {
		return v.visit(this);
	}

	@Override
	public String toString() {
		return "BinOpExpr [left=" + left + ", right=" + right + ", op=" + op
				+ "]";
	}

}

class IntegerExpr extends Expr {

	int val;

	SourceLoc sourceLoc;

	public IntegerExpr(int line, int column, int val) {
		this.val = val;
		sourceLoc = new SourceLoc(line, column);
	}

	@Override
	public Value accept(IActionVisitor v) {
		return v.visit(this);

	}

	@Override
	public String toString() {
		return "" + val;
	}

	@Override
	public SourceLoc getSourceLoc() {
		return sourceLoc;
	}

	@Override
	public Type accept(ITypeVisitor v) {
		return v.visit(this);
	}

}

class IdentExpr extends Expr {
	String name;

	SourceLoc sourceLoc;

	public IdentExpr(int line, int column, String name) {
		this.name = name;
		sourceLoc = new SourceLoc(line, column);
	}

	@Override
	public Value accept(IActionVisitor v) {
		return v.visit(this);

	}

	public IdentExpr(String name, SourceLoc sourceLoc) {
		super();
		this.name = name;
		this.sourceLoc = sourceLoc;
	}

	@Override
	public SourceLoc getSourceLoc() {
		return sourceLoc;
	}

	@Override
	public Type accept(ITypeVisitor v) {
		return v.visit(this);
	}

	@Override
	public String toString() {
		return name;
	}

}

class StringLiteralExpr extends Expr {
	String val;

	SourceLoc sourceLoc;

	public StringLiteralExpr(String val, int line, int column) {
		super();
		this.val = val;
		this.sourceLoc = new SourceLoc(line, column);
	}

	@Override
	public Value accept(IActionVisitor v) {
		return v.visit(this);

	}

	@Override
	public SourceLoc getSourceLoc() {
		return sourceLoc;
	}

	@Override
	public Type accept(ITypeVisitor v) {
		return v.visit(this);
	}

	@Override
	public String toString() {
		return val;
	}

}

class CallExpr extends Expr {
	IdentExpr ident;

	Vector<Expr> params;

	public CallExpr(IdentExpr ident, Vector<Expr> params) {
		super();
		this.ident = ident;
		this.params = params;
	}

	@Override
	public Value accept(IActionVisitor v) {
		return v.visit(this);

	}

	@Override
	public SourceLoc getSourceLoc() {

		return ident.getSourceLoc();
	}

	@Override
	public Type accept(ITypeVisitor v) {
		return v.visit(this);
	}

	@Override
	public String toString() {
		return "CallExpr [ident=" + ident + ", params=" + params + "]";
	}

}

abstract class Stmt extends AST {
	public abstract void accept(IActionVisitor v);

	public abstract void accept(ITypeVisitor v);

}

class VariableDeclStmt extends Stmt {

	Type type;
	IdentExpr ident;
	Expr initalValueExpr;

	public VariableDeclStmt(Type type, IdentExpr ident, Expr initalValueExpr) {
		super();
		this.type = type;
		this.ident = ident;
		this.initalValueExpr = initalValueExpr;
	}

	@Override
	public void accept(IActionVisitor v) {
		v.visit(this);

	}

	@Override
	public SourceLoc getSourceLoc() {
		return ident.getSourceLoc();
	}

	@Override
	public void accept(ITypeVisitor v) {
		v.visit(this);

	}

	@Override
	public String toString() {
		String str = type + " " + ident;
		if (initalValueExpr != null)
			str += " = " + initalValueExpr;
		return str;
	}

}

class ReturnStmt extends Stmt {

	Expr returnedExpr;

	public ReturnStmt(Expr returnedExpr) {
		super();
		this.returnedExpr = returnedExpr;
	}

	@Override
	public void accept(IActionVisitor v) {
		v.visit(this);

	}

	@Override
	public SourceLoc getSourceLoc() {
		if (returnedExpr != null) {
			return returnedExpr.getSourceLoc();
		}
		Assume.assumeTrue(false);
		return null;
	}

	@Override
	public void accept(ITypeVisitor v) {
		v.visit(this);

	}

}

class ExprStmt extends Stmt {
	Expr exp;

	public ExprStmt(Expr exp) {
		super();
		this.exp = exp;
	}

	@Override
	public void accept(IActionVisitor v) {
		v.visit(this);

	}

	@Override
	public SourceLoc getSourceLoc() {
		return exp.getSourceLoc();
	}

	@Override
	public void accept(ITypeVisitor v) {
		v.visit(this);

	}

}

class BlockStmt extends Stmt {
	Vector<Stmt> sts;

	public BlockStmt(Vector<Stmt> sts) {
		super();
		this.sts = sts;
	}

	@Override
	public void accept(IActionVisitor v) {
		v.visit(this);

	}

	@Override
	public SourceLoc getSourceLoc() {

		// Assume.assumeTrue(false);

		return null;
	}

	@Override
	public void accept(ITypeVisitor v) {
		v.visit(this);

	}

}

class ForLoopStmt extends Stmt {
	Stmt one;
	Expr two;
	Stmt three;
	BlockStmt body;

	public ForLoopStmt(Stmt one, Expr two, Stmt three, BlockStmt body) {
		super();
		this.one = one;
		this.two = two;
		this.three = three;
		this.body = body;
	}

	@Override
	public void accept(IActionVisitor v) {
		v.visit(this);

	}

	@Override
	public SourceLoc getSourceLoc() {

		Assume.assumeTrue(false);

		return null;
	}

	@Override
	public void accept(ITypeVisitor v) {
		v.visit(this);

	}

}

class FunctionStmt extends Stmt {

	Type returnType;
	IdentExpr name;
	Vector<VariableDeclStmt> arguments;
	BlockStmt body;

	public FunctionStmt(Type returnType, IdentExpr name,
			Vector<VariableDeclStmt> arguments, BlockStmt body) {
		super();
		this.returnType = returnType;
		this.name = name;
		this.arguments = arguments;
		this.body = body;
	}

	@Override
	public void accept(IActionVisitor v) {
		v.visit(this);

	}

	@Override
	public SourceLoc getSourceLoc() {
		return name.getSourceLoc();
	}

	@Override
	public void accept(ITypeVisitor v) {
		v.visit(this);

	}

	@Override
	public String toString() {
		return "FunctionStmt [returnType=" + returnType + ", name=" + name
				+ ", arguments=" + arguments + ", body=" + body + "]";
	}

}

class IfStmt extends Stmt {

	Expr prediction;
	BlockStmt body;
	Stmt elseStmt;

	public IfStmt(Expr prediction, BlockStmt body, Stmt elseStmt) {
		super();
		this.prediction = prediction;
		this.body = body;
		this.elseStmt = elseStmt;
	}

	@Override
	public void accept(IActionVisitor v) {
		v.visit(this);

	}

	@Override
	public SourceLoc getSourceLoc() {
		return prediction.getSourceLoc();
	}

	@Override
	public void accept(ITypeVisitor v) {
		v.visit(this);

	}

}