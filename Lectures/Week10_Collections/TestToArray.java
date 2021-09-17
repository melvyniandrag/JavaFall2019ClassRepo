import java.util.List;
import java.util.LinkedList;

public class TestToArray{
    public static void main(String[] args){
        List<Integer> list = new LinkedList<Integer>();
        list.add(1); list.add(2);// list.add(3);
        Integer[] tryThisOne = new Integer[2];
        Integer[] ints = list.toArray(tryThisOne);
        System.out.println("ints");
        for(int i = 0; i < ints.length; i++){
            System.out.println(ints[i]);
        }
        System.out.println("tryThisOne");
        for(int i = 0; i < tryThisOne.length; i++){
            System.out.println(tryThisOne[i]);
        }
        ints[0] = 100;
        System.out.println("ints");
        for(int i = 0; i < ints.length; i++){
            System.out.println(ints[i]);
        }
        System.out.println("tryThisOne");
        for(int i = 0; i < tryThisOne.length; i++){
            System.out.println(tryThisOne[i]);
        }
 
    }
}
