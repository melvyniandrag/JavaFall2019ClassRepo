# Homework 2

## Problem 1

Given this table of values:

Name | Age
Madison | 34
Stephanie | 60
Allyson | 50
Maria | 17
Michael | 26
Joseph | 30
Stephen | 40
Ryan | 14
Margaret | 13
Candace | 39

Write a Java class containing the methods:

```
reportOldestPerson()
reportYoungestPerson()
reportNthOldestPerson( int n )
```

that will report the indicated person from the data list.

Code to get you started:

```
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
```

Expected output:

```
melvyn@thinkpad$ javac Ages.java
melvyn@thinkpad$ java Ages
The oldest person is Stephanie
The youngest person is Ryan
The 2nd oldest person is Allyson
[ERROR] You can't choose the 20th oldest. Choose a number between 1- 10
[ERROR] You can't choose the -1th oldest. Choose a number between 1-10
The 1st oldest person is Stephanie
```

### Grading 
To verify that your code is good, I am going to change you mNames and mAges arrays to make sure that your code still reports the correct oldest, second oldest, nth oldest, and error messages.


## Problem 2

Write an interface `Shape` with methods `getShapeName` and `getPerimeter`. Write classes called `Square`, `Rectangle`, `Circle` and `Triangle` that implement the interface. Supply the proper parameters to the `Square`, `Rectangle`, `Triangle` and `Circle` constructors such that the perimeter can be calculated.


Sample code:
```
//Save this in Shape.java
public interface Shape{
	public double getPerimeter();
	public String getShapeName();
}

//Save this in Triangle.java
public class Triangle implements Shape{
	private double side1;
	private double side2;

	Triangle(double length1, double length2, double length3 ){
		side1 = length1;
		side2 = length2;
		side3 = length3;
	}

	public String getShapeName(){
		return "Triangle";
	}
	
	public double getPerimeter(){
		return side1 + side2 + side3;
	}
}


// Save this in Main.java
public class Main{
	public static void main(String[] args){
		Shape shape = new Triangle(3, 4, 5);
		System.out.println(String.valueOf(shape.getPerimeter()));
		System.out.println(shape.getShapeName());

		// Now do the same thing for the Square, Rectangle and Circle classes.

		
		// Shape circle = new Circle(PARAMS);
		// System.out.println(String.valueOf(circle.getPerimeter());
		// System.out.println(circle.getShapeName());

		// Shape square = new Circle(PARAMS);
		// System.out.println(String.valueOf(square.getPerimeter());
		// System.out.println(square.getShapeName());

		// Shape rectangle = new Circle(PARAMS);
		// System.out.println(String.valueOf(rectangle.getPerimeter());
		// System.out.println(rectangle.getShapeName());
	}
}
```



## Submission Guidelines
Submit via blackboard by Sunday, September 15th by Midnight ( the midnight between Sunday and Monday ). For each problem, submit your .java files along with a screen shot showing that you compiled and ran the code using javac + java on the command line.

All submissions should be unique.

50% for each problem completely and correctly done.
