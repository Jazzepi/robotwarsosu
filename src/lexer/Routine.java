package lexer;

import java.util.TreeSet;

public class Routine {
	
	MainProgram main;
	TreeSet<Subroutines> subroutines = new TreeSet<Subroutines>();
	
	void print()
	{
		for(Subroutines element : subroutines)
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
					Subroutines temp = new Subroutines(body);
					subroutines.add(temp);
					current = body.getNonWSToken(false); //Prime for next subroutine call or main program body
				}
				else
				{
					System.out.println("ERROR: MAIN or SUBROUTINE keyword expected while parsing line "+ body.getReport()+ ". Token " + current.getText() + " of type " + current.getType() + " found instead.");						
				}
			}			
		}
	}
}
