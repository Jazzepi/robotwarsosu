package lexer;

public class GameFunction {

	
	String gameFunctionID;
	private Parameters parameters;

	public GameFunction(TextFile body, Token gameFunctionAlreadyPulledOff) {
		
		gameFunctionID = gameFunctionAlreadyPulledOff.getText();

		Token current = body.getNonWSToken(false);
		
		if(current != null)
		{
			if(!current.getText().equals("("))
			{
				System.out.println("ERROR: ( symbol expected before PARAMETERS while parsing line "+ body.getLine()+ ". Token " + current.getText() + " of type " + current.getType() + "found instead.");
			}
		}
		
		parameters = new Parameters(body);
		
		current = body.getNonWSToken(false);
		
		if(current != null)
		{
			if(!current.getText().equals(")"))
			{
				System.out.println("ERROR: ) symbol expected after PARAMETERS while parsing line "+ body.getLine()+ ". Token " + current.getText() + " of type " + current.getType() + "found instead.");
			}
		}
	}

}
