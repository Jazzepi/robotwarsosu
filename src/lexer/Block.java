package lexer;

import java.util.ArrayList;

import lexer.Token.TokenType;

public class Block {

	ArrayList<Statement> block = new ArrayList<Statement>();

	Block(Token current, Token lookAhead, TextFile body)
	{
		while(current != null && lookAhead != null && (current.getType() == TokenType.KEYWORD | current.getType() == TokenType.GAMEORDER) && !current.getText().equals("RETURN")) 
		{
			if(current.equals("IF"))
			{
				if (lookAhead.equals("("))
				{
					current = body.getNonWSToken();
					lookAhead = body.getNonWSToken();
					IfOnly ifOnlyBlockToBeAdded = new IfOnly(current, lookAhead, body);
					block.add(ifOnlyBlockToBeAdded);
				}
				else
				{
					System.out.println("ERROR: ( expected after IF on line" + body.getLine());
				}
			}
			else if(current.equals("IFELSE"))
			{
				if (lookAhead.equals("("))
				{
					current = body.getNonWSToken();
					lookAhead = body.getNonWSToken();
					IfElse ifElseBlockToBeAdded = new IfElse(current, lookAhead, body);
					block.add(ifElseBlockToBeAdded);
				}
				else
				{
					System.out.println("ERROR: ( expected after IFELSE on line" + body.getLine());
				}
			}
			else if(current.equals("WHILE"))
			{
				if (lookAhead.equals("("))
				{
					current = body.getNonWSToken();
					lookAhead = body.getNonWSToken();
					While whileBlockToBeAdded = new While(current, lookAhead, body);
					block.add(whileBlockToBeAdded);
				}
				else
				{
					System.out.println("ERROR: ( expected after WHILE on line" + body.getLine());
				}		
			}
			else if(current.equals("VAR"))
			{
				if (lookAhead.getType() == TokenType.IDENTIFIER)
				{
					current = body.getNonWSToken();
					lookAhead = body.getNonWSToken();
					VariableAssignment variableAssignmentStatementToBeAdded = new VariableAssignment(current, lookAhead, body);
					block.add(variableAssignmentStatementToBeAdded);
				}
				else
				{
					System.out.println("ERROR: IDENTIFIER expected after VAR on line" + body.getLine());
				}
			}
			else if(current.getType() == TokenType.GAMEORDER)
			{
				if (lookAhead.equals("("))
				{
					current = body.getNonWSToken();
					lookAhead = body.getNonWSToken();
					GameCommand gameCommandStatementToBeAdded = new GameCommand(current, lookAhead, body);
					block.add(gameCommandStatementToBeAdded);
				}
				else
				{
					System.out.println("ERROR: ( expected after GAMEORDER on line" + body.getLine());
				}
			}
			else
			{
				System.out.println("ERROR: IF, IFELSE, WHILE, VAR, or GAMEORDER command expected while parsing line "+ body.getLine());
			}
		}
	}
}
