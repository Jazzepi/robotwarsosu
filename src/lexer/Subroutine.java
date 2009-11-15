package lexer;


import lexer.Token.TokenType;

public class Subroutine {

	String methodName; 
	Parameters parameters;
	Block subroutineBlock;
	
	String fetchName()
	{
		return methodName;
	}
	
	Block fetchBlock()
	{
		return subroutineBlock;
	}
	
	Parameters fetchParameters()
	{
		return parameters;
	}
	
	public void compile(TextFile flag) {
		
		String builder = new String("SUBROUTINE "); 
		
		if(methodName != null)
		{
			builder += methodName + " ";
		}
		else
		{
			System.out.print("COMPILATION ERROR:Missing Subroutine Name ");
			builder += "ERROR: Missing subroutine name";
		}
		
		builder += "( ";

		if(parameters != null )
		{
			builder += parameters.compile(flag);
		}
		else
		{
			System.out.print("COMPILATION ERROR:Missing Parameters");
			builder += "ERROR:Missing Parameters";
		}
		
		builder += " )";
		
		flag.input(builder);
		if(subroutineBlock != null)
		{
			subroutineBlock.compile(flag);
		}
		else
		{
			System.out.print("COMPILATION ERROR:Missing Subroutine Block");
			flag.input("ERROR:Missing Subroutine Block");
		}
	}
	
	public void print() {
		System.out.print("SUBROUTINE ");
		if(methodName != null)
		{
			System.out.print(methodName + " ");
		}
		else
		{
			System.out.print("ERROR:MissingSubroutineName ");
		}
		
		System.out.print("(");

		
		if(parameters != null )
		{
			parameters.print();
		}
		else
		{
			System.out.print("ERROR:MissingParameters");
		}
		
		System.out.print(") {");
		
		if(subroutineBlock != null)
		{
			subroutineBlock.print();
		}
		else
		{
			System.out.print("ERROR:MissingBlock");
		}
		
		System.out.println("}");
		System.out.println();
	}
	
	public Subroutine(TextFile body) {
		
		Token current = body.getNonWSToken(false);
		
		if(current != null)
		{
			if(current.getType() == TokenType.IDENTIFIER)
			{
				methodName = current.getText();
			}
			else
			{
				System.out.println("ERROR: IDENTIFIER token expected after SUBROUTINE while parsing line "+ body.getReport()+ ". Token " + current.getText() + " of type " + current.getType() + "found instead.");
			}
		}
		
		current = body.getNonWSToken(false);
		
		if(current != null)
		{
			if(!current.getText().equals("("))
			{
				System.out.println("ERROR: ( symbol expected before PARAMETERS of SUBROUTINE while parsing line "+ body.getReport()+ ". Token " + current.getText() + " of type " + current.getType() + "found instead.");
			}
		}
		
		parameters = new Parameters(body);
		
		current = body.getNonWSToken(false);
		
		if(current != null)
		{
			if(!current.getText().equals(")"))
			{
				System.out.println("ERROR: ) symbol expected after PARAMETERS of SUBROUTINE while parsing line "+ body.getReport()+ ". Token " + current.getText() + " of type " + current.getType() + "found instead.");
			}
		}	
		
		current = body.getNonWSToken(false);
		
		if(current != null)
		{
			if(!current.getText().equals("{"))
			{
				System.out.println("ERROR: { symbol expected before body of SUBROUTINE while parsing line "+ body.getReport()+ ". Token " + current.getText() + " of type " + current.getType() + "found instead.");
			}
		}
		
		
		
		subroutineBlock = new Block(body);
		
		
		current = body.getNonWSToken(false);
		
		if(current != null)
		{
			if(!current.getText().equals("}"))
			{
				System.out.println("ERROR: } symbol expected after body of SUBROUTINE while parsing line "+ body.getReport()+ ". Token " + current.getText() + " of type " + current.getType() + "found instead.");
			}
		}
		 
	}




}
