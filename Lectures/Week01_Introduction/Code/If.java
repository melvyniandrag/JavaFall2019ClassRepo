public class If{
    public static void main( String[] args ){
        // If
        if ( 1 < 2 ){
            System.out.println("Good");
        }
        
        // If, else
        if( 2 < 1 ){
            System.out.println("Bad");
        }
        else {
            System.out.println("Good.");
        }

        // If, else if, else. 
        if ( 2 < 1 ) {
            System.out.println("Bad");
        }
        else if ( 3 < 1 ) {
            System.out.println("Also bad");
        }
        else{
            System.out.println("Good.");
        }

    }
}
