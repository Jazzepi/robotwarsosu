package lexer;

import java.util.ArrayList;

import lexer.Token.TokenType;

public class GameCommand implements Statement {

	Parameters parameters;
	String gameFunctionID;
	
	public GameCommand(TextFile body) {
		Token current = body.getNonWSToken(false);
		
		if (current != null)
		{
			if(current.getType() == TokenType.GAMEORDER)
			{
				
			}
			else
			{
				
			}
		}
	}

	@Override
	public ArrayList<String> evaluate() {
		// TODO Auto-generated method stub
		return null;
	}

}
