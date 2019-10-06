class UserDaemonThreadDemo
{
   public static void main (String [] args)
   {
      if (args.length == 0)
         new MyThread ().start ();
      else
      {
         MyThread mt = new MyThread ();
         mt.setDaemon(true);
         mt.start ();
      }
      try
      {
         Thread.sleep (100);
      }
      catch (InterruptedException e)
      {
      }
   }
}
class MyThread extends Thread
{
   public void run ()
   {
      System.out.println ("Daemon is " + isDaemon ());
      while (true);
   }
}
