public class Emoji{
	public static void main(String[] args){
		/*
		 Problem! This \\uXXXX is only for 4 Xs.
		 int smiley = '\\u1f600';
		 Question - so how do we print out a smiley?!!?
		*/
		String val = "\ud83d\ude00";
		System.out.println(val);
	}
}
