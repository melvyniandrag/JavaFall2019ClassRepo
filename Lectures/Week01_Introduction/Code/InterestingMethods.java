public class InterestingMethods{
    private int i;

    // Note this says 'public int' whereas the function in ClassMethodsExample.sayHelloWorld() said 'public void'.
    // The 'int' says that the function will return an integer - not void aka nothing. 
    public int getInt(){
        return i;
    }

    InterestingMethods(int constructorArgument){
        i = constructorArgument;
    }

    public static void main(String[] args){
        InterestingMethods im = new InterestingMethods(10);
        int i = im.getInt();
        System.out.println("im.getInt() returned value: " + i );
    }
}
