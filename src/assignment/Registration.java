/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author HP
 */
public class Registration implements Runnable{

    BufferedReader br;
    Hospital hospital;
    List<String[]> list;
    int choice;
    //1 for doctor
    //2 for patient
    
    public Registration(String path, Hospital h, int i) {
        try {
            this.br = new BufferedReader(new FileReader(path));
        } catch (FileNotFoundException ex) {
            
        }
        this.hospital = h;
        this.list = new ArrayList<>();
        this.choice = i;
    }

    @Override
    public void run() {
        if(this.choice == 1){
            this.hospital.doctorRegistration(this);
        }
        else{
            this.hospital.patientRegistration(this);
        }
    }
    

}

//    ArrayList<String[]> arr = new ArrayList<>();
//    ArrayList<String[]> forDoctor = new ArrayList<>();
//    BufferedReader br;
//    Timestone myTime = new Timestone();
//    Lock lock = new ReentrantLock();
//    
//    public MyBufferedReader(String path, Timestone t) throws FileNotFoundException{
//        br = new BufferedReader(new FileReader(path));
//        myTime = t;
//    }
//    
//    @Override
//    public void run() {
//        lock.lock();
//        String line = "";
//        String[] str;
//        try {
//            while((line = this.br.readLine()) != null){
//                while(Integer.parseInt((str = line.split(","))[1]) != myTime.currentTime){
//                    try {
//                        //wait for one minute to let the system complete task reading the valid input
//                        Thread.sleep(100);
//                        //clean up the arraylist to add new valid data for the minute
//                        arr.clear();
//                    } catch (InterruptedException ex) {
//                        
//                    }
//                }
//                //add the next input detail to arraylist if the time arrive is same as current time
//                arr.add(str);
//            }
//            //an arraylist for adding doctor to doctor list
//            for(int i = 0; i < arr.size(); i++){
//                forDoctor.add(arr.get(i));
//            }
//            try {
//                Thread.sleep(100);
//            } catch (InterruptedException ex) {
//                Logger.getLogger(MyBufferedReader.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            //clear up the arraylist after done reading text file
//            arr.clear();
//            br.close();
//        } catch (IOException ex) {
//            
//        }
//        finally{
//            lock.unlock();
//        }
//        
//    }