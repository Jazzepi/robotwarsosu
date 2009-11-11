package lexer;

import java.util.TreeSet;

public class Routine {
	
	MainProgram main;
	TreeSet<RoutineProgram> subroutines = new TreeSet<RoutineProgram>();
	
	public Routine(TextFile body)
	{
		Token current = body.getNonWSToken(false);
	
		if(current != null) 
		{
			if(current.getText().equals("MAIN"))
			{
				current = body.getNonWSToken(false);
				if(current != null)
				{
					if(current.getText().equals("{"))
					{
						main = new MainProgram(body);	
					}
					else
					{
						System.out.println("ERROR: { symbol expected before body of MAINPROGRAM while parsing line "+ body.getLine()+ ". Token " + current.getText() + " of type " + current.getType() + "found instead.");					
					}					
				}

				
			}
			else if(current.getText().equals("SUBROUTINE"))
			{
				RoutineProgram temp = new RoutineProgram(body);
				subroutines.add(temp);
			}
			else
			{
				System.out.println("ERROR: MAIN or SUBROUTINE keyword expected while parsing line "+ body.getLine()+ ". Token " + current.getText() + " of type " + current.getType() + "found instead.");						
			}
		}
	}
}
