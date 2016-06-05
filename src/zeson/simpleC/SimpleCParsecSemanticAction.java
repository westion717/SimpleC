package zeson.simpleC;

import java.util.List;
import java.util.Vector;

import org.junit.Assume;

import Zeson.AZLRJ.common.IParsec;
import Zeson.AZLRJ.common.Source;
import Zeson.AZLRJ.parsec.action.BinOperatorSemanticAction;
import Zeson.AZLRJ.parsec.action.ParsecLiteralSemanticAction;
import Zeson.AZLRJ.parsec.action.ParsecObjectsSemanticAction;

public class SimpleCParsecSemanticAction {

	static final ParsecLiteralSemanticAction wordAction = new ParsecLiteralSemanticAction() {

		@Override
		public Object doAction(String word, Source source) {

			return word;
		}
	};

	static final ParsecLiteralSemanticAction integerAcion = new ParsecLiteralSemanticAction() {

		@Override
		public Object doAction(String word, Source source) {

			return new IntegerExpr(source.line, source.column,
					Integer.parseInt(word));
		}
	};

	static final ParsecLiteralSemanticAction identAcion = new ParsecLiteralSemanticAction() {

		@Override
		public Object doAction(String word, Source source) {

			return new IdentExpr(source.line, source.column, word);
		}
	};

	static final ParsecObjectsSemanticAction callExp0Acion = new ParsecObjectsSemanticAction() {

		@Override
		public Object doAction(List<Object> resultObjects) {
			Assume.assumeNotNull(resultObjects.get(0));
			Assume.assumeTrue(resultObjects.get(0).getClass() == IdentExpr.class);

			return new CallExpr((IdentExpr) resultObjects.get(0),
					new Vector<Expr>());
		}
	};

	static final BinOperatorSemanticAction binOpAction = new BinOperatorSemanticAction() {

		@Override
		public Object doAction(IParsec op, Object left, Object right) {

			Assume.assumeNotNull(left, right);
			Assume.assumeTrue(left instanceof Expr);
			Assume.assumeTrue(right instanceof Expr);

			String opStr = null;

			if (op == Grammar.add) {
				opStr = "+";
			} else if (op == Grammar.sub) {
				opStr = "-";

			} else if (op == Grammar.mul) {
				opStr = "*";

			} else if (op == Grammar.div) {
				opStr = "/";

			} else if (op == Grammar.assign) {
				opStr = "=";

			} else if (op == Grammar.eq) {
				opStr = "==";

			} else if (op == Grammar.less) {
				opStr = "<";

			} else if (op == Grammar.greater) {
				opStr = ">";

			}

			return new BinOpExpr((Expr) left, (Expr) right, opStr);
		}

	};

	static final ParsecObjectsSemanticAction paramAcion = new ParsecObjectsSemanticAction() {

		@Override
		public Object doAction(List<Object> resultObjects) {
			Assume.assumeNotNull(resultObjects.get(1));
			Assume.assumeTrue(resultObjects.get(1) instanceof Expr);

			return resultObjects.get(1);

		}
	};
	static final ParsecObjectsSemanticAction paramsListAcion = new ParsecObjectsSemanticAction() {

		@Override
		public Object doAction(List<Object> resultObjects) {

			Vector<Expr> params = new Vector<Expr>();

			for (Object obj : resultObjects) {
				Assume.assumeNotNull(obj);
				Assume.assumeTrue(obj instanceof Expr);

				params.add((Expr) obj);
			}

			return params;
		}
	};

	static final ParsecObjectsSemanticAction callExp1Acion = new ParsecObjectsSemanticAction() {

		@Override
		public Object doAction(List<Object> resultObjects) {
			Assume.assumeNotNull(resultObjects.get(0));
			Assume.assumeTrue(resultObjects.get(0).getClass() == IdentExpr.class);

			Assume.assumeNotNull(resultObjects.get(2));
			Assume.assumeTrue(resultObjects.get(2) instanceof Expr);

			Assume.assumeNotNull(resultObjects.get(3));
			Assume.assumeTrue(resultObjects.get(3).getClass() == Vector.class);

			@SuppressWarnings("unchecked")
			Vector<Expr> params = (Vector<Expr>) resultObjects.get(3);
			params.insertElementAt((Expr) resultObjects.get(2), 0);

			return new CallExpr((IdentExpr) resultObjects.get(0), params);
		}
	};

