/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mclv.device;
import edu.wpi.first.wpilibj.PWM;

import java.util.*;
/**
 *
 * @author god
 */
public class Pwm { //This wraps info for monitor into the actual jag object initialized. Use this class to catch CANTimeoutException
    private static Vector pwmDevice;
    private static final int PWM_BUS_MIN = 1;
    private static int pwmBus = PWM_BUS_MIN; //use index value to identify jag
    public int instanceBus; //public for debugging purposes
    public PWM pwmInstance; //public for monitor class
    
    public Pwm(int bus){
        instanceBus = bus;
        pwmInstance = new PWM(bus);
    }
    public static Vector init(){ // boolean reinit represents whether this is the first time you're re-initializing
        
        pwmDevice = new Vector(0);
        pwmDevice.addElement(new Pwm(pwmBus));
        pwmDevice.addElement(new Vector(0)); //This is for data storage
        pwmDevice.addElement(new Integer(pwmBus -1)); //this is the ID number
        pwmBus++;
        return pwmDevice; //Might want to attach success boolean for outputting which jaguar was at fault in the overall grid.
    }
    public static void reInit(){
            pwmBus = PWM_BUS_MIN;
    }
    public void assign(double output){
            pwmInstance.setPosition(output);
    }
}
