/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mclv.logomotion;
import java.util.*;
/**
 *
 * @author god
 */
public class drive { //Consider static methods
    private static Vector driveSet;
    private static Vector driveOut;
    public drive(Vector driveSetConfig){
        driveSet = driveSetConfig;    
    }
  
    public static Vector request(Vector positionReq, Vector controllerDrive){ //Makes 'driveAssign' for hardware object ..... positionReq includes data as to automation of drive from sensor class
        driveSet = new Vector(0);
        driveOut = new Vector(0);
        System.out.println((((Boolean) controllerDrive.elementAt(0)).booleanValue()));
        if( ((((Integer) positionReq.lastElement()).intValue())==0) && ((((Boolean) controllerDrive.elementAt(0)).booleanValue()) != true)){
            /*THAT looks fucking complicated but it's the first value of the last element in the provided request from the 
             * position object which tells drive whether it's taking driver input.
             * It also checks to see if button/sequence has been pressed that would disable automatic movement
            */
            // controllerDrive(0) is false when drivers r drivin... (sequence not hit) controller (>0) refers to joystick vals that r changed

        }
        /* else if..... for(int i = 0; i<positionReq.size() -2; i++){
                for(int c = 0; c<((Vector) positionReq.elementAt(i)).size(); c++){

                }
            }*/
        for(int i = 0; i< positionReq.size(); i++){
            System.out.println("posReq index value:");
            System.out.print(i);
                    
            for(int c = 0; driveOut.size()< positionReq.size(); c++){
                driveOut.addElement(new Vector());
                ((Vector) driveOut.elementAt(c)).addElement(new Double(0));
            }
            for(int j = 0; j<((Integer) positionReq.elementAt(i)).intValue(); j++){
                if(i == 0){ //EXPAND TO MORE SYSTEMS!
                ((Vector)  driveOut.elementAt(i)).addElement(controllerDrive.elementAt(1));
                }
                else{
                //((Double) ((Vector)  driveOut.elementAt(i)).elementAt(j)).equals(((Double) controllerDrive.elementAt(2)));
                ((Vector)  driveOut.elementAt(i)).addElement(controllerDrive.elementAt(2));
                }
            }
        }
        return driveOut; //Send to hardware
    }
    public void reconfig(Vector controllerConfig, Vector driveSetConfig){
        
    }
}