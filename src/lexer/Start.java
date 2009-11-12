package lexer;

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

		Routine program = new Routine(sFile);
		
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
		
	}

}
