/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mclv.device;
import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import java.util.*;
/**
 *
 * @author god
 */
public class CANJag { //This wraps info for monitor into the actual jag object initialized. Use this class to catch CANTimeoutException
    private static Vector jaguar;
    private static final int CAN_BUS_MIN = 1;
    private static int canBus = CAN_BUS_MIN; //use index value to identify jag
    public int instanceBus; //public for debugging purposes
    public CANJaguar jagInstance; //public for monitor class
    
    public CANJag(int bus){
        instanceBus = bus;
        try{
            jagInstance = new CANJaguar(bus);
            System.out.println("CANJag constructor: created CANJaguar on bus");
            System.out.println(bus);
        }
        catch(CANTimeoutException canFail){
            System.out.println("CAN Timeout while assigning ID:");
            System.out.println(bus);
        }
        
    }
    public static Vector init(){ // boolean reinit represents whether this is the first time you're re-initializing
        
        jaguar = new Vector(0);
        System.out.println("CANJag.init: creating new CANJag");
        jaguar.addElement(new CANJag(canBus));
        System.out.println("CANJag.init: creating new datastorage vector");
        jaguar.addElement(new Vector(0)); //This is for data storage
        System.out.println("CANJag.init: creating new integer ID");
        jaguar.addElement(new Integer(canBus -1)); //this is the ID number
        canBus++;
        return jaguar; //Might want to attach success boolean for outputting which jaguar was at fault in the overall grid.
    }
    public static void reInit(){
            canBus = CAN_BUS_MIN;
    }
    public void assign(double output){
        try{
            System.out.println("CANJag.assign: assigning ouput value to CANJaguar device");
            System.out.println(output);
            jagInstance.setX(output);
        }
        catch(CANTimeoutException canFail){
            System.out.println("CAN Timeout while assigning drive value:");
            System.out.println(output);
        }
    }
}
