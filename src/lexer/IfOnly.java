package lexer;

import java.util.ArrayList;

public class IfOnly implements Statement {

	Expression leftExp, rightExp;
	String Condition;
	
	public IfOnly(Token current, Token lookAhead, TextFile body)
	{
		leftExp = new Expression(current, lookAhead, body);
		Condition = current.getText();
		rightExp = new Expression(current, lookAhead, body);
	}

	@Override
	public ArrayList<String> evaluate() {
		// TODO Auto-generated method stub
		return null;
	}

}
