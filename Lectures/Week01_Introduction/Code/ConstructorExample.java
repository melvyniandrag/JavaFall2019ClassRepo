public class ConstructorExample{
    
    public int x; // x has no value yet.
    
    // class constructor
    ConstructorExample(int i){
        x = i; // now x has a value.
    }

    public static void main(String[] args){
        ConstructorExample ex = new ConstructorExample(1);
        System.out.println(ex.x);
    }

    
}
