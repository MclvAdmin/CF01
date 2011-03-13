/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package mclv.logomotion;
import java.util.*;
import mclv.utils.*;
import mclv.*;
import edu.wpi.first.wpilibj.Timer;
/**
 *
 * @author god
 */
public class Drive { //Consider static methods
    private static Vector driveSet;
    private static Vector driveOut;
    public static Vector driveVals;
    public static Vector historicAutoVals;
    public static int init = 0;
    public static boolean autoFinished = false;
    public static double autoStoppedTime;
    public static boolean autoStopped = false;
    public Drive(Vector driveSetConfig){
        driveSet = driveSetConfig;    
    }
    
    public static void init(){
        driveVals = new Vector(0);
        //historicVals = new Vector(0);
        init++;
    }
  
    public static Vector request(Vector positionReq, Vector controllerDrive){ //Makes 'driveAssign' for hardware object ..... positionReq includes data as to automation of drive from sensor class
        driveSet = new Vector(0);
        driveOut = new Vector(0);
        Debug.output("Drive.request: first element from driver input", controllerDrive.elementAt(0), ConstantManager.driveDebug);
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
            Debug.output("Drive.request: posReq index value", new Integer(i), ConstantManager.driveDebug);
                    
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
        Debug.output("Drive.request: Final Vector", driveOut, ConstantManager.driveDebug);
        return driveOut; //Send to hardware
    }
    public static void auto(){
        Vector nonFormAssign = new Vector(0);
        Vector formAssign = new Vector(0);
        if(init == 0){
            historicAutoVals = new Vector(0);
        }
        if(requestBuffer.driveFlag && Arm.autoSequenceFinished){
            nonFormAssign = requestBuffer.driveBufferUse();
            //if(((Double) nonFormAssign.elementAt(0))
            if(((Integer) nonFormAssign.lastElement()).intValue() == ConstantManager.driveType){
                nonFormAssign.removeElementAt(nonFormAssign.size() - 1);
                for(int systemIndex = 0; systemIndex < nonFormAssign.size(); systemIndex++){
                    formAssign.addElement(new Vector(0));
                    while(((Vector) formAssign.elementAt(systemIndex)).size() < ((Vector) ((Vector) Hardware.hardware.elementAt(ConstantManager.driveType - ConstantManager.minTypes())).elementAt(systemIndex)).size()){
                        ((Vector) formAssign.elementAt(systemIndex)).addElement(nonFormAssign.elementAt(systemIndex)); // expands double outputs to fill a full assign
                    }
                }
                formAssign.addElement(new Integer(ConstantManager.driveType));
                historicAutoVals = formAssign;
                Hardware.assign(formAssign);
                if(((Double) ((Vector) formAssign.elementAt(0)).elementAt(0)).equals(new Double(0)) && ((Double) ((Vector) formAssign.elementAt(1)).elementAt(0)).equals(new Double(0))){
                    autoStopped = true;
                    autoStoppedTime = Timer.getFPGATimestamp();
                }
                if(autoStopped == true){
                    if(Timer.getFPGATimestamp() - autoStoppedTime >= ConstantManager.autoDrivePause){
                        autoFinished = true;
                    }
                }
            }
            else{
                Debug.output("Drive.auto: requestBuffer assigned incorrectly!", nomFormAssign, ConstantManager.autoDebug);
            }
        }
        else{
            if(historicAutoVals.size() != 0){
                Hardware.assign(historicAutoVals);
            }
            else{
               for(int systemIndex = 0; systemIndex < ((Vector) Hardware.hardware.elementAt(ConstantManager.driveType - ConstantManager.minTypes())).size(); systemIndex++){
                    formAssign.addElement(new Vector(0));
                    while(((Vector) formAssign.elementAt(systemIndex)).size() < ((Vector) ((Vector) Hardware.hardware.elementAt(ConstantManager.driveType - ConstantManager.minTypes())).elementAt(systemIndex)).size()){
                        ((Vector) formAssign.elementAt(systemIndex)).addElement(new Double(0)); // expands double outputs to fill a full assign
                    }
                }
               formAssign.addElement(new Integer(ConstantManager.driveType));
               Hardware.assign(formAssign);
               Debug.output("Drive.auto: requestBuffer driveFlag false AND missing historic values. Staying still.", formAssign, ConstantManager.autoDebug); 
            }
        }
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
                    if(requestBuffer.driveFlag){
                        driveAssign = requestBuffer.driveBufferUse();
                    }
                }
            }
        }
        if(driveAssign.lastElement().getClass() != (new Integer(0)).getClass()){
        driveAssign.addElement(new Integer(ConstantManager.driveType));
        }
        else if(((Integer) driveAssign.lastElement()).intValue() != ConstantManager.driveType){
            Debug.output("Drive.drive(): Changing driveAssign Integer tag from " + driveAssign.lastElement().toString() + "to " + new Integer(ConstantManager.driveType).toString(),null, ConstantManager.driveDebug);
            driveAssign.removeElementAt(driveAssign.size()-1);
            driveAssign.addElement(new Integer(ConstantManager.driveType));
        }
        Hardware.assign(driveAssign);
    }
    public void reconfig(Vector controllerConfig, Vector driveSetConfig){
        
    }
}