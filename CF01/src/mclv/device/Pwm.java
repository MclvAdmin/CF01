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
        System.out.println("Pwm constructor: created PWM on bus");
        System.out.println(bus);
    }
    public static Vector init(){ // boolean reinit represents whether this is the first time you're re-initializing
        
        pwmDevice = new Vector(0);
        System.out.println("Pwm.init: creating new Pwm");
        pwmDevice.addElement(new Pwm(pwmBus));
        System.out.println("Pwm.init: creating new data storage vector");
        pwmDevice.addElement(new Vector(0)); //This is for data storage
        System.out.println("Pwm.init: creating new integer ID");
        pwmDevice.addElement(new Integer(pwmBus -1)); //this is the ID number
        pwmBus++;
        return pwmDevice; //Might want to attach success boolean for outputting which jaguar was at fault in the overall grid.
    }
    public static void reInit(){
            pwmBus = PWM_BUS_MIN;
    }
    public void assign(double output){
            System.out.println("Pwm.assign: assigning ouput value to PWM device");
            System.out.println(output);
            pwmInstance.setPosition(output);
    }
}
