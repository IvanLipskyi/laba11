package concurrency;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

import static concurrency.Race.startRaceTime;
import static java.lang.Thread.sleep;

public class RaceCarRunnable extends Car implements Runnable {

    private int passed;
    long finishTime;
    private int distance;
    private boolean isFinished;
    CountDownLatch latch;

    public int getPassed() {
        return passed;
    }

    public int getDistance() {
        return distance;
    }

    public boolean isFinished() {
        return isFinished;
    }
    public RaceCarRunnable(String name, int maxSpeed, int distance, CountDownLatch latch) {
        super(name, maxSpeed);
        this.distance = distance;
        this.latch = latch;
    }

    public int getRandomSpeed() {
        return ThreadLocalRandom.current().nextInt(getMaxSpeed()/2, getMaxSpeed());
    }

    @Override
    public void run() {
        while (!isFinished) {
            int currentSpeed = getRandomSpeed();
            try{
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            passed +=(currentSpeed*1000 / (60*60));
            System.out.println(getName() + "=> speed: " + currentSpeed + "; progress: "+ passed+"/"+distance);
            if(passed >=distance){
                finishTime = System.currentTimeMillis() - startRaceTime.get();
                isFinished = true;
                latch.countDown();
            }
        }
    }

}
