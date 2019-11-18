import java.util.ArrayList;

public class ToArrayTest{
	public static void main(String[] args){
		ArrayList<Double> al = new ArrayList<Double>();
		al.add(1.0);
		al.add(2.0);	
		Double[] dArr = al.toArray(new Double[al.size()]);
		for( Double d : dArr ){
			System.out.println(d);
		}
	}
}

