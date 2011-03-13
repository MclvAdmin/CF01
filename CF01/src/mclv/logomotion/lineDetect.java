/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mclv.logomotion;
import mclv.*;
import mclv.utils.*;
import java.util.*;
import edu.wpi.first.wpilibj.DigitalInput;
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
    public static boolean atEnd = false;
    public static void acquire(){ //from monitor
        if(lineDetectRun == 0){
            
        }
    lineDetectRun++;
    }
    public static void feed(){ //create monitor entry to track history
        /*if(((Boolean) sensorStates.elementAt(0)) == null || ((Boolean) sensorStates.elementAt(1)) == (null) || ((Boolean) sensorStates.elementAt(2)) == (null)){
            System.out.println("error retrieving line sensor states");
            lineSt = 0;
        }
        else{
            leftSense = Hardware.lineLeft.get();
            midSense = ((Boolean) sensorStates.elementAt(1)).booleanValue();
            rightSense = ((Boolean) sensorStates.elementAt(2)).booleanValue();
        }*/
        if(lineDetectRun == 0){
            lineData = new Vector(0);
        }
        
        leftSense = Hardware.lineLeft.get();
        midSense = Hardware.lineMid.get();
        rightSense = Hardware.lineRight.get();
        
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
 
        
        lineData.addElement(new Integer(lineSt));        
        analyze(lineSt);
        lineDetectRun++;
    }
    private static void analyze(int state){
        //driveRequest = new Vector(((Vector) Hardware.hardware.elementAt(ConstantManager.driveType - ConstantManager.minTypes())).size()); //This will allow for systemwide values... should only ever be two systems anyway! Maybe include a slight turn request built into drive?
        driveRequest = new Vector(0);
        if(history(7, ConstantManager.atEndDepth) >= ConstantManager.atEndHistCount){
            atEnd = true;
        }
        
        if(!atEnd){
            switch (state) {
                case 0: Debug.output("warning: ambigous line sensor states: 000. Build team missed the damn line", null, ConstantManager.lineDebug); break;
                case 1: driveRequest.addElement(new Double(ConstantManager.straight + ConstantManager.hardTurn)); driveRequest.addElement(new Double(ConstantManager.straight)); break;
                case 2: driveRequest.addElement(new Double(ConstantManager.straight)); driveRequest.addElement(new Double(ConstantManager.straight)); break;
                case 3: driveRequest.addElement(new Double(ConstantManager.straight + ConstantManager.slightTurn)); driveRequest.addElement(new Double(ConstantManager.straight)); break;
                case 4: driveRequest.addElement(new Double(ConstantManager.straight)); driveRequest.addElement(new Double(ConstantManager.straight+ ConstantManager.hardTurn)); break;
                case 5: driveRequest.addElement(null); driveRequest.addElement(null); Debug.output("lineDetect.analyze: warning: ambigous line sensor states: 101", null, ConstantManager.lineDebug); break;
                case 6: driveRequest.addElement(new Double(ConstantManager.straight)); driveRequest.addElement(new Double(ConstantManager.straight+ ConstantManager.slightTurn)); break;
                case 7: driveRequest.addElement(new Double(ConstantManager.straight)); driveRequest.addElement(new Double(ConstantManager.straight)); break; //Just go straight, might want to find last state in a classwide history as well as map opposite states to each other w/boolean logic
            }
        }
        else{
            driveRequest.addElement(new Double(0));
            driveRequest.addElement(new Double(0));
            Debug.output("lineDetect.analyze: Reached end!", null, ConstantManager.lineDebug);
        }
        request(driveRequest);
    }
    private static void request(Vector driveOut){ //ensure systemwide via object verification in assign
        driveOut.addElement(new Integer(ConstantManager.driveType));
        requestBuffer.add(driveOut);
    }
    
    public static void info(){
        
    }
    
    public static int history(int state, int depth){
        int stateReturn = state;
        int internalCount = 0;
        
        if(depth<=lineData.size()){
            for(int dataIndex = lineData.size() - 1; dataIndex > lineData.size() - depth - 1; dataIndex--){
                if(dataIndex>0){
                    if(((Integer) lineData.elementAt(dataIndex)).intValue() == state){
                        internalCount++;
                    }
                }
            }
        }
        return stateReturn;
    }
}
