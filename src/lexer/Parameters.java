package lexer;

import java.util.ArrayList;

import lexer.Token.TokenType;

public class Parameters {

	ArrayList<String> parameterList = new ArrayList<String>();
	
	public void print() {
		if(parameterList.size() > 0)
		{
			System.out.print(parameterList.get(0));
		}
		
		for(int i = 1; i<parameterList.size(); i++)
		{
			System.out.print("," + parameterList.get(i));
		}
	}
	
	public Parameters(TextFile body) {
		
		Token current = body.getNonWSToken(true);
		
		if(current != null)
		{
			if(!current.getText().equals(")"))
			{
				if(current.getType() == TokenType.IDENTIFIER)
				{
					current = body.getNonWSToken(false);
					parameterList.add(current.getText());
				}
				else
				{
					System.out.println("ERROR: IDENTIFIER token expected as a parameter, while parsing line "+ body.getReport()+ ". Token " + current.getText() + " of type " + current.getType() + "found instead.");
				}
			}
		}
		
		current = body.getNonWSToken(true); //Look for a , to see if there are more variables
		
		while(current != null && current.getText().equals(","))
		{
			current = body.getNonWSToken(false); //Peel off ,
			current = body.getNonWSToken(false); //Get next variable
			if(current.getType() == TokenType.IDENTIFIER)
			{
				parameterList.add(current.getText());
			}
			else
			{
				System.out.println("ERROR: IDENTIFIER token expected as a parameter, while parsing line "+ body.getReport()+ ". Token " + current.getText() + " of type " + current.getType() + "found instead.");
			}
			
			current = body.getNonWSToken(true); //Look for a , to see if there are more variables
		}
		
	}



}
