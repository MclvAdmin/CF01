/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mclv;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import mclv.utils.*;
import mclv.logomotion.*;
import java.util.*;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DriverStation;
/**
 *
 * @author god
 * Needs to grab the val of all joystick input methods @ beginning of each loop... also needs to check for sequences
 * Convert simple values/states into relevant data: add method for each other class with relevant data derived from state of button/axis
 * Be able to provide other objects with a simple yes/no for relevant q's...
 */
public class driverInput {
    private static Vector controllers;
    public static int init =0;
    public static int armInit =0;
    private static int nextPort = 1; //The port min initially... Research Cypress module for other input devices
    private static Vector driveVals;
    private static Vector armVals;
    private static Integer testInt;
    public static Vector inputVals;
    public static int usedTypesMaxType = ConstantManager.pneuType;
    public static double mainStart;
    public static int mainSt;
    public static double mainInterval;
    public static boolean mainQuickTo0;
    public static boolean mainQuickTo2;  
    public static double wristStart;
    public static int wristSt;
    public static double wristInterval;
    public static boolean wristQuickTo0;
    public static boolean wristQuickTo2; 
    public static double clawStart;
    public static int clawSt;
    public static double clawInterval;
    public static boolean clawQuickTo0;
    public static boolean clawQuickTo2; 
    public static boolean lastArm = true;
    public static double lastOutput;
    public static MotorSequence mainSequence;
    public static MotorSequence wristSequence;
    
    
    public static void init(Vector controllerConfig){ //make static asap
        testInt = new Integer(1);
        mainSt = 0;
        wristSt = 0;
        clawSt = 0;
        clawStart = Timer.getFPGATimestamp();
        
        controllers = new Vector(0); //Configure controller type vals in constant manager
        
         for(int i = 0; i<controllerConfig.size(); i++){
            controllers.addElement(new Vector(0));
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
         
         init++;
    }
    
    public static void update(){
        if(init == 0){
        //init++;
        }
        inputVals = new Vector(0);
        for(int typeIndex = 0; typeIndex + ConstantManager.minTypes()-1 != usedTypesMaxType; typeIndex++){
            //System.out.println("driverInput.update: typeIndex");
            //System.out.println(typeIndex);
            if(typeIndex + 1 == ConstantManager.driveType){
                inputVals.addElement(drive()); //Include special input changes in specific component methods
            }
            else if(typeIndex + 1 == ConstantManager.armType){
                //System.out.println("driverInput.update: placing arm vector at index");
                //System.out.println(typeIndex + 1);
                Vector armValues = new Vector(0);
                armValues = arm();
                armValues.removeElementAt(armValues.size() - 1);
                inputVals.addElement(armValues);
            }/*
            else if(typeIndex + 1 == ConstantManager.victorType){
                inputVals.addElement(new Vector(0));
            }*/
            else if(typeIndex + 1 == ConstantManager.pneuType){
                Vector pneuVals = new Vector(0);
                pneuVals.addElement(arm().lastElement());
                inputVals.addElement(pneuVals); 
            }
            else{
                inputVals.addElement(null);    
            }
        }
        Debug.output("driverInput.update: assignment vector", inputVals, ConstantManager.inputDebug);
        
        
    }

    public static Vector drive(){ //Reconfig to run from single driverInput call
        driveVals = new Vector(0);
        if(DriverStation.getInstance().isAutonomous() && DriverStation.getInstance().isEnabled()){
            driveVals.addElement(new Boolean(true));
        }
        else if (DriverStation.getInstance().isOperatorControl() && DriverStation.getInstance().isEnabled()){
            driveVals.addElement(new Boolean(false));
        }
        
        //driveVals.addElement(new Boolean(false)); //Expand for more values! FALSE 
        if(ConstantManager.joyMix && (100*Math.abs(((Joystick) ((Vector) controllers.elementAt(ConstantManager.joyIndex)).elementAt(0)).getY() - ((Joystick) ((Vector) controllers.elementAt(ConstantManager.joyIndex)).elementAt(1)).getY()) < ConstantManager.joyMixMargin) &! ((Math.abs(100*((Joystick) ((Vector) controllers.elementAt(ConstantManager.joyIndex)).elementAt(0)).getY()) < ConstantManager.joyMixNullRange) || (Math.abs(100*((Joystick) ((Vector) controllers.elementAt(ConstantManager.joyIndex)).elementAt(1)).getY()) < ConstantManager.joyMixNullRange))){
            double mixedValue = 0.5*Math.abs(((Joystick) ((Vector) controllers.elementAt(ConstantManager.joyIndex)).elementAt(0)).getY() + ((Joystick) ((Vector) controllers.elementAt(ConstantManager.joyIndex)).elementAt(1)).getY());
            if(((Joystick) ((Vector) controllers.elementAt(ConstantManager.joyIndex)).elementAt(0)).getY() + ((Joystick) ((Vector) controllers.elementAt(ConstantManager.joyIndex)).elementAt(1)).getY() < 0){
            mixedValue = -mixedValue;
            }
            driveVals.addElement(new Double(driveChoose(mixedValue)));
            driveVals.addElement(new Double(driveChoose(mixedValue)));
        }
        else{
            driveVals.addElement(new Double(driveChoose(((Joystick) ((Vector) controllers.elementAt(ConstantManager.joyIndex)).elementAt(0)).getY())));
            driveVals.addElement(new Double(driveChoose(((Joystick) ((Vector) controllers.elementAt(ConstantManager.joyIndex)).elementAt(1)).getY())));
        }
        return driveVals;
    }
    
    private static Vector driveButtons(){
        Vector buttonStates = new Vector(0);
        
        for(int joyIndex = 0; joyIndex<((Vector) controllers.elementAt(ConstantManager.joyIndex)).size(); joyIndex++){
        buttonStates.addElement(new Vector(0));
            for(int buttonIndex = 0; buttonIndex< ConstantManager.atk3ButtonMax; buttonIndex++){
                ((Vector) buttonStates.elementAt(joyIndex)).addElement(new Boolean(((Joystick) ((Vector) controllers.elementAt(ConstantManager.joyIndex)).elementAt(joyIndex)).getRawButton(buttonIndex + 1)));
            }
        }
        
        return buttonStates;
    }
    
    public static Vector arm(){
        armVals = new Vector(0);
        if(armInit == 0){
            mainSequence = new MotorSequence(ConstantManager.mainIntervalLength, ConstantManager.mainDownSpeed, ConstantManager.mainUpSpeed);
            wristSequence = new MotorSequence(ConstantManager.wristIntervalLength, ConstantManager.wristDownSpeed, ConstantManager.wristUpSpeed);
        }
        if(DriverStation.getInstance().isAutonomous() && DriverStation.getInstance().isEnabled()){
            armVals.addElement(new Boolean(true));
        }
        else if (DriverStation.getInstance().isOperatorControl() && DriverStation.getInstance().isEnabled()){
            armVals.addElement(new Boolean(false));
        }
        
        //armVals.addElement(new Boolean(false)); //Expand for more values! FALSE 
        if(!ConstantManager.armSequenceEnabled){
            double mainVal = -((Joystick) ((Vector) controllers.elementAt(ConstantManager.joyIndex)).elementAt(2)).getY();
            double wristVal = ((Joystick) ((Vector) controllers.elementAt(ConstantManager.joyIndex)).elementAt(2)).getRawAxis(4);
            double depVal =  ((Joystick) ((Vector) controllers.elementAt(ConstantManager.joyIndex)).elementAt(2)).getRawAxis(6);
            
            /*
            if(((Joystick) ((Vector) controllers.elementAt(ConstantManager.joyIndex)).elementAt(2)).getRawButton(ConstantManager.mainPlusButton) &! ((Joystick) ((Vector) controllers.elementAt(ConstantManager.joyIndex)).elementAt(2)).getRawButton(ConstantManager.mainMinusButton)){
                armVals.addElement(new Double(ConstantManager.mainManualSpeed));   
            }
            else if(((Joystick) ((Vector) controllers.elementAt(ConstantManager.joyIndex)).elementAt(2)).getRawButton(ConstantManager.mainMinusButton) &! ((Joystick) ((Vector) controllers.elementAt(ConstantManager.joyIndex)).elementAt(2)).getRawButton(ConstantManager.mainPlusButton)){
                armVals.addElement(new Double(-ConstantManager.mainManualSpeed));
            }
            else{
                armVals.addElement(new Double(0));
            }*/
            //double mainSequenceVal = motorSequence(armButtons("main"), ConstantManager.mainIntervalLength, ConstantManager.mainDownSpeed, ConstantManager.mainDownSpeed);
            //double wristSequenceVal = motorSequence(armButtons("wrist"), ConstantManager.wristIntervalLength, ConstantManager.wristDownSpeed, ConstantManager.wristDownSpeed);
            double mainSequenceVal = mainSequence.motorOut(armButtons("main"));
            double wristSequenceVal = wristSequence.motorOut(armButtons("wrist"));
            
            if(mainSequenceVal != 0){
                mainVal = mainSequenceVal;
            }
            else{
                if(((Joystick) ((Vector) controllers.elementAt(ConstantManager.joyIndex)).elementAt(2)).getRawButton(1) && wristSequenceVal == 0){
                    mainVal = ConstantManager.mainSimulUp;
                }
                else if(((Joystick) ((Vector) controllers.elementAt(ConstantManager.joyIndex)).elementAt(2)).getRawButton(2) && wristSequenceVal == 0){
                    mainVal = ConstantManager.mainSimulDown;
                }
                else if(((Joystick) ((Vector) controllers.elementAt(ConstantManager.joyIndex)).elementAt(2)).getRawButton(8) && wristSequenceVal == 0){
                    mainVal = ConstantManager.mainQuickRelease;
                }
                else{
                    if(mainVal >= 0){
                        mainVal = mainVal*ConstantManager.mainUpCoeff;
                    }
                    else{
                        mainVal = mainVal*ConstantManager.mainDownCoeff;
                    }
                }
            }
            
            if(wristSequenceVal != 0){
                wristVal = wristSequenceVal;
            }
            else{
               if(((Joystick) ((Vector) controllers.elementAt(ConstantManager.joyIndex)).elementAt(2)).getRawButton(1) && mainSequenceVal == 0){
                    wristVal = ConstantManager.wristSimulUp;
                }
                else if(((Joystick) ((Vector) controllers.elementAt(ConstantManager.joyIndex)).elementAt(2)).getRawButton(2) && mainSequenceVal == 0){
                    wristVal = ConstantManager.wristSimulDown;
                }
                else if(((Joystick) ((Vector) controllers.elementAt(ConstantManager.joyIndex)).elementAt(2)).getRawButton(8) && mainSequenceVal == 0){
                    wristVal = ConstantManager.wristQuickRelease;
                }
                else{
                    if(wristVal >= 0){
                        wristVal = wristVal*ConstantManager.wristDownCoeff;
                    }
                    else{
                        wristVal = wristVal*ConstantManager.wristUpCoeff;
                    }
                } 
            }
            
            if(ConstantManager.mainMasterInv){
                mainVal = -mainVal;
            }
            if(ConstantManager.wristMasterInv){
                wristVal = -wristVal;
            }
            
            if(ConstantManager.depInverted){
                depVal = -depVal;
            }
            
            if(depVal >0){
                depVal = ConstantManager.depOut;
            }
            else if(depVal <0){
                depVal = -ConstantManager.depIn;
            }
            else{
                depVal = 0;
            }
            
            armVals.addElement(new Double(mainVal));
            armVals.addElement(new Double(wristVal));
            armVals.addElement(new Double(depVal));
            /*
            if(((Joystick) ((Vector) controllers.elementAt(ConstantManager.joyIndex)).elementAt(2)).getRawButton(ConstantManager.wristPlusButton) &! ((Joystick) ((Vector) controllers.elementAt(ConstantManager.joyIndex)).elementAt(2)).getRawButton(ConstantManager.wristMinusButton)){
                armVals.addElement(new Double(ConstantManager.wristManualSpeedDown));   
            }
            else if(((Joystick) ((Vector) controllers.elementAt(ConstantManager.joyIndex)).elementAt(2)).getRawButton(ConstantManager.wristMinusButton) &! ((Joystick) ((Vector) controllers.elementAt(ConstantManager.joyIndex)).elementAt(2)).getRawButton(ConstantManager.wristPlusButton)){
                armVals.addElement(new Double(-ConstantManager.wristManualSpeedUp));
            }
            else{
                armVals.addElement(new Double(0));
            }*/
            
            //armVals.addElement(new Double(((Joystick) ((Vector) controllers.elementAt(ConstantManager.joyIndex)).elementAt(2)).getRawAxis(ConstantManager.wristAxis)));
            
            if(((Joystick) ((Vector) controllers.elementAt(ConstantManager.joyIndex)).elementAt(2)).getRawButton(ConstantManager.clawPlusButton) &! ((Joystick) ((Vector) controllers.elementAt(ConstantManager.joyIndex)).elementAt(2)).getRawButton(ConstantManager.clawMinusButton)){
                //armVals.addElement(new Double(ConstantManager.clawManualSpeed)); 
                //armVals.addElement(new Double(clawManualSequence(0)));
                armVals.addElement(new Double(1));
                lastArm = true;
            }
            else if(((Joystick) ((Vector) controllers.elementAt(ConstantManager.joyIndex)).elementAt(2)).getRawButton(ConstantManager.clawMinusButton) &! ((Joystick) ((Vector) controllers.elementAt(ConstantManager.joyIndex)).elementAt(2)).getRawButton(ConstantManager.clawPlusButton)){
                //armVals.addElement(new Double(-ConstantManager.clawManualSpeed));
                //armVals.addElement(new Double(clawManualSequence(1)));
                armVals.addElement(new Double(0));
                lastArm = false;
            }
            else{
                if(lastArm){
                    armVals.addElement(new Double(1));    
                }
                else{
                    armVals.addElement(new Double(0));    
                }
                
                //armVals.addElement(new Double(0));
                //armVals.addElement(new Double(clawManualSequence(2)));
            }
        }
        else{
            //armVals.addElement(new Double(mainSequence(armButtons("main"))));
            //armVals.addElement(new Double(wristSequence(armButtons("wrist"))));
            //armVals.addElement(new Double(clawSequence(armButtons("claw"))));
            
        }
        
        /*armVals.addElement(armChoose("main"));
        armVals.addElement(armChoose("wrist")); // top hat
        armVals.addElement(armChoose("claw"));*/
        
        return armVals;
    }
    public static int armButtons(String joint){
        int directionReq = 3; //ensures you know if value has been changed at all
        if(joint.equals("main")){
            if(((Joystick) ((Vector) controllers.elementAt(ConstantManager.joyIndex)).elementAt(2)).getRawButton(ConstantManager.sequencePlusButton) &! ((Joystick) ((Vector) controllers.elementAt(ConstantManager.joyIndex)).elementAt(2)).getRawButton(ConstantManager.sequenceMinusButton)){
                directionReq = 0;
            }
            else if(((Joystick) ((Vector) controllers.elementAt(ConstantManager.joyIndex)).elementAt(2)).getRawButton(ConstantManager.sequenceMinusButton) &! ((Joystick) ((Vector) controllers.elementAt(ConstantManager.joyIndex)).elementAt(2)).getRawButton(ConstantManager.sequencePlusButton)){
                directionReq = 1;
            }
            else{
                directionReq = 2;
            }
        }
        else if(joint.equals("wrist")){
            if(((Joystick) ((Vector) controllers.elementAt(ConstantManager.joyIndex)).elementAt(2)).getRawButton(ConstantManager.sequencePlusButton) &! ((Joystick) ((Vector) controllers.elementAt(ConstantManager.joyIndex)).elementAt(2)).getRawButton(ConstantManager.sequenceMinusButton)){
                directionReq = 0;
            }
            else if(((Joystick) ((Vector) controllers.elementAt(ConstantManager.joyIndex)).elementAt(2)).getRawButton(ConstantManager.sequenceMinusButton) &! ((Joystick) ((Vector) controllers.elementAt(ConstantManager.joyIndex)).elementAt(2)).getRawButton(ConstantManager.sequencePlusButton)){
                directionReq = 1;
            }
            else{
                directionReq = 2;
            }
        }
        else if(joint.equals("claw")){
            if(((Joystick) ((Vector) controllers.elementAt(ConstantManager.joyIndex)).elementAt(2)).getRawButton(ConstantManager.clawPlusButton) &! ((Joystick) ((Vector) controllers.elementAt(ConstantManager.joyIndex)).elementAt(2)).getRawButton(ConstantManager.clawMinusButton)){
                directionReq = 0;
            }
            else if(((Joystick) ((Vector) controllers.elementAt(ConstantManager.joyIndex)).elementAt(2)).getRawButton(ConstantManager.clawMinusButton) &! ((Joystick) ((Vector) controllers.elementAt(ConstantManager.joyIndex)).elementAt(2)).getRawButton(ConstantManager.clawPlusButton)){
                directionReq = 1;
            }
            else{
                directionReq = 2;
            }
        }
        Debug.output("driverInput.armButtons: sequence variable state", new Integer(directionReq), ConstantManager.inputDebug);
        //System.out.println(directionReq);
        return directionReq;
    }
    public static double mainSequence(int directionReq){ //+ is 0, - is 1, no further action is 2;
        System.out.println("driverInput.mainSequence: START");
        double output = 0;
        double timeElapsed = Timer.getFPGATimestamp() - mainStart;
        if(mainSt == 0 ){ //0 is idle, 1 is moving +, 2 is sequence complete, 3 is moving - (back to 0)
            System.out.println("driverInput.mainSequence: currently at pre-sequence state");
            mainQuickTo2 = false;
            mainQuickTo0 = false;
            
            if(directionReq == 0){
                mainSt = 1;
                mainStart = Timer.getFPGATimestamp();
                mainInterval = ConstantManager.mainIntervalLength;
            }
            else if(directionReq == 1){
                System.out.println("driverInput.mainSequence: cannot start sequence, already in rest position");
                
            }
            else{ //dir ==2
                System.out.println("driverInput.mainSequence: taking no further action");
                
            }
            output = 0;
        }
        else if(mainSt == 1){ //moving to 2 (+)
            System.out.println("driverInput.mainSequence: currently moving positive");
            if(directionReq == 0){ //(continue moving +)
                
            }
            else if(directionReq == 1){ //quick reverse during movement
                mainSt = 3;
                if(!mainQuickTo2){
                    mainInterval = ConstantManager.mainIntervalLength - timeElapsed;
                    mainStart = Timer.getFPGATimestamp();
                }
                else{
                    mainQuickTo2 = false;
                    mainInterval = ConstantManager.mainIntervalLength  - mainInterval + timeElapsed;
                    mainStart = Timer.getFPGATimestamp();
                }
                mainQuickTo0 = true;
            }
            
            if(timeElapsed >= mainInterval){
               mainSt = 2;
               output = 0;
               mainStart = Timer.getFPGATimestamp();
               
            }
            else{
               output = ConstantManager.mainSpeed; 
            }
        }
        else if(mainSt == 2){
            System.out.println("driverInput.mainSequence: at end of extension");
            mainQuickTo2 = false;
            mainQuickTo0 = false;
            if(directionReq == 0){ //No action, already there
                
            }
            else if(directionReq == 1){
                mainSt = 3;
                mainStart = Timer.getFPGATimestamp();
                mainInterval = ConstantManager.mainIntervalLength;
                mainQuickTo0 = false;
            }
            else{ // =3, no input (remain stopped)
                
            }
            output = 0;
        }
        else if(mainSt == 3){
            System.out.println("driverInput.mainSequence: currently moving negative");
            if(directionReq == 0){ //quick reverse during movement to 0
                mainSt = 1;
                if(!mainQuickTo0){
                    mainInterval = ConstantManager.mainIntervalLength - timeElapsed;
                    mainStart = Timer.getFPGATimestamp();
                }
                else{
                    mainStart = Timer.getFPGATimestamp();
                    mainInterval = ConstantManager.mainIntervalLength  - mainInterval + timeElapsed;
                    mainQuickTo0 = false;
                }
               mainQuickTo2 = true;
            }
            else if(directionReq == 1){
                //continue in same direction
            }
            
            if(timeElapsed >= mainInterval){
                mainSt = 0;
                output = 0;
                mainStart = Timer.getFPGATimestamp();
            }
            else{
                output = -ConstantManager.mainSpeed;
            }
            
        }
        
        if(ConstantManager.mainSeqInv){
           output = -output; 
        }
        
        return output;
        
    }
    public static double motorSequence(int directionReq, double interval, double downSpeed, double upSpeed){ //+ is 0, - is 1, no further action is 2;
        Debug.output("driverInput.motorSequence: Start. Current state", new Integer(wristSt), ConstantManager.inputDebug);
        double output = 0;
        double timeElapsed = Timer.getFPGATimestamp() - wristStart;
        if(wristSt == 0 ){ //0 is idle, 1 is moving +, 2 is sequence complete, 3 is moving - (back to 0)
            wristQuickTo2 = false;
            wristQuickTo0 = false;
            
            if(directionReq == 0){
                wristSt = 1;
                wristStart = Timer.getFPGATimestamp();
                wristInterval = interval;
                output = upSpeed;
            }
            else if(directionReq == 1){
                //System.out.println("driverInput.wristSequence: cannot start sequence, already in rest position");
                Debug.output("driverInput.motorSequence: cannot start sequence, already in rest position", null, ConstantManager.inputDebug);
                output = 0;
            }
            else{ //dir ==2
                //System.out.println("driverInput.wristSequence: taking no further action");
                Debug.output("driverInput.motorSequence: taking no further action", null, ConstantManager.inputDebug);
                output = 0;
            }
            
        }
        else if(wristSt == 1){ //moving to 2 (+)
            if(directionReq == 0){ //(continue moving +)
                output = lastOutput;
            }
            else if(directionReq == 1){ //quick reverse during movement
                wristSt = 3;
                if(!wristQuickTo2){
                    wristInterval = interval - timeElapsed;
                    wristStart = Timer.getFPGATimestamp();
                }
                else{
                    wristQuickTo2 = false;
                    wristInterval = interval  - wristInterval + timeElapsed;
                    wristStart = Timer.getFPGATimestamp();
                }
                wristQuickTo0 = true;
                wristSt = 3;
                output = -downSpeed;
            }
            
            if(timeElapsed >= wristInterval && wristSt == 1){
               wristSt = 2;
               output = 0;
               wristStart = Timer.getFPGATimestamp();
               
            }
            else{
               output = upSpeed; 
            }
        }
        else if(wristSt == 2){
            wristQuickTo2 = false;
            wristQuickTo0 = false;
            if(directionReq == 0){ //No action, already there
                output = 0;
            }
            else if(directionReq == 1){
                wristSt = 3;
                wristStart = Timer.getFPGATimestamp();
                wristInterval = interval;
                wristQuickTo0 = false;
                output = -downSpeed;
            }
            else{ // =3, no input (remain stopped)
                output = 0; 
            }
        }
        else if(wristSt == 3){
            if(directionReq == 0){ //quick reverse during movement to 0
                wristSt = 1;
                if(!wristQuickTo0){
                    wristInterval = interval - timeElapsed;
                    wristStart = Timer.getFPGATimestamp();
                }
                else{
                    wristStart = Timer.getFPGATimestamp();
                    wristInterval = interval  - wristInterval + timeElapsed;
                    wristQuickTo0 = false;
                }
               wristSt = 1;
               wristQuickTo2 = true;
               output = upSpeed;
            }
            else if(directionReq == 1){
                //continue in same direction
                output = lastOutput;
            }
            
            if(timeElapsed >= wristInterval && wristSt == 3){
                wristSt = 0;
                output = 0;
                wristStart = Timer.getFPGATimestamp();
            }
            else{
                output = -downSpeed;
            }
            
        }
        
        if(ConstantManager.wristSeqInv){
           output = -output; 
        }
        
        lastOutput = output;
        return output;
        
    }
    public static double clawSequence(int directionReq){ //+ is 0, - is 1, no further action is 2;
        System.out.println("driverInput.clawSequence: START");
        double output = 0;
        double timeElapsed = Timer.getFPGATimestamp() - clawStart;
        if(clawSt == 0 ){ //0 is idle, 1 is moving +, 2 is sequence complete, 3 is moving - (back to 0)
            clawQuickTo2 = false;
            clawQuickTo0 = false;
            
            if(directionReq == 0){
                clawSt = 1;
                clawStart = Timer.getFPGATimestamp();
                clawInterval = ConstantManager.clawIntervalLength;
            }
            else if(directionReq == 1){
                System.out.println("driverInput.clawSequence: cannot start sequence, already in rest position");
            }
            else{ //dir ==2
                System.out.println("driverInput.clawSequence: taking no further action");
            }
            output = 0;
        }
        else if(clawSt == 1){ //moving to 2 (+)
            if(directionReq == 0){ //(continue moving +)
                
            }
            else if(directionReq == 1){ //quick reverse during movement
                clawSt = 3;
                if(!clawQuickTo2){
                    clawInterval = ConstantManager.clawIntervalLength - timeElapsed;
                    clawStart = Timer.getFPGATimestamp();
                }
                else{
                    clawQuickTo2 = false;
                    clawInterval = ConstantManager.clawIntervalLength  - clawInterval + timeElapsed;
                    clawStart = Timer.getFPGATimestamp();
                }
                clawQuickTo0 = true;
            }
            
            if(timeElapsed >= clawInterval){
               clawSt = 2;
               output = 0;
               clawStart = Timer.getFPGATimestamp();
               
            }
            else{
               output = ConstantManager.clawSpeed; 
            }
        }
        else if(clawSt == 2){
            clawQuickTo2 = false;
            clawQuickTo0 = false;
            if(directionReq == 0){ //No action, already there

            }
            else if(directionReq == 1){
                clawSt = 3;
                clawStart = Timer.getFPGATimestamp();
                clawInterval = ConstantManager.clawIntervalLength;
                clawQuickTo0 = false;
            }
            else{ // =3, no input (reclaw stopped)

            }
            output = 0;
        }
        else if(clawSt == 3){
            if(directionReq == 0){ //quick reverse during movement to 0
                clawSt = 1;
                if(!clawQuickTo0){
                    clawInterval = ConstantManager.clawIntervalLength - timeElapsed;
                    clawStart = Timer.getFPGATimestamp();
                }
                else{
                    clawStart = Timer.getFPGATimestamp();
                    clawInterval = ConstantManager.clawIntervalLength  - clawInterval + timeElapsed;
                    clawQuickTo0 = false;
                }
               clawQuickTo2 = true;
            }
            else if(directionReq == 1){
                //continue in same direction
            }
            
            if(timeElapsed >= clawInterval){
                clawSt = 0;
                output = 0;
                clawStart = Timer.getFPGATimestamp();
            }
            else{
                output = -ConstantManager.clawSpeed;
            }
            
        }
        
        if(ConstantManager.clawSeqInv){
           output = -output; 
        }
        
        return output;
        
    }
    public static double clawManualSequence(int buttonSt){ //0 = +, 1 = -, 2 = no direction change
        double output = 0;
        System.out.println("driverInput.clawManualSequence: START");
        double timeElapsed = Timer.getFPGATimestamp() - clawStart;
        if(clawSt == 0){ // 0 is open beginning, 1 is moving + to close, 2 is closed, 3 is moving - to close
            System.out.println("driverInput.clawManualSequence: clawSt = 0");
            if(buttonSt == 0){
                System.out.println("driverInput.clawManualSequence: buttonSt = 0");
                clawStart = Timer.getFPGATimestamp();
                System.out.println("driverInput.clawManualSequence: clawStart set to:");
                System.out.println(clawStart = Timer.getFPGATimestamp());
                System.out.println("driverInput.clawManualSequence: clawSt = 0");
                clawSt = 1;
                clawInterval = ConstantManager.clawIntervalLength;
                output = ConstantManager.clawSpeed;
            }
            else{
                output = 0;
            }
        }
        if(clawSt == 1){
            if(timeElapsed >= clawInterval){
                output = 0;
                clawSt = 2;
                clawStart = Timer.getFPGATimestamp();
            }
            else{
                output = ConstantManager.clawSpeed;
            }
        }
        if(clawSt == 2){
            if(buttonSt == 1){
                clawSt = 3;
                clawStart = Timer.getFPGATimestamp();
                clawInterval = ConstantManager.clawIntervalLength;
                output = -ConstantManager.clawSpeed;
            }
            else{
                output = 0;
            }
        }
        if(clawSt == 3){
            if(timeElapsed >= clawInterval){
                output = 0;
                clawSt = 0;
            }
            else{
                output = -ConstantManager.clawSpeed;
            }
        }
        
        if(ConstantManager.clawSeqInv){
            output = -output;
        }
        
        return output;
    }
    /*
   public static Object armChoose(String type){
        if(type.equals("main")){
           output = 
        }
        
        //if(STOP CONDITION)
        if(ConstantManager.armInverted){
            //output = -output;
        }
        //if()
        
        return output;
    }*/   
    public static Vector info(){ //create info generator
        return new Vector(0);
    }
    private static double driveChoose(double output){ //handles all adjustments
        boolean positive = true;
        if(output < 0){
            positive = false;
        }
        
        if(((Boolean) ((Vector) driveButtons().elementAt(ConstantManager.joySquareSide)).elementAt(ConstantManager.joySquareButton - 1)).booleanValue()){            //Square condition
        output = output*output;
            if(!positive){
                output = -output;
            }
        }
        else if(((Boolean) ((Vector) driveButtons().elementAt(ConstantManager.joySqrtSide)).elementAt(ConstantManager.joySqrtButton - 1)).booleanValue()){
            
            
            if(positive){
                output = Math.sqrt(output);
            }
            else{
                output = Math.sqrt(-output);
                output = -output;
            }
        }
        else if(((Boolean) ((Vector) driveButtons().elementAt(ConstantManager.joySqrtSide)).elementAt(ConstantManager.joySqrtButton - 1)).booleanValue() && ((Boolean) ((Vector) driveButtons().elementAt(ConstantManager.joySquareSide)).elementAt(ConstantManager.joySquareButton - 1)).booleanValue()){
            
        }
        
        if(((Joystick) ((Vector) controllers.elementAt(ConstantManager.joyIndex)).elementAt(1)).getTrigger()){
            output = -1;
        }
        
        if(((Joystick) ((Vector) controllers.elementAt(ConstantManager.joyIndex)).elementAt(0)).getTrigger()){
            output = 1;
        }
        
        if(((Joystick) ((Vector) controllers.elementAt(ConstantManager.joyIndex)).elementAt(2)).getRawButton(8) && ConstantManager.coDriveOverride){
            output = ConstantManager.driveQuickRelease;
        }
        
        if(ConstantManager.joyInverted && output != 0){ //must come last (squarin' will occur)
            output = -output;
        }
        return output;
    }
}
