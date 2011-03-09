/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mclv.utils;
//import mclv.*;
import java.util.*;
import mclv.Hardware;
import edu.wpi.first.wpilibj.Timer;
//import suanshu.*;


/**
 *
 * @author god
 */
public class Monitor {
    public int instanceType;
    public static Vector objectContainer;
    private static int initCount = 0;
    private static int updateCount = 0;
    private static Vector updateTimeLog;
    public int initId;
    private Vector instanceData;
    public Vector instanceStatus;
    /* Datastructure:
     * instanceData for driveType
     */
    
    
    public Monitor(int type){
        if(initCount == 0){
            objectContainer = new Vector(0);
            updateTimeLog = new Vector(0);
        }
        instanceData = new Vector(0);
        instanceStatus = new Vector(0);
        initId = initCount;
        
        
        objectContainer.addElement(this);
        initCount++;
    }
    
    public Vector getState(){ //first element is a Boolean, the rest is specific info about the state tbd.
        System.out.println("Monitor.getState: be advised, monitor object requested is of type");
        System.out.println(instanceType);
        
        return instanceStatus;
    }
    public static void updateAll(){
        for(int i = 0; i<objectContainer.size(); i++){
            ((Monitor) objectContainer.elementAt(i)).update();
        }
        
    }
    public static void updateByType(int type){
        for(int i = 0; i<objectContainer.size(); i++){
            if(((Monitor) objectContainer.elementAt(i)).instanceType == type){
                ((Monitor) objectContainer.elementAt(i)).update();
            }
        }
    }
    public void update(){
        if(instanceType == ConstantManager.driveType){
            
        }
        else if(instanceType == ConstantManager.armType){
            
        }
        else if(instanceType == ConstantManager.victorType){
            
        }
        else if(instanceType == ConstantManager.lineType){
            
        }
        else if(instanceType == ConstantManager.posType){
            
        }
        updateTimeLog.addElement(new Double(Timer.getFPGATimestamp())); //time stamp, element is the update count
        updateCount++;
    }
}
