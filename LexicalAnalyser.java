import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class LexicalAnalyser {

	private enum State {
		PROCESSINGNUMBER, ONENUMBER, ONESYMBOL, TOOMANY
	};

	public static List<Token> analyse(String input) throws NumberException, ExpressionException {
		ArrayList<Token> tokens = new ArrayList<Token>();
		String buffer = "";
		State state = State.ONESYMBOL;
		
		for (char inputChar : input.toCharArray()){
			System.out.println("The input is: " + inputChar);
			if (Token.typeOf(inputChar) == Token.TokenType.NUMBER){
				switch (state){
					case PROCESSINGNUMBER:
					case ONESYMBOL:
						buffer += inputChar;
						state = State.PROCESSINGNUMBER;
						break;
					case ONENUMBER:
						state = State.TOOMANY;
						break;
					case TOOMANY:
						state = State.TOOMANY;
						break;
				}
			}

			else if (inputChar == '.'){
				switch (state){
					case PROCESSINGNUMBER:
						if (Double.parseDouble(buffer) == 0){
							buffer += inputChar;
							state = State.PROCESSINGNUMBER;
							break;
						}
					case ONESYMBOL:
					case ONENUMBER:
						throw new NumberException();
					case TOOMANY:
						state = State.TOOMANY;
						break;
				}
			}

			else if (Token.typeOf(inputChar) == Token.TokenType.PLUS || Token.typeOf(inputChar) == Token.TokenType.MINUS || Token.typeOf(inputChar) == Token.TokenType.TIMES || Token.typeOf(inputChar) == Token.TokenType.DIVIDE){
				switch (state){
					case ONESYMBOL:
						state = State.TOOMANY;
						break;
					case PROCESSINGNUMBER:
					case ONENUMBER:
						buffer = processBuffer(buffer, tokens);
						tokens.add(new Token(Token.typeOf(inputChar)));
						state = State.ONESYMBOL;
						break;
					case TOOMANY:
						state = State.TOOMANY;
						break;
				}
			}

			else if (inputChar == ' '){
				switch (state){
					case PROCESSINGNUMBER:
						buffer = processBuffer(buffer, tokens);
						state = State.ONENUMBER;
						break;
					default: break;
				}
			}

			else{
				throw new ExpressionException();
			}

			System.out.println("The buffer is currently: " + buffer);
		}

		switch (state){
			case PROCESSINGNUMBER:
				buffer = processBuffer(buffer, tokens);
				state = State.ONENUMBER;
				break;
			case ONESYMBOL:
				state = State.TOOMANY;
			default: break;
		}

		if (state == State.TOOMANY){
			throw new ExpressionException();
		}

		return tokens;
	}

	private static String processBuffer(String buffer, List<Token> tokens) throws NumberException{
		if (buffer.length() > 0){
			if (buffer.charAt(buffer.length() - 1) == '.'){
				throw new NumberException();
			}
			double numberInput = Double.parseDouble(buffer);
			tokens.add(new Token(numberInput));
			return "";
		}
		return buffer;
	}
}
