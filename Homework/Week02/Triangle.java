public class Triangle implements Shape{
	private double side1;
	private double side2;
	private double side3;

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


