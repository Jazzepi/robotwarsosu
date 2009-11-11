package lexer;

import java.util.ArrayList;

import lexer.Token.TokenType;

public class Block {

	ArrayList<Statement> block = new ArrayList<Statement>();

	Block(TextFile body)
	{
		Token current = body.getNonWSToken(false);
		while(current != null && (current.getType() == TokenType.KEYWORD | current.getType() == TokenType.GAMEORDER) && !current.getText().equals("RETURN")) 
		{
			if(current.getText().equals("IF"))
			{
				current = body.getNonWSToken(false);
				if (current.getText().equals("("))
				{
					IfOnly ifOnlyBlockToBeAdded = new IfOnly(body);
					block.add(ifOnlyBlockToBeAdded);
				}
				else
				{
					System.out.println("ERROR: ( symbol expected after after IF while parsing line "+ body.getLine()+ ". Token " + current.getText() + " of type " + current.getType() + "found instead.");					
				}
			}
			else if(current.getText().equals("IFELSE"))
			{
				current = body.getNonWSToken(false);
				if (current.getText().equals("("))
				{
					IfElse ifElseBlockToBeAdded = new IfElse(body);
					block.add(ifElseBlockToBeAdded);
				}
				else
				{
					System.out.println("ERROR: ( symbol expected after IFELSE while parsing line "+ body.getLine()+ ". Token " + current.getText() + " of type " + current.getType() + "found instead.");
				}
			}
			else if(current.getText().equals("WHILE"))
			{
				current = body.getNonWSToken(false);
				if (current.getText().equals("("))
				{
					While whileBlockToBeAdded = new While(body);
					block.add(whileBlockToBeAdded);
				}
				else
				{
					System.out.println("ERROR: ( symbol expected after WHILE while parsing line "+ body.getLine()+ ". Token " + current.getText() + " of type " + current.getType() + "found instead.");
				}		
			}
			else if(current.getText().equals("VAR"))
			{
				current = body.getNonWSToken(false);
				
				if (current.getType() == TokenType.IDENTIFIER)
				{
					VariableAssignment variableAssignmentStatementToBeAdded = new VariableAssignment(body);
					block.add(variableAssignmentStatementToBeAdded);
				}
				else
				{
					System.out.println("ERROR: IDENTIFIER token expected after VAR while parsing line "+ body.getLine()+ ". Token " + current.getText() + " of type " + current.getType() + "found instead.");
				}
			}
			else if(current.getText().equals("RETURN"))
			{
				ReturnStatement returnStatementToBeAdded = new ReturnStatement(body);
				block.add(returnStatementToBeAdded);
			}
			else if(current.getType() == TokenType.GAMEORDER)
			{
				current = body.getNonWSToken(false);
				
				if (current.getText().equals("("))
				{
					GameCommand gameCommandStatementToBeAdded = new GameCommand(body);
					block.add(gameCommandStatementToBeAdded);
				}
				else
				{
					System.out.println("ERROR: ( expected after GAMEORDER on line" + body.getLine());
					
				}
			}
			else
			{
				System.out.println("ERROR: IF, IFELSE, WHILE, VAR, RETURN, or GAMEORDER command expected while parsing line "+ body.getLine()+ ". Token " + current.getText() + " of type " + current.getType() + "found instead.");					
			}
		}
	}
}
