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
   private static Vector victor;
    private static final int PWM_MIN = 1;
    private static int pwmPin = PWM_MIN; //use index value to identify jag
    public static Vector init(){ // boolean reinit represents whether this is the first time you're re-initializing
        
        victor = new Vector(0);
        victor.addElement(new Victor(pwmPin));
        victor.addElement(new Vector(0)); //This is for data storage
        victor.addElement(new Integer(pwmPin -1)); //this is the ID number
        pwmPin++;
        
        return victor; //Might want to attach success boolean for outputting which jaguar was at fault in the overall grid.
    }
    public static void reInit(){
            pwmPin = PWM_MIN;
    }
}
