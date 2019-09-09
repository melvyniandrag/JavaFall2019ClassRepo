public class Primitives{
	int i = 100;
	byte b = (byte) 1;
	double d = 123.01;
	short s = 123;
	long l = 1234;
	char c = 'a';
	float f = 11.0f;
	float f2 = (float) 1.0;
	int[] iArr = { 1, 2, 3, -100, -19, 1};

	public static void main(String[] args){
		Primitives p = new Primitives();
		System.out.println("My int is " + p.i);
		System.out.println("My long is " + p.l);
		System.out.println("My float is " + p.f);
		System.out.println("My byte is " + p.b);
		//System.out.println("The first element of my array is " + p.iArr[-1]);
		System.out.println("The first element of my array is " + p.iArr[15]);
	}

}
