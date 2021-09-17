import java.util.List;
import java.util.Iterator;
import java.util.LinkedList;

public class IteratorList{
    public static void main(String[] args){
        List<Foo> list = new LinkedList<Foo>();
        list.add(new Foo());
        list.add(new Foo());
        list.add(new Foo());
        for(Iterator<Foo> itr = list.iterator(); itr.hasNext();){
            System.out.println(itr.next().myFoo);
        }
    }
}

class Foo{
    public static int N_FOOS = 0;
    public int myFoo;
    public Foo(){
        myFoo = N_FOOS;
        N_FOOS++;
    }
}
