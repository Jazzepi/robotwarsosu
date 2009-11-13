package lexer;

import java.util.ArrayList;

public class GameCommand implements Statement {

	Parameters parameters;
	String gameCommandID;
	
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

	@Override
	public ArrayList<String> evaluate() {
		// TODO Auto-generated method stub
		return null;
	}



}
