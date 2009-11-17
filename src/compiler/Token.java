package compiler;

import java.util.TreeSet;

/**
 * Tokens are built from terminal symbols in the Robot Wars language. Each classification of symbol has its terminal symbols listed in the static initialization arrays inside the class.
 * The matchesToken( ) method gives strict regular expressions that describe the set of strings contained in each token type.
 * Some types of tokens are subsets of others. Keywords is contained within identifiers, but because of the way that the tokenizer in TextFile
 * prioritizes the identification of token types, the IDENTIFIER set is actually = {IDENTIFIER} - {KEYWORDS + GAMEFUNCTION + GAMEORDER + DIGITS} 
 * <p>
 *	 
 * @author Michael Pinnegar
 *
 */
public class Token {

	/**
	 * Whitespace terminal symbols
	 */
	private static String[] WS1 = {"\t"," "};
	/**
	 * IDENTIFIER terminal symbols
	 */
	private static String[] IDENTIFIER1 = {"0","1","2","3","4","5","6","7","8","9",
		"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z",
		"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","_"};
	/**
	 * DIGITS terminal symbols
	 */
	private static String[] DIGITS1 = {"0","1","2","3","4","5","6","7","8","9"};
	/**
	 * OPERATOR terminal symbols (ADDOP/MULTOP in the context free grammar)
	 */
	private static String[] OPERATOR1 = {"+","-","/","*","%"};
	/**
	 * SYMBOL terminal symbols
	 */
	private static String[] SYMBOL1 = {";",")","(","}","{","=",","};
	/**
	 * CONDITION terminal symbols
	 */
	private static String[] CONDITION1 = {"!=", "==", "<", ">", "<=", ">="};
	/**
	 * KEYWORD terminal symbols
	 */
	private static String[] KEYWORD1 = {"IF", "IFELSE","ELSE", "VAR", "SUBROUTINE", "RETURN", "MAIN", "WHILE", "CALL"};
	/**
	 * GAMEFUNCTION terminal symbols
	 */
	private static String[] GAMEFUNCTION1 = {"isEnemyInRange","isHealth","directionOfClosestEnemy"};
	/**
	 * GAMEORDER terminal symbols
	 */
	private static String[] GAMEORDER1 = {"move","turn","skip","selfDestruct","attackWithWeapon","useItem","halt"};

	/**
	 * Type of the token
	 */
	private TokenType type;
	/**
	 * Text contained in the token
	 */
	private String text;

	/**
	 * Collection of token types. Initalized to the arrays contained in {@link Token}
	 * TYPES-
	 * <P>ERROR: A correct program should never generate an error token.
	 * <P>WS: Whitespace. Used to delimit IDENTIFIERS from surrounding alphanumeric code 
	 * <P>IDENTIFIER: Described by the regular expression [a-zA-Z][a-zA-Z0-9_]*
	 * <P>DIGITS: Described by the regular expression [0-9]+
	 * <P>OPERATOR: + - * % or / Used between expressions to evaluate if a jump should be taken
	 * <P>SYMBOL: ; ) ( } { = or , Used as delimiters in the language
	 * <P>CONDITION: != == < > <= or >= Used as conditions between expressions to determine if a jump should be taken
	 * <P>KEYWORD: IF IFELSE ELSE VAR SUBROUTINE RETURN MAIN WHILE CALL Used as delimiters throughout the language
	 * <P>GAMEORDER: Used to issue orders to the robots in the Robot Wars game world
	 * <P>GAMEFUNCTION: Used to retrieve information from the Robot Wars game world
	 * @author Michael Pinnegar
	 *
	 */
	public enum TokenType {
		//I have to put this as an inner class or else I can't initialize these to the long strings. Very frustrating.
		ERROR(), WS(WS1), IDENTIFIER(IDENTIFIER1), DIGITS(DIGITS1), OPERATOR(OPERATOR1), SYMBOL(SYMBOL1), CONDITION(CONDITION1), KEYWORD(KEYWORD1), GAMEORDER(GAMEORDER1),GAMEFUNCTION(GAMEFUNCTION1);

		/**
		 * Collection of small finite sets describing all, or a portion of, the strings in a given language
		 */
		private TreeSet<String> collection = new TreeSet<String>();

		TokenType(String[] x)
		{
			for(String elements: x)
			{
				collection.add(elements);
			}
		}
		
		/**
		 * Builds an empty token.
		 */
		TokenType()
		{}
		
		/**
		 * Returns true iff the string is in the language specified by type 
		 * @param type Category of language the string is being compared to
		 * @param text 
		 * @return True if the string is a member of that type
		 */
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
					if(element.equals(text))
					{
						flag = true;
					}
				}
			}
			return flag;
		}
	}


	/**
	 * 
	 * @return Returns the type stored in a token
	 * @see TokenType
	 */
	TokenType getType()
	{
		return type;
	}

	/**
	 * 
	 * @return Returns the text stored in a token
	 */
	String getText()
	{
		return text;
	}

	/**
	 * Builds a token with the given text and type
	 * @param type
	 * @param text
	 * @see TokenType
	 */
	Token(TokenType type, String text)
	{
		this.type = type;
		this.text = text;
	}

	/**
	 * Returns the algebraic opposite of a given condition. The three possible transformations can be read left to right, or right to left.
	 * <P>== to !=
	 * <p>< to !>
	 * <p>> to !< 
	 * @param condition Condition to be reversed
	 * @return Opposite condition of the one given
	 */
	public static String NegateCondition(String condition) {
		if(condition.equals("=="))
		{
			condition = "!=";
		}
		else if(condition.equals("<"))
		{
			condition = ">=";
		}
		else if(condition.equals(">"))
		{
			condition = "<=";
		}
		else if(condition.equals("<="))
		{
			condition = ">";
		}
		else if(condition.equals(">="))
		{
			condition = "<";
		}
		else if(condition.equals("!="))
		{
			condition = "==";
		}
		return condition;
	}



}
