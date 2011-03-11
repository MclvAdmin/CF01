/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mclv.logomotion;
import mclv.utils.*;
import mclv.*;
import java.util.*;

/**
 *
 * @author god
 */
public class Arm {
    public static Vector armRequest;
    public static Vector armVals;
    public static int init = 0;
    private static double clawStart;
    private static double mainStart;
    private static double wristStart;
    private static int clawSt = 0; //0 for idle, 1 for extend, two for retract
    private static int mainSt = 0; 
    private static int wristSt = 0;
    
    
    public static void request(Vector values){
        armRequest = new Vector(Hardware.assignmentByType(ConstantManager.armType).size());
        /*if(values.elementAt(0) == null){
            if(((Boolean) driverInput.arm().elementAt(0)).booleanValue() == false){
                
            }
            
        }*/
    }
    public static void arm(){
        Debug.output("Arm.arm: Start", null, ConstantManager.armDebug);
        Vector armAssign = new Vector(0);
        if(init == 0){
            armVals = new Vector(0);
        }
        
        for(int systemIndex = 0; systemIndex< ((Vector) Hardware.hardware.elementAt(ConstantManager.armType - ConstantManager.minTypes())).size(); systemIndex++){
            armAssign.addElement(new Vector(0));
            for(int memberIndex = 0; memberIndex < ((Vector) ((Vector) Hardware.hardware.elementAt(ConstantManager.armType - ConstantManager.minTypes())).elementAt(systemIndex)).size(); memberIndex++){
                if(!((Boolean) ((Vector) driverInput.inputVals.elementAt(ConstantManager.armType - ConstantManager.minTypes())).elementAt(0)).booleanValue()){
                    Debug.output("Arm.arm: ASSIGNING TO ARM", null, ConstantManager.armDebug);
                    ((Vector) armAssign.elementAt(systemIndex)).addElement(((Vector) driverInput.inputVals.elementAt(ConstantManager.armType - ConstantManager.minTypes())).elementAt(systemIndex + 1)); //+ 1?
                }
                else{
                    Debug.output("Arm.arm: NOT ASSIGNING TO ARM", null, ConstantManager.armDebug);
                    //Needs choices here; goto class that determines the course of action if drivers r not in control!
                }
            }
        }
        Vector wristAssign = new Vector(0);
        for(int systemIndex = 0; systemIndex< ((Vector) Hardware.hardware.elementAt(ConstantManager.pneuType - ConstantManager.minTypes())).size(); systemIndex++){
            wristAssign.addElement(new Vector(0));
            for(int memberIndex = 0; memberIndex < ((Vector) ((Vector) Hardware.hardware.elementAt(ConstantManager.pneuType - ConstantManager.minTypes())).elementAt(systemIndex)).size(); memberIndex++){
                if(!((Boolean) ((Vector) driverInput.inputVals.elementAt(ConstantManager.armType - ConstantManager.minTypes())).elementAt(0)).booleanValue()){
                    Debug.output("Arm.arm: ASSIGNING TO CLAW", null, ConstantManager.armDebug);
                    
                    ((Vector) wristAssign.elementAt(systemIndex)).addElement(((Vector) driverInput.inputVals.elementAt(ConstantManager.armType - ConstantManager.minTypes())).elementAt(3)); //systemIndex + 1?
                    
                }
                else{
                    Debug.output("Arm.arm: NOT ASSIGNING TO ARM", null, ConstantManager.armDebug);
                    //Needs choices here; goto class that determines the course of action if drivers r not in control!
                }
            }
        }
        
        armAssign.addElement(new Integer(ConstantManager.armType));
        wristAssign.addElement(new Integer(ConstantManager.pneuType));
        Vector holder = new Vector(0);
        holder.addElement(armAssign);
        holder.addElement(wristAssign);
        //armAssign.removeAllElements();
        
        Hardware.assign(holder);
    }
}
