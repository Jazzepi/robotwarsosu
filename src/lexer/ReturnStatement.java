package lexer;

import lexer.Token.TokenType;

/**
 * A RETURN statement consisting of the variable that is to be used as the return value.
 * @author Michael Pinnegar
 *
 */
public class ReturnStatement implements Statement {

	/**
	 * Name of the variable to be used as the return value.
	 */
	private String variableName = null;

	@Override
	/**
	 * Adds the VMC representation of this RETURN to the compiled code. 
	 * 
	 * @param flag source code
	 */
	public void compile(TextFile flag) {
		String builder = new String("RETURN ");

		if(variableName != null)
		{
			builder +=variableName;
		}
		else
		{
			System.out.println("COMPLIATION ERROR:Missing Variable Name");
			builder +=("ERROR:Missing Variable Name");
		}
		
		flag.input(builder);
		
	}
	
	@Override
	/**
	 * Prints out the elements of this RETURN as they were taken in as source code.
	 * Useful for verifying that the intermediate tree structure has been built correctly.  
	 */
	public void print() {

		System.out.print("RETURN ");

		if(variableName != null)
		{
			System.out.print(variableName);
		}
		else
		{
			System.out.print("ERROR:EmptyVariableName");
		}

		System.out.print(";");
	}

	/**
	 * This constructor builds a RETURN from the source code found in body.
	 * @param body source code
	 */
	public ReturnStatement(TextFile body) {

		Token current = body.getNonWSToken(false); //get variable name

		if(current != null)
		{
			if (current.getType() == TokenType.IDENTIFIER)
			{
				variableName =current.getText();
			}
			else
			{
				System.out.println("ERROR: IDENTIFIER token expected after RETURN while parsing line "+ body.getReport()+ ". Token " + current.getText() + " of type " + current.getType() + "found instead.");
			}
		}

		current = body.getNonWSToken(false); //Peel off ; only check to make sure it was the right symbol

		if(current != null)
		{
			if (!current.getText().equals(";"))
			{
				System.out.println("ERROR: ; symbol expected after RETURN STATEMENT while parsing line "+ body.getReport()+ ". Token " + current.getText() + " of type " + current.getType() + "found instead.");
			}
		}
	}



}
