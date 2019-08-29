public class WhileAndFor{
    public static void main( String[] args ){
        
        // start i at 1. Then increment it and keep running so long as it is less than 4.  
        int i = 1;
        System.out.println("Before while loop: i = " + i );
        while( i < 4 ){
            i =  i + 1;
        }
        System.out.println("After while loop: i = " + i );
        

        // Loop from j = -1, so long as it is less than 10.
        for( int j = -1; j < 10; j++){
            System.out.println("Inside for loop, j = " + j );
        }
    
    }


}
