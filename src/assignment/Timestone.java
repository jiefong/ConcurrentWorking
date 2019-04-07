/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment;

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
        this.hospital.timeLoop();
    }
    
}

//    int currentTime = 0;
//    boolean timeStop = false;
//    
//    public Timestone(){
//        
//    }
//    
//    public int getCurrentTime(){
//        return currentTime;
//    }
//    
//    public void setCurrentTime(int t){
//        this.currentTime = t;
//    }
//    
//    public void stop(){
//        this.timeStop = true;
//    }