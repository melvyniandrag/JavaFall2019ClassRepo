public class ClassMethodsExample{
    public void sayHelloWorld(){
        System.out.println("Hello World");
    }

    public static void main(String[] args){
        ClassMethodsExample ex1 = new ClassMethodsExample();
        ex1.sayHelloWorld();

        ClassMethodsExample ex2 = new ClassMethodsExample();
        ex2.sayHelloWorld();
    }
}
