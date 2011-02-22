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
    
    public static void request(Vector values){
        armRequest = new Vector(Hardware.assignmentByType(ConstantManager.armType).size());
        if(values.elementAt(0) == null){
            if(((Boolean) driverInput.arm().elementAt(0)).booleanValue() == false){
                
            }
            
        }
    }
}
