package lexer;


public class GameCommand implements Statement {

	Parameters parameters;
	String gameCommandID;
	
	@Override
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
				builder += ("ERROR:No Parameters For Game Command");
			}
			builder += ")";
		}
		else
		{
			System.out.print("COMPILATION ERROR:No Game Command ID");
			builder += "ERROR:No Game Command ID";
		}
	}
	
	@Override
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

	public GameCommand(TextFile body, Token gameOrderAlreadyPulledOff) {
		
		gameCommandID = gameOrderAlreadyPulledOff.getText();

		Token current = body.getNonWSToken(false);
		
		if(current != null)
		{
			if(!current.getText().equals("("))
			{
				System.out.println("ERROR: ( symbol expected before PARAMETERS while parsing line "+ body.getReport()+ ". Token " + current.getText() + " of type " + current.getType() + "found instead.");
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
