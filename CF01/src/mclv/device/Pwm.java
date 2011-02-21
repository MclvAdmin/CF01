/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mclv.device;
import java.util.*;
import edu.wpi.first.wpilibj.PWM;

/**
 *
 * @author God
 */
public class Pwm {
    private static Vector pwmDevice;
    private static final int PWM_PIN_MIN = 1;
    private static int pwmPin = PWM_PIN_MIN; //use index value to identify jag
    public static Vector init(){ // boolean reinit represents whether this is the first time you're re-initializing
        
        pwmDevice = new Vector(0);
        pwmDevice.addElement(new PWM(pwmPin));
        pwmDevice.addElement(new Vector(0)); //This is for data storage
        pwmDevice.addElement(new Integer(pwmPin -1)); //this is the ID number
        pwmPin++;
        return pwmDevice; //Might want to attach success boolean for outputting which jaguar was at fault in the overall grid.
    }
    public static void reInit(){
            pwmPin = PWM_PIN_MIN;
    }
}
