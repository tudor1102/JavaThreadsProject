import java.util.ArrayList;
import java.util.Collections;
import java.util.ListIterator;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


public class SimManager implements Runnable
{
    public int timeLimit;
    public int maxProcessingTime;
    public int minProcessingTime;
    public int minArrivalTime;
    public int maxArrivalTime;
    public int maxNumClients;
    public int maxNumQueues;
    public ReadFile readerFile;
    public FileWriter writerFile;
    public String writeToFile="";
    public int numClientsDeleted=0;
    public int sum=0;


    private Scheduler scheduler;
    private ArrayList<Client> clients;
    private ArrayList<Integer> aTimes;
    private ArrayList<Integer> aux;

    public SimManager(File f,File fw) throws IOException {
        this.readerFile=new ReadFile(f);
        this.writerFile=new FileWriter(fw);
        this.maxNumClients=readerFile.getNumClients();
        this.maxNumQueues=readerFile.getNumQueues();
        this.timeLimit=readerFile.getSimulationTime();
        this.minArrivalTime=readerFile.getMinArrivalTime();
        this.maxArrivalTime=readerFile.getMaxArrivalTime();
        this.minProcessingTime=readerFile.getMinProcessTime();
        this.maxProcessingTime=readerFile.getMaxProcessTime();
        this.scheduler=new Scheduler(maxNumQueues,maxNumClients);
        this.clients=new ArrayList<Client>();
        this.aTimes=new ArrayList<Integer>();
        this.aux=new ArrayList<Integer>();

        generateNRandomClients();
        for(Queue q:this.scheduler.getQueues())
        {
            Thread t=new Thread(q);
            t.start();
        }
        for(Client c:this.clients)
        {
            this.aTimes.add(c.getArrivalTime());
        }
    }
    public int generateRandomNumber(int min,int max)
    {
        if (min>=max) throw new IllegalArgumentException("Min este mai mare decat Max!!");

        return (int)((Math.random() * ((max - min) + 1)) + min);
    }
    private void generateNRandomClients()
    {
        for(int i=0;i<maxNumClients;i++)
        {
            int auxArrivalTime=generateRandomNumber(minArrivalTime,maxArrivalTime);
            int auxProcTime=generateRandomNumber(minProcessingTime,maxProcessingTime);

            Client t=new Client(i,auxArrivalTime,auxProcTime);
            this.clients.add(t);
        }
        Collections.sort(this.clients);
    }
    public void writeQueues(int time)
    {
        for(Queue q:scheduler.getQueues())
        {
            writeToFile+=q.toString();
            if (q.getClients().peek()!=null)
            {
                q.decrementFirst();
                if (q.getClients().peek().getProcessTime()==0)
                {
                    numClientsDeleted++;
                    try {
                        this.updateTimes(time,aux);
                        q.getClients().take();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    public boolean checkStoreEmpty(int time)
    {
        boolean empty=false;
        if (time>2 && checkAllQueues()==true && checkNoClientsRemaining()==true)
        {
            String aux="";
            aux+="Time "+Integer.toString(time+1)+"\n";
            for(Queue q:this.scheduler.getQueues())
            { aux+=q.toString(); }
            try {
                sum=this.sumTimes();
                this.writerFile.write(aux+"\n\n"+"Average waiting time: "+Double.toString(sum/numClientsDeleted));
                this.writerFile.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
           empty=true;
        }
        return empty;
    }
    public void writeTheWaitingClients()
    {
        writeToFile+="Waiting \n";
        for (Client c : clients) {
            writeToFile+=c.toString();
        }
    }
    public void updateTimes(int time,ArrayList<Integer> list)
    {
       for(Integer i:this.aTimes)
       {
           i=time-i+1;
           list.add(i);
       }
    }
    public int sumTimes()
    {
        int result=0;
        for(Integer i:this.aux)
        {
            result+=i;
        }
        return result;
    }
    @Override
    public void run() {
        int currentTime=0;
                while(currentTime<timeLimit)
                {
                   this.writeToFile="";
                   writeToFile+="Time"+Integer.toString(currentTime)+"\n";
                    for(ListIterator<Client> i=clients.listIterator();i.hasNext();)
                    {
                        Client c=i.next();
                        if (c.getArrivalTime()==currentTime)
                        { scheduler.dispatchClient(c);i.remove(); }
                    }
                  this.writeQueues(currentTime);
                    if (checkNoClientsRemaining()==false) { this.writeTheWaitingClients(); }
                    try {
                        this.writerFile.write(writeToFile+"\n\n");
                        this.writerFile.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        this.writerFile.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (this.checkStoreEmpty(currentTime)==true){

                        break;
                    }
                    currentTime++;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
    }
    public boolean checkAllQueues()
    {
        boolean allGood=true;
        ArrayList<Queue> queues=this.scheduler.getQueues();
        for(Queue q:queues)
        {
            if (q.checkQueue()==false)
            {
                allGood=false;
                break;
            }

        }
        return allGood;
    }
    public boolean checkNoClientsRemaining()
    {
        boolean noClients=true;
        ArrayList<Client> clients=this.clients;
        for(Client c:clients)
        {
            if (c!=null)
            {
                noClients=false;
                break;
            }
        }
        return noClients;
    }
    public static void main(String[] args) throws IOException {
        File f = new File(args[0]);
        File fw=new File(args[1]);
        SimManager sim=new SimManager(f,fw);
        Thread t=new Thread(sim);
        t.start();
    }

}
