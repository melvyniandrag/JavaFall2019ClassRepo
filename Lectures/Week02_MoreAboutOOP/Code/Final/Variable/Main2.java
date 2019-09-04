/*
 * Another example of usage of the final keyword - we don't have to initialize the variable x when we declare it final. 
 * final just means it can be initialized at most once.
 *
 * Note that this code works.
 *
 * Question - if the variable is final and you put its initialization in a for loop, is the generated error a compiler error or a runtime error?
 */
public class Main2{

	public static void main(String[] args){
	
		final int x;
		x = 2;

	}
}
