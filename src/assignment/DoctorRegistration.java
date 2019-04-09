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
public class DoctorRegistration implements Runnable{

    BufferedReader br;
    Hospital hospital;
    List<String[]> list;
    
    public DoctorRegistration(String path, Hospital h) {
        try {
            this.br = new BufferedReader(new FileReader(path));
        } catch (FileNotFoundException ex) {
            
        }
        this.hospital = h;
        this.list = new ArrayList<>();
    }

    @Override
    public void run() {
        System.out.println("100" + Thread.currentThread().getName());
        String line;
        String[] str;
        try {
            while ((line = br.readLine()) != null) {
                if((Integer.parseInt((str = line.split(","))[1])) == hospital.getCurrentTime()) {
                    list.add(str);
                }
            }
            hospital.doctorListTrigger(list);
            br.close();
        }
        catch(IOException e){}
    }
    
}
