public class ThreadForever implements Runnable{
    @Override
    public void run(){
        while(true){
            System.out.println("Im still running");
            try{
                Thread.sleep(10);
            }
            catch(Exception e){
            }
        }
    }

    public static void main(String[] args){
        Thread tf = new Thread( new ThreadForever());
        tf.start();
    }
}
