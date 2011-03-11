/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mclv;
import mclv.utils.*;
import mclv.device.*;
import java.util.*;

/**
 *
 * @author god
 */
public class Debug {
public static CANJag testCANJag = new CANJag(CANJag.unusedBus);
public static Pwm testPwm = new Pwm(Pwm.unusedBus);
public static VictorMclv testVictorMclv = new VictorMclv(VictorMclv.unusedBus);
public static SolenoidMclv testSolenoidMclv = new SolenoidMclv(SolenoidMclv.unusedBus);

public static void output(String message, Object output, int verbosity){
    int verb = ConstantManager.debugVerbose;
    if(ConstantManager.debug){
       if(verbosity >= verb){ // this means you're good to print
           decideAndOut(message, output, verbosity);
        }
    }
  }
private static void decideAndOut(String message, Object output, int verbosity){ //This class checks the type of requested output and outs accordingly
    if(output != null){
        if(output.getClass() == (new Vector(0)).getClass()){ //If the object is a vector
            System.out.println(message + ":");
            for(int index = 0; index < ((Vector) output).size(); index++){
                output(message + ": Vector index " + (new Integer(index)).toString() + ": ", ((Vector) output).elementAt(index), verbosity); //Sends it back each time for each element
            }
        }
        else if(output.getClass().equals((new Double(0)).getClass()) || output.getClass().equals((new Integer(0)).getClass()) || output.getClass().equals((new Boolean(false)).getClass())){
            System.out.println(message + ": " + output.toString());
        }
        else if(output.getClass() == testCANJag.getClass()){
            System.out.println(message + output.toString() + ": Bus: " + (((CANJag) output).instanceBus) + " | Last Sent Value: " + (((CANJag) output).lastSentVal) + " | Last Actually Assigned Value: " + (((CANJag) output).lastActualVal));
        }
        else if(output.getClass() == testPwm.getClass()){
            System.out.println(message + output.toString() + ": Bus: " + (((Pwm) output).instanceBus) + " | Last Sent Value: " + (((Pwm) output).lastSentVal) + " | Last Actually Assigned Value: " + (((Pwm) output).lastActualVal));
        }
        else if(output.getClass() == testVictorMclv.getClass()){
            System.out.println(message + output.toString() + ": Bus: " + (((VictorMclv) output).instanceBus) + " | Last Sent Value: " + (((VictorMclv) output).lastVal) + " | Last Actually Assigned Value: " + (((VictorMclv) output).lastActualVal));
        }
        else if(output.getClass() == testSolenoidMclv.getClass()){
            System.out.println(message + output.toString() + ": Bus: " + (((SolenoidMclv) output).instanceBus) + " | Last Sent Value: " + (((SolenoidMclv) output).lastVal));
        }
        else{
            System.out.println("Debug: ERROR: UNRECOGNIZED TYPE" + message);
        }
    }
    else{
        System.out.println(message);
    }
}
}
