package lexer;


/**
 * A STATEMENT is an object that is nested inside of {@link Block}s. 
 * @author Michael Pinnegar
 *
 */
public interface Statement {

	/**
	 * Prints out the object's source code without comments or trailing whitespace.
	 * Useful for verifying the correctness of the intermediate tree structure.
	 */
	void print();

	/**
	 * Creates the VMC representation of the intermediate tree structure.
	 * @param flag unfinished compiled code
	 */
	void compile(TextFile flag);
}
