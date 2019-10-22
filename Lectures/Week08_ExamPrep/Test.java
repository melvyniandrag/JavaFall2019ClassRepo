public class Test{
	/**
	 * Print a string and return if the printed value was more than 1
	 * character long.
	 *
	 * @param s - the input string
	 * @return a bool saying if s was longer than 1 character
	 *
	 * This method actually counts the code points in the string `s'
	 * and not Java 2-byte chars. 
	 */
	public bool PrintString(String s){
		System.out.println(s);
		if(s.length() > 1 ){
			return false;
		}
		else{
			return true;
		}
	}
	
	public static void main(String[] args){
		String testString = " "
		bool isLongerThan1 = PrintString(testString);
	}

}

