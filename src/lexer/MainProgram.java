package lexer;
import java.util.*;

public class MainProgram {

	Block mainProgram; 

	void print()
	{
		System.out.print("MAIN {");
		mainProgram.print();
		System.out.print("}");
	}
	
	public MainProgram(TextFile body) {

		mainProgram = new Block(body);

		Token current = body.getNonWSToken(false);

		if(current != null)
		{
			if(!current.getText().equals("}"))
			{
				System.out.println("ERROR: } symbol expected after body of MAINPROGRAM while parsing line "+ body.getReport()+ ". Token " + current.getText() + " of type " + current.getType() + " found instead.");
			}
		}
	}

}
