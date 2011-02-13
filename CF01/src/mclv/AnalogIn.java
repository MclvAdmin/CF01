/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mclv;
import java.util.*;
import edu.wpi.first.wpilibj.AnalogChannel;
/**
 *
 * @author God
 */
public class AnalogIn {
private static Vector analogDevice;
    private static final int AN_PIN_MIN = 1;
    private static int analogPin = AN_PIN_MIN; //use index value to identify jag
    public static Vector init(){ // boolean reinit represents whether this is the first time you're re-initializing
        
        analogDevice = new Vector(0);
        analogDevice.addElement(new AnalogChannel(analogPin));
        analogDevice.addElement(new Vector(0)); //This is for data storage
        analogDevice.addElement(new Integer(analogPin -1)); //this is the ID number
        analogPin++;
        
        return analogDevice; //Might want to attach success boolean for outputting which jaguar was at fault in the overall grid.
    }
    public static void reInit(){
            analogPin = AN_PIN_MIN;
    }
}
