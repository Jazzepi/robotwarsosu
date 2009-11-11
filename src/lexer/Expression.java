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
