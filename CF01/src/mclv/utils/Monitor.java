/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mclv.utils;
//import mclv.*;
import java.util.*;
import mclv.Hardware;
//import suanshu.*;


/**
 *
 * @author god
 */
public class Monitor {/*
public final int freq = 1; //Default frequency value
public Vector hardware;
    public Monitor(){ //Should be initialized from hardware class
        hardware = new Vector(0); //0 = drive, 1 = arm, 2 = sensors. Hierarchy: Type, element + Freq val, Data Vector 
        for(int i = 0; i<Hardware.hardwareReport().size(); i++){
            hardware.addElement(new Vector(0));
            for(int c = 0; c<((Vector) Hardware.hardwareReport().elementAt(i)).size(); c++){
                ((Vector) hardware.elementAt(i)).addElement(new Vector(0));
                 if(c == Hardware.hardwareReport().size() - 1 && ((Vector) Hardware.hardwareReport().elementAt(i)).elementAt(c) != null && i != 2){
                    hardware.addElement(((Vector) Hardware.hardwareReport().elementAt(i)).elementAt(c));
                }
                 else if(c == Hardware.hardwareReport().size() - 1 && i !=2){ //Null case
                    hardware.addElement(new Integer(freq)); //assign default value
                 }
                for(int j = 0; j<((Integer) ((Vector) Hardware.hardwareReport().elementAt(i)).elementAt(c)).intValue(); j++){
                ((Vector) ((Vector) hardware.elementAt(i)).elementAt(c)).addElement(new Vector(0)); //This is where data is actually stored

                }
            }
        }
        
    }*/
    public void update(){
        //add analysis here
    }
}
