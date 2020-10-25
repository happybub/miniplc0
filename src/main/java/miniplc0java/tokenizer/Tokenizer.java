package miniplc0java.tokenizer;
import miniplc0java.util.Pos;
import miniplc0java.error.TokenizeError;
import miniplc0java.error.ErrorCode;

public class Tokenizer {

    private StringIter it;

    public Tokenizer(StringIter it) {
        this.it = it;
    }

    public Token nextToken() throws TokenizeError {
        it.readAll();


        skipSpaceCharacters();

        if (it.isEOF()) {
            return new Token(TokenType.EOF, "", it.currentPos(), it.currentPos());
        }

        char peek = it.peekChar();
        if (Character.isDigit(peek)) {
            return lexUInt();
        } else if (Character.isAlphabetic(peek)) {
            return lexIdentOrKeyword();
        } else {
            return lexOperatorOrUnknown();
        }
    }

    private Token lexUInt() throws TokenizeError {
        
    	Pos tempBegin = new Pos(it.currentPos().row, it.currentPos().col);
    	int tempNum = 0;
    	while(Character.isDigit(it.peekChar())) {
    		tempNum = tempNum * 10 + it.nextChar() - 48;
    	}
    	
    	return new Token(TokenType.Uint, tempNum ,tempBegin ,it.currentPos());
    }

    private Token lexIdentOrKeyword() throws TokenizeError {
    	
    	Pos tempBegin = new Pos(it.currentPos().row, it.currentPos().col);
    	StringBuilder tempStringBuilder = new StringBuilder("");
    	
    	
    	while(Character.isLetterOrDigit(it.peekChar())) {
    		tempStringBuilder.append(it.nextChar());
    	}
    	String tempString = tempStringBuilder.toString();
    	switch(tempString.toLowerCase()) {
    		case "begin":
				return new Token(TokenType.Begin, tempString, tempBegin, it.currentPos());
			case "end":
				return new Token(TokenType.End, tempString, tempBegin, it.currentPos());
			case "const":
				return new Token(TokenType.Const, tempString, tempBegin, it.currentPos());
			case "var":
				return new Token(TokenType.Var, tempString, tempBegin, it.currentPos());
			case "print":
				return new Token(TokenType.Print, tempString, tempBegin, it.currentPos());
			default:
				return new Token(TokenType.Ident, tempString, tempBegin, it.currentPos());
    	}
    
    }

    private Token lexOperatorOrUnknown() throws TokenizeError {
    	switch (it.nextChar()) {
	        case '+':
	            return new Token(TokenType.Plus, '+', it.previousPos(), it.currentPos());
	
	        case '-':
	        	return new Token(TokenType.Minus, '-', it.previousPos(), it.currentPos());
	
	        case '*':
	            return new Token(TokenType.Mult, '*', it.previousPos(), it.currentPos());
	
	        case '/':
	        	return new Token(TokenType.Div, '/', it.previousPos(), it.currentPos());
	        	
	        case '=':
	            return new Token(TokenType.Equal, '=', it.previousPos(), it.currentPos());
	
	        case ';':
	        	return new Token(TokenType.Semicolon, ';', it.previousPos(), it.currentPos());
	
	        case '(':
	            return new Token(TokenType.LParen, '(', it.previousPos(), it.currentPos());
	
	        case ')':
	        	return new Token(TokenType.RParen, ')', it.previousPos(), it.currentPos());
	        	
	        default:
	            throw new TokenizeError(ErrorCode.InvalidInput, it.previousPos());
    	}
    }

    private void skipSpaceCharacters() {
        while (!it.isEOF() && Character.isWhitespace(it.peekChar())) {
            it.nextChar();
        }
    }
}
