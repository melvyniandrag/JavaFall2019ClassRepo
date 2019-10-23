public class PrintString{

	/**
	 * Print a string and return if the printed value was more than 80 
	 * characters long.
	 *
	 * @param s - the input string
	 * @return a bool saying if s was longer than 80 characters
	 *
	 * This method actually counts the code points in the string `s'
	 * and not Java 2-byte chars. 
	 */
	public boolean printString(String s){
		System.out.println(s);
		return s.length() > 80 ? true : false;
	}

	public static void main(String[] args){
		PrintString ps = new PrintString();
		String s = "Hello World";
		boolean truth = ps.printString(s);
		System.out.println(String.format("Is '%s' longer than 80? %b", s, truth) );
	}
}

