package lexer;

/**
 * A {@link block} of MAIN{} code with any number of statements between the opening and closing braces
 * @author Michael Pinnegar
 *
 */
public class MainProgram {

	/**
	 * Block of code between the braces of MAIN{}
	 */
	private Block mainProgram; 

	/**
	 * Adds the VMC representation of this MAIN block of code to the compiled code. 
	 * @param flag unfinished compiled code
	 */
	public void compile(TextFile flag) {
		flag.input("MAIN");
		
		if(mainProgram != null)
		{
			mainProgram.compile(flag);
		}
		else
		{
			System.out.println("COMPILATION ERROR: Missing Main Block");
			flag.input("ERROR: MISSING BLOCK");
		}	
	}
	
	/**
	 * Prints out this MAIN program as it was taken in as source code.
	 * Useful for verifying that the intermediate tree structure has been built correctly.  
	 */
	void print()
	{
		System.out.print("MAIN {");
		mainProgram.print();
		System.out.print("}");
	}
	
	/**
	 * This constructor builds an MAIN PROGRAM from the source code found in body.
	 * @param body unfinished source code
	 */
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
