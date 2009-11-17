package lexer;

import java.util.ArrayList;

/**
 * A Routine is an entire Robot Wars program represented by a list of {@link Subroutine}s and a singleton {@link MainProgram}
 * @author Michael Pinnegar
 *
 */
public class Routine {
	
	/**
	 * The single MAIN{} program 
	 */
	private MainProgram main;
	/**
	 * Each SUBROUTINE(x,y,z){} is stored in this list. 
	 */
	private ArrayList<Subroutine> subroutines = new ArrayList<Subroutine>();
	
	/**
	 * Adds the VMC representation of this entire program to the compiled code. 
	 * 
	 * @return finished compiled code in VMC format
	 */
	TextFile compile()
	{
		TextFile flag = new TextFile();
		for(Subroutine element : subroutines)
		{
			element.compile(flag);
		}
		
		if(main != null)
		{
			main.compile(flag);
		}
		else
		{
			System.out.println("COMPLIATION ERROR:Main Program Missing");
		}
		
		return flag;
	}
	
	/**
	 * Prints out the entire program after it has been built from the source code.
	 * Useful for verifying that the intermediate tree structure has been built correctly.  
	 */
	void print()
	{
		for(Subroutine element : subroutines)
		{
			element.print();
		}
		
		if(main != null)
		{
			main.print();
		}
		else
		{
			System.out.print("ERROR:MainProgramMissing");
		}
	}
	
	/**
	 * Class constructor that builds a Robot Wars program from the source code found in body.
	 * @param body source code
	 */
	public Routine(TextFile body)
	{
		boolean stillProcessingSubroutines = true;
		
		Token current = body.getNonWSToken(false);
		
		while(stillProcessingSubroutines)
		{
			if(current != null)
			{
				if(current.getText().equals("MAIN"))
				{
					stillProcessingSubroutines = false;
					
					current = body.getNonWSToken(false);
					
					if(current != null)
					{
						if(current.getText().equals("{"))
						{
							main = new MainProgram(body);
						}
						else
						{
							System.out.println("ERROR: { symbol expected before body of MAINPROGRAM while parsing line "+ body.getReport()+ ". Token " + current.getText() + " of type " + current.getType() + " found instead.");					
						}
					}
				}
				else if(current.getText().equals("SUBROUTINE"))
				{
					Subroutine temp = new Subroutine(body);
					subroutines.add(temp);
					current = body.getNonWSToken(false); //Prime for next subroutine call or main program body
				}
				else
				{
					System.out.println("ERROR: MAIN or SUBROUTINE keyword expected while parsing line "+ body.getReport()+ ". Token " + current.getText() + " of type " + current.getType() + " found instead.");
					Token garbage = body.getNonWSToken(false);
					if(garbage == null)
					{
						stillProcessingSubroutines = false;
					}
				}
			}
			else
			{
				stillProcessingSubroutines = false;
				if(main == null)
				{
					System.out.println("ERROR: MAIN program required, none found.");
				}
				
			}
		}
	}
}
