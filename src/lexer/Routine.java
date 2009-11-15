package lexer;

import java.util.ArrayList;

public class Routine {
	
	MainProgram main;
	static ArrayList<Subroutine> subroutines = new ArrayList<Subroutine>();
	
	static Subroutine fetchSubroutine(String subroutineName)
	{
		Subroutine flag = null;
		for(Subroutine element: subroutines)
		{
			if(subroutineName.equals(element.fetchName()))
			{
				flag = element;
			}
		}
		
		if(flag == null)
		{
			System.out.println("COMPILATION ERROR: Referenced subroutine " + subroutineName + " does not exsist.");
		}
		
		return flag;
	}
	
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
