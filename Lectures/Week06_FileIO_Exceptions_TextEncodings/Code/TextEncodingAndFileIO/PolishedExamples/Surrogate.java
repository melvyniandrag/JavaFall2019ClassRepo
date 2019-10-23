

public class Surrogate{
	public static void main(String[] args){
		int trebleClefInt = 0x1D11E;
		String s2 = new String(new int[]{trebleClefInt, trebleClefInt},
					 0, 2);
		System.out.println(s2);
		System.out.println(s2.length());
	}
}

