class Main{
	public static void main(String[] args){
		Implementation impl = new Implementation();
		System.out.println(impl.whatAmI());
		
		Interface interf = new Implementation();
		System.out.println(interf.whatAmI());

		Interface interf2 = new AlternateImplementation();
		System.out.println(interf2.whatAmI());
	}
}
