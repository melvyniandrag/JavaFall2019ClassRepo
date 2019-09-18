public class FloatCompareExample{
	public static void main(String[] args){
		Float f = new Float(1);
		Float f0 = new Float(0.0f);
		Float f1 = new Float(1.0f);
		Float f2 = new Float(2.0f);

		int comparisonReturnValue0 = f.compareTo(f0);
		System.out.println("Comparison of f and f0 = " + comparisonReturnValue0 );

		int comparisonReturnValue1 = f.compareTo(f1);
		System.out.println("Comparison of f and f1 = " + comparisonReturnValue1 );

		int comparisonReturnValue2 = f.compareTo(f2);
		System.out.println("Comparison of f and f2 = " + comparisonReturnValue2 );
	}
}
