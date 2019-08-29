public class MelvynWeek01{
    private int[] myIntArr = new int[] {
            1,
            2,
            3
        };

    public int[] getMyIntArray(){
        System.out.println("Getting int array . . .");
        return myIntArr;
    }

    public void useAnOrComparator(){
        System.out.println("Using or comparator . . .");
        int x = 1;
        if( (x > 1) || ( x == 1 ) ){
            System.out.println("Successfully used an 'or' comparison");
        }
    }

    public static void main( String[] arguments ){
        System.out.println("Starting to run homework code");
        MelvynWeek01 homework = new MelvynWeek01();
        int[] intArr = homework.getMyIntArray();
        homework.useAnOrComparator();
        System.out.println("Finished running homework code");
    }
}
