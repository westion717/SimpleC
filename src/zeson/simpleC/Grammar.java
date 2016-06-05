package zeson.simpleC;

import java.util.HashMap;
import java.util.List;

import java.util.Map;

import org.junit.Assume;

import Zeson.AZLRJ.common.IParsec;
import Zeson.AZLRJ.common.Source;
import Zeson.AZLRJ.parsec.AndParsec;
import Zeson.AZLRJ.parsec.BinOperatorParsec;
import Zeson.AZLRJ.parsec.ClosureOrPlusParsec;
import Zeson.AZLRJ.parsec.EmptyOrOneParsec;
import Zeson.AZLRJ.parsec.IntegerParsec;
import Zeson.AZLRJ.parsec.NotPredictionParsec;
import Zeson.AZLRJ.parsec.OrParsec;
import Zeson.AZLRJ.parsec.TermParsec;
import Zeson.AZLRJ.parsec.action.ParsecLiteralSemanticAction;
import Zeson.AZLRJ.parsec.action.ParsecObjectSemanticAction;
import Zeson.AZLRJ.parsec.action.ParsecObjectsSemanticAction;

import Zeson.AZLRJ.parser.WordBuilder;

public class Grammar {

	static final WordBuilder wb = WordBuilder.get('\n', '\t', ' ', '\r');

	static final IParsec add = wb.createParsec("+");
	static final IParsec sub = wb.createParsec("-");

	static final IParsec mul = wb.createParsec("*");
	static final IParsec div = wb.createParsec("/");
	static final IParsec less = wb.createParsec("<");
	static final IParsec greater = wb.createParsec(">");
	static final IParsec eq = wb.createParsec("==");

	static final IParsec assign = wb.createParsec("=");

	static final IParsec comma = wb.createParsec(",");

	static final IParsec left = wb.createParsec("(");
	static final IParsec right = wb.createParsec(")");

	static final IParsec left_brace = wb.createParsec("{");
	static final IParsec right_brace = wb.createParsec("}");

	static final IParsec semicolon = wb.createParsec(";");

	static final IParsec key_if = wb.createParsec("if");
	static final IParsec key_else = wb.createParsec("else");
	static final IParsec key_for = wb.createParsec("for");
	static final IParsec key_int = wb.createParsec("int",
			SimpleCParsecSemanticAction.wordAction);

	static final OrParsec keywords = new OrParsec(key_if, key_else, key_for,
			key_int);

	static final IntegerParsec _integer = new IntegerParsec(
			SimpleCParsecSemanticAction.integerAcion);

	static final TermParsec __ident = new TermParsec("simpleC identifier",
			SimpleCParsecSemanticAction.identAcion);

	static final AndParsec _ident = new AndParsec(
			new ParsecObjectsSemanticAction() {

				@Override
				public Object doAction(List<Object> resultObjects) {
					Assume.assumeNotNull(resultObjects.get(1));
					Assume.assumeTrue(resultObjects.get(1).getClass() == IdentExpr.class);
					return resultObjects.get(1);
				}
			}, new NotPredictionParsec(keywords), __ident);

	static final StringLiteral _string = new StringLiteral(
			new ParsecLiteralSemanticAction() {

				@Override
				public Object doAction(String arg0, Source arg1) {
					return new StringLiteralExpr(arg0, arg1.line, arg1.column);
				}
			});

	static final IParsec Int = wb.createParsec(_integer);
	static final IParsec Ident = wb.createParsec(_ident);
	static final IParsec string = wb.createParsec(_string);

	static final OrParsec F = new OrParsec();

	static final IParsec callExp0 = new AndParsec(
			SimpleCParsecSemanticAction.callExp0Acion, Ident, left, right);

	static final BinOperatorParsec E = new BinOperatorParsec(F,
			SimpleCParsecSemanticAction.binOpAction);

	static final IParsec param = new AndParsec(
			SimpleCParsecSemanticAction.paramAcion, comma, E);

	static final IParsec paramsList = new ClosureOrPlusParsec(param,
			SimpleCParsecSemanticAction.paramsListAcion, false);
	static final IParsec callExp1 = new AndParsec(
			SimpleCParsecSemanticAction.callExp1Acion, Ident, left, E,
			paramsList, right);

