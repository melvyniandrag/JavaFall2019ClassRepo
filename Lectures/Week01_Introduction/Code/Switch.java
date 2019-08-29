public class Switch{
    public static void main(String[] args){
        int i = 1;
        //switch is like if, but you use it if you have alot of ifs.
        switch(i){
            case 4:
                System.out.println("Bad");
                break; // Remove the breaks and see what happens.
            case 3:
                System.out.println("Bad");
                break;
            case 2:
                System.out.println("Bad");
                break;
            case 1:
                System.out.println("Good");
                break;
            default:
                System.out.println("Bad");
        }
    }
}
