import java.util.ArrayList; // import the arrayList class
import java.util.Queue; // import the Queue class
import java.util.LinkedList;//linked list structure of queue
import java.util.*;
import java.util.Scanner;  // Import the Scanner class
import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors

public class FB
{
    private Queue<Task> que = new LinkedList<Task>(); // Create an ArrayList object for initial storage of tasks
    private Queue<Task> tempQue = new LinkedList<Task>(); // Create an ArrayList object for initial storage of task
    private Queue<Task> finishQue = new LinkedList<Task>(); // Create an ArrayList object for initial storage of tasks
    private Queue<Task> p0 = new LinkedList<Task>(); //priority level 1
    private Queue<Task> p1 = new LinkedList<Task>(); //priority level 2
    private Queue<Task> p2 = new LinkedList<Task>(); //priority level 3
    private Queue<Task> p3 = new LinkedList<Task>(); //priority level 4
    private Queue<Task> p4 = new LinkedList<Task>(); //priority level 5
    private Queue<Task> p5 = new LinkedList<Task>(); //priority level 6

    private int TaskCount;
    private Task active = null;
    private Dispatcher disp = new Dispatcher();//dispatcher working time value
    private int timer = 0;//reference of time for tasks starts and stops

    public double FB_Avg_Wait;
    public double FB_Avg_Trn;

    public void run(String fileName)
    {
        readFile(fileName);

        int quanta;
        timer = 0;
        checkIn();
        while(allEmpty() == false || que.peek() != null || active != null)
        {
            quanta = 0;
            checkIn();

            if(active == null)//if CPU empty
            {
                timer = timer + disp.get_workT();//run dispatcher
                //keep track of how long other jobs are waiting
                wait(p0);
                wait(p1);
                wait(p2);
                wait(p3);
                wait(p4);
                wait(p5);

                //put next job into CPU
                active = nextAvailable().poll();
                //log the change of CPU job
                disp.addLog(timer, active);

                while(quanta < 4)
                {
                    checkIn();
                    active.set_execCount(active.get_execCount()+ 1);//keep track of where current job is up to

                //keep track of how long other jobs are waiting
                    wait(p0);
                    wait(p1);
                    wait(p2);
                    wait(p3);
                    wait(p4);
                    wait(p5);

                    if(active.get_execSize() == active.get_execCount())//if task finished
                    {
                        active.set_finishT(timer);//mark the time in task
                        active.set_turnTRR();//record the task trun around Time
                        finishQue.add(active);//store task in finish list
                        active = null; //empty CPU
                        timer++;
                        quanta = 4;

                    }
                    else if(quanta == 3 && allEmpty() == true)//continue on task if its the only thing left
                    {
                        quanta = 0;
                        timer++;
                    }
                    else if(quanta == 3)//if quanta has been reached
                    {
                        active.less_priority(1);
                        sendPriority(active);
                        active = null;
                        quanta = 4;
                        timer++;
                    }
                    else if(quanta < 3)
                    {
                        quanta++;
                        timer++;
                    }
                }
            }
        }

        //sort task info for printing
        while(finishQue.peek() != null || tempQue.peek() != null)//while both queue arent empty sort and store in final
        {
            for(int i = 1; i < TaskCount+1; i++)//go through finishQue
            {
                while(finishQue.peek() != null)
                {
                    if(finishQue.peek().get_idInt() == i)//check if id matches i
                    {
                        que.add(finishQue.poll());//if so then store task in que
                    }
                    else
                    {
                        tempQue.add(finishQue.poll());//if not set aside to check next i
                    }
                }
                while(tempQue.peek() != null)//put the set aside list back onto finished que to check for next item
                {
                    finishQue.add(tempQue.poll());
                }
            }
        }

        System.out.println("FB:");
        disp.printLog();
        System.out.println();
        System.out.println("Process Turnaround Time Waiting Time");
        while(que.peek() != null)
        {
            System.out.println(que.element().get_id() + "      " + que.element().get_turnTRR() + "               " + que.element().get_waitC());
            FB_Avg_Trn = FB_Avg_Trn + que.element().get_turnTRR();
            FB_Avg_Wait = FB_Avg_Wait + que.element().get_waitC();
            que.remove();
        }
        System.out.println();
        FB_Avg_Trn = FB_Avg_Trn/TaskCount;
        FB_Avg_Wait = FB_Avg_Wait/TaskCount;

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
                    p0.add(que.poll());//if so then add to ready queue
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

    private void wait(Queue<Task> readyQue)
    {
        while(readyQue.peek() != null)
        {
            readyQue.peek().set_waitC(readyQue.peek().get_waitC()+ 1);
            tempQue.add(readyQue.poll());
        }
        while(tempQue.peek() != null)
        {
            readyQue.add(tempQue.poll());
        }
    }
    private boolean allEmpty()
    {
        if(p0.peek() == null && p1.peek() == null && p2.peek() == null && p3.peek() == null && p4.peek() == null && p5.peek() == null)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    private Queue<Task> nextAvailable()
    {
        if(p0.peek() != null)
        {
            return p0;
        }
        else if(p1.peek() != null)
        {
            return p1;
        }
        else if(p2.peek() != null)
        {
            return p2;
        }
        else if(p3.peek() != null)
        {
            return p3;
        }
        else if(p4.peek() != null)
        {
            return p4;
        }
        else if(p5.peek() != null)
        {
            return p5;
        }
        else
        {
            return null;
        }
    }
    private void sendPriority(Task a)
    {
        switch(a.get_priority())
        {
            case 0:
                p0.add(active);
                break;
            case 1:
                p1.add(active);
                break;
            case 2:
                p2.add(active);
                break;
            case 3:
                p3.add(active);
                break;
            case 4:
                p4.add(active);
                break;
            case 5:
                p5.add(active);
                break;
        }
    }
    public double get_FB_Avg_Wait()
    {
        return FB_Avg_Wait;
    }
    public double get_FB_Avg_Trn()
    {
        return FB_Avg_Trn;
    }
}
