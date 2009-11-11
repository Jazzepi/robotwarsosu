package lexer;

import java.util.ArrayList;

import lexer.Token.TokenType;

public class Factor implements Statement {

	String digits = null;
	String identifier = null;
	Expression expression = null;

	public Factor(TextFile body) {
		Token current = body.getNonWSToken(true);
		if(current != null)
		{
			if (current.getText().equals("(")) //Then we're processing an expression
			{
				//Peel off first ( , no need to error check, we know what token it is
				current = body.getNonWSToken(false);

				//Process expression
				expression = new Expression(body);
				//Peel off matching )
				current = body.getNonWSToken(false);
				
				if(current != null)
				{
					if(!current.getText().equals(")"))
					{
						System.out.println("ERROR: Symbol ) expected after EXPRESSION CONDITION EXPRESSION tokens while parsing line "+ body.getLine()+ ". Token " + current.getText() + " of type " + current.getType() + "found instead.");		
					}					
				}
			}
			else if(current.getType() == TokenType.DIGITS) //Then we're processing digits, just store them and we're done
			{
				digits = current.getText();
			}
			else if(current.getType() == TokenType.IDENTIFIER)
			{
				identifier = current.getText();
			}
			else //Error
			{
				System.out.println("ERROR: Symbol (, DIGITS token, or IDENTIFIER token expected for FACTOR, while parsing line "+ body.getLine()+ ". Token " + current.getText() + " of type " + current.getType() + "found instead.");
			}
		}
	}

	@Override
	public ArrayList<String> evaluate() {
		// TODO Auto-generated method stub
		return null;
	}

}
