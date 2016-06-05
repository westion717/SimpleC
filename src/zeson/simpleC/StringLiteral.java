package zeson.simpleC;

import Zeson.AZLRJ.common.EOFStream;
import Zeson.AZLRJ.common.Source;
import Zeson.AZLRJ.parsec.TermParsec;
import Zeson.AZLRJ.parsec.action.ParsecLiteralSemanticAction;

public class StringLiteral extends TermParsec {

	public StringLiteral(ParsecLiteralSemanticAction literalSemanticAction) {
		super("string literal", literalSemanticAction);
	}

	@Override
	public String parseString(Source inputString) throws EOFStream {
		StringBuilder str = new StringBuilder();

		char c;
		c = inputString.getCurrentChar();

		if (c != '\"')
			return null;

		while (true) {
			inputString.peek(1);
			c = inputString.getCurrentChar();

			if (c == '\n') {
				inputString.column = 1;
				inputString.line += 1;
			} else
				inputString.column += 1;

			if (c == '\"' && inputString.getLastChar() != '\\') {
				inputString.peek(1);
				break;
			}
			str.append(c);
		}
		return str.toString();
	}
}
