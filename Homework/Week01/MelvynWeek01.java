public class MelvynWeek01{
    private int[] myIntArr = new int[] {
            1,
            2,
            3
        };

    public int[] getMyIntArray(){
        return myIntArr;
    }

    public static void main( String[] arguments ){
        System.out.println("Starting to run homework code");
        MelvynWeek01 homework = new MelvynWeek01();
        int[] intArr = homework.getMyIntArray();
        System.out.println("Finished running homework code");
    }
}
