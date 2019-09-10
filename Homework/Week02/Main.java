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