	static final ParsecObjectsSemanticAction ExprStAcion = new ParsecObjectsSemanticAction() {

		@Override
		public Object doAction(List<Object> resultObjects) {
			Assume.assumeNotNull(resultObjects.get(0));
			Assume.assumeTrue(resultObjects.get(0) instanceof Expr);

			return new ExprStmt((Expr) resultObjects.get(0));
		}
	};

	static final ParsecObjectsSemanticAction arg2Action = new ParsecObjectsSemanticAction() {

		@Override
		public Object doAction(List<Object> resultObjects) {
			Assume.assumeNotNull(resultObjects.get(1));

			Assume.assumeTrue(resultObjects.get(1).getClass() == VariableDeclStmt.class);

			return resultObjects.get(1);
		}
	};

	static final ParsecObjectsSemanticAction argListAction = new ParsecObjectsSemanticAction() {

		@Override
		public Object doAction(List<Object> resultObjects) {

			Vector<VariableDeclStmt> args = new Vector<VariableDeclStmt>();

			for (Object obj : resultObjects) {
				Assume.assumeNotNull(obj);
				Assume.assumeTrue(obj.getClass() == VariableDeclStmt.class);

				args.add((VariableDeclStmt) obj);
			}

			return args;
		}
	};

	static final ParsecObjectsSemanticAction stsAction = new ParsecObjectsSemanticAction() {

		@Override
		public Object doAction(List<Object> resultObjects) {
			Vector<Stmt> sts = new Vector<>();
			for (Object object : resultObjects) {
				Assume.assumeNotNull(object);

				Assume.assumeTrue(object instanceof Stmt);
				sts.add((Stmt) object);
			}

			return sts;
		}

	};

	static final ParsecObjectsSemanticAction blockStAction = new ParsecObjectsSemanticAction() {

		@SuppressWarnings("unchecked")
		@Override
		public Object doAction(List<Object> resultObjects) {
			Assume.assumeNotNull(resultObjects.get(1));

			Assume.assumeTrue(resultObjects.get(1).getClass() == Vector.class);

			return new BlockStmt((Vector<Stmt>) resultObjects.get(1));

		}
	};

	static final ParsecObjectsSemanticAction functionSt0Action = new ParsecObjectsSemanticAction() {

		@Override
		public Object doAction(List<Object> resultObjects) {
			Assume.assumeNotNull(resultObjects.get(0));
			Assume.assumeNotNull(resultObjects.get(1));
			Assume.assumeNotNull(resultObjects.get(4));

			Assume.assumeTrue(resultObjects.get(0).getClass() == Type.class);
			Assume.assumeTrue(resultObjects.get(1).getClass() == IdentExpr.class);
			Assume.assumeTrue(resultObjects.get(4).getClass() == BlockStmt.class);

			return new FunctionStmt((Type) resultObjects.get(0),
					(IdentExpr) resultObjects.get(1),
					new Vector<VariableDeclStmt>(),
					(BlockStmt) resultObjects.get(4));
		}
	};

	static final ParsecObjectsSemanticAction functionSt1Action = new ParsecObjectsSemanticAction() {

		@Override
		public Object doAction(List<Object> resultObjects) {
			Assume.assumeNotNull(resultObjects.get(0));
			Assume.assumeNotNull(resultObjects.get(1));
			Assume.assumeNotNull(resultObjects.get(3));
			Assume.assumeNotNull(resultObjects.get(4));
			Assume.assumeNotNull(resultObjects.get(6));

			Assume.assumeTrue(resultObjects.get(0).getClass() == Type.class);
			Assume.assumeTrue(resultObjects.get(1).getClass() == IdentExpr.class);
			Assume.assumeTrue(resultObjects.get(3).getClass() == VariableDeclStmt.class);
			Assume.assumeTrue(resultObjects.get(4).getClass() == Vector.class);
			Assume.assumeTrue(resultObjects.get(6).getClass() == BlockStmt.class);

			@SuppressWarnings("unchecked")
			Vector<VariableDeclStmt> args = (Vector<VariableDeclStmt>) resultObjects
					.get(4);
			args.insertElementAt((VariableDeclStmt) resultObjects.get(3), 0);

			return new FunctionStmt((Type) resultObjects.get(0),
					(IdentExpr) resultObjects.get(1), args,
					(BlockStmt) resultObjects.get(6));
		}
	};
}