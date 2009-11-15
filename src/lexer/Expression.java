package lexer;

import java.util.ArrayList;

public class Expression {

	/**
	 * List of terms. Goes [ADDOP->TERM] in pairs.
	 */
	ArrayList<Term> terms = new ArrayList<Term>();
	
	/**
	 * List of addops. Goes [ADDOP->TERM] in pairs.
	 */
	ArrayList<String> addops = new ArrayList<String>();
	
	/**
	 * Very first term. Has no addop pair.
	 */
	Term first = null;
	
	private static int counter = 0;
	
	public static void incrementCounter()
	{
		counter++;
	}
	
	public static int getCounterVal()
	{
		return counter;
	}
	
	public int compile(TextFile flag) {
		
		Expression.incrementCounter();
		int thisM = Expression.getCounterVal();
		int firstM;
		
		if(first != null)
		{
			firstM = first.compile(flag);
			flag.input("#" + thisM + " = " + "#" + firstM);
		}
		else
		{
			System.out.print("COMPILATION ERROR:No First Term Expression");
			flag.input("ERROR:No First Term Expression");
		}
		
		int i = 0;
		while(i < terms.size())
		{
			int tempM;
			tempM = terms.get(i).compile(flag);
			flag.input("#" + thisM + " = " + "#" + thisM + " " + addops.get(i) + " #" + tempM );
			i++;
		}
		
		return thisM;
	}
	
	public void print() {
		if(first != null)
		{
			first.print();
		}
		else
		{
			System.out.print("ERROR:NoFirstTermExpression");
		}
		
		int i = 0;
		while(i < terms.size())
		{
			System.out.print(addops.get(i));
			terms.get(i).print();
			i++;
		}
	}
	
	public Expression(TextFile body) {
	
	
		first = new Term(body); //Process the first term

		Token current = body.getNonWSToken(true);  //Pull out lookahead from possible ADDOP to see if there will be another term
		
		while((current.getText().equals("+") || (current.getText().equals("-")))) //If there is an ADDOP
		{
			current = body.getNonWSToken(false); //Since we looked before at a lookahead, and we know there is an addop, now we need to pull the addop out
			addops.add(current.getText()); //Put the addop into the list of addops
			Term tempT = new Term(body); //Once we get an addop we know we have to get a term, so peel the next term off 
			terms.add(tempT); //Put the term into the list of terms after the addop.
			current = body.getNonWSToken(true); //Check to see if there is another addop to process
		}
		
	}





}
