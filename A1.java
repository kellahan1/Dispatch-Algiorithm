/*Written by Benjamin Kellahan
 * For COMP2240, Assessment 1
 * Student No#: c3311635
 * Program interface class for CPU scheduling*/


 import java.util.Scanner;  // Import the Scanner class
 import java.io.File;  // Import the File class
 import java.io.FileNotFoundException;  // Import this class to handle errors

 public class A1{
     static String fileName;//file name from user

     public static void main(String[] args)
     {         
         fileName = args;  // Read user input

         FCFS a = new FCFS();
         RR b = new RR();
         NRR c = new NRR();
         FB d = new FB();

         a.run(fileName);
         b.run(fileName);
         c.run(fileName);
         d.run(fileName);

         System.out.println("Summary");
         System.out.println("Algorithm       Average Turnaround Time   Average Waiting Time");
         System.out.println("FCFS            " + a.get_FCFS_Avg_Trn()+ "                      " + a.get_FCFS_Avg_Wait());
         System.out.println("RR              " + b.get_RR_Avg_Trn()  + "                      " + b.get_RR_Avg_Wait());
         System.out.println("NRR             "  + c.get_NRR_Avg_Trn()+ "                      " + c.get_NRR_Avg_Wait());
         System.out.println("FB              " + d.get_FB_Avg_Trn()  + "                      " + d.get_FB_Avg_Wait());
     }

     //file reader method










 }
