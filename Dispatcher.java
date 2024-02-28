/*Written by Benjamin Kellahan
 * For COMP2240, Assessment 1
 * Student No#: c3311635
 * Program dispatcher class, manipulates CPU activity */
import java.util.ArrayList; // import the ArrayList class
import java.util.ListIterator;


 public class Dispatcher{
     private int workT;
     ArrayList<String> switchLog = new ArrayList<String>(); // Create an ArrayList object

     public void set_workT(int a)
     {
         workT = a;
     }
     public int get_workT()
     {
         return workT;
     }

     public void addLog(int t, Task a)
     {
         switchLog.add("T"+ t + ":" + a.get_id());
     }
     public void cleanLog()
     {
         switchLog.clear();
     }

     public void printLog()
     {
         ListIterator<String> it = switchLog.listIterator();

             while (it.hasNext())
             {
                 String s = it.next();
                 System.out.println(s);
             }
     }

     
 }
