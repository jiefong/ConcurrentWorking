/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author HP
 */
public class Hospital {

    private List<Doctor> doctors;
    private List<Patient> commonList;
    List<Thread> doctorsThread = new ArrayList<>();
    List<Thread> patientsThread = new ArrayList<>();
    Timestone time;
    
    private final Lock lock = new ReentrantLock();
    private final Condition allDone = lock.newCondition();
    private final Condition startJob = lock.newCondition();
    
    public Hospital(){
        this.doctors = new ArrayList<>();
        this.commonList = new ArrayList<>();
        this.time = new Timestone(this);
    }
    
    public void addPatient(Doctor d, Patient p){
        d.getWaitingList().add(p);
        p.setMyDoctor(d);
    }
    
    public void addToCommonList(Patient p){
        this.commonList.add(p);
    }
    
    public void addToWaitingList(){
        lock.lock();
        Collections.sort(doctors);
        if (!commonList.isEmpty()) {
            //make sure the patients done is not differ by 3 before new assignment of patient 
            if ((doctors.get(doctors.size() - 1).patientsDone - doctors.get(0).patientsDone) <= 2) {
                //start compare doctors waiting list 
                for (int i = 1; i < doctors.size(); i++) {
                    //if first doctors waiting list is less than or equal next doctor
                    if (doctors.get(i).getWaitingList().size() >= doctors.get(i - 1).getWaitingList().size()) {
                        //and first doctor is not rest with more than 3 patient awaiting
                        if (doctors.get(i - 1).getWaitingList().size() < 3 
                                && ((doctors.get(i - 1).getWaitingList().size() + doctors.get(i - 1).patientsDone) % 8 != 0 || doctors.get(i - 1).patientsDone == 0)) {
                            //assign patient to it
                            addPatient(doctors.get(i - 1), commonList.remove(0));
                            System.out.println (doctors.get(i - 1).getWaitingList().get(doctors.get(i - 1).getWaitingList().size() - 1).getID() 
                                    + " added to " + doctors.get(i - 1).getID());
                            //else assign to next doctor available
                        } else if (doctors.get(i).getWaitingList().size() < 3 
                                && ((doctors.get(i).getWaitingList().size() + doctors.get(i).patientsDone) % 8 != 0 || doctors.get(i).patientsDone == 0)) {
                            addPatient(doctors.get(i), commonList.remove(0));
                            System.out.println (doctors.get(i).getWaitingList().get(doctors.get(i).getWaitingList().size() - 1).getID() 
                                    + " added to " + doctors.get(i).getID());
                        }
                        //if first doctors waiting list is more than next doctor
                    } else if (doctors.get(i).getWaitingList().size() < doctors.get(i - 1).getWaitingList().size()) {
                        //assign to next doctor
                        if (doctors.get(i).getWaitingList().size() < 3 
                                && ((doctors.get(i).getWaitingList().size() + doctors.get(i).patientsDone) % 8 != 0  || doctors.get(i).patientsDone == 0)) {
                            addPatient(doctors.get(i), commonList.remove(0));
                            System.out.println (doctors.get(i).getWaitingList().get(doctors.get(i).getWaitingList().size() - 1).getID() 
                                    + " added to " + doctors.get(i).getID());
                        }
                    }
                }
                System.out.println("No patient is assigned doctor. Waiting list is full");
            } else {
                System.out.println("No patient is assigned doctor. Patients Done is not balanced");
            }
        } else {
            System.out.println("Commonlist is empty");
        }
        lock.unlock();
    }
    
    public void updateStatus() {
        for (Doctor d : doctors) {
            System.out.println(d.id + " waiting list : " + d.getWaitingList());
        }
        System.out.println("Hospital common waiting list : " + commonList);
        for (Doctor d : doctors) {
            System.out.println(d.id + " patients done : " + d.patientsDone);
        }
    }
    
    public boolean anyAwaitingPatient(){
        lock.lock();
        try{
        for (int i = 0; i < doctors.size(); i++) {
            if (!doctors.get(i).getWaitingList().isEmpty() || 
                    doctors.get(i).currentPatient != null || !commonList.isEmpty()) {
                return true;
            }
        }
        return false;
        }finally{
            lock.unlock();
        }
    }
    
//    public void timeIncrement(){
//        lock.lock();
//            while(counter < totalCounter){
//                startJob.signal();
//                counter++;
//            }
//            counter = 0;
//            totalCounter = this.doctors.size() + 1;
//            while(counter < totalCounter){
//                allDone.await();
//                counter++;
//            }
//        this.time.setCurrentTime(this.getCurrentTime() + 1);
//        lock.unlock();
//    }
    
