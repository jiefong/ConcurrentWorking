/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author HP
 */
public class Timestone implements Runnable{

    private int currentTime;
    boolean timeStop = false;
    Hospital hospital;
    Lock lock = new ReentrantLock();
    Condition startJob = lock.newCondition();
    Condition jobDone = lock.newCondition();
    Condition timeIncrement = lock.newCondition();
    Condition doneTimeIncrement = lock.newCondition();
    
    int totalJobDone = 0;
    
    public Timestone(Hospital h){
        this.currentTime = 0;
        this.hospital = h;
    }
    
    public int getCurrentTime(){
        return this.currentTime;
    }
    
    public void setCurrentTime(int i){
        this.currentTime = i;
    }

    public boolean getTimeStop(){
        return this.timeStop;
    }
    
    public void setTimeStop(){
        this.timeStop = true;
    }
    
    public Hospital getHospital(){
        return this.hospital;
    }
    
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName());
        
        while (this.getCurrentTime() <= 240) {
            
            try {
                int counter = 0;
                int threadCount;
        
                if(this.getHospital().registration){threadCount = this.getHospital().doctorsThread.size() + 1;}
                else{threadCount = this.getHospital().doctorsThread.size();}
        
                while(counter < threadCount){
                    jobDone.await();
                    counter++;
                }
                
                hospital.addToWaitingList();
                hospital.updateStatus();
                
            } catch (InterruptedException ex) {
                Logger.getLogger(Timestone.class.getName()).log(Level.SEVERE, null, ex);
            }
            this.setCurrentTime(this.getCurrentTime() + 1);
            startJob.signal();
        }
        while (hospital.anyAwaitingPatient()) {

                int counter = 0;
                int threadCount;
        
                threadCount = this.getHospital().doctorsThread.size();
                
                while(counter < threadCount){
                    try {
                        jobDone.await();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Timestone.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    counter++;
                }
                
                hospital.addToWaitingList();
                hospital.updateStatus();
                
            this.setCurrentTime(this.getCurrentTime() + 1);
            startJob.signal();
        }
        this.setTimeStop();
        this.hospital.killRegistration();
    }
    
    public void JobDone() throws InterruptedException{
        lock.lock();
        jobDone.signal();
        startJob.await();
        lock.unlock();
    }
    
}
