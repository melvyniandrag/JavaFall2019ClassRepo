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
        FileOutputStream fout=new FileOutputStream("file.txt"); 
          
        //creating Printstream obj 
        PrintStream out=new PrintStream(fout); 
        String s="First"; 
  
        //writing to file.txt 
        char c[]={'G','E','E','K'}; 
          
        //illustrating print(boolean b) method 
        out.print(true); 
          
        //illustrating print(int i) method 
        out.print(1); 
          
        //illustrating print(float f) method 
        out.print(4.533f); 
          
        //illustrating print(String s) method 
        out.print("GeeksforGeeks"); 
        out.println(); 
          
        //illustrating print(Object Obj) method 
        out.print(fout); 
        out.println(); 
          
        //illustrating append(CharSequence csq) method 
        out.append("Geek"); 
        out.println(); 
          
        //illustrating checkError() method 
        out.println(out.checkError()); 
          
        //illustrating format() method 
        out.format(Locale.UK, "Welcome to my %s program", s); 
          
        //illustrating flush method 
        out.flush(); 
          
        //illustrating close method 
        out.close(); 
    } 
} 