    public void doctorRegistration(Registration r){
        String line;
        String[] str;
        try{
            while((line = r.br.readLine()) != null){
                while((Integer.parseInt((str = line.split(","))[1])) != this.getCurrentTime()){
                    if(!r.list.isEmpty()){
                        patientListTrigger(r.list);
                    }
                    r.list.clear();
                }
                r.list.add(str);
            }           
            doctorListTrigger(r.list);
            r.list.clear();
            r.br.close();
        }catch(IOException e ){
        }
    }
    
    public void patientRegistration(Registration r){
        String line;
        String[] str;
        try{
            lock.lock();
            while((line = r.br.readLine()) != null){
                while((Integer.parseInt((str = line.split(","))[1])) != this.getCurrentTime()){
                    if(!r.list.isEmpty()){
                        patientListTrigger(r.list);
                    }
//                    allDone.signal();
                    lock.unlock();
                    lock.lock();
//                    startJob.await();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                    }
                    r.list.clear();
                }
                r.list.add(str);
            }
            patientListTrigger(r.list);
            r.list.clear();
            lock.unlock();
            r.br.close();
        }catch(IOException e ){
        }
    }
        
    //await all done
    //signal startjob
    public void timeLoop(){
        while(this.getCurrentTime() <=240){
            try {
                //startJob.signalAll();
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(Hospital.class.getName()).log(Level.SEVERE, null, ex);
            }
            this.time.setCurrentTime(this.getCurrentTime() + 1);
            updateStatus();
        }
        while(this.anyAwaitingPatient()){
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(Hospital.class.getName()).log(Level.SEVERE, null, ex);
            }
            this.addToWaitingList();
            this.time.setCurrentTime(this.getCurrentTime() + 1);
            updateStatus();
        }
        this.time.setTimeStop();
    }
    
    public void doctorJob(Doctor d){
//    //each doctor run on five situation
//    //1. no current patient, no patient await (ignore, keep wait)
//    //2. no current patient, has patient await (add current patient from waiting list)
//    //3. has current patient, no patient await (check patient leaving time & kick if patient done)
//    //4. has current patient, has patient await (check patient leaving time & kick if patient done, add current patient from waiting list)
//    //5. has current patient(leaving time not yet reach, keep wait) 
        while(!this.time.getTimeStop()){
            lock.lock();
            if(d.currentPatient == null){
                if(!d.getWaitingList().isEmpty()){
                    d.currentPatient = d.getWaitingList().remove(0);
                    d.currentPatient.setMyDoctor(d);
                    d.currentPatient.setTimeLeave(d.currentPatient.getTimeConsult() + this.getCurrentTime());
//                    allDone.signal();
                }
                else{
//                    allDone.signal();
                }
            }
            else{
                if(d.currentPatient.getTimeLeave() == this.getCurrentTime()){
                    d.currentPatient = null;
                    d.patientsDone ++;
                    if(!d.getWaitingList().isEmpty()){
                        if(d.patientsDone % 8 == 0 && this.getCurrentTime() != 0){
                            d.restTimeEnd = this.getCurrentTime() + 15;
                            while(this.getCurrentTime()!= d.restTimeEnd){
                                try {
//                                allDone.signal();
//                                startJob.await();
                                    Thread.sleep(100);
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(Hospital.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                            d.restTimeEnd = 0;
                        }
                        d.currentPatient = d.getWaitingList().remove(0);
                        d.currentPatient.setMyDoctor(d);
                        d.currentPatient.setTimeLeave(d.currentPatient.getTimeConsult() + this.getCurrentTime());
//                        allDone.signal();
                    }
                    else{
//                        allDone.signal();
                    }
                }
                else{
//                    allDone.signal();
                }
            }
            lock.unlock();
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(Hospital.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
    public void patientUpdate(Patient p){
        //while(p.getMyDoctor() == null){
            this.addToWaitingList();
        //}
    }
    
    //**************************************************************************
    //Get method
    public int getCurrentTime(){
        return this.time.getCurrentTime();
    }

    public List<Doctor> getDoctorList(){
        return this.doctors;
    }
    
    public List<Patient> getCommonList(){
        return this.commonList;
    }

    //**************************************************************************
    //Registration method
    public synchronized void patientListTrigger(List<String[]> list) {
        for(int i = 0; i < list.size(); i++){
            this.patientsThread.add(new Thread(new Patient(list.get(i)[0],
                    Integer.parseInt(list.get(i)[2]),this)));
            this.patientsThread.get(this.patientsThread.size() - 1).start();
        }
    }

    public synchronized void doctorListTrigger(List<String[]> list) {
        for(int i = 0; i < list.size(); i++){
            this.doctors.add(new Doctor(list.get(i)[0],this));
        }
        for(int i = 0; i < getDoctorList().size(); i++){
            doctorsThread.add(new Thread(getDoctorList().get(i)));
        }
    }
}

//    List<Doctor> doctors = new ArrayList<>();
//    List<Patient> patients = new ArrayList<>();
//    Queue<Patient> commonList = new LinkedList<>();
//
//    public Hospital() {
//
//    }
//
//    //add a patient to hospital common list & quickly check if any doctor waiting list is available
//    public synchronized void addtoCommonList(Patient p) {
//        commonList.add(p);
//        System.out.println(checkRulesToAddPatient());
//        updateStatus();
//    }
//
//    //update the status of all waiting list and doctors details when call
//    public synchronized void updateStatus() {
//        for (Doctor d : doctors) {
//            System.out.println(d.id + " waiting list : " + d.waitingList);
//        }
//        System.out.println("Hospital common waiting list : " + commonList);
//        for (Doctor d : doctors) {
//            System.out.println(d.id + " patients done : " + d.patientsDone + " & rest status : " + d.isRest);
//        }
//    }
//
//    //check rules add the head of common waiting list patient to an available doctor waiting list
//    public synchronized String checkRulesToAddPatient() {
//        //sort doctors by patients done
//        Collections.sort(doctors);
//        
//        //common list must not empty to start the assignment of patient
//        if (!commonList.isEmpty()) {
//            //make sure the patients done is not differ by 3 before new assignment of patient 
//            if ((doctors.get(doctors.size() - 1).patientsDone - doctors.get(0).patientsDone) <= 2) {
//                //start compare doctors waiting list 
//                for (int i = 1; i < doctors.size(); i++) {
//                    //if first doctors waiting list is less than or equal next doctor
//                    if (doctors.get(i).waitingList.size() >= doctors.get(i - 1).waitingList.size()) {
//                        //and first doctor is not rest with more than 3 patient awaiting
//                        if (!doctors.get(i - 1).isRest && doctors.get(i - 1).waitingList.size() < 3) {
//                            //assign patient to it
//                            addPatient(doctors.get(i - 1), commonList.remove());
//                            return (doctors.get(i - 1).waitingList.get(doctors.get(i - 1).waitingList.size() - 1).getID() + " added to " + doctors.get(i - 1).getID());
//                            //else assign to next doctor available
//                        } else if (!doctors.get(i).isRest && doctors.get(i).waitingList.size() < 3) {
//                            addPatient(doctors.get(i), commonList.remove());
//                            return (doctors.get(i).waitingList.get(doctors.get(i).waitingList.size() - 1).getID() + " added to " + doctors.get(i).getID());
//                        }
//                        //if first doctors waiting list is more than next doctor
//                    } else if (doctors.get(i).waitingList.size() < doctors.get(i - 1).waitingList.size()) {
//                        //assign to next doctor
//                        if (!doctors.get(i).isRest && doctors.get(i).waitingList.size() < 3) {
//                            addPatient(doctors.get(i), commonList.remove());
//                            return (doctors.get(i).waitingList.get(doctors.get(i).waitingList.size() - 1).getID() + " added to " + doctors.get(i).getID());
//                        }
//                    }
//                }
//                return "No patient is assigned doctor. Waiting list is full";
//            } else {
//                return "No patient is assigned doctor. Patients Done is not balanced";
//            }
//        } else {
//            return "Commonlist is empty";
//        }
//    }
//
//    //add patient from common list to doctor waiting list
//    public synchronized void addPatient(Doctor d, Patient p) {
//        d.waitingList.add(p);
//    }
//
//    //check if there is any patients not complete consultation or waiting
//    public synchronized boolean anyPatientAwaiting() {
//        for (int i = 0; i < doctors.size(); i++) {
//            if (!doctors.get(i).waitingList.isEmpty() || doctors.get(i).currentPatient != null || !commonList.isEmpty()) {
//                return true;
//            }
//        }
//        return false;
//    }