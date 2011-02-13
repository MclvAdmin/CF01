/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mclv;
import java.util.*;
import edu.wpi.first.wpilibj.DigitalInput;

/**
 *
 * @author God
 */
public class DigIn {
    private static Vector digitalDevice;
    private static final int DIG_PIN_MIN = 1;
    private static int digPin = DIG_PIN_MIN; //use index value to identify jag
    public static Vector init(){ // boolean reinit represents whether this is the first time you're re-initializing
        
        digitalDevice = new Vector(0);
        digitalDevice.addElement(new DigitalInput(digPin));
        digitalDevice.addElement(new Vector(0)); //This is for data storage
        digitalDevice.addElement(new Integer(digPin -1)); //this is the ID number
        digPin++;
        return digitalDevice; //Might want to attach success boolean for outputting which jaguar was at fault in the overall grid.
    }
    public static void reInit(){
            digPin = DIG_PIN_MIN;
    }
}
