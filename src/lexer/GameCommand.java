package lexer;

import java.util.ArrayList;

public class GameCommand implements Statement {

	Parameters parameters;
	String gameCommandID;

	public GameCommand(TextFile body, Token gameOrderAlreadyPulledOff) {
		
		gameCommandID = gameOrderAlreadyPulledOff.getText();

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

	@Override
	public ArrayList<String> evaluate() {
		// TODO Auto-generated method stub
		return null;
	}

}
