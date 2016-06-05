package zeson.simpleC;

import java.io.File;
import java.util.Locale;
import java.util.Map.Entry;

import Zeson.AZLRJ.common.AbstractParsec;
import Zeson.AZLRJ.common.DefaultFailHandler;
import Zeson.AZLRJ.parser.Parser;

public class SimpleC {

	public static void main(String args[]) {

		if (args.length < 1) {
			System.out.println("[usage]: sc filename");
			return;
		}
		File file = new File(args[0]);

		if (!file.exists()) {
			System.out.println(file.getPath() + " is not exist");
			return;
		}
		Parser parser = null;
		try {
			parser = new Parser(file, Grammar.program);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return;
		}

		AbstractParsec.handler = new DefaultFailHandler(
				Grammar.wb.getWhiteParsec()) {

			@Override
			public String getErrorMsg() {
				return failMsg.getMessage(Locale.CHINESE) + " at line:"
						+ failMsg.getLineNumber() + ", column:"
						+ failMsg.getColumnNumber();
			}

		};
		long start = System.currentTimeMillis();

		if (parser.parse()) {

			//PrintVisitor printVisitor = new PrintVisitor();
			Environment env = new Environment();
			SemanticVisitor semanticVisitor = new SemanticVisitor(env);

			EvalVisitor evalVisitor = new EvalVisitor(env);

			for (Entry<String, FunctionStmt> f : Grammar.functionStmts
					.entrySet()) {
				env.funcs.put(f.getKey(), f.getValue());
			}

			for (Entry<String, FunctionStmt> f : Grammar.functionStmts
					.entrySet()) {

				//f.getValue().accept(printVisitor);
				try {
					f.getValue().accept(semanticVisitor);
				} catch (SimpleCSemanticError e) {
					System.out.println(e.getMessage());
					return;
				}

			}

			FunctionStmt mainFunctionStmt = Grammar.functionStmts.get("main");
			if (mainFunctionStmt == null) {
				System.out.println("can not find function `main` to run");
				return;
			}

			mainFunctionStmt.body.accept(evalVisitor);

		} else {

			System.out.println(AbstractParsec.getFailMessage());
		}

		long end = System.currentTimeMillis();
		System.out.println("time:" + (end - start) / 1000.0 + "s");
	}
}
