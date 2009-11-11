package lexer;
import java.util.*;

public class MainProgram {

	ArrayList<Block> mainProgram = new ArrayList<Block>(); 

	public MainProgram(TextFile body) {


		Token lookAhead = body.getNonWSToken(true);		//Get first item off the statement 

		while(lookAhead != null && !lookAhead.getText().equals("}"))
		{
			Block temp = new Block(body);
			mainProgram.add(temp);
			lookAhead = body.getNonWSToken(true);
		}

		if(lookAhead != null)
		{
			if(!lookAhead.getText().equals("}"))
			{
				System.out.println("ERROR: } symbol expected after body of MAINPROGRAM while parsing line "+ body.getLine()+ ". Token " + lookAhead.getText() + " of type " + lookAhead.getType() + "found instead.");
			}
		}
	}

}