	static final OrParsec type = new OrParsec(new ParsecObjectSemanticAction() {

		@Override
		public Object doAction(Object resultObject) {
			if (resultObject.getClass() == String.class) {

				String str = (String) resultObject;

				switch (str) {
				case "int":
					return Type.Int;
				default:
					Assume.assumeTrue(false);
					break;
				}

			} else {
				Assume.assumeTrue(false);
			}
			Assume.assumeTrue(false);
			return null;
		}
	}, key_int);

	static {
		F.addParsec(new AndParsec(new ParsecObjectsSemanticAction() {

			@Override
			public Object doAction(List<Object> resultObjects) {

				Assume.assumeNotNull(resultObjects.get(1));
				Assume.assumeTrue(resultObjects.get(1) instanceof Expr);
				return resultObjects.get(1);
			}
		}, left, E, right));

		E.addOperator(add, 10);
		E.addOperator(sub, 10);
		E.addOperator(mul, 20);
		E.addOperator(div, 20);
		E.addOperator(less, 9);
		E.addOperator(greater, 9);
		E.addOperator(eq, 9);
		E.addOperator(assign, 8);

		F.addParsec(callExp0);
		F.addParsec(callExp1);
		F.addParsec(Ident);
		F.addParsec(Int);
		F.addParsec(string);

	}

	static final AndParsec initialAssign = new AndParsec(
			new ParsecObjectsSemanticAction() {

				@Override
				public Object doAction(List<Object> resultObjects) {
					Assume.assumeNotNull(resultObjects.get(1));
					Assume.assumeTrue(resultObjects.get(1) instanceof Expr);

					return resultObjects.get(1);
				}
			}, assign, E);

	static final EmptyOrOneParsec inital = new EmptyOrOneParsec(initialAssign);

	static final AndParsec VariableDeclSt0 = new AndParsec(
			new ParsecObjectsSemanticAction() {

				@Override
				public Object doAction(List<Object> resultObjects) {
					Assume.assumeNotNull(resultObjects.get(0));
					Assume.assumeNotNull(resultObjects.get(1));

					Assume.assumeTrue(resultObjects.get(0).getClass() == Type.class);
					Assume.assumeTrue(resultObjects.get(1).getClass() == IdentExpr.class);

					Expr intialExpr = null;

					if (resultObjects.get(2) != null) {
						Assume.assumeTrue(resultObjects.get(2) instanceof Expr);
						intialExpr = (Expr) resultObjects.get(2);
					}

					return new VariableDeclStmt((Type) resultObjects.get(0),
							(IdentExpr) resultObjects.get(1), intialExpr);
				}
			}, type, Ident, inital);

	static final AndParsec ExprSt = new AndParsec(
			SimpleCParsecSemanticAction.ExprStAcion, E);

	static final OrParsec arg = new OrParsec(VariableDeclSt0);

	static final AndParsec arg2 = new AndParsec(
			SimpleCParsecSemanticAction.arg2Action, comma, arg);

	static final IParsec argList = new ClosureOrPlusParsec(arg2,
			SimpleCParsecSemanticAction.argListAction, false);

	static final IParsec key_return = wb.createParsec("return");

	static final EmptyOrOneParsec returnedExpr = new EmptyOrOneParsec(E);

	static final AndParsec ReturnSt = new AndParsec(
			new ParsecObjectsSemanticAction() {

				@Override
				public Object doAction(List<Object> resultObjects) {

					Expr intialExpr = null;

					if (resultObjects.get(1) != null) {
						Assume.assumeTrue(resultObjects.get(1) instanceof Expr);
						intialExpr = (Expr) resultObjects.get(1);
					}

					return new ReturnStmt(intialExpr);
				}
			}, key_return, returnedExpr);

	static final OrParsec __st = new OrParsec();
	static final AndParsec _st = new AndParsec(
			new ParsecObjectsSemanticAction() {

				@Override
				public Object doAction(List<Object> resultObjects) {

					Assume.assumeNotNull(resultObjects.get(0));
					Assume.assumeTrue(resultObjects.get(0) instanceof Stmt);

					return resultObjects.get(0);
				}
			}, __st, semicolon);

	static final OrParsec st = new OrParsec(_st);

	static final ClosureOrPlusParsec sts = new ClosureOrPlusParsec(st,
			SimpleCParsecSemanticAction.stsAction, false);

