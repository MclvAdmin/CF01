/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mclv.logomotion;
import mclv.*;
import mclv.utils.*;
import java.util.*;
/**
 *
 * @author god
 */
public class lineDetect {
    public static int lineDetectRun = 0;
    private static int lineSt = 0;
    private static Vector lineData; //export to monitor
    private static boolean leftSense;
    private static boolean midSense;
    private static boolean rightSense;
    private static Vector driveRequest;
    //public lineDetect(){}
    public void acquire(){ //from monitor
        if(lineDetectRun == 0){
            
        }
    lineDetectRun++;
    }
    private void feed(Vector sensorStates){ //create monitor entry to track history
        if(((Boolean) sensorStates.elementAt(0)) == null || ((Boolean) sensorStates.elementAt(1)) == (null) || ((Boolean) sensorStates.elementAt(2)) == (null)){
            System.out.println("error retrieving line sensor states");
            lineSt = 0;
        }
        else{
            leftSense = ((Boolean) sensorStates.elementAt(0)).booleanValue();
            midSense = ((Boolean) sensorStates.elementAt(1)).booleanValue();
            rightSense = ((Boolean) sensorStates.elementAt(2)).booleanValue();
        }
        
        if(!leftSense && !midSense && !rightSense){
            lineSt = 0;
        }
        else if(!leftSense && !midSense && rightSense){ //hard right  track all vals relative to last mid state
            lineSt = 1;
        }
        else if(!leftSense && midSense && !rightSense){ //straight
            lineSt = 2;
        }
        else if(!leftSense && midSense && rightSense){ //slight right
            lineSt = 3;
        }
        else if(leftSense && !midSense && !rightSense){ //hard left
            lineSt = 4;
        }
        else if(leftSense && !midSense && rightSense){ //brainfuck, invalid state -- frequency of this state is a possible noise indicator
            lineSt = 5;
            System.out.println("warning: ambigous line sensor states: 101");
        }
        else if(leftSense && midSense && !rightSense){ //slight left
            lineSt = 6;
        }
        else if(leftSense && midSense && rightSense){ //see last relevant state -- means you're probably about to make a bad choice -- do opposite of last state
            lineSt = 7;
        }
 
        
                
        analyze(lineSt);
    }
    private void analyze(int state){
        driveRequest = new Vector(((Vector) Hardware.hardware.elementAt(ConstantManager.driveType - ConstantManager.minTypes())).size()); //This will allow for systemwide values... should only ever be two systems anyway! Maybe include a slight turn request built into drive?
        switch (state) {
            case 0: System.out.println("warning: ambigous line sensor states: 000"); break;
            case 1: driveRequest.setElementAt(new Double(ConstantManager.straight + ConstantManager.hardTurn), 0); driveRequest.setElementAt(new Double(ConstantManager.straight), 1); break;
            case 2: driveRequest.setElementAt(new Double(ConstantManager.straight), 0); driveRequest.setElementAt(new Double(ConstantManager.straight), 1); break;
            case 3: driveRequest.setElementAt(new Double(ConstantManager.straight + ConstantManager.slightTurn), 0); driveRequest.setElementAt(new Double(ConstantManager.straight), 1); break;
            case 4: driveRequest.setElementAt(new Double(ConstantManager.straight), 0); driveRequest.setElementAt(new Double(ConstantManager.straight+ ConstantManager.hardTurn), 1); break;
            case 5: driveRequest.setElementAt(null, 0); driveRequest.setElementAt(null, 1); System.out.println("warning: ambigous line sensor states: 101"); break;
            case 6: driveRequest.setElementAt(new Double(ConstantManager.straight), 0); driveRequest.setElementAt(new Double(ConstantManager.straight+ ConstantManager.slightTurn), 1); break;
            case 7: driveRequest.setElementAt(new Double(ConstantManager.straight), 0); driveRequest.setElementAt(new Double(ConstantManager.straight), 1); break; //Just go straight, might want to find last state in a classwide history as well as map opposite states to each other w/boolean logic
        }
        request(driveRequest);
    }
    private void request(Vector driveOut){ //ensure systemwide via object verification in assign
        driveOut.addElement(new Integer(ConstantManager.driveType));
        requestBuffer.add(driveOut);
    }
    
    public void info(){
        
    }
}
