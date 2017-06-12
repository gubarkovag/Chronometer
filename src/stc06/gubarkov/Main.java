package stc06.gubarkov;

public class Main {

    public static void main(String[] args) {
        SessionTimer sessionTimer = new SessionTimer();
        Thread th1 = new Thread(sessionTimer);
        SessionMessage sessionMessage1 = new SessionMessage(sessionTimer, 5);
        Thread th2 = new Thread(sessionMessage1);
        SessionMessage sessionMessage2 = new SessionMessage(sessionTimer, 7);
        Thread th3 = new Thread(sessionMessage2);
        th2.start();
        th3.start();
        th1.start();
    }
}

class SessionTimer implements Runnable {
    private int secondsCount;
    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(1000);
                secondsCount++;
                System.out.println(secondsCount + " seconds passed from beginning");
                synchronized (this) {
                    notifyAll();
                }
            }
        } catch (InterruptedException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public int getSecondsCount() {
        return secondsCount;
    }
}

class SessionMessage implements Runnable {
    private SessionTimer sessionTimer;
    private int neededSecsNumber;

    SessionMessage(SessionTimer sessionTimer, int neededSecsNumber) {
        this.sessionTimer = sessionTimer;
        this.neededSecsNumber = neededSecsNumber;
    }

    @Override
    public void run() {
        try {
            while(true) {
                synchronized (sessionTimer) {
                    sessionTimer.wait();
                    if ((sessionTimer.getSecondsCount() % neededSecsNumber) == 0) {
                        System.out.println(sessionTimer.getSecondsCount() + " seconds passed from beginning");
                    }
                }
            }
        } catch (InterruptedException ex) {
            System.out.println(ex.getMessage());
        }
    }

}
