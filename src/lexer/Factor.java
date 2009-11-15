package lexer;

import lexer.Token.TokenType;

public class Factor {

	String digits = null;
	String identifier = null;
	Expression expression = null;
	
	public int compile(TextFile flag) {
		Expression.incrementCounter();
		int thisM = Expression.getCounterVal();
		
		if(digits != null)
		{
			flag.input("#" + thisM + " = " + digits);
		}
		else if(identifier != null)
		{
			flag.input("#" + thisM + " = " + identifier);
		}
		else if(expression != null)
		{
			flag.input("#" + thisM + " = " + "#" + expression.compile(flag));
		}
		else
		{
			System.out.print("COMPILATION ERROR:Empty Factor");
			flag.input("ERROR:Empty Factor");
		}
		
		return thisM;
	}
	
	public void print() {
			if(digits != null)
			{
				System.out.print(digits);
			}
			else if(identifier != null)
			{
				System.out.print(identifier);
			}
			else if(expression != null)
			{
				System.out.print("(");
				expression.print();
				System.out.print(")");
			}
			else
			{
				System.out.print("ERROR:EmptyFactor");
			}
	}

	public Factor(TextFile body) {
		
		Token current = body.getNonWSToken(false);
		
		if(current != null)
		{
			if (current.getText().equals("(")) //Then we're processing an expression
			{
				//Process expression
				expression = new Expression(body);
				//Peel off matching )
				current = body.getNonWSToken(false);
				
				if(current != null)
				{
					if(!current.getText().equals(")"))
					{
						System.out.println("ERROR: Symbol ) expected after EXPRESSION CONDITION EXPRESSION tokens while parsing line "+ body.getReport()+ ". Token " + current.getText() + " of type " + current.getType() + "found instead.");		
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
				System.out.println("ERROR: Symbol (, DIGITS token, or IDENTIFIER token expected for FACTOR, while parsing line "+ body.getReport()+ ". Token " + current.getText() + " of type " + current.getType() + "found instead.");
			}
		}
	}



}
