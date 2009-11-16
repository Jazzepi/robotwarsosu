package lexer;

import game.DecisionEngine;
import game.GameCommand;

import java.io.*;

public class Start {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		TextFile sFile = null;

	
		try {
			sFile = new TextFile(bufferedReader.readLine());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		sFile.display();
		sFile.stripCommentsAndWhiteSpace();
//		sFile.display();
		sFile.reset();
		
		Routine program = new Routine(sFile);
		//program.print();
		TextFile compilation = program.compile();
		compilation.display();
		
		DecisionEngine test = new DecisionEngine(compilation);
		GameCommand tempCommand = test.getNextGameCommand();
		System.out.println(tempCommand.getName());
		
//		while(!sFile.isEndOfFile())
//		{
//			Token temp = null;
//			for(int i = 0; i < 2;i++)
//			{
//				temp = sFile.getNonWSToken(true);
//				System.out.print(temp.getText() +" IS A:");
//				System.out.println(temp.getType());
//			}
//			
//			temp = sFile.getNonWSToken(false);
//			System.out.print(temp.getText() +" IS A:");
//			System.out.println(temp.getType());
//		}
//		
//		for(int i = 0; i<50;i++)
//		{
//			Token temp = sFile.getNonWSToken(true);
//		}
			

	}

}
