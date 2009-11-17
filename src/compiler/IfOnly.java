package compiler;

import compiler.Token.TokenType;

/**
 *  An IF statement consisting of an {@link Expression} CONDITION {@link Expression} triplet where CONDITION is defined in {@link Token}
 *  Also contains the if {@link Block} of code. 
 * @author Michael Pinnegar
 */
public class IfOnly implements Statement {

	/**
	 * Left and right expressions centered around a single CONDITION
	 */
	private Expression leftExp, rightExp;
	/**
	 * Comparison operator used between the left and right expression
	 */
	private String condition;
	/**
	 * Block of code to be executed if the expression operator expression triplet evaluate to true
	 */
	private Block ifBody;
	
	@Override
	/**
	 * Adds the VMC representation of this if statement to the compiled code. 
	 * @param flag unfinished compiled code
	 */
	public void compile(TextFile flag) {
		
		flag.input("LEFTEXPRESSION");
		leftExp.compile(flag);
		flag.input("RIGHTEXPRESSION");
		rightExp.compile(flag);
		
		int spotToInputIFJUMP = flag.getReport();
		ifBody.compile(flag);
		
		if(condition != null)
		{
			flag.insertLine(spotToInputIFJUMP, "IF " + Token.NegateCondition(condition) + " JUMP " + (flag.getReport() - spotToInputIFJUMP + 1));	
		}
		else
		{
			flag.insertLine(spotToInputIFJUMP, "IF " + "ERROR:Missing condition " + " JUMP " + (flag.getReport() - spotToInputIFJUMP + 1));
			System.out.println("COMPILATION ERROR:Missing condition");
		}
	}
	
	@Override
	/**
	 * Prints out the elements of this if statement as they were taken in as source code.
	 * Useful for verifying that the intermediate tree structure has been built correctly.  
	 */
	public void print() {
		System.out.print("IF (");
		leftExp.print();
		if(condition != null)
		{
			System.out.print(condition);	
		}
		else
		{
			System.out.print("ERROR:NoCondition");
		}
		rightExp.print();
		System.out.print(") {");
		ifBody.print();
		System.out.print("}");
	}
	
	/**
	 * This constructor builds an if statement from the source code found in body.
	 * @param body source code
	 */
	public IfOnly(TextFile body)
	{
		
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
				System.out.println("ERROR: CONDITION token expected after EXPRESSION and before EXPRESSION while parsing line "+ body.getReport()+ ". Token " + current.getText() + " of type " + current.getType() + "found instead.");
			}
		}		
		
		rightExp = new Expression(body);
		
		current = body.getNonWSToken(false);
		
		if(current != null)
		{
			if(!current.getText().equals(")"))
			{
				System.out.println("ERROR: ) symbol expected before body of IFONLY while parsing line "+ body.getReport()+ ". Token " + current.getText() + " of type " + current.getType() + "found instead.");	
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
				System.out.println("ERROR: { symbol expected before body of IFONLY while parsing line "+ body.getReport()+ ". Token " + current.getText() + " of type " + current.getType() + "found instead.");					
			}
		}
		
		
		
		current = body.getNonWSToken(false);
		
		if(current != null)
		{
			if(current.getText().equals("}"))
			{}
			else
			{
				System.out.println("ERROR: } symbol expected after body of IFONLY while parsing line "+ body.getReport()+ ". Token " + current.getText() + " of type " + current.getType() + "found instead.");					
			}
		}
	}
}