	static final AndParsec blockSt = new AndParsec(
			SimpleCParsecSemanticAction.blockStAction, left_brace, sts,
			right_brace);

	static final AndParsec ForLoopSt = new AndParsec(
			new ParsecObjectsSemanticAction() {

				@Override
				public Object doAction(List<Object> resultObjects) {
					Stmt one = null;
					Expr two = null;
					Stmt three = null;

					if (resultObjects.get(2) != null) {
						Assume.assumeTrue(resultObjects.get(2) instanceof Stmt);
						one = (Stmt) resultObjects.get(2);
					}
					if (resultObjects.get(4) != null) {
						Assume.assumeTrue(resultObjects.get(4) instanceof Expr);
						two = (Expr) resultObjects.get(4);
					}
					if (resultObjects.get(6) != null) {
						Assume.assumeTrue(resultObjects.get(6) instanceof Stmt);
						three = (Stmt) resultObjects.get(6);
					}

					Assume.assumeNotNull(resultObjects.get(8));

					Assume.assumeTrue(resultObjects.get(8).getClass() == BlockStmt.class);
					return new ForLoopStmt(one, two, three,
							(BlockStmt) resultObjects.get(8));
				}
			}, key_for, left, new EmptyOrOneParsec(__st), semicolon,
			new EmptyOrOneParsec(E), semicolon, new EmptyOrOneParsec(__st),
			right, blockSt);

	static final AndParsec if_else_St = new AndParsec(
			new ParsecObjectsSemanticAction() {

				@Override
				public Object doAction(List<Object> resultObjects) {
					Assume.assumeNotNull(resultObjects.get(2));
					Assume.assumeNotNull(resultObjects.get(4));
					Assume.assumeNotNull(resultObjects.get(6));
					Assume.assumeTrue(resultObjects.get(2) instanceof Expr);
					Assume.assumeTrue(resultObjects.get(4).getClass() == BlockStmt.class);
					Assume.assumeTrue(resultObjects.get(6) instanceof Stmt);

					return new IfStmt((Expr) resultObjects.get(2),
							(BlockStmt) resultObjects.get(4),
							(Stmt) resultObjects.get(6));
				}
			}, key_if, left, E, right, blockSt, key_else, st);

	static final AndParsec ifSt = new AndParsec(
			new ParsecObjectsSemanticAction() {

				@Override
				public Object doAction(List<Object> resultObjects) {
					Assume.assumeNotNull(resultObjects.get(2));
					Assume.assumeNotNull(resultObjects.get(4));
					Assume.assumeTrue(resultObjects.get(2) instanceof Expr);
					Assume.assumeTrue(resultObjects.get(4).getClass() == BlockStmt.class);

					return new IfStmt((Expr) resultObjects.get(2),
							(BlockStmt) resultObjects.get(4), null);
				}
			}, key_if, left, E, right, blockSt);

	static {
		__st.addParsec(ReturnSt);
		__st.addParsec(VariableDeclSt0);
		__st.addParsec(ExprSt);
		st.addParsec(blockSt);
		st.addParsec(ForLoopSt);
		st.addParsec(if_else_St);
		st.addParsec(ifSt);

	}

	static final AndParsec FunctionSt0 = new AndParsec(
			SimpleCParsecSemanticAction.functionSt0Action, type, Ident, left,
			right, blockSt);

	static final AndParsec FunctionSt1 = new AndParsec(
			SimpleCParsecSemanticAction.functionSt1Action, type, Ident, left,
			arg, argList, right, blockSt);

	static final Map<String, FunctionStmt> functionStmts = new HashMap<String, FunctionStmt>();

	static final OrParsec function = new OrParsec(wb.getWhiteParsec(),
			FunctionSt0, FunctionSt1);

	static final ClosureOrPlusParsec program = new ClosureOrPlusParsec(
			function, new ParsecObjectsSemanticAction() {

				@Override
				public Object doAction(List<Object> resultObjects) {

					for (Object object : resultObjects) {

						if (object != null) {
							Assume.assumeTrue(object.getClass() == FunctionStmt.class);
							FunctionStmt functionStmt = (FunctionStmt) object;
							functionStmts.put(functionStmt.name.name,
									functionStmt);
						}
					}

					return null;
				}
			}, false);

}
