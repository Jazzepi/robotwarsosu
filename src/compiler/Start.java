package compiler;

import game.DecisionEngine;
import game.GameOperation;

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
		System.out.println("*****************");
		
		for(int i = 0; i<20; i++)
		{
			GameOperation tempCommand = test.getNextGameCommand();
			System.out.println(tempCommand.getName());
			String[] temp = tempCommand.getParameterList();
			for(String element : temp)
			{
				System.out.println(element);
			}
		}
		

		
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
