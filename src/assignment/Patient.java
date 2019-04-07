/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author HP
 */
public class Patient extends Person{

//    private List<Patient> myList;
    private int timeConsult;
    private int timeLeave;
    private Doctor myDoctor;
    
    public Patient(String str, int time2, Hospital h){
        super(str, h);
        this.timeConsult = time2;
        this.timeLeave = 0;
        this.myDoctor = null;
    }
    
//    public void setMyList(List<Patient> list){
//        this.myList = list;
//    }
//    
    public int getTimeConsult(){
        return this.timeConsult;
    }
    
    public int getTimeLeave(){
        return this.timeLeave;
    }
    
    public void setTimeLeave(int t){
        this.timeLeave = t;
    }
    
    public Doctor getMyDoctor(){
        return this.myDoctor;
    }
    
    public void setMyDoctor(Doctor d){
        this.myDoctor = d;
    }
    
    @Override
    public void run() {
        System.out.println(this.getID() + "");
        this.hospital.addToCommonList(this);
        this.hospital.patientUpdate(this);
    }

}

//    int timeArrive;
//    int timeConsult;
//    int timeLeave;
//    List<Patient> myList = new ArrayList<>();
//    Hospital hospital = new Hospital();
//    
//    public Patient(String id, int time1, int time2, Hospital h){
//        super(id);
//        this.timeArrive = time1;
//        this.timeConsult = time2;
//        this.hospital = h;
//    }
//    
//    //add to common list and wait for further assignment to doctor waiting list after it is called
//    @Override
//    public void run() {
//        this.hospital.addtoCommonList(this);
//    }