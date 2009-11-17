package lexer;

import lexer.Token.TokenType;

/**
 * A factor consisting of a single expression object encapsulated in parentheses
 * OR a DIGITS as defined by {@link Token}
 * OR an IDENTIFIER as defined by {@link Token}
 * @author Michael Pinnegar
 *
 */
public class Factor {

	/**
	 * String representation of a DIGITS as defined by {@link Token}
	 */
	private String digits = null;
	/**
	 * String representation of an IDENTIFIER as defined by {@link Token} 
	 */
	private String identifier = null;
	/**
	 * An ( expression ) where the parentheses are implicitly stored by its depth in the tree structure indicated
	 * by its node value.
	 */
	private Expression expression = null;

	/**
	 * Adds the VMC representation of this factor to the compiled code.
	 * Returns the integer numbering of the factor's node in the tree structure. 
	 * @param flag unfinished compiled code
	 * @return factor's node numbering
	 */
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

	/**
	 * Prints out the elements of this factor as they were taken in as source code.
	 * Useful for verifying that the intermediate tree structure has been built correctly.  
	 */
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

	/**
	 * This constructor builds a factor from the source code found in body.
	 * @param body source code
	 */
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
