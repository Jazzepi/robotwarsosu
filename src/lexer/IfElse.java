package lexer;

import java.util.ArrayList;

import lexer.Token.TokenType;

public class IfElse implements Statement {
	Expression leftExp, rightExp;
	String condition;
	Block ifBody,elseBody;

	public IfElse(TextFile body) {
		
		leftExp = new Expression(body);
		
		Token current = body.getNonWSToken(false);
		
		if(current != null)
		{
			if(current.getType() == TokenType.CONDITION)
			{
				condition = current.getText();
			}
			else
			{
				System.out.println("ERROR: CONDITION token expected after EXPRESSION and before EXPRESSION while parsing line "+ body.getLine()+ ". Token " + current.getText() + " of type " + current.getType() + "found instead.");
			}
		}		
		
		rightExp = new Expression(body);
		
		current = body.getNonWSToken(false);
		
		if(current != null)
		{
			if(!current.getText().equals(")"))
			{
				System.out.println("ERROR: ) symbol expected before body of IFELSE while parsing line "+ body.getLine()+ ". Token " + current.getText() + " of type " + current.getType() + "found instead.");	
			}
		}
		
		current = body.getNonWSToken(false);
		
		if(current != null)
		{
			if(current.getText().equals("{"))
			{
				ifBody = new Block(body);
			}
			else
			{
				System.out.println("ERROR: { symbol expected before first body of IFELSE while parsing line "+ body.getLine()+ ". Token " + current.getText() + " of type " + current.getType() + "found instead.");					
			}
		}
		
		
		
		current = body.getNonWSToken(false);
		
		if(current != null)
		{
			if(current.getText().equals("ELSE"))
			{
				elseBody = new Block(body);
			}
			else
			{
				System.out.println("ERROR: ELSE keyword expected after first body of IFELSE while parsing line "+ body.getLine()+ ". Token " + current.getText() + " of type " + current.getType() + "found instead.");					
			}
		}
		
		current = body.getNonWSToken(false);
		
		if(current != null)
		{
			if(!current.getText().equals("}"))
			{
				System.out.println("ERROR: } symbol expected after second body of IFELSE while parsing line "+ body.getLine()+ ". Token " + current.getText() + " of type " + current.getType() + "found instead.");
			}
		}

	}

	@Override
	public ArrayList<String> evaluate() {
		// TODO Auto-generated method stub
		return null;
	}

}
