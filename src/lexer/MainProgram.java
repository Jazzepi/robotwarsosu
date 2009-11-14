package lexer;

public class MainProgram {

	Block mainProgram; 

	public void compile(TextFile flag) {
		flag.input("MAIN");
		
		if(mainProgram != null)
		{
			mainProgram.compile(flag);
		}
		else
		{
			System.out.println("COMPILATION ERROR:Missing Subroutine Block");
			flag.input("ERROR: MISSING BLOCK");
		}	
	}
	
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
