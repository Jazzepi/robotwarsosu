package lexer;

import lexer.Token.TokenType;

/**
 *  A WHILE statement consisting of an {@link Expression} CONDITION {@link Expression} triplet where CONDITION is defined in {@link Token}
 *  Also contains the while {@link Block} of code. 
 * @author Michael Pinnegar
 */
public class While implements Statement {

	/**
	 * Left and right expressions centered around a single CONDITION
	 */
	private Expression leftExp,rightExp;
	/**
	 * Comparison operator used between the left and right expression
	 */
	private String condition;
	/**
	 * Block of code to be executed if the expression operator expression triplet evaluate to true
	 */
	private Block whileBody;
	
	@Override
	/**
	 * 
	 */
	public void compile(TextFile flag) {
		int spotForWhileToJumpBackTo = flag.getReport();
		
		flag.input("LEFTEXPRESSION");
		int lengthOfLeftExpression = flag.getReport();
		leftExp.compile(flag);
		lengthOfLeftExpression = flag.getReport() - lengthOfLeftExpression;
		
		flag.input("RIGHTEXPRESSION");
		int lengthOfRightExpression = flag.getReport();
		rightExp.compile(flag);
		lengthOfRightExpression = flag.getReport() - lengthOfRightExpression;
		
		int spotToInsertWHILEJUMP = flag.getReport();
		
		whileBody.compile(flag);
		
		flag.input("JUMP -" + (flag.getReport() - spotForWhileToJumpBackTo + 1));
		flag.insertLine(spotToInsertWHILEJUMP, "WHILE " + Token.NegateCondition(condition) + " JUMP " + (flag.getReport() - spotToInsertWHILEJUMP + 1));
		
		if(condition != null)
		{
			
		}
		else
		{
			
		}
		
	}
	
	@Override
	/**
	 * 
	 */
	public void print() {
		System.out.print("WHILE (");
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
		whileBody.print();
		System.out.print("}");
	}

	
	/**
	 * This constructor builds a while statement from the source code found in body.
	 * @param body source code
	 */
	public While(TextFile body) {

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
				System.out.println("ERROR: ) symbol expected before body of WHILE while parsing line "+ body.getReport()+ ". Token " + current.getText() + " of type " + current.getType() + "found instead.");	
			}
		}
		
		current = body.getNonWSToken(false);
		
		if(current != null)
		{
			if(current.getText().equals("{"))
			{
				whileBody = new Block(body);
			}
			else
			{
				System.out.println("ERROR: { symbol expected before body of WHILE while parsing line "+ body.getReport()+ ". Token " + current.getText() + " of type " + current.getType() + "found instead.");					
			}
		}
		
		
		
		current = body.getNonWSToken(false);
		
		if(current != null)
		{
			if(current.getText().equals("}"))
			{}
			else
			{
				System.out.println("ERROR: } symbol expected after body of WHILE while parsing line "+ body.getReport()+ ". Token " + current.getText() + " of type " + current.getType() + "found instead.");					
			}
		}
	}
}
