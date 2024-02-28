import java.util.Queue; // import the ArrayList class
import java.util.LinkedList;//linked list structure of queue
import java.util.Scanner;  // Import the Scanner class
import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors

public class FCFS{
    private Queue<Task> que = new LinkedList<Task>(); // Create an ArrayList object for initial storage of tasks
    private Queue<Task> tempQue = new LinkedList<Task>(); // Create an ArrayList object for initial storage of tasks
    private Queue<Task> readyQue = new LinkedList<Task>(); // Create an ArrayList object for ready tasks
    private Queue<Task> finishQue = new LinkedList<Task>(); // Create an ArrayList object for initial storage of tasks

    private int TaskCount;
    private Task active = null;
    private Dispatcher disp = new Dispatcher();//dispatcher working time value
    private int timer = 0;//reference of time for tasks starts and stops

    private double FCFS_Avg_Wait;
    private double FCFS_Avg_Trn;

    public void run(String fileName)
    {
        readFile(fileName);
        checkIn();
        while(readyQue.peek() != null || que.peek() != null)
        {
            checkIn();
            if(active == null)//if CPU empty
            {
                    timer = timer + disp.get_workT();//run dispatcher

                    active = readyQue.peek();//put first job from ready que into CPU
                    readyQue.remove();//pull task off ready que
                    disp.addLog(timer, active);//log the change of CPU job
                    active.set_execCount(active.get_execCount()+ 1);//keep track of where job is up to
            }
            else if(active != null)
            {
                if(active.get_execSize() == active.get_execCount())//if job is finished
                {
                    timer++;
                    active.set_finishT(timer);//mark the time in task
                    active.set_turnT();//record the task trun around Time
                    active.set_waitT();//record wait time of tasks
                    finishQue.add(active);//store task in finish list
                    //System.out.println(finishQue.element().get_id() + "     " + finishQue.element().get_turnT() + "      " + finishQue.element().get_waitT());
                    active = null; //empty CPU

                }
                else
                {
                    active.set_execCount(active.get_execCount()+1);//keep track of where job is up to
                    timer++;//incriment timer
                }
            }

        }
        while(active != null)
        {
            if(active.get_execSize() == active.get_execCount())//if job is finished
            {
                timer++;//incriment timer
                active.set_finishT(timer);//mark the time in task
                active.set_turnT();//record the task trun around Time
                active.set_waitT();//record wait time of tasks
                finishQue.add(active);//store task in finish list
                //System.out.println(finishQue.element().get_id() + "     " + finishQue.element().get_turnT() + "      " + finishQue.element().get_waitT());
                active = null; //empty CPU

            }
            else
            {
                active.set_execCount(active.get_execCount()+1);//keep track of where job is up to
                timer++;//incriment timer
            }
        }
        System.out.println("FCFS:");
        disp.printLog();
        System.out.println();
        System.out.println("Process Turnaround Time Waiting Time");
        while(finishQue.peek() != null)
        {
            System.out.println(finishQue.element().get_id() + "      " + finishQue.element().get_turnT() + "               " + finishQue.element().get_waitT());
            FCFS_Avg_Trn = FCFS_Avg_Trn + finishQue.element().get_turnT();
            FCFS_Avg_Wait = FCFS_Avg_Wait + finishQue.element().get_waitT();
            finishQue.remove();
        }
        System.out.println();
        FCFS_Avg_Trn = FCFS_Avg_Trn/TaskCount;
        FCFS_Avg_Wait = FCFS_Avg_Wait/TaskCount;
        while(tempQue.peek() != null)//clean temp queue
        {
            tempQue.remove();
        }
        while(que.peek() != null)//clean queue
        {
            que.remove();
        }
        while(readyQue.peek() != null)//clean ready queue
        {
            readyQue.remove();
        }
        while(finishQue.peek() != null)//clean finish queue
        {
            finishQue.remove();
        }
        active = null;//clean CPU
        disp.cleanLog();
    }

    private void readFile(String fileName)
    {
        TaskCount = 0;
        try
        {
            File file = new File(fileName);//create file
            Scanner fileReader = new Scanner(file);//create scanner
            while (fileReader.hasNextLine())//keep reading file as long as there is new text to be read
            {
                String[] strArray = null;//array to store split string when obtaining specific data from each line
                String data;//temp variable for each line to be stored in
                data = fileReader.nextLine();//store next line in temp var
                if(data.contains(" ") == true)//if it has a space in the line
                {
                    strArray = data.split(" ");//split the line at the space
                    //System.out.println(strArray[0]);

                    switch (strArray[0]) //check what label is storing data on that line by checking left sid eof split string
                    {
                        case "DISP:"://dispatcher value to be stored in dispatcher
                           disp.set_workT(Integer.valueOf(strArray[1]));
                           //System.out.println(disp.get_workT());

                           break;
                        case "ID:"://id value
                           TaskCount++;
                           Task temp = new Task();//create temp Task
                           temp.set_id(strArray[1]);//set the id value from file string
                           //System.out.println(temp.get_id());

                           temp.set_idInt(TaskCount);

                           data = fileReader.nextLine();//go to next line
                           strArray = data.split(" ");//split it at the space character
                           temp.set_arrive(Integer.valueOf(strArray[1]));// set arrive time in task class
                           //System.out.println(temp.get_arrive());


                           data = fileReader.nextLine();//go to next line
                           strArray = data.split(" ");// split at the space character
                           temp.set_execSize(Integer.valueOf(strArray[1]));//set the execSize character in task class
                           //System.out.println(temp.get_execSize());

                           if(temp != null){
                               que.add(temp);//store the Task in the initial que
                           }
                           break;
                   }
                }
            }
            fileReader.close();
        }
        catch (FileNotFoundException e)//if file isnt found, print error
        {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    private void checkIn()
    {
            while (que.peek() != null)//while wait queue isnt empty
            {
                if(timer == que.element().get_arrive())//see if next task has arrived
                {
                    readyQue.add(que.poll());//if so then add to ready queue
                }
                else
                {
                    tempQue.add(que.poll());//if not ready put on temp waiting queue
                }
            }
            while(tempQue.peek() != null)//put left over waiting tasks back onto wait queue
            {
                que.add(tempQue.poll());
            }
    }
    public double get_FCFS_Avg_Wait()
    {
        return FCFS_Avg_Wait;
    }
    public double get_FCFS_Avg_Trn()
    {
        return FCFS_Avg_Trn;
    }

}
