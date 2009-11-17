package lexer;

import java.util.ArrayList;

public class Term {
	
	/**
	 * List of factors. Goes [MULTOP->FACTOR] in pairs.
	 */
	private ArrayList<Factor> factors = new ArrayList<Factor>();

	/**
	 * List of multops. Goes [MULTOP->FACTOR] in pairs. * / % are multops
	 */
	private ArrayList<String> multops = new ArrayList<String>();

	/**
	 * Very first factor. Has no multop pair.
	 */
	private Factor first = null;
	
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
			System.out.print("COMPILATION ERROR:No First Factor Term");
			flag.input("ERROR:No First Factor Term");
		}
		
		int i = 0;
		
		while(i < factors.size())
		{
			int tempM;
			tempM = factors.get(i).compile(flag);
			flag.input("#" + thisM + " = " + "#" + thisM + " " + multops.get(i) + " #" + tempM );
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
			System.out.print("ERROR:NoFirstFactorTerm");
		}
		
		int i = 0;
		while(i < factors.size())
		{
			System.out.print(multops.get(i));
			factors.get(i).print();
			i++;
		}
	}
	
	public Term(TextFile body)
	{

		first = new Factor(body); //Process the first factor

		Token current = body.getNonWSToken(true);  //Pull out lookahead from possible MULTOP to see if there will be another term

		while((current.getText().equals("*") || (current.getText().equals("%") || current.getText().equals("/")))) //If there is an MULTOP next
		{
			current = body.getNonWSToken(false); //Since we looked before at a lookahead, and we know there is an multop, now we need to pull the multop out
			multops.add(current.getText()); //Put the multop into the list of multops
			Factor tempT = new Factor(body); //Once we get an multop we know we have to get a factor, so peel the next factor off 
			factors.add(tempT); //Put the factor into the list of factors
			current = body.getNonWSToken(true); //Check to see if there is another [multop->factor] pair to process
		}
	}




}
