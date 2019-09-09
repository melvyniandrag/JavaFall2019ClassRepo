public class Student{
	public char finalGrade;
	public int age;

	Student(char grade, int myage){
		finalGrade = grade;
		age = myage;
	}

	public static void main(String[] args){
		Student Bruno = new Student( 'F', 11 );
		System.out.println("Bruno got an :" + Bruno.finalGrade );
		System.out.println("Bruno is this old:" + Bruno.age );
	}
}
