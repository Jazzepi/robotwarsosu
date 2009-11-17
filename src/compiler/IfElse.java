package compiler;

import compiler.Token.TokenType;

/**
 *  An IFELSE consisting of an {@link Expression} CONDITION {@link Expression} triplet where CONDITION is defined in {@link Token}
 *  Also contains the if {@link Block} and else {@link Block} of code. 
 * @author Michael Pinnegar
 *
 */
public class IfElse implements Statement {
	/**
	 * Left and right expressions centered around a single CONDITION
	 */
	private Expression leftExp, rightExp;
	/**
	 * Comparison operator used between the left and right expression
	 */
	private String condition;
	/**
	 * the first and second {@link Block} of the if-else ladder
	 */
	private Block ifBody,elseBody;

	@Override
	/**
	 * Adds the VMC representation of this if-else ladder to the compiled code. 
	 * @param flag unfinished compiled code
	 */
	public void compile(TextFile flag) {

		flag.input("LEFTEXPRESSION");
		leftExp.compile(flag);

		flag.input("RIGHTEXPRESSION");
		rightExp.compile(flag);

		int spotToInputIFJUMP = flag.getReport();
		ifBody.compile(flag);
		int ifBlockSize = flag.getReport() - spotToInputIFJUMP; 
		int spotToInputELSEJUMP = flag.getReport() + 1;
		elseBody.compile(flag);

		if(condition != null)
		{
			flag.insertLine(spotToInputIFJUMP, "IFELSE " + Token.NegateCondition(condition) + " JUMP " + (ifBlockSize + 2));
			flag.insertLine(spotToInputELSEJUMP, "JUMP " + (flag.getReport() - spotToInputELSEJUMP + 1));
		}
		else
		{
			System.out.print("COMPILATION ERROR:No Condition");
			flag.input("ERROR:No condition");
		}		

	}


	@Override
	/**
	 * Prints out the elements of this if-else ladder as they were taken in as source code.
	 * Useful for verifying that the intermediate tree structure has been built correctly.  
	 */
	public void print() {
		System.out.print("IFELSE (");
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
		System.out.print("} ELSE {");
		elseBody.print();
		System.out.print("}");
	}


	/**
	 * This constructor builds an if-else ladder from the source code found in body.
	 * @param body source code
	 */
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
				System.out.println("ERROR: CONDITION token expected after EXPRESSION and before EXPRESSION while parsing line "+ body.getReport()+ ". Token " + current.getText() + " of type " + current.getType() + " found instead.");
			}
		}		

		rightExp = new Expression(body);

		current = body.getNonWSToken(false);

		if(current != null)
		{
			if(!current.getText().equals(")"))
			{
				System.out.println("ERROR: ) symbol expected before body of IFELSE while parsing line "+ body.getReport()+ ". Token " + current.getText() + " of type " + current.getType() + " found instead.");	
			}
		}

		current = body.getNonWSToken(false);

		if(current != null)
		{
			if(!current.getText().equals("{"))
				System.out.println("ERROR: { symbol expected before first body of IFELSE while parsing line "+ body.getReport()+ ". Token " + current.getText() + " of type " + current.getType() + " found instead.");					
		}

		ifBody = new Block(body);


		current = body.getNonWSToken(false);

		if(current != null)
		{
			if(!current.getText().equals("}"))
			{
				System.out.println("ERROR: } symbol expected after first body of IFELSE while parsing line "+ body.getReport()+ ". Token " + current.getText() + " of type " + current.getType() + " found instead.");
			}
		}



		current = body.getNonWSToken(false);

		if(current != null)
		{
			if(!current.getText().equals("ELSE"))
			{
				System.out.println("ERROR: ELSE keyword expected after first body of IFELSE while parsing line "+ body.getReport()+ ". Token " + current.getText() + " of type " + current.getType() + " found instead.");				
			}
		}

		current = body.getNonWSToken(false);

		if(current != null)
		{
			if(!current.getText().equals("{"))
			{
				System.out.println("ERROR: { symbol expected after first body of IFELSE while parsing line "+ body.getReport()+ ". Token " + current.getText() + " of type " + current.getType() + " found instead.");				
			}
		}

		elseBody = new Block(body);

		current = body.getNonWSToken(false);

		if(current != null)
		{
			if(!current.getText().equals("}"))
			{
				System.out.println("ERROR: } symbol expected after second body of IFELSE while parsing line "+ body.getReport()+ ". Token " + current.getText() + " of type " + current.getType() + " found instead.");
			}
		}
	}
}
