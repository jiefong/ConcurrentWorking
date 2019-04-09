/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment;

/**
 *
 * @author HP
 */
abstract public class Person{
    
    String id;
    Hospital hospital;
    
    public Person(){
        
    }
    
    public Person(String i, Hospital h){
        this.id = i;
        this.hospital = h;
    }
    
    public void setID(String id){
        this.id = id;
    }
    
    public String getID(){
        return this.id;
    }
    
    public Hospital getHospital(){
        return this.hospital;
    }
}
