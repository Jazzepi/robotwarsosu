package lexer;

import java.util.TreeSet;


public class Token {

	private static String[] WS1 = {"\t"," "};
	private static String[] IDENTIFIER1 = {"0","1","2","3","4","5","6","7","8","9","10",
		"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z",
		"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","_"};
	private static String[] DIGITS1 = {"0","1","2","3","4","5","6","7","8","9"};
	private static String[] OPERATOR1 = {"+","-","/","*","%"};
	private static String[] SYMBOL1 = {";",")","(","}","{","="};
	private static String[] CONDITION1 = {"!=", "==", "<", ">", "<=", ">="};
	private static String[] KEYWORD1 = {"IF", "IFELSE", "VAR", "SUBROUTINE", "RETURN", "MAIN", "WHILE"};
	private static String[] GAMEFUNCTION1 = {"isEnemyInRange","isHealth","directionOfClosestEnemy"};
	private static String[] GAMEORDER1 = {"move","turn","skip","selfDestruct","attackWithWeapon","useItem"};

	private TokenType type;
	private String text;

	public enum TokenType {
		//I have to put this as an inner class or else I can't initialize these to the long strings. Very frustrating.
		ERROR(), WS(WS1), IDENTIFIER(IDENTIFIER1), DIGITS(DIGITS1), OPERATOR(OPERATOR1), SYMBOL(SYMBOL1), CONDITION(CONDITION1), KEYWORD(KEYWORD1), GAMEORDER(GAMEORDER1),GAMEFUNCTION(GAMEFUNCTION1);


		private TreeSet<String> collection = new TreeSet<String>();

		TokenType(String[] x)
		{
			for(String elements: x)
			{
				collection.add(elements);
			}
		}

		TokenType()
		{}

		public static Boolean matchesToken(TokenType type, String text)
		{
			boolean flag = false;
			
			if(type == TokenType.WS)
			{
				if(text.matches("[\\s]+"))
					flag = true;
			}
			else if(type == TokenType.DIGITS)
			{
				if(text.matches("[\\d]+"))
					flag = true;
			}
			else if(type == TokenType.IDENTIFIER)
			{
				if(text.matches("[a-zA-Z][a-zA-Z0-9_]*"))
					flag = true;
			}
			else
			{
				for(String element : type.collection)
				{
					if(element.startsWith(text))
					{
						flag = true;
					}
				}
			}
			return flag;
		}
	}


	TokenType getType()
	{
		return type;
	}

	String getText()
	{
		return text;
	}

	Token(TokenType type, String text)
	{
		this.type = type;
		this.text = text;
	}



}
