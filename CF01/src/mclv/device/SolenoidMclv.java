/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mclv.device;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import mclv.utils.*;
import mclv.*;
import java.util.*;
/**
 *
 * @author god
 */
public class SolenoidMclv {
DoubleSolenoid solenoidInstance;
private static double lastStart;
private static final int SOL_BUS_MIN = 1;
private static int solBus = SOL_BUS_MIN; //use index value to identify jag
public static int unusedBus = 99;
public Vector instanceBus; //public for debugging purposes
public boolean lastVal = true;
public boolean lastActualVal;
    
    public SolenoidMclv(int bus){
       if(!(bus == unusedBus)){
       //DoubleSolenoid.
       solenoidInstance = new DoubleSolenoid(bus, bus+1); //int forwardchannel, int reverse
       instanceBus = new Vector(0);
       instanceBus.addElement(new Integer(bus));
       instanceBus.addElement(new Integer(bus + 1));
       //solenoidInstance.set(DoubleSolenoid.Value.kReverse);
       Debug.output("SolenoidMclv constructor: created DoubleSolenoid on buses", instanceBus, ConstantManager.deviceDebug);
       }
       else{
           
       }
    }
    public void assign(boolean output){
            Debug.output("SolenoidMclv.assign: assigning ouput value to DoubleSolenoid device", new Boolean(output), ConstantManager.deviceDebug);
            lastVal = output;
            //lastActualVal = solenoidInstance.;
            if(output){
            Debug.output("SolenoidMclv.assign: asking for foward value", null, ConstantManager.deviceDebug);
            solenoidInstance.set(DoubleSolenoid.Value.kForward);  
            }
            else{
            Debug.output("SolenoidMclv.assign: asking for reverse value", null, ConstantManager.deviceDebug);
            solenoidInstance.set(DoubleSolenoid.Value.kReverse); 
            }
            
    }
    public static Vector init(){
        Vector solenoidVector = new Vector(0);
        Debug.output("SolenoidMclv.init: creating new SolenoidMclv", new Integer(solBus), ConstantManager.deviceDebug);
        solenoidVector.addElement(new SolenoidMclv(solBus)); //base buss
        Debug.output("SolenoidMclv.init: creating new SolenoidMclv", new Integer(solBus), ConstantManager.deviceDebug);
        solenoidVector.addElement(new Vector(0));
        solenoidVector.addElement(new Integer(solBus -1)); //represents base bus
        solBus++;
        solBus++;
        return solenoidVector;
    }
}
