package lexer;

import java.util.ArrayList;

import lexer.Token.TokenType;

/**
 *  An ordered list of parameters. Used in the larger context of {@link GameCommand}s and {@link GameFunctions}s
 *  
 *  EXAMPLE: (x,y,z) would have the parameters x y and z stored.
 * @author Michael Pinnegar
 *
 */
public class Parameters {
	
	/**
	 * List of parameters stored.
	 */
	private ArrayList<String> parameterList = new ArrayList<String>();
	
	/**
	 * Returns a string representation of all the parameters for a given function call separated by ','
	 * 
	 * @param flag source code
	 * @return String representation of parameters separated by ,s
	 */
	public String compile(TextFile flag) {
		String builder = new String();
		
		if(parameterList.size() > 0)
		{
			builder += parameterList.get(0);
		}
		
		for(int i = 1; i<parameterList.size(); i++)
		{
			builder += "," + parameterList.get(i);
		}
		
		return builder;
	}
	
	/**
	 * Prints out the elements of this parameter list as they were taken in as source code.
	 * Useful for verifying that the intermediate tree structure has been built correctly.  
	 */
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
	
	/**
	 * This constructor builds a parameters list from the source code found in body.
	 * @param body source code
	 */
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
