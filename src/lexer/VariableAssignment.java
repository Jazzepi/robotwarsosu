package lexer;

import java.util.ArrayList;

import lexer.Token.TokenType;

public class VariableAssignment implements Statement {

	String leftSideVar;
	Expression expression;
	String rightSidedFunctionCallID;
	GameFunction gameFunction;
	Parameters rightSidedFunctionCallParameters;
	
	
	public VariableAssignment(TextFile body) {
		
		Token current = body.getNonWSToken(false);
		
		if(current != null)
		{
			if(current.getType() == TokenType.IDENTIFIER)
			{
				leftSideVar = current.getText();
			}
			else
			{
				System.out.println("ERROR: IDENTIFIER token expected after VAR while parsing line "+ body.getLine()+ ". Token " + current.getText() + " of type " + current.getType() + "found instead.");					
			}
		}
		
		current = body.getNonWSToken(false);
		
		if(current != null)
		{
			if(!current.getText().equals("="))
			{
				System.out.println("ERROR: = expected after VAR IDENTIFIER while parsing line "+ body.getLine()+ ". Token " + current.getText() + " of type " + current.getType() + "found instead.");
			}
		}
		
		current = body.getNonWSToken(true);
		
		if(current != null)
		{
			if(current.getType() == TokenType.GAMEFUNCTION)
			{				
				gameFunction = new GameFunction(body);
			}
			else if(current.getType() == TokenType.IDENTIFIER)
			{
				current = body.getNonWSToken(false); //Peel off the subroutine ID
				rightSidedFunctionCallID = current.getText();
				
				current = body.getNonWSToken(false);
				
				if(current != null)
				{
					if(!current.getText().equals("("))
					{
						System.out.println("ERROR: ( symbol expected to preceed PARAMETERS while parsing line "+ body.getLine()+ ". Token " + current.getText() + " of type " + current.getType() + "found instead.");
					}
				}
				
				rightSidedFunctionCallParameters = new Parameters(body);
				
				
				current = body.getNonWSToken(false);
				
				if(current != null)
				{
					if(!current.getText().equals(")"))
					{
						System.out.println("ERROR: ) symbol expected to terminate PARAMETERS while parsing line "+ body.getLine()+ ". Token " + current.getText() + " of type " + current.getType() + "found instead.");
					}
				}
				
			}
			else //Must be an expression
			{
				expression = new Expression(body);
			}
		}
		
		current = body.getNonWSToken(false);
		
		if(current != null)
		{
			if(!current.getText().equals(";"))
			{
				System.out.println("ERROR: ; symbol expected to terminate VARIABLE ASSIGNMENT while parsing line "+ body.getLine()+ ". Token " + current.getText() + " of type " + current.getType() + "found instead.");
			}
		}

		
	}

	@Override
	public ArrayList<String> evaluate() {
		// TODO Auto-generated method stub
		return null;
	}

}
