package lexer;

import lexer.Token.TokenType;	

public class VariableAssignment implements Statement {

	String leftSideVar;
	Expression expression;
	String rightSidedFunctionCallID;
	GameFunction gameFunction;
	Parameters rightSidedFunctionCallParameters;
	
	@Override
	public void compile(TextFile flag) {
		String builder = new String();
		
		if(gameFunction != null)
		{
			gameFunction.compile(flag);
		}
		else if(expression != null)
		{
			expression.compile(flag);
		}
		else if(rightSidedFunctionCallID != null)
		{
			builder += ("CALL " + rightSidedFunctionCallID + "(");
			
			if(rightSidedFunctionCallParameters != null )
			{
				builder += rightSidedFunctionCallParameters.compile(flag);
			}
			else
			{
				System.out.println("COMPILATION ERROR:Missing Right Side Function Call Parameters");
				builder += "ERROR: Missing Parameters";
			}
			
			builder += (")");
			flag.input(builder);
//			Subroutine temp = Routine.fetchSubroutine(rightSidedFunctionCallID);
//			if(temp != null)
//			{
//				Routine.fetchSubroutine(rightSidedFunctionCallID).compile(flag);
//			}
//			else
//			{
//				flag.input("ERROR:Missing Subroutine");
//			}
			
		}
		else
		{
			System.out.print("COMPILATION ERROR:Missing Right Side Of Assignment Statement");
			builder += "ERROR: Missing Right Side Of Assignment Statement";
			flag.input(builder);
		}
		
		builder = ("VAR ");
		
		if(leftSideVar != null)
		{
			builder += leftSideVar;
		}
		else
		{
			System.out.print("COMPLIATION ERROR:Missing Assignment Variable");
			builder += "ERROR:Missing Assignment Variable";
		}
		flag.input(builder);
	}
	
	@Override
	public void print() {
		
		System.out.print("VAR ");
		
		if(leftSideVar != null)
		{
			System.out.print(leftSideVar + " ");
		}
		else
		{
			System.out.print("ERROR:MissingLeftSideID");
		}
		
		System.out.print("=");
		
		if(gameFunction != null)
		{
			gameFunction.print();
		}
		else if(expression != null)
		{
			expression.print();
		}
		else if(rightSidedFunctionCallID != null)
		{
			System.out.print("CALL " + rightSidedFunctionCallID + "( ");
			if(rightSidedFunctionCallParameters != null )
			{
				rightSidedFunctionCallParameters.print();
			}
			else
			{
				System.out.print("ERROR:MissingRightSideFunctionCallParameters");
			}
			System.out.print(" )");
		}
		else
		{
			System.out.print("ERROR:MissingRightSideOfAssignmentStatement");
		}
		
		System.out.print(";");
	}
	
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
				System.out.println("ERROR: IDENTIFIER token expected after VAR while parsing line "+ body.getReport()+ ". Token " + current.getText() + " of type " + current.getType() + " found instead.");					
			}
		}
		
		current = body.getNonWSToken(false);
		
		if(current != null)
		{
			if(!current.getText().equals("="))
			{
				System.out.println("ERROR: = expected after VAR IDENTIFIER while parsing line "+ body.getReport()+ ". Token " + current.getText() + " of type " + current.getType() + " found instead.");
			}
		}
		
		current = body.getNonWSToken(true);
		
		if(current != null)
		{
			if(current.getType() == TokenType.GAMEFUNCTION)
			{
				current = body.getNonWSToken(false);
				gameFunction = new GameFunction(body, current);
			}
			else if(current.getText().equals("CALL")) //If this is a subroutine call
			{
				current = body.getNonWSToken(false); //Peel off CALL, no need to error check, we already verified it
				
				current = body.getNonWSToken(false); //Peel off the subroutine ID
				
				if(current.getType() == TokenType.IDENTIFIER)
				{
					rightSidedFunctionCallID = current.getText();
				}
				else
				{
					System.out.println("ERROR: IDENTIFIER token expected to preceed ( symbol while parsing line "+ body.getReport()+ ". Token " + current.getText() + " of type " + current.getType() + " found instead.");
				}
				
				current = body.getNonWSToken(false);
				
				if(current != null)
				{
					if(!current.getText().equals("("))
					{
						System.out.println("ERROR: ( symbol expected to preceed PARAMETERS while parsing line "+ body.getReport()+ ". Token " + current.getText() + " of type " + current.getType() + " found instead.");
					}
				}
				
				rightSidedFunctionCallParameters = new Parameters(body);
				
				
				current = body.getNonWSToken(false);
				
				if(current != null)
				{
					if(!current.getText().equals(")"))
					{
						System.out.println("ERROR: ) symbol expected to terminate PARAMETERS while parsing line "+ body.getReport()+ ". Token " + current.getText() + " of type " + current.getType() + " found instead.");
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
				System.out.println("ERROR: ; symbol expected to terminate VARIABLE ASSIGNMENT while parsing line "+ body.getReport()+ ". Token " + current.getText() + " of type " + current.getType() + " found instead.");
			}
		}

		
	}



}
