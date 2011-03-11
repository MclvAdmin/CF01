/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mclv.device;
import edu.wpi.first.wpilibj.Victor;
import mclv.*;
import mclv.utils.*;

import java.util.*;
/**
 *
 * @author god
 */
public class VictorMclv { //This wraps info for monitor into the actual jag object initialized. Use this class to catch CANTimeoutException
    private static Vector victorDevice;
    private static final int PWM_BUS_MIN = 1;
    private static int pwmBus = PWM_BUS_MIN; //use index value to identify jag
    public static int unusedBus = Pwm.unusedBus + 1;
    public int instanceBus; //public for debugging purposes
    public double lastVal;
    public double lastActualVal;
    public Victor pwmInstance; //public for monitor class
    
    public VictorMclv(int bus){
        instanceBus = bus;
        if(bus == unusedBus){
            
        }
        else{
            pwmInstance = new Victor(bus);
            Debug.output("VictorMclv constructor: created PWM on bus", new Integer(bus), ConstantManager.deviceDebug);
        }
    }
    public static Vector init(){ // boolean reinit represents whether this is the first time you're re-initializing
        
        victorDevice = new Vector(0);
        Debug.output("VictorMclv.init: creating new Pwm", new Integer(pwmBus), ConstantManager.deviceDebug);
        victorDevice.addElement(new Pwm(pwmBus));
        Debug.output("VictorMclv.init: creating new data storage vector", new Integer(pwmBus), ConstantManager.deviceDebug);
        victorDevice.addElement(new Vector(0)); //This is for data storage
        Debug.output("VictorMclv.init: creating new integer ID", new Integer(pwmBus -1), ConstantManager.deviceDebug);
        victorDevice.addElement(new Integer(pwmBus -1)); //this is the ID number
        pwmBus++;
        Debug.output("VictorMclv.init: Final Vector", victorDevice, 0); //very low priority because it takes time to do
        return victorDevice; //Might want to attach success boolean for outputting which jaguar was at fault in the overall grid.
    }
    public static void reInit(){
            pwmBus = PWM_BUS_MIN;
    }
    public void assign(double output){
            Debug.output("VictorMclv.assign: assigning ouput value to PWM device", new Double(output), ConstantManager.deviceDebug);
            lastVal = output;
            lastActualVal = pwmInstance.get();
            pwmInstance.set(output);
    }
}
