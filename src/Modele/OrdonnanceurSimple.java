package Modele;

public class OrdonnanceurSimple extends Thread {

    public Runnable monRunnable;
    private volatile long sleepDuration = 400;
    private volatile boolean flag = false;

    private volatile boolean pause = false;


    public OrdonnanceurSimple(Runnable _monRunnable) {
        monRunnable = _monRunnable;

    }

    @Override
    public void run() {

        while(!flag) {
            while (pause)
            {
                try {
                    sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            try {
                Thread.sleep(sleepDuration);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            monRunnable.run();


        }
    }

    public void setSleepDuration(long duration) {
        this.sleepDuration = duration;
    }
    public void interrupt () {this.flag = true;}

    public void invertPause () {
        pause = !pause;
    }

}
