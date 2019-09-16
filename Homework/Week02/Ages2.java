import java.util.Arrays;

class Ages2{
	int[] ages = {1, 12, 3, 10, 4};

	String[] names = {"a", "b", "c", "d", "e"};

	public int findNthOldestHelper(int i){
		int[] agesCopy = Arrays.copyOf(ages, ages.length);
		Arrays.sort(agesCopy);
		return agesCopy[agesCopy.length - i];
	}

	public void reportNthOldestPerson(int index){
		int nthOldest = findNthOldestHelper(index);
		int nthOldestIndex = -1;
		for( int i = 0; i < ages.length; ++i){
			if ( ages[i] == nthOldest ){
				nthOldestIndex = i;
				break;
			}
		}
		System.out.println( "You chose: " + names[nthOldestIndex]);
	}

	public static void main(String[] args){
		Ages2 a = new Ages2();
		a.reportNthOldestPerson(1); // expect b
		a.reportNthOldestPerson(2); // expect d 
		a.reportNthOldestPerson(3); // expect e
		a.reportNthOldestPerson(4); // expect c  
		a.reportNthOldestPerson(5); // expect a
	}

}
