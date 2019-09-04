/*
 * Compiling this should generate an error. 
 * The javac compiler should say something like 
 *
 * 	'cannot inherit from a final class'
 * 
 */
public class PastFinal extends FinalClass{
	public void doesntWork(){
		System.out.println("This class should not compile.");
	}
}
