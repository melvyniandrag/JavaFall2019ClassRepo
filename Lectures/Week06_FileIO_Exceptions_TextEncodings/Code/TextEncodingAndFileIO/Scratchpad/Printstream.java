/*
 * Stolen from 
 * https://www.geeksforgeeks.org/java-io-printstream-class-java-set-1/
 *
 *
 *
 *
 */
import java.io.FileNotFoundException; 
import java.io.FileOutputStream; 
import java.io.PrintStream; 
import java.util.Locale; 
//Java program to demonstrate PrintStream methods 
class Printstream 
{ 
    public static void main(String args[]) throws FileNotFoundException 
    { 
        FileOutputStream fout = new FileOutputStream("file.txt"); 
          
        //creating Printstream obj 
        PrintStream out=new PrintStream(fout); 
        String s="First"; 
  
        //writing to file.txt 
        char c[]={'G','E','E','K'}; 
          
        //illustrating flush method 
        out.flush(); 
          
        //illustrating close method 
        out.close(); 
    } 
} 
