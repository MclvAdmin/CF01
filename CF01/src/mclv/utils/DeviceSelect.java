/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mclv.utils;
import mclv.device.*;
import java.util.*;

/**
 *
 * @author god
 */
public class DeviceSelect {
    private static Vector objectHolder;
    private static Object objectTest;
    public DeviceSelect(){}
    public static Vector selectInit(int type){
        objectHolder = new Vector(0);
        objectTest = new Object();
         if(type == ConstantManager.driveType || type == ConstantManager.armType){   
            if(!ConstantManager.pwm){
                System.out.println("DeviceSelect selectInit asking for CANJag");
                objectHolder = CANJag.init();
            }
            else{
                System.out.println("DeviceSelect selectInit asking for Pwm");
                objectHolder = Pwm.init();
            }
         }
         else if(type == ConstantManager.victorType){
             objectHolder = VictorMclv.init();
         }
         else if(type == ConstantManager.lineType){
             objectHolder = DigIn.init();
         }
         else if(type == ConstantManager.posType){
             objectHolder = AnalogIn.init();
         }
        return objectHolder;
    }
    /*public static Class selectAssign(int type){
        objectHolder = new Object();
         if(type == ConstantManager.driveType || type == ConstantManager.armType){   
            if(ConstantManager.pwm){
                objectHolder = CANJag.class;
            }
            else{
                objectHolder = Pwm.class;
            }
         }
         else if(type == ConstantManager.victorType){
             objectHolder = VictorMclv.class;
         }
         else if(type == ConstantManager.lineType){
             objectHolder = DigIn.class;
         }
         else if(type == ConstantManager.posType){
             objectHolder = AnalogIn.class;
         }
        return objectHolder.getClass();
    }*/
}
