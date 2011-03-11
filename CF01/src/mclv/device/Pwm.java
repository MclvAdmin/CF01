/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mclv.device;
import edu.wpi.first.wpilibj.PWM;
import mclv.*;
import mclv.utils.*;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Timer;

import java.util.*;
/**
 *
 * @author god
 */
public class Pwm { //This wraps info for monitor into the actual jag object initialized. Use this class to catch CANTimeoutException
    private static Vector pwmDevice;
    private static double lastStart;
    private static final int PWM_BUS_MIN = 1;
    private static int pwmBus = PWM_BUS_MIN; //use index value to identify jag
    public static int unusedBus = 99;
    public int instanceBus; //public for debugging purposes
    public double lastSentVal;
    public double lastActualVal;
    public Jaguar pwmInstance; //public for monitor class
    
    public Pwm(int bus){
        instanceBus = bus;
        if(bus == unusedBus){
            
        }
        else{
            pwmInstance = new Jaguar(bus);
            Debug.output("Pwm constructor: created PWM on bus", new Integer(bus), ConstantManager.deviceDebug);
        }
    }
    public static Vector init(){ // boolean reinit represents whether this is the first time you're re-initializing
        
        pwmDevice = new Vector(0);
        Debug.output("Pwm.init: creating new Pwm", new Integer(pwmBus), ConstantManager.deviceDebug);
        pwmDevice.addElement(new Pwm(pwmBus));
        Debug.output("Pwm.init: creating new data storage vector", null, ConstantManager.deviceDebug);
        pwmDevice.addElement(new Vector(0)); //This is for data storage
        System.out.println("Pwm.init: creating new integer ID");
        Debug.output("Pwm.init: creating new integer ID", new Integer(pwmBus - 1), ConstantManager.deviceDebug);
        pwmDevice.addElement(new Integer(pwmBus -1)); //this is the ID number
        pwmBus++;
        Debug.output("Pwm.init: Final Vector", pwmDevice, 0); //very low priority because it takes time to do
        return pwmDevice; //Might want to attach success boolean for outputting which jaguar was at fault in the overall grid.
    }
    public static void reInit(){
            pwmBus = PWM_BUS_MIN;
    }
    public void assign(double output){
            Debug.output("Pwm.assign: assigning ouput value to PWM device", new Double(output), 0);
            double timeInterval = Timer.getFPGATimestamp() - Hardware.lastOn;
            Debug.output("Pwm.assign: time interval", new Double(timeInterval), 3);
            lastSentVal = output;
            lastActualVal = pwmInstance.get();
            pwmInstance.set(output);
            if(timeInterval >= ConstantManager.compDelay){
                if(!Hardware.compOn){
                    if(Math.abs(output) < ConstantManager.compCutoff){
                       Debug.output("Pwm.assign: stopping compressor at time", new Double(Timer.getFPGATimestamp()), 3);
                       Hardware.comp.start();
                       Hardware.compOn = true;
                    }
                }
                else{
                    if(Math.abs(output) >= ConstantManager.compCutoff){
                        Debug.output("Pwm.assign: starting compressor at time", new Double(Timer.getFPGATimestamp()), 3);
                        Hardware.comp.stop();
                        Hardware.compOn = false;
                        Hardware.lastOn = Timer.getFPGATimestamp();
                    }
                }
            }
    }
}
