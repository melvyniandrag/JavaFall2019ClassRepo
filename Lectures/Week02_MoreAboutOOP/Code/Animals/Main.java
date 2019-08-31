public class Main{
	public static void main(String[] args){
		Animal animal = new Animal();
		Dog dog = new Dog();
		Cat cat = new Cat();

		animal.Talk("I don't know what to say!");
		dog.Talk("Woof!");
		cat.Talk("Meow!");
		cat.Hiss();
	}
}
