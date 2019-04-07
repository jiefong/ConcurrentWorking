/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Important Note: Time of One Minute(1min) in real world is simulated by One Hundred Millisecond (100ms) in the program
 * @author HP
 */
public class Assignment {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException, InterruptedException {
        
        Hospital hospital = new Hospital();
        ExecutorService executor = Executors.newCachedThreadPool();
        Thread t = new Thread(hospital.time);
        
        Registration r1 = new Registration("src/doctor.txt", hospital, 1);
        Thread t1 = new Thread(r1);
        t1.start();
        t1.join();

        for (Thread doctor : hospital.doctorsThread) {
            doctor.start();
        }
        
        Registration r2 = new Registration("src/patients1.txt", hospital, 2);
        Thread t2 = new Thread(r2);
        t2.start();       
        t.start();
        
        while(t.isAlive()){
            Thread.sleep(100);
            if(!t2.isAlive()){
                t2.join();
                System.out.println("No more new patient");
            }
        }
        
        t.join();
        System.out.println("Hospital shutdown");
    }
    
}

//Declare variable
//        Hospital hospital = new Hospital();
//        List<Thread> doctors = new ArrayList<>();
//        List<Thread> patients = new ArrayList<>();
//        Timestone myTime = new Timestone();
//        Lock lock = new ReentrantLock();
//        
//        //declare and start bufferedReader for doctor.txt
//        MyBufferedReader r1 = new MyBufferedReader("src/doctor.txt", myTime);
//        Thread t1 = new Thread(r1);
//        t1.start();
//        //end of bufferedReader thread
//        t1.join();
//        
//        //Add doctors to hospital doctor list and make each doctor as thread
//        for(int i = 0; i < r1.forDoctor.size(); i++){
//            hospital.doctors.add(new Doctor((r1.forDoctor.get(i))[0], myTime));
//            doctors.add(new Thread(hospital.doctors.get(i)));
//        }
//        //start doctors threads
//        for (Thread doctor : doctors) {
//            doctor.start();
//        }
//        
//        //declare bufferedReader for patient.txt
//        MyBufferedReader r2 = new MyBufferedReader("src/patients1.txt", myTime);
//        Thread t2 = new Thread(r2);
//        
//        //Timestone
//        //Time controller
//        for(;myTime.currentTime <= 240;myTime.currentTime++){
//            //start thread to read from patient.txt
//            if(myTime.currentTime == 0 && !t2.isAlive()){
//                t2.start();
//            }
//            //if bufferedReader thread done reading from txt
//            //keep update the waiting list from common list
//            else if(!t2.isAlive()){
//                t2.join();
//                System.out.println(hospital.checkRulesToAddPatient());
//                hospital.updateStatus();
//            }
//            //for every minute, check if bufferedReader read any valid data
//            if(!r2.arr.isEmpty()){
//                for(int i = 0; i < r2.arr.size(); i++){
//                    hospital.patients.add(new Patient((r2.arr.get(i))[0],Integer.parseInt((r2.arr.get(i))[1]), Integer.parseInt((r2.arr.get(i))[2]),hospital));
//                    patients.add(new Thread(hospital.patients.get(hospital.patients.size()-1))); 
//                    patients.get(patients.size()-1).start();
//                }
//            }
//            
//            //give space for other thread to run within the minute
//            Thread.sleep(100);
//         
//            System.out.println("current time "+myTime.getCurrentTime());
//            
//        }
//        //after the hospital regular opening hour end
//        //hospital will continue to treat if any patient is still waiting but not accepting new patient
//        while(hospital.anyPatientAwaiting()){
//            System.out.println(hospital.checkRulesToAddPatient());
//            hospital.updateStatus();
//            Thread.sleep(50);
//            hospital.updateStatus();
//            Thread.sleep(50);
//            System.out.println("current time "+myTime.getCurrentTime());
//            myTime.currentTime++;
//        }
//        myTime.stop();