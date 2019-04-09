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

/**
 *
 * @author HP
 */
public class PatientRegistration implements Runnable {
    BufferedReader br;
    Hospital hospital;
    List<String[]> list;
    
    public PatientRegistration(String path, Hospital h) {
        try {
            this.br = new BufferedReader(new FileReader(path));
        } catch (FileNotFoundException ex) {
            
        }
        this.hospital = h;
        this.list = new ArrayList<>();
    }

    @Override
    public void run() {
        System.out.println("200" + Thread.currentThread().getName());
        String line;
        String[] str;
        try {
            while ((line = br.readLine()) != null) {
                System.out.println("1");
                while((Integer.parseInt((str = line.split(","))[1])) != hospital.getCurrentTime()) {
                    System.out.println("2");
                    if(!list.isEmpty()){
                        System.out.println("3");
                        this.hospital.patientListTrigger(list);
                        System.out.println("4");
                    }
                    this.hospital.time.JobDone();
                    list.clear();
                }
                System.out.println("5");
                list.add(str);
            }
            System.out.println("6");
            this.hospital.patientListTrigger(list);
            this.hospital.time.JobDone();
            list.clear();
            br.close();
            this.hospital.killRegistration();
        }
        catch(IOException e ){}
        catch(InterruptedException ex){}
    }
    
}
