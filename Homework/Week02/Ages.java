public class Ages{
	private int[] mAges = {
		34, 60, 50, 17, 26, 30, 40, 14, 13, 39
	};

	private String[] mNames = {
		"Madison", "Stephanie", "Allyson", "Maria", "Michael",
		"Joseph", "Stephen", "Ryan", "Margaret", "Candace"
	};

	public void reportOldestPerson(){
		// Find the oldest person using mNames and mAges
		System.out.println("The oldest person is XX");
	}
	

	public void reportYoungestPerson(){
		// Find youngest person using mNames and mAges
		System.out.println("The youngest person is YY");
	}

	public void reportNthOldestPerson(int index){
		// Find the nth oldest person in mAges/mNames
		// if `index` is < 1, report an error like:
		//	 "You can't access the -500th oldest, doesn't make sense"
		// if `index` is > the number of elements in mNames, report an error like 
		// 	"There aren't enough people to report the `100th oldest`,
		//	 please choose a number between A and B."
		System.out.println("You choose: " + index);
	}

	public static void main(String[] args){
		Ages ages = new Ages();
		ages.reportOldestPerson();
		ages.reportYoungestPerson();
		ages.reportNthOldestPerson(2);
		ages.reportNthOldestPerson(20);
		ages.reportNthOldestPerson(-1);
		ages.reportNthOldestPerson(1);
	}
}
