/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.defaultCode;


import mclv.logomotion.*;
import mclv.utils.*;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
//import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import mclv.*;
import java.util.*;


/**
 * This "BuiltinDefaultCode" provides the "default code" functionality as used in the "Benchtop Test."
 *
 * The BuiltinDefaultCode extends the IterativeRobot base class to provide the "default code"
 * functionality to confirm the operation and usage of the core control system components, as
 * used in the "Benchtop Test" described in Chapter 2 of the 2009 FRC Control System Manual.
 *
 * This program provides features in the Disabled, Autonomous, and Teleop modes as described
 * in the benchtop test directions, including "once-a-second" debugging printouts when disabled,
 * a "KITT light show" on the solenoid lights when in autonomous, and elementary driving
 * capabilities and "button mapping" of joysticks when teleoperated.  This demonstration
 * program also shows the use of the user watchdog timer.
 *
 * This demonstration is not intended to serve as a "starting template" for development of
 * robot code for a team, as there are better templates and examples created specifically
 * for that purpose.  However, teams may find the techniques used in this program to be
 * interesting possibilities for use in their own robot code.
 *
 * The details of the behavior provided by this demonstration are summarized below:
 *
 * Disabled Mode:
 * - Once per second, print (on the console) the number of seconds the robot has been disabled.
 *
 * Autonomous Mode:
 * - Flash the solenoid lights like KITT in Knight Rider
 * - Example code (commented out by default) to drive forward at half-speed for 2 seconds
 *
 * Teleop Mode:
 * - Select between two different drive options depending upon Z-location of Joystick1
 * - When "Z-Up" (on Joystick1) provide "arcade drive" on Joystick1
 * - When "Z-Down" (on Joystick1) provide "tank drive" on Joystick1 and Joystick2
 * - Use Joystick buttons (on Joystick1 or Joystick2) to display the button number in binary on
 *   the solenoid LEDs (Note that this feature can be used to easily "map out" the buttons on a
 *   Joystick.  Note also that if multiple buttons are pressed simultaneously, a "15" is displayed
 *   on the solenoid LEDs to indicate that multiple buttons are pressed.)
 *
 * This code assumes the following connections:
 * - Driver Station:
 *   - USB 1 - The "right" joystick.  Used for either "arcade drive" or "right" stick for tank drive
 *   - USB 2 - The "left" joystick.  Used as the "left" stick for tank drive
 *
 * - Robot:
 *   - Digital Sidecar 1:
 *     - PWM 1/3 - Connected to "left" drive motor(s)
 *     - PWM 2/4 - Connected to "right" drive motor(s)
 *
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class DefaultRobot extends IterativeRobot{
        DriverStation mclvDs;
        Vector driveAssign;

        //Hardware cfHard;
        Vector emptyVector;
        Vector controllerAssign;
        Vector lineAssign;
        Vector armAssign;
        boolean bool;

        //Drive cfDrive;
        //driverInput cfIn;
        Vector positionReq;
        Vector driveJagStatus;
        Vector posReq;
        Integer int2;

    /**
     * Constructor for this "BuiltinDefaultCode" Class.
     *
     * The constructor creates all of the objects used for the different inputs and outputs of
     * the robot.  Essentially, the constructor defines the input/output mapping for the robot,
     * providing named objects for each of the robot interfaces.
     */
    public DefaultRobot(){
        //Watchdog.getInstance().feed();
        System.out.println("Roman Constructor Started\n");
         //cfDrive = new Drive(new Vector());
         //DriverStation.
         mclvDs = DriverStation.getInstance();
         int2 = new Integer(17);
         driveAssign = new Vector(0);
         armAssign = new Vector(0);
         controllerAssign = new Vector(0);
         posReq = new Vector(0);
         driveJagStatus = new Vector(0);
         emptyVector = new Vector(0);
         lineAssign = new Vector(0);
         bool = false;

            driveAssign.addElement(new Integer(2));
            driveAssign.addElement(new Integer(2));
            driveAssign.addElement(new Integer(ConstantManager.driveType));
             
            armAssign.addElement(new Integer(1));
            armAssign.addElement(new Integer(1));
            armAssign.addElement(new Integer(1));
            armAssign.addElement(new Integer(ConstantManager.armType));
            
            Vector pneuAssign = new Vector(0);
            
            pneuAssign.addElement(new Integer(1));
            pneuAssign.addElement(new Integer(ConstantManager.pneuType));
            
            controllerAssign.addElement(new Vector(0));
            posReq.addElement(new Integer(2));
            posReq.addElement(new Integer(2));
            //posReq.addElement(new Vector(2));
            //posReq.addElement(new Vector(2));
            posReq.addElement(new Integer(0));
            //((Vector) posReq.elementAt(0)).addElement(new Integer(0));
            //((Vector) posReq.elementAt(1)).addElement(new Integer(0));
            //posReq.addElement(new Integer(0));
            lineAssign.addElement(new Integer(3));
            driveJagStatus.addElement(new Vector(0));
            driveJagStatus.addElement(new Vector(0));
            ((Vector) driveJagStatus.elementAt(0)).addElement(new Integer(1)); //0 is the failed state!
            ((Vector) driveJagStatus.elementAt(0)).addElement(new Integer(1));
            ((Vector) driveJagStatus.elementAt(1)).addElement(new Integer(1));
            ((Vector) driveJagStatus.elementAt(1)).addElement(new Integer(1));
            driveJagStatus.addElement(new Vector(2));
            ((Vector) controllerAssign.elementAt(0)).addElement(new Integer(1));
            ((Vector) controllerAssign.elementAt(0)).addElement(new Integer(1));
            ((Vector) controllerAssign.elementAt(0)).addElement(new Integer(1));
            Vector hardwareAssign = new Vector(0);
            hardwareAssign.addElement(driveAssign);
            hardwareAssign.addElement(armAssign);
            Vector filler = new Vector(1);
            hardwareAssign.addElement(filler);
            hardwareAssign.addElement(pneuAssign);
           
            
            Hardware.init(hardwareAssign, "fresh");
            //Hardware.lineInit();
            driverInput.init(controllerAssign);
		
// Start roman constructor



                for(int i = 0; i<Hardware.assignment.size(); i++){
                    System.out.println(((Vector) Hardware.assignment.elementAt(i)).lastElement());
                    System.out.print(":");
                    for(int j = 0; j<((Vector) Hardware.assignment.elementAt(i)).size(); j++){
                        /*for(int h = 0; h<((Vector) ((Vector) Hardware.assignment.elementAt(i)).elementAt(j)).size(); h++){
                            System.out.println(((Integer) ((Vector) ((Vector) Hardware.assignment.elementAt(i)).elementAt(j)).elementAt(h)).intValue());
                         }*/
                        System.out.println(((Integer) ((Vector) Hardware.assignment.elementAt(i)).elementAt(j)).intValue());
                    }
                }
		System.out.println("Roman Constructor Completed\n");
                
	}


	/********************************** Init Routines *************************************/

	public void robotInit() {
		System.out.println("RobotInit() completed.\n");
                
                if(mclvDs.isFMSAttached()){
                    Debug.output("In a field match, disabling debug features now", null, 3); //a very important message.
                    ConstantManager.debug = false;
                }
                else{
                    Debug.output("Be advised, not connected to FMS", null, 3); //a very important message.
                }
                
	}

	public void disabledInit() {
            
	}

	public void autonomousInit() {
           
	}

	public void teleopInit() {
            
	}

	public void disabledPeriodic()  {
		
	}

	public void autonomousPeriodic() {
		
	}

	   public void teleopPeriodic(){ //CHANGE ITERATIVE ROBOT
       
        Debug.output("DefaultRobot: values to be sent as driverInput", null, 1);
        Debug.output("Boolean value", driverInput.drive().elementAt(0), 1);
        Debug.output("Left Joystick",driverInput.drive().elementAt(1), 1);
        Debug.output("Right Joystick",driverInput.drive().elementAt(2), 1);
        
        //try{ //best to reference other mclv 'main'
        
        //Hardware.assign(Drive.request(posReq, driverInput.drive()));
       driverInput.update(); 
       Drive.drive();
       Arm.arm();
        /*catch (CANTimeoutException canFail){
            System.out.println("Can timeout, switching to pwm");
            ConstantManager.pwm = true;
            Hardware.init(new Vector(0), "reinit"); //now THAT is the shit.. i hope it works. This will reinitialize a
        }*/

    }

}
