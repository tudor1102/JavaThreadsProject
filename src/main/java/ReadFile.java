import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class ReadFile {
    private int numClients;
    private int numQueues;
    private int simulationTime;
    private int[] arrivalInterval;
    private int[] processInterval;

    public ReadFile(File f)
    {
        ArrayList<String> dataFile=new ArrayList<String>();
        try {
        Scanner myReader = new Scanner(f);
        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            dataFile.add(data);
        }
        myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        this.numClients=Integer.parseInt(dataFile.get(0));
        this.numQueues=Integer.parseInt(dataFile.get(1));
        this.simulationTime=Integer.parseInt(dataFile.get(2));
        this.arrivalInterval=readAfterComa(dataFile.get(3));
        this.processInterval=readAfterComa(dataFile.get(4));

    }

    public int[] readAfterComa(String s)
    {
        int[] values=new int[2];
        for(int i=0;i<s.length();i++)
        {
            if (s.charAt(i)==',')
            {
                values[0]=Integer.parseInt(s.substring(0,i));
                values[1]=Integer.parseInt(s.substring(i+1,s.length()));

            }
        }
        return values;
    }
    public int getNumClients()
    {
        return this.numClients;
    }
    public int getNumQueues()
    {
        return this.numQueues;
    }
    public int getSimulationTime()
    {
        return this.simulationTime;
    }
    public int getMinArrivalTime()
    {
        return this.arrivalInterval[0];
    }
    public int getMaxArrivalTime()
    {
        return this.arrivalInterval[1];
    }
    public int getMinProcessTime()
    {
        return this.processInterval[0];
    }
    public int getMaxProcessTime()
    {
        return this.processInterval[1];
    }



    public static void main(String[] args)
     {
             File myObj = new File("In-Test.txt");
             ReadFile r=new ReadFile(myObj);

     }

}
