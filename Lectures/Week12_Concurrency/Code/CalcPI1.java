class CalcPI1
{
    public static void main (String [] args)
    {
        MyThread mt = new MyThread ();
        mt.start ();
        for(int  i = 0; i < 100; i++){
          System.out.println("interupting computation");    
     }
    }
}

class MyThread extends Thread
{
    boolean negative = true;
    double pi; // Initializes to 0.0, by default
    public void run ()
    {
        for (int i = 3; i < 1000000; i += 2)
        {
              if (negative)
                pi -= (1.0 / i);
              else
                pi += (1.0 / i);
              negative = !negative;
              if(i%1001 == 0)
                System.out.println("computing... " + (i%51));
        }
        pi += 1.0;
        pi *= 4.0;
        System.out.println ("Finished calculating PI: " + pi);
    }
}
