package lexer;

import java.util.TreeSet;

public class Routine {
	
	MainProgram main;
	TreeSet<RoutineProgram> subroutines = new TreeSet<RoutineProgram>();
	
	Routine(TextFile body)
	{
		Token current = body.getNonWSToken();
		Token lookAhead = body.getNonWSToken();
	
		if(current != null && lookAhead != null) 
		{
			if(current.getText().equals("MAIN"))
			{
				main = new MainProgram(current, lookAhead, body);
			}
			else if(current.getText().equals("ROUTINE"))
			{
				RoutineProgram temp = new RoutineProgram(current, lookAhead, body);
				subroutines.add(temp);
			}
			else
			{
				System.out.println("ERROR: MAIN or SUBROUTINE keyword expected while parsing line "+ body.getLine());
			}
		}
	}
}
