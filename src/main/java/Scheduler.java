import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Scheduler {

    private ArrayList<Queue> queues;
    private int maxNumQueues;
    private int maxClientsPerQueue;

    public Scheduler(int maxNumQueues,int maxClientsPerQueue)
    {
        this.maxNumQueues=maxNumQueues;
        this.maxClientsPerQueue=maxClientsPerQueue;
        this.queues=new ArrayList<Queue>();
        for(int i=0;i<maxNumQueues;i++)
        {
            Queue q=new Queue(i);
            this.queues.add(q);
            Thread t=new Thread(q);
            t.start();
        }

    }

    public void dispatchClient(Client c)
    {
            ArrayList<Queue> queues=this.queues;
            int minimumTime=queues.get(0).getWaitTime().get();
            Queue aux=queues.get(0);
            for(Queue q : queues)
            {
                int currTime=q.getWaitTime().get();
                if (currTime<minimumTime)
                {
                    minimumTime=currTime;
                    aux=q;
                }
            }
        aux.addClientToQueue(c);
    }

   public ArrayList<Queue> getQueues()
   {
       return this.queues;
   }

}
