public class Client implements Comparable<Client>{
    private int ID;
    private int arrivalTime;
    private int processTime;
    private int finalTime;

    public Client(int ID,int arrivalTime,int processTime)
    {

        this.ID=ID;
        this.arrivalTime=arrivalTime;
        this.processTime=processTime;
    }

    public int getID()
    {
        return this.ID;
    }
    public int getArrivalTime()
    {
        return this.arrivalTime;
    }

    public int getProcessTime()
    {
        return this.processTime;
    }
    public String showInfo()
    {
        return Integer.toString(this.ID)+" "+Integer.toString(this.arrivalTime)+" "+Integer.toString(this.processTime);
    }

    public void setFinalTime(int queueTime) {
        this.processTime=this.processTime+queueTime;
    }
    public int getFinalTime()
    {
        return this.finalTime;
    }

    public int compareTo(Client client) {

        return this.arrivalTime-client.getArrivalTime();
    }

    public void decrementProcessTime()
    {
        this.processTime--;
    }

    public String toString()
    {
        return "("+this.ID+" "+this.arrivalTime+" "+this.processTime+")"+"\n";
    }
}
