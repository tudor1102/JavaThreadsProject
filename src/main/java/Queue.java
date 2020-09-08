import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Queue implements Runnable{
    private LinkedBlockingQueue<Client> clients;
    private AtomicInteger waitTime;
    private int ID;
    private volatile boolean queueOn;
    public Queue(int ID)
    {
        this.ID=ID;
        this.clients=new LinkedBlockingQueue<Client>();
        this.waitTime=new AtomicInteger(0);
    }
    public void addClientToQueue(Client c)
    {
        this.clients.add(c);
        this.queueOn=true;
        this.waitTime.getAndAdd(c.getProcessTime());
    }

    public void setQueueOff()
    {
        this.queueOn=false;
    }

    public boolean checkQueue()
    {
        boolean queueEmpty=true;
        for(Client c:this.clients)
        {
            if (c!=null)
            {
                queueEmpty=false;
                break;
            }
        }
        return queueEmpty;
    }
    @Override
    public void run() {

            while(this.queueOn==true)
            {
                    if (this.checkQueue()==true)
                    {
                        this.setQueueOff();
                    }
                    else
                    {
                        try {

                            Thread.sleep(this.clients.peek().getProcessTime()*1000);
                            this.clients.peek().setFinalTime(this.clients.peek().getProcessTime()+this.waitTime.get());
                            this.waitTime.getAndAdd(-this.clients.peek().getProcessTime());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
            }
    }
    public void decrementFirst()
    {
        this.clients.peek().decrementProcessTime();
    }
    public void showQueueInfo()
    {
        if (this.clients.peek()==null)
        {
            System.out.println("Queue "+this.ID+" closed");
        }
        else
        {
            System.out.println("Queue "+this.ID);

            for(Client c:this.clients) {

                    System.out.println("(" + c.getID() + " " + c.getArrivalTime() + " " + c.getProcessTime() + ")");

            }
        }
    }
    public String toString()
    {
        String result="";
        if (this.clients.peek()==null)
        {
            result+="Queue "+this.ID+" closed"+"\n";
        }
        else
        {
            result+="Queue "+this.ID+"\n";

            for(Client c:this.clients) {

               result+=c.toString();

            }
        }
        return result;
    }
    public AtomicInteger getWaitTime()
    {
        return this.waitTime;
    }
    public LinkedBlockingQueue<Client> getClients()
    {
        return this.clients;
    }
}
