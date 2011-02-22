/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mclv.utils;
import mclv.device.*;

/**
 *
 * @author god
 */
public class DeviceSelect {
    private static Object objectHolder;
    public DeviceSelect(){}
    public static Object selectInit(int type){
        objectHolder = new Object();
         if(type == ConstantManager.driveType || type == ConstantManager.armType){   
            if(ConstantManager.pwm){
                objectHolder = CANJag.init();
            }
            else{
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
    public static Class selectAssign(int type){
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
    }
}
