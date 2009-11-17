package compiler;

import java.util.ArrayList;

/**
 * An expression consisting of a single {@link Term}, followed by zero or more ADDOP TERM pairs.
 * ADDOP is + or -
 * @author Michael Pinnegar
 *
 */
public class Expression {

	/**
	 * List of {@link Term}. Goes [ADDOP->TERM] in pairs.
	 */
	private ArrayList<Term> terms = new ArrayList<Term>();
	
	/**
	 * List of addops (+/-). Goes [ADDOP->TERM] in pairs.
	 */
	private ArrayList<String> addops = new ArrayList<String>();
	
	/**
	 * Very first term. Has no addop pair.
	 */
	private Term first = null;
	
	/**
	 * Counter used to uniquely label the nodes of the tree representation of the source code. Node
	 * numbers are used in the linearization of the tree's expressions, where it is converted into
	 * source code.
	 */
	private static int counter = 0;
	
	/**
	 * Adds one to the counter.
	 */
	public static void incrementCounter()
	{
		counter++;
	}
	
	/**
	 * Retrieves the value of the counter.
	 * @return counter value
	 */
	public static int getCounterVal()
	{
		return counter;
	}
	
	/**
	 * Adds the VMC representation of this expression to the compiled code.
	 * Returns the integer numbering of the expression's node in the tree structure. 
	 * @param flag unfinished compiled code
	 * @return expression's node numbering
	 */
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
	
	/**
	 * Prints out the elements of this expression as they were taken in as source code.
	 * Useful for verifying that the intermediate tree structure has been built correctly.  
	 */
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
	
	/**
	 * This constructor builds an expression from the source code found in body.
	 * @param body source code
	 */
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
