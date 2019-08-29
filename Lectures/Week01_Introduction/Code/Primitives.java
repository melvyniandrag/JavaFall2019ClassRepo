/*
 * This class is like our HelloWorld class, but now it has some variables associated with it.
 * We can print them out in main() now. I told you this is OOP! See we have an object called 
 * 'myObject' of type 'Primitives' that can carry around some variables ( e.g. x, s, etc.)
 *
 *  Have no fear, this is baby stuff and this code will be clear as water to you in minutes - days from now.
 */
public class Primitives{
    int    x = 1;
    short  s = 11;
    long   l = 1008;
    float  f = 1.0f;
    double d = 1.0;
    boolean b = true;
    char c = 'a';
    
    // Note how we have to say (byte) here.
    byte   y = (byte) 0x01;

    // How to make an array
    int[] intArr = { 1, 2, 3 };
    
    

    public static void main(String[] args){
        Primitives myObject = new Primitives();
        System.out.println(myObject.f);
        System.out.println(myObject.intArr[2]);
        System.out.println(myObject.c);
    }
}
