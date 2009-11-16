package lexer;

public class GameFunction {

	
	String gameFunctionID;
	private Parameters parameters;

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
	
	public GameFunction(TextFile body, Token gameFunctionAlreadyPulledOff) {
		
		gameFunctionID = gameFunctionAlreadyPulledOff.getText();

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
