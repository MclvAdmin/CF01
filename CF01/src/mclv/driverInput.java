/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mclv;
import java.util.*;
import edu.wpi.first.wpilibj.Joystick;
/**
 *
 * @author god
 * Needs to grab the val of all joystick input methods @ beginning of each loop... also needs to check for sequences
 * Convert simple values/states into relevant data: add method for each other class with relevant data derived from state of button/axis
 * Be able to provide other objects with a simple yes/no for relevant q's...
 */
public class driverInput {
    private static Vector controllers;
    private static int nextPort = 1; //The port min initially... Research Cypress module for other input devices
    private static Vector driveVals;
    private static Vector armVals;
    private static Integer testInt;
    
    public static void init(Vector controllerConfig){ //make static asap
        testInt = new Integer(1);
        controllers = new Vector(0); //Configure controller type vals in constant manager
        
         for(int i = 0; i<controllerConfig.size(); i++){
            controllers.addElement(new Vector());
            for(int c = 0; c<((Vector) controllerConfig.elementAt(i)).size(); c++){
                if(((Integer) ((Vector) controllerConfig.elementAt(i)).elementAt(c)).intValue() == 1){
                    ((Vector) controllers.elementAt(i)).addElement(new Joystick(nextPort));
                    nextPort++;
                }
                else if(((Integer) ((Vector) controllerConfig.elementAt(i)).elementAt(c)).intValue() == 2){
                    //RESEARCH other input devices
                }
            }
        }
    }

    public static Vector drive(){ //Reconfig to run from single driverInput call
        driveVals = new Vector(0);
        driveVals.addElement(new Boolean(true)); //Expand for more values! FALSE
        driveVals.addElement(new Double(((Joystick) ((Vector) controllers.elementAt(0)).elementAt(0)).getY()));
        driveVals.addElement(new Double(((Joystick) ((Vector) controllers.elementAt(0)).elementAt(1)).getY()));
        return driveVals;
    }
    public static Vector arm(){
        armVals = new Vector(3);
        ((Boolean) armVals.elementAt(0)).equals(new Boolean(false)); //Expand for more values!
        ((Double) armVals.elementAt(1)).equals(new Double(((Joystick) ((Vector) controllers.elementAt(0)).elementAt(0)).getX()));
        ((Double) armVals.elementAt(1)).equals(new Double(((Joystick) ((Vector) controllers.elementAt(0)).elementAt(1)).getX()));
        return armVals;
    }
    public static Vector info(){ //create info generator
        return new Vector(0);
    }
}
