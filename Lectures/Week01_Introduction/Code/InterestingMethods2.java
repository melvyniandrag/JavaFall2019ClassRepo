public class InterestingMethods2{
    private int i;

    public int getInt(){
        return i;
    }

    public void setInt( int parameter ){
        System.out.println("changing the value of i . . . ");
        i = parameter;
    }

    InterestingMethods2(int constructorArgument){
        i = constructorArgument;
    }

    public static void main(String[] args){
        InterestingMethods2 im2 = new InterestingMethods2(10);
        int i = im2.getInt();
        System.out.println("First im2.getInt() returned value: " + i );

        im2.setInt(11);
        int j = im2.getInt();
        System.out.println("Then im2.getInt() returned value: " + j );

    }
}
