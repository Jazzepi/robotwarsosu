package lexer;

import lexer.Token.TokenType;

public class ReturnStatement implements Statement {

	String variableName = null;

	@Override
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
