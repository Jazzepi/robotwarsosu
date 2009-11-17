package lexer;

/**
 * GameCommand is a class that holds the Game Function itself, as defined by {@link Token}
 * and the parameters to that game function call.
 *
 * EXAMPLE: isNextEnemy(z)
 * isNextEnemy is the game function
 * z is the first and only parameter 
 * 
 * @author Michael Pinnegar
 *
 */
public class GameFunction {

	/**
	 * The name of the game function to be called.
	 */
	private String gameFunctionID;
	/**
	 * List of parameters that belong to the game function.
	 */
	private Parameters parameters;

	/**
	 * Adds the VMC representation of this game function call to the compiled code.
	 * @param flag unfinished compiled code
	 */
	public void compile(TextFile flag) {
		String builder = new String();
		
		if(gameFunctionID != null)
		{
			builder += gameFunctionID+ "(";
			if(parameters != null)
			{
				builder += parameters.compile(flag);
			}
			else
			{
				System.out.print("COMPILATION ERROR:No Parameters For Game Function");
				builder += "ERROR:No Parameters For Game Function";
			}
			builder +=")";
		}
		else
		{
			System.out.print("COMPILATION ERROR:No Game Function ID");
			builder += "ERROR:No ID For Game Function";
		}
		flag.input(builder);
	}
	
	/**
	 * Prints out the elements of this game function as they were taken in as source code.
	 * Useful for verifying that the intermediate tree structure has been built correctly.  
	 */
	void print()
	{
		if(gameFunctionID != null)
		{
			System.out.print(gameFunctionID+ "(");
			if(parameters != null)
			{
				parameters.print();
			}
			else
			{
				System.out.print("ERROR:NoParametersForGameFunction");
			}
			System.out.print(")");
		}
		else
		{
			System.out.print("ERROR:NoGameFunctionID");
		}
	}
	
	/**
	 * Class constructor that builds a GameFunction object from the source code found in body, and the name of the Game Function found in gameFunctionAlreadyPulledOff.
	 * EXAMPLE: isNextEnemy(x,z)
	 * gameOrderAlreadyPulledOff = isNextEnemy
	 * 
	 * @param body source code
	 * @param gameFunctionAlreadyPulledOff name of function
	 */
	public GameFunction(TextFile body, Token gameFunctionAlreadyPulledOff) {
		
		gameFunctionID = gameFunctionAlreadyPulledOff.getText();

		Token current = body.getNonWSToken(false);
		
		if(current != null)
		{
			if(!current.getText().equals("("))
			{
				System.out.println("ERROR: ( symbol expected before PARAMETERS while parsing GAMEFUNCTION on line "+ body.getReport()+ ". Token " + current.getText() + " of type " + current.getType() + " found instead.");
			}
		}
		
		parameters = new Parameters(body);
		
		current = body.getNonWSToken(false);
		
		if(current != null)
		{
			if(!current.getText().equals(")"))
			{
				System.out.println("ERROR: ) symbol expected after PARAMETERS while parsing line "+ body.getReport()+ ". Token " + current.getText() + " of type " + current.getType() + "found instead.");
			}
		}
	}



}
