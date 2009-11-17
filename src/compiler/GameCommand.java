package compiler;


/**
 * GameCommand is a class that holds the Game Command itself, as defined by {@link Token}
 * and the parameters to that game command call.
 *
 * EXAMPLE: skip(x)
 * skip is the game command
 * x is the first and only parameter 
 * 
 * @author Michael Pinnegar
 *
 */
public class GameCommand implements Statement {

	/**
	 * List of parameters that belong to the game command.
	 */
	private Parameters parameters;
	/**
	 * The name of the game command to be called.
	 */
	private String gameCommandID;

	@Override
	/**
	 * Adds the VMC representation of this game command call to the compiled code. 
	 * @param flag unfinished compiled code
	 */
	public void compile(TextFile flag) {

		String builder = new String();

		if(gameCommandID != null)
		{
			builder += gameCommandID+ "(";

			if(parameters != null)
			{
				builder += parameters.compile(flag);
			}
			else
			{
				System.out.print("COMPILATION ERROR:No Parameters For Game Command");
			}
			builder += ")";
		}
		else
		{
			System.out.print("COMPILATION ERROR:No Game Command ID");
		}

		flag.input(builder);
	}

	@Override
	/**
	 * Prints out the elements of this game command as they were taken in as source code.
	 * Useful for verifying that the intermediate tree structure has been built correctly.  
	 */
	public void print() {
		if(gameCommandID != null)
		{
			System.out.print(gameCommandID+ "(");
			if(parameters != null)
			{
				parameters.print();
			}
			else
			{
				System.out.print("ERROR:NoParametersForGameCommand");
			}
			System.out.print(")");
		}
		else
		{
			System.out.print("ERROR:NoGameCommandID");
		}
		System.out.print(";");
	}

	/**
	 * Class constructor that builds a GameCommand object from the source code found in body, and the name of the Game Command found in gameOrderAlreadyPulledOff.
	 * EXAMPLE: skip(x,z)
	 * gameOrderAlreadyPulledOff = skip
	 * 
	 * @param body Source code
	 * @param gameOrderAlreadyPulledOff Previously removed game name.  
	 */
	public GameCommand(TextFile body, Token gameOrderAlreadyPulledOff) {


		gameCommandID = gameOrderAlreadyPulledOff.getText();

		Token current = body.getNonWSToken(false);

		if(current != null)
		{
			if(!current.getText().equals("("))
			{
				System.out.println("ERROR: ( symbol expected before PARAMETERS while parsing GAMECOMMAND on line "+ body.getReport()+ ". Token " + current.getText() + " of type " + current.getType() + " found instead.");
			}
		}

		parameters = new Parameters(body);

		current = body.getNonWSToken(false);

		if(current != null)
		{
			if(!current.getText().equals(")"))
			{
				System.out.println("ERROR: ) symbol expected after PARAMETERS while parsing GAMECOMMAND on line "+ body.getReport()+ ". Token " + current.getText() + " of type " + current.getType() + " found instead.");
			}
		}
	}
}
