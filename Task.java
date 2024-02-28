/*Written by Benjamin Kellahan
 * For COMP2240, Assessment 1
 * Student No#: c3311635
 * Program Task class, stores information on tasked to be executed by CPU */
import java.util.*;
 public class Task{

     private String id;//task id
     private int idInt;//id in int form
     private int execSize;//size of task
     private int execCount;//counter to keep track of task completeness
     private int arrive;//task arrival time
     private int TurnT;//FCFS turn around time
     private int TurnTRR;//RR turn around time
     private int waitT;//FCFS wait time
     private int waitC;//RR wait time
     private int finishT;//time task finished
     private int qLim;//quanta limit(reduces to 2)
     private int priority;//amount of task left to execute

//setters and getters grouped in like pairs
     public void set_id(String a)
     {
         id = a;
     }
     public String get_id()
     {
         return id;
     }

     public void set_execSize(int a)
     {
         execSize = a;
     }
     public int get_execSize()
     {
         return execSize;
     }

     public void set_arrive(int a)
     {
         arrive = a;
     }
     public int get_arrive()
     {
         return arrive;
     }

     public void set_turnT()
     {
         TurnT = finishT-arrive;
     }
     public int get_turnT()
     {
         return TurnT;
     }

     public void set_turnTRR()
     {
         TurnTRR = execSize + waitC;
     }
     public int get_turnTRR()
     {
         return TurnTRR;
     }

     public void set_waitT()
     {
         waitT = TurnT - execSize;
     }
     public int get_waitT()
     {
         return waitT;
     }

     public void set_execCount(int a)
     {
         execCount = a;
     }
     public int get_execCount()
     {
         return execCount;
     }

     public void set_finishT(int a)
     {
         finishT = a;
     }
     public int get_finishT()
     {
         return finishT;
     }

     public void set_waitC(int a)
     {
         waitC = a;
     }
     public int get_waitC()
     {
         return waitC;
     }

     public void set_idInt(int a)
     {
         idInt = a;
     }
     public int get_idInt()
     {
         return idInt;
     }

     public void set_qLim(int a)
     {
         qLim = a;
     }
     public void sub_qLim()
     {
         --qLim;
     }
     public int get_qLim()
     {
         return qLim;
     }

     public void less_priority(int a)
     {
         priority = priority + a;
     }
     public int get_priority()
     {
         return priority;
     }


}
