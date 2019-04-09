/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment;

import java.io.IOException;
import java.util.*;
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

    boolean registration = false;

    public Hospital() {
        this.doctors = new ArrayList<>();
        this.commonList = new ArrayList<>();
        this.time = new Timestone(this);
    }

    //**********************************************************************************************
    //hospital related method 
    public void addPatient(Doctor d, Patient p) {
        d.getWaitingList().add(p);
    }

    public void addToCommonList(Patient p) {
        this.commonList.add(p);
    }

    public void addToWaitingList() {
        try {
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
                                System.out.println(doctors.get(i - 1).getWaitingList().get(doctors.get(i - 1).getWaitingList().size() - 1).getID()
                                        + " added to " + doctors.get(i - 1).getID());
                                //else assign to next doctor available
                            } else if (doctors.get(i).getWaitingList().size() < 3
                                    && ((doctors.get(i).getWaitingList().size() + doctors.get(i).patientsDone) % 8 != 0 || doctors.get(i).patientsDone == 0)) {
                                addPatient(doctors.get(i), commonList.remove(0));
                                System.out.println(doctors.get(i).getWaitingList().get(doctors.get(i).getWaitingList().size() - 1).getID()
                                        + " added to " + doctors.get(i).getID());
                            }
                            //if first doctors waiting list is more than next doctor
                        } else if (doctors.get(i).getWaitingList().size() < doctors.get(i - 1).getWaitingList().size()) {
                            //assign to next doctor
                            if (doctors.get(i).getWaitingList().size() < 3
                                    && ((doctors.get(i).getWaitingList().size() + doctors.get(i).patientsDone) % 8 != 0 || doctors.get(i).patientsDone == 0)) {
                                addPatient(doctors.get(i), commonList.remove(0));
                                System.out.println(doctors.get(i).getWaitingList().get(doctors.get(i).getWaitingList().size() - 1).getID()
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
        } finally {
            lock.unlock();
        }
    }

    //***************************************************************************************
    //System method
    public void updateStatus() {
        lock.lock();
        for (Doctor d : doctors) {
            System.out.println(d.id + " waiting list : " + d.getWaitingList());
        }
        System.out.println("Hospital common waiting list : " + commonList);
        for (Doctor d : doctors) {
            System.out.println(d.id + " patients done : " + d.patientsDone);
        }
        lock.unlock();
    }

    public boolean anyAwaitingPatient() {
        lock.lock();
        try {
            for (int i = 0; i < doctors.size(); i++) {
                if (!doctors.get(i).getWaitingList().isEmpty()
                        || doctors.get(i).currentPatient != null || !commonList.isEmpty()) {
                    return true;
                }
            }
            return false;
        } finally {
            lock.unlock();
        }
    }

    public void killRegistration() {
        this.registration = false;
    }

    //*****************************************************************************************
    //Get method
    public int getCurrentTime() {
        return this.time.getCurrentTime();
    }

    public List<Doctor> getDoctorList() {
        return this.doctors;
    }

    public List<Patient> getCommonList() {
        return this.commonList;
    }

    //******************************************************************************************
    //Registration method
    public synchronized void patientListTrigger(List<String[]> list) {
        lock.lock();
        System.out.println("11");
        for (int i = 0; i < list.size(); i++) {
            System.out.println("12");
            this.addToCommonList(new Patient(list.get(i)[0],
                    Integer.parseInt(list.get(i)[2]), this));
            System.out.println(this.getCommonList().get(this.getCommonList().size() - 1).getID()
                    + " is added to common list");
            System.out.println("13");
        }
        lock.unlock();
    }

    public synchronized void doctorListTrigger(List<String[]> list) {
        lock.lock();
        for (int i = 0; i < list.size(); i++) {
            this.doctors.add(new Doctor(list.get(i)[0], this));
        }
        for (int i = 0; i < getDoctorList().size(); i++) {
            doctorsThread.add(new Thread(getDoctorList().get(i)));
        }
        lock.unlock();
    }
    //*******************************************************************************************

}
