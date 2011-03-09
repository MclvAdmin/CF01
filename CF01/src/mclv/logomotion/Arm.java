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
        System.out.println("Arm.arm: arm() start");
        Vector armAssign = new Vector(0);
        if(init == 0){
            armVals = new Vector(0);
        }
        
        for(int systemIndex = 0; systemIndex< ((Vector) Hardware.hardware.elementAt(ConstantManager.armType - ConstantManager.minTypes())).size(); systemIndex++){
            armAssign.addElement(new Vector(0));
            for(int memberIndex = 0; memberIndex < ((Vector) ((Vector) Hardware.hardware.elementAt(ConstantManager.armType - ConstantManager.minTypes())).elementAt(systemIndex)).size(); memberIndex++){
                if(!((Boolean) ((Vector) driverInput.inputVals.elementAt(ConstantManager.armType - ConstantManager.minTypes())).elementAt(0)).booleanValue()){
                    System.out.println("Arm.arm: ASSIGNING TO ARM");
                    ((Vector) armAssign.elementAt(systemIndex)).addElement(((Vector) driverInput.inputVals.elementAt(ConstantManager.armType - ConstantManager.minTypes())).elementAt(systemIndex + 1)); //+ 1?
                }
                else{
                    System.out.println("Arm.arm: NOT ASSIGNING TO ARM");
                    //Needs choices here; goto class that determines the course of action if drivers r not in control!
                }
            }
        }
        armAssign.addElement(new Integer(ConstantManager.armType));
        Hardware.assign(armAssign);
    }
}
