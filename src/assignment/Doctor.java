/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment;

import java.util.*;

/**
 *
 * @author HP
 */
public class Doctor extends Person implements Comparable{
    
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
        this.hospital.doctorJob(this);
    }
     
    @Override
    public int compareTo(Object o) {
        int compare = ((Doctor)o).patientsDone;
        return this.patientsDone - compare;
    }
}

//    int patientsDone = 0;
//    boolean isRest = false;
//    boolean wait = false;
//    boolean jobDone = false;
//    Patient currentPatient = null;
//    Timestone myTime = new Timestone();
//    List<Patient> waitingList = new ArrayList<>(3);
//    
//    public Doctor(String id, Timestone t){
//        super(id);
//        myTime = t;
//    }
//
//    //each doctor run on five situation
//    //1. no current patient, no patient await (ignore, keep wait)
//    //2. no current patient, has patient await (add current patient from waiting list)
//    //3. has current patient, no patient await (check patient leaving time & kick if patient done)
//    //4. has current patient, has patient await (check patient leaving time & kick if patient done, add current patient from waiting list)
//    //5. has current patient(leaving time not yet reach, keep wait) 
//    @Override
//    public void run() {
//        try{
//            //Thread.sleep(1000);
//            while(!myTime.timeStop){
//                //jobDone = false;
//                //check if no current patient
//                //System.out.println("time : " + myTime.currentTime + " " + this.getID() + " call 1");
//                if(this.currentPatient == null){
//                    //situatio 2
//                    if(!this.waitingList.isEmpty()){
//                        this.currentPatient = this.waitingList.remove(0);
//                        this.currentPatient.timeLeave = this.myTime.getCurrentTime() + this.currentPatient.timeConsult;
//                    }
//                    //situation 1
//                    else{  
//                    }
//                }
//                //check if has current patient & kick if patient done 
//                else if(this.currentPatient != null && this.currentPatient.timeLeave == this.myTime.getCurrentTime()){
//                    //situation 3
//                    if(this.waitingList.isEmpty()){
//                        this.currentPatient = null;
//                        this.patientsDone++;
//                    }
//                    //situation 4
//                    else{
//                        this.currentPatient = null;
//                        this.patientsDone++;
//                        //if doctor not reach rest time, add new patient
//                        if(this.patientsDone % 8 != 0){
//                            this.currentPatient = this.waitingList.remove(0);
//                            this.currentPatient.timeLeave = this.myTime.getCurrentTime() + this.currentPatient.timeConsult;
//                        }
//                        //if doctor reach rest time, doctor rest for 15 minutes
//                        else{
//                            this.isRest = true;
//                            Thread.sleep(1500);
//                            this.isRest = false;
//                        }
//                    }
//                }
//                else{
//                    //situation 5
//                }
//                if(!myTime.timeStop){
//                    //jobDone = true;
//                    Thread.sleep(100);
//                }
//            }
//        }
//        catch(InterruptedException e){
//            
//        }
//    }
