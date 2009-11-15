package lexer;

import lexer.Token.TokenType;

public class IfElse implements Statement {
	Expression leftExp, rightExp;
	String condition;
	Block ifBody,elseBody;
	
	@Override
	public void compile(TextFile flag) {
		
		flag.input("LEFTEXPRESSION");
		leftExp.compile(flag);
		
		flag.input("RIGHTEXPRESSION");
		rightExp.compile(flag);
		
		if(condition != null)
		{
			int insertSpot = flag.getReport();
			
			ifBody.compile(flag);
			
			int jumpToElseSpot = flag.getReport() - insertSpot + 3;
			
			flag.input("IF " + Token.NegateCondition(condition) + " JUMP " + jumpToElseSpot);
		}
		else
		{
			System.out.print("COMPILATION ERROR:No Condition");
			flag.input("ERROR:No condition");
		}		
		
	}

	
	@Override
	public void print() {
		System.out.print("IFELSE (");
		leftExp.print();
		if(condition != null)
		{
			System.out.print(condition);	
		}
		else
		{
			System.out.print("ERROR:NoCondition");
		}
		rightExp.print();
		System.out.print(") {");
		ifBody.print();
		System.out.print("} ELSE {");
		elseBody.print();
		System.out.print("}");
	}
	

	public IfElse(TextFile body) {
		
		leftExp = new Expression(body);
		
		Token current = body.getNonWSToken(false);
		
		if(current != null)
		{
			if(current.getType() == TokenType.CONDITION)
			{
				condition = current.getText();
			}
			else
			{
				System.out.println("ERROR: CONDITION token expected after EXPRESSION and before EXPRESSION while parsing line "+ body.getReport()+ ". Token " + current.getText() + " of type " + current.getType() + " found instead.");
			}
		}		
		
		rightExp = new Expression(body);
		
		current = body.getNonWSToken(false);
		
		if(current != null)
		{
			if(!current.getText().equals(")"))
			{
				System.out.println("ERROR: ) symbol expected before body of IFELSE while parsing line "+ body.getReport()+ ". Token " + current.getText() + " of type " + current.getType() + " found instead.");	
			}
		}
		
		current = body.getNonWSToken(false);
		
		if(current != null)
		{
			if(!current.getText().equals("{"))
				System.out.println("ERROR: { symbol expected before first body of IFELSE while parsing line "+ body.getReport()+ ". Token " + current.getText() + " of type " + current.getType() + " found instead.");					
		}

		ifBody = new Block(body);
		
		
		current = body.getNonWSToken(false);
		
		if(current != null)
		{
			if(!current.getText().equals("}"))
			{
				System.out.println("ERROR: } symbol expected after first body of IFELSE while parsing line "+ body.getReport()+ ". Token " + current.getText() + " of type " + current.getType() + " found instead.");
			}
		}
		
		
		
		current = body.getNonWSToken(false);
		
		if(current != null)
		{
			if(!current.getText().equals("ELSE"))
			{
				System.out.println("ERROR: ELSE keyword expected after first body of IFELSE while parsing line "+ body.getReport()+ ". Token " + current.getText() + " of type " + current.getType() + " found instead.");				
			}
		}
		
		current = body.getNonWSToken(false);
		
		if(current != null)
		{
			if(!current.getText().equals("{"))
			{
				System.out.println("ERROR: { symbol expected after first body of IFELSE while parsing line "+ body.getReport()+ ". Token " + current.getText() + " of type " + current.getType() + " found instead.");				
			}
		}
		
		elseBody = new Block(body);
		
		current = body.getNonWSToken(false);
		
		if(current != null)
		{
			if(!current.getText().equals("}"))
			{
				System.out.println("ERROR: } symbol expected after second body of IFELSE while parsing line "+ body.getReport()+ ". Token " + current.getText() + " of type " + current.getType() + " found instead.");
			}
		}
	}
}
