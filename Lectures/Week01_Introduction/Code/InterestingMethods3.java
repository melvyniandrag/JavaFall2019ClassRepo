public class InterestingMethods3{
    private int i;

    public int getInt(){
        return i;
    }

    public void setInt( int parameter ){
        System.out.println("changing the value of i . . . ");
        i = parameter;
    }

    InterestingMethods3(int constructorArgument){
        i = constructorArgument;
    }

    public static void main(String[] args){
        InterestingMethods3 im2 = new InterestingMethods3(10);
        int mel = im2.getInt();
        System.out.println("First im2.getInt() returned value: " + mel );

        im2.setInt(11);
        int j = im2.getInt();
        System.out.println("Then im2.getInt() returned value: " + j );

    }
}
