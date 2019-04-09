/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author HP
 */
public class Doctor extends Person implements Comparable , Runnable{
    
    int patientsDone;
    int restTimeEnd;
    Patient currentPatient;
    private List<Patient> waitingList;

    public Doctor(String str, Hospital h){
        super(str, h);
        this.patientsDone = 0;
        this.currentPatient = null;
        this.restTimeEnd = 0;
        this.waitingList = new ArrayList<>();
    }
    
    public List<Patient> getWaitingList(){
        return this.waitingList;
    }
    
    @Override
    public void run() {
        System.out.println(this.getID() + "");
        System.out.println(Thread.currentThread().getName());
        try {
            Thread.sleep(200);
        } catch (InterruptedException ex) {
            Logger.getLogger(Doctor.class.getName()).log(Level.SEVERE, null, ex);
        }
//    //each doctor run on five situation
//    //1. no current patient, no patient await (ignore, keep wait)
//    //2. no current patient, has patient await (add current patient from waiting list)
//    //3. has current patient, no patient await (check patient leaving time & kick if patient done)
//    //4. has current patient, has patient await (check patient leaving time & kick if patient done, add current patient from waiting list)
//    //5. has current patient(leaving time not yet reach, keep wait) 
        try{
        while(!this.hospital.time.getTimeStop()){
            if(this.currentPatient == null){
                if(!this.getWaitingList().isEmpty()){
                    this.currentPatient = this.getWaitingList().remove(0);
                    this.currentPatient.setTimeLeave(this.hospital.getCurrentTime() + this.currentPatient.getTimeConsult());
                    this.hospital.time.JobDone();
                }
                else{
                    this.hospital.time.JobDone();
                }
            }
            else{
                if(this.currentPatient.getTimeLeave() == this.hospital.getCurrentTime()){
                    if(!this.getWaitingList().isEmpty()){
                        this.currentPatient = null;
                        this.patientsDone++;
                        if(this.patientsDone%8 == 0 && this.hospital.getCurrentTime() != 0){
                            this.restTimeEnd = this.hospital.getCurrentTime() + 15;
                            while(this.restTimeEnd != this.hospital.getCurrentTime()){
                                this.hospital.time.JobDone();
                            }
                        }
                        this.currentPatient = this.getWaitingList().remove(0);
                        this.currentPatient.setTimeLeave(this.hospital.getCurrentTime() + this.currentPatient.getTimeConsult());
                        this.hospital.time.JobDone();
                    }
                    else{
                        this.currentPatient = null;
                        this.patientsDone++;
                        if(this.patientsDone%8 == 0 && this.hospital.getCurrentTime() != 0){
                            this.restTimeEnd = this.hospital.getCurrentTime() + 15;
                            while(this.restTimeEnd != this.hospital.getCurrentTime()){
                                this.hospital.time.JobDone();
                            }
                        }
                        this.hospital.time.JobDone();
                    }
                }
                else{
                    this.hospital.time.JobDone();
                }
            }
        }
        }
        catch (InterruptedException ex) {}
    }
     
    @Override
    public int compareTo(Object o) {
        int compare = ((Doctor)o).patientsDone;
        return this.patientsDone - compare;
    }
}
