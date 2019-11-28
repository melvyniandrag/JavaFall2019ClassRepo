import java.util.Comparator;

public class ComparatorWithLambdas{
	public static void main(String[] args){
		Comparator<String> stringComparatorLambda =
			(o1, o2) -> { return o1.compareTo(o2); };

		int lambdaComparison = stringComparatorLambda.compare("Hello", "Worlds");
		System.out.println(lambdaComparison);
		int lambdaComparison2 = stringComparatorLambda.equals("Hello", "Worlds");
		System.out.println(lambdaComparison2);
	}

}
