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
    
    public int getTimeConsult(){
        return this.timeConsult;
    }
    
    public int getTimeLeave(){
        return this.timeLeave;
    }
    
    public void setTimeLeave(int t){
        this.timeLeave = t;
    }
}