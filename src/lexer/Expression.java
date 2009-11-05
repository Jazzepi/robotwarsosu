package lexer;

import java.util.ArrayList;

import lexer.Token.TokenType;

public class Expression {

	
	
	public Expression(Token current, Token lookAhead, TextFile body) {
	
		
		ArrayList<Term> terms;
		ArrayList<String> addops;
		
		Term first = new Term(current, lookAhead, body);
		
		while(!(current.getType() == TokenType.CONDITION))
		{
			
			Term tempT = new Term(current, lookAhead, body); 
		}
		
	}

}
