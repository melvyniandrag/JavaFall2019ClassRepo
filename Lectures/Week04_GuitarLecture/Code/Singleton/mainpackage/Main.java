package mainpackage;

import singletonpackage.SingletonExample;

public class Main{
	public static void main(String[] args){
		SingletonExample s = SingletonExample.getInstance();
		s.i = 100;
		SingletonExample s2 = SingletonExample.getInstance();
		
		System.out.println("s2.i = " + s2.i);
	}
}
