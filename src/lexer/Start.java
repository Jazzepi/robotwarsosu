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
		
		while(!sFile.isEndOfFile())
		{
			Token temp = sFile.getNonWSToken();
			System.out.print(temp.getText() +" IS A:");
			System.out.println(temp.getType());
		}
		
	}

}
