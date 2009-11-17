package lexer;

import java.util.ArrayList;

import lexer.Token.TokenType;

/**
 * A block consisting of zero or more statements.
 * @author Michael Pinnegar
 *
 */
public class Block {

	/**
	 * An ordered collection of statements in a block.  
	 */
	private ArrayList<Statement> block = new ArrayList<Statement>();

	/**
	 * Adds the VMC representation of this block, statement by statement, to the compiled code.
	 * @param flag unfinished compiled code
	 */
	public void compile(TextFile flag) {
		
		for(Statement element : block)
		{
			element.compile(flag);
		}
		
	}
	
	/**
	 * Prints out the elements of a block as they were taken in as source code.
	 * Useful for verifying that the intermediate tree structure has been built correctly.  
	 */
	void print()
	{
		for(Statement element : block)
		{
			element.print();
		}
	}
	
	/**
	 * Class constructor that builds itself from the source code found in body.
	 * @param body source code
	 */
	Block(TextFile body)
	{
		Token current = body.getNonWSToken(true);
		
		while(current != null && !current.getText().equals("}")) 
		{
			if(current.getText().equals("IF"))
			{
				current = body.getNonWSToken(false);
				current = body.getNonWSToken(false);
				if (current.getText().equals("("))
				{
					IfOnly ifOnlyBlockToBeAdded = new IfOnly(body);
					block.add(ifOnlyBlockToBeAdded);
				}
				else
				{
					System.out.println("ERROR: ( symbol expected after after IF while parsing line "+ body.getReport()+ ". Token " + current.getText() + " of type " + current.getType() + " found instead.");					
				}
			}
			else if(current.getText().equals("IFELSE"))
			{
				current = body.getNonWSToken(false);
				current = body.getNonWSToken(false);
				if (current.getText().equals("("))
				{
					IfElse ifElseBlockToBeAdded = new IfElse(body);
					block.add(ifElseBlockToBeAdded);
				}
				else
				{
					System.out.println("ERROR: ( symbol expected after IFELSE while parsing line "+ body.getReport()+ ". Token " + current.getText() + " of type " + current.getType() + " found instead.");
				}
			}
			else if(current.getText().equals("WHILE"))
			{
				current = body.getNonWSToken(false);
				current = body.getNonWSToken(false);
				if (current.getText().equals("("))
				{
					While whileBlockToBeAdded = new While(body);
					block.add(whileBlockToBeAdded);
				}
				else
				{
					System.out.println("ERROR: ( symbol expected after WHILE while parsing line "+ body.getReport()+ ". Token " + current.getText() + " of type " + current.getType() + " found instead.");
				}		
			}
			else if(current.getText().equals("VAR"))
			{
				current = body.getNonWSToken(false);
				VariableAssignment variableAssignmentStatementToBeAdded = new VariableAssignment(body);
				block.add(variableAssignmentStatementToBeAdded);
			}
			else if(current.getText().equals("RETURN"))
			{
				current = body.getNonWSToken(false);
				ReturnStatement returnStatementToBeAdded = new ReturnStatement(body);
				block.add(returnStatementToBeAdded);
			}
			else if(current.getType() == TokenType.GAMEORDER)
			{
				current = body.getNonWSToken(false);
				
				GameCommand gameCommandStatementToBeAdded = new GameCommand(body, current);
				block.add(gameCommandStatementToBeAdded);
				current = body.getNonWSToken(false);
				
				if(!current.getText().equals(";"))
				{
					System.out.println("ERROR: ; symbol expected after GAMEORDER while parsing GAMEORDER on line "+ body.getReport()+ ". Token " + current.getText() + " of type " + current.getType() + " found instead.");
				}
			}
			else
			{
				System.out.println("ERROR: IF, IFELSE, WHILE, VAR, RETURN, or GAMEORDER command expected while parsing line "+ body.getReport()+ ". Token " + current.getText() + " of type " + current.getType() + " found instead.");
				current = body.getNonWSToken(false); //Get rid of bad token.
			}
			current = body.getNonWSToken(true);
		}
		
		if(current != null)
		{
			if(!current.getText().equals("}"))
			{
				System.out.println("ERROR: } symbol expected after BLOCK while parsing line "+ body.getReport()+ ". Token " + current.getText() + " of type " + current.getType() + " found instead.");				
			}
		}
	}


}
