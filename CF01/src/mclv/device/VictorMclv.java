/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mclv.device;
import edu.wpi.first.wpilibj.Victor;

import java.util.*;
/**
 *
 * @author god
 */
public class VictorMclv { //This wraps info for monitor into the actual jag object initialized. Use this class to catch CANTimeoutException
    private static Vector victorDevice;
    private static final int PWM_BUS_MIN = 1;
    private static int pwmBus = PWM_BUS_MIN; //use index value to identify jag
    public int instanceBus; //public for debugging purposes
    public Victor pwmInstance; //public for monitor class
    
    public VictorMclv(int bus){
        instanceBus = bus;
        pwmInstance = new Victor(bus);
        System.out.println("Pwm constructor: created PWM on bus");
        System.out.println(bus);
    }
    public static Vector init(){ // boolean reinit represents whether this is the first time you're re-initializing
        
        victorDevice = new Vector(0);
        System.out.println("Pwm.init: creating new Pwm");
        victorDevice.addElement(new Pwm(pwmBus));
        System.out.println("Pwm.init: creating new data storage vector");
        victorDevice.addElement(new Vector(0)); //This is for data storage
        System.out.println("Pwm.init: creating new integer ID");
        victorDevice.addElement(new Integer(pwmBus -1)); //this is the ID number
        pwmBus++;
        return victorDevice; //Might want to attach success boolean for outputting which jaguar was at fault in the overall grid.
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
