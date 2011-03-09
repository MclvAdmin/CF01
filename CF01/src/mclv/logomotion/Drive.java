/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package mclv.logomotion;
import java.util.*;
import mclv.utils.*;
import mclv.*;
/**
 *
 * @author god
 */
public class Drive { //Consider static methods
    private static Vector driveSet;
    private static Vector driveOut;
    public static Vector driveVals;
    public static int init = 0;
    public Drive(Vector driveSetConfig){
        driveSet = driveSetConfig;    
    }
    
    public static void init(){
        driveVals = new Vector(0);
        init++;
    }
  
    public static Vector request(Vector positionReq, Vector controllerDrive){ //Makes 'driveAssign' for hardware object ..... positionReq includes data as to automation of drive from sensor class
        driveSet = new Vector(0);
        driveOut = new Vector(0);
        System.out.println("Drive.request: first element from driver input");
        System.out.println((((Boolean) controllerDrive.elementAt(0)).booleanValue()));
        if(((((Integer) positionReq.lastElement()).intValue())==0) && ((((Boolean) controllerDrive.elementAt(0)).booleanValue()) != true)){
            /*THAT looks fucking complicated but it's the first value of the last element in the provided request from the 
             * position object which tells drive whether it's taking driver input.
             * It also checks to see if button/sequence has been pressed that would disable automatic movement
            */
            // controllerDrive(0) is false when drivers r drivin... (sequence not hit) controller (>0) refers to joystick vals that r changed

        }
        /* else if..... for(int i = 0; i<positionReq.size() -2; i++){
                for(int c = 0; c<((Vector) positionReq.elementAt(i)).size(); c++){9

                }
            }*/
        for(int i = 0; i<positionReq.size() -1; i++){
            System.out.println("Drive.request: posReq index value:");
            System.out.println(i);
                    
            for(int c = 0; driveOut.size()< positionReq.size() -1; c++){
                driveOut.addElement(new Vector(0));
                //((Vector) driveOut.elementAt(c)).addElement(new Double(0));
            }
            for(int j = 0; j<((Integer) positionReq.elementAt(i)).intValue(); j++){
                if(i == 0){ //EXPAND TO MORE SYSTEMS!
                ((Vector)  driveOut.elementAt(i)).addElement(controllerDrive.elementAt(1));
                }
                else{
                //((Double) ((Vector)  driveOut.elementAt(i)).elementAt(j)).equals(((Double) controllerDrive.elementAt(2)));
                ((Vector)  driveOut.elementAt(i)).addElement(controllerDrive.elementAt(2));
                }
            }
        }
        driveOut.addElement(new Integer(ConstantManager.driveType));
        System.out.println("Drive.request: REPORT:");
        System.out.println("Drive.request: Assignment size:");
        System.out.println(driveOut.size() -1);
        return driveOut; //Send to hardware
    }
    
    public static void drive(){
        Vector driveAssign = new Vector(0);
        if(init == 0){
            driveVals = new Vector(0);
        }
        
        for(int systemIndex = 0; systemIndex< ((Vector) Hardware.hardware.elementAt(ConstantManager.driveType - ConstantManager.minTypes())).size(); systemIndex++){
            driveAssign.addElement(new Vector(0));
            for(int memberIndex = 0; memberIndex < ((Vector) ((Vector) Hardware.hardware.elementAt(ConstantManager.driveType - ConstantManager.minTypes())).elementAt(systemIndex)).size(); memberIndex++){
                if(!((Boolean) ((Vector) driverInput.inputVals.elementAt(ConstantManager.driveType - ConstantManager.minTypes())).elementAt(0)).booleanValue()){
                    ((Vector) driveAssign.elementAt(systemIndex)).addElement(((Vector) driverInput.inputVals.elementAt(ConstantManager.driveType - ConstantManager.minTypes())).elementAt(systemIndex + 1));
                }
                else{
                    //Needs choices here; goto class that determines the course of action if drivers r not in control!
                }
            }
        }
        driveAssign.addElement(new Integer(ConstantManager.driveType));
        Hardware.assign(driveAssign);
    }
    public void reconfig(Vector controllerConfig, Vector driveSetConfig){
        
    }
}