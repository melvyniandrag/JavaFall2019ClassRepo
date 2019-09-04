public class EnumExample {

	public enum Color {
		GREEN,
		BLUE,
		RED,
		YELLOW
	};

	public Color myColor = Color.RED;

	public void yellowMethod(){
		System.out.println("Ran the yellow method");
	}


	public static void main(String[] args){
		EnumExample e = new EnumExample();
		if( e.myColor == Color.RED ){
			System.out.println("Color is red.");
		}

		e.myColor = Color.YELLOW;

		switch( e.myColor ){
			case GREEN : //This is correct syntax
			//case Color.GREEN : // This is wrong syntax
			//case( Color.GREEN ): // This is wrong syntax
			//case ( GREEN ): // This is wrong syntax.
				System.out.println("Do nothing");
				break;
			case BLUE :
				System.out.println("Do nothing here either.");
				break;
			case  RED :
				System.out.println("Nothing to be done about it.");
				break;
			default:
				System.out.println("By default you must have chosen yellow!");
				e.yellowMethod();
		}
		 
	}
}
