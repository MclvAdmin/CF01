/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mclv;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import java.util.*;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
//import edu.wpi.first.wpilibj.RobotDrive;
//import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Watchdog;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.DigitalInput;
/**
 *
 * @author god
 */
public class Hardware {
    private static Vector driveJaguars; //ensure default size of vector is zero
    private static Vector armJaguars;
    private static Vector victors;
    private static Vector linePs;
    private static Vector posSense;
    private static int canBusMin = 1;
    private static int digPinMin = 1;
    private static boolean driveSize;
    private static boolean extDriveReq;
    private static boolean armSize;
    private static boolean victorSize;
    
    public static Vector driveWiring;
    public static Vector armWiring;
    public static Vector victorWiring;
    public static Vector lineWiring;
    public static Vector posWiring;
    public static Vector hardware;
    public static Vector assignment;
    
    private static int driveInit = 0;
    private static int armInit = 0;
    private static int victorInit = 0;
    private static int lineInit = 0;
    private static int posInit = 0;
    private static int assignInit = 0;
    
    public static final int driveJagFreq = 1;
    public static final int driveType = 1;
    public static final int armJagFreq = 1;
    public static final int armType = 2;
    public static final int lineFreq = 1;
    public static final int lineType = 3;
    public static final int posFreq = 1;
    public static final int posType = 4;
    
    private static int typeMatch;
    //Sensor vals here
   
    // Make visualization of vectors
    
    public Hardware(Vector driveJagAssign, Vector armJagAssign, Vector victorAssign, Vector lineAssign, Vector posAssign, boolean camera) throws CANTimeoutException{
        driveJaguars = new Vector(0);
        driveWiring = new Vector(0);
        armJaguars = new Vector(0);
        armWiring = new Vector(0);
        victors = new Vector(0);
        victorWiring = new Vector(0);
        posSense = new Vector(0);
        posWiring = new Vector(0);
        linePs = new Vector(0);
        lineWiring = new Vector(0);
        
        /*if(driveJagAssign.size()>0){
            for(int i =0; i<driveJagAssign.size(); i++){
                driveJaguars.addElement(new Vector());
                driveWiring.addElement(new Vector());
                for(int c =0; c<((Integer) driveJagAssign.elementAt(i)).intValue(); c++,canBusMin++){
                    ((Vector) driveJaguars.elementAt(i)).addElement(new CANJaguar(c + canBusMin));
                    ((Vector) driveWiring.elementAt(i)).addElement(new Integer(c + canBusMin));
                }
            //canBusMin = canBusMin + ((Integer) driveJagAssign.elementAt(i)).intValue();
            }//Done initializing drive system jaguars, in unique system for special temp management and symmetrical shutdown/control processes
        }*/
        if(armJagAssign.size()>0){    
            for(int i =0; i<armJagAssign.size(); i++){
                armJaguars.addElement(new Vector(0));
                armWiring.addElement(new Vector(0));
                for(int c =0; c<((Integer) armJagAssign.elementAt(i)).intValue(); c++,canBusMin++){
                    ((Vector) armJaguars.elementAt(i)).addElement(new CANJaguar(c + canBusMin));
                    ((Vector) armWiring.elementAt(i)).addElement(new Integer(c + canBusMin));
                }
            //canBusMin = canBusMin + ((Integer) armJagAssign.elementAt(i)).intValue();
            }//Done initializing drive system jaguars, in unique system for special temp management and symmetrical shutdown/control processes
        }
        if(victorAssign.size()>0){    
            for(int i =0; i<victorAssign.size(); i++){
                victors.addElement(new Vector(0));
                victorWiring.addElement(new Vector(0));
                for(int c =0; c<((Integer) victorAssign.elementAt(i)).intValue(); c++,digPinMin++){
                    ((Vector) victors.elementAt(i)).addElement(new Victor(c + digPinMin));
                    ((Vector) victorWiring.elementAt(i)).addElement(new Integer(c + digPinMin));
                }
            //digPinMin = digPinMin + ((Integer) victorAssign.elementAt(i)).intValue();
            }
        }
        if(lineAssign.size()>0){
            for(int i =0; i<lineAssign.size(); i++){
                linePs.addElement(new Vector(0));
                lineWiring.addElement(new Vector(0));
                for(int c =0; c<((Integer) lineAssign.elementAt(i)).intValue(); c++,digPinMin++){
                    ((Vector) linePs.elementAt(i)).addElement(new DigitalInput(c + digPinMin));
                    ((Vector) lineWiring.elementAt(i)).addElement(new Integer(c + digPinMin));
                } 
            //digPinMin = digPinMin + ((Integer) lineAssign.elementAt(i)).intValue();
            }
        }
        if(posAssign.size()>0){
            for(int i =0; i<posAssign.size(); i++){
                posSense.addElement(new Vector(0));
                posWiring.addElement(new Vector(0));
                for(int c =0; c<((Vector) posAssign.elementAt(i)).size(); c++,digPinMin++){
                    if(i == 1){
                    ((Vector) posSense.elementAt(i)).addElement(new DigitalInput(c + digPinMin));
                    ((Vector) posWiring.elementAt(i)).addElement(new Integer(c + digPinMin)); 
                    }
                }
            //digPinMin = digPinMin + ((Integer) lineAssign.elementAt(i)).intValue();
            }
        }
        //ADD SENSOR HANDLING
        
        //Make error codes!
        
        /*
        this.driveWiring = driveWiring;
        this.armWiring = armWiring;
        this.victorWiring = victorWiring
         */
 
        

    }
    public static void driveInit(Vector driveAssign, String mode){ //Modes: fresh, reinit, grow (fresh is anything besides reinit or grow)
        if(assignInit == 0){
            assignment = new Vector(0);
            assignInit++;
        }
        if(driveInit == 0){
            driveJaguars = new Vector(0);
            driveWiring = new Vector(0);
        }
        if(mode.equals("reinit")){
        for(int i = 0; i<driveAssign.size(); i++){
            driveJaguars.addElement(new Vector(0));
            for(int j = 0; j<((Integer) driveAssign.elementAt(i)).intValue(); j++){
                ((Vector) driveJaguars.elementAt(i)).addElement(CANJag.init());
            }
        }
        
        if(assignment.isEmpty() &! mode.equals("reinit")){
            driveAssign.addElement(new Integer(driveType)); //specifies type, last element of driveAssign in assignment
            assignment.addElement(driveAssign);
        }
        else if(mode.equals("reinit") &! assignment.isEmpty()){
            for(int j = 0; j<assignment.size(); j++){
                if(((Integer)((Vector) assignment.elementAt(j)).lastElement()).intValue() == 1){
                    (assignment.elementAt(j)).equals(driveAssign);
                }
            }
        }
        else if(mode.equals("reinit") && assignment.isEmpty()){ //make case for reinit + existing drive assignments!
            //throw improper first init exception
            Hardware.driveInit(driveAssign, "fresh");
        }
        else{

        }
        }
        else if(mode.equals("grow")){           
            for(int i = 0; i<driveAssign.size(); i++){
                driveJaguars.addElement(new Vector(0));
                for(int j = 0; j<((Integer) driveAssign.elementAt(i)).intValue(); j++){
                    ((Vector) driveJaguars.lastElement()).addElement(CANJag.init());
                }
            }
            for(int i = 0; i<assignment.size(); i++){
                    if(((Integer) ((Vector) assignment.elementAt(i)).lastElement()).intValue() == driveType){
                        ((Vector) assignment.elementAt(i)).removeElementAt(((Vector) assignment.elementAt(i)).size()-1); //last element
                        for(int c = 0; c<driveAssign.size(); c++){
                        ((Vector) assignment.elementAt(i)).addElement(driveAssign.elementAt(c));
                        }
                        ((Vector) assignment.elementAt(i)).addElement(new Integer(driveType));
                    }
            }
        }
        else if(mode.equals("fresh")){
            if(!driveJaguars.isEmpty()){
                driveInit(driveAssign,"hardware");
            }
            if(assignment.isEmpty()){
                driveAssign.addElement(new Integer(driveType)); //specifies type, last element of driveAssign in assignment
                assignment.addElement(driveAssign);
            }
            else if(!assignment.isEmpty()){
                driveAssign.addElement(new Integer(driveType));
                for(int i = 0; i<assignment.size(); i++){
                    if(((Integer) ((Vector) assignment.elementAt(i)).lastElement()).intValue() == driveType){
                        ((Vector) assignment.elementAt(i)).removeAllElements();
                        ((Vector) assignment.elementAt(i)).addElement(driveAssign);
                    }
                }
            }
        }
        else{
            //reInit(driveAssign, new Integer(1)); //basically initializes
            driveInit(driveAssign, "reinit");
        }
        if(mode.equals("hardware")){
            System.out.println("warning: fresh assignment with risk of existing drive jaguars. Removing current drive jaguars and reinitializing");
            driveJaguars.removeAllElements();
            CANJag.reInit();
            for(int i = 0; i<driveAssign.size(); i++){
                driveJaguars.addElement(new Vector(0));
                for(int j = 0; j<((Integer) driveAssign.elementAt(i)).intValue(); j++){
                    ((Vector) driveJaguars.lastElement()).addElement(CANJag.init());
                }
            }
        }
        driveInit++;
    }
    public static void armInit(Vector armAssign, String mode){
        if(assignInit == 0){
            assignment = new Vector(0);
            assignInit++;
        }
        if(armInit == 0){
        armJaguars = new Vector(0);
        armWiring = new Vector(0);
        for(int i = 0; i<armAssign.size(); i++){
            armJaguars.addElement(new Vector(0));
            for(int j = 0; j<((Integer) armAssign.elementAt(i)).intValue(); j++){
                ((Vector) armJaguars.elementAt(i)).addElement(CANJag.init());
            }
        }
        }
        else if(mode.equals("grow")){           
        for(int i = 0; i<armAssign.size(); i++){
            armJaguars.addElement(new Vector(0));
            for(int j = 0; j<((Integer) armAssign.elementAt(i)).intValue(); j++){
                ((Vector) armJaguars.lastElement()).addElement(CANJag.init());
            }
        }
        }
        else{
            reInit(new Integer(2));
        }
        armInit++;
    }
    public static void victorInit(Vector victorAssign, String mode){
        if(assignInit == 0){
            assignment = new Vector(0);
            assignInit++;
        }
        if(victorInit == 0){
        victors = new Vector(0);
        victorWiring = new Vector(0);
        for(int i = 0; i<victorAssign.size(); i++){
            victors.addElement(new Vector(0));
            for(int j = 0; j<((Integer) victorAssign.elementAt(i)).intValue(); j++){
                ((Vector) victors.elementAt(i)).addElement(VictorMclv.init());
            }
        }
        }
        else if(mode.equals("grow")){           
        for(int i = 0; i<victorAssign.size(); i++){
            victors.addElement(new Vector(0));
            for(int j = 0; j<((Integer) victorAssign.elementAt(i)).intValue(); j++){
                ((Vector) victors.lastElement()).addElement(VictorMclv.init());
            }
        }
        }
        else{
            reInit(new Integer(3));
        }
        victorInit++;
    }
    public static void lineInit(Vector lineAssign, String mode){
        if(assignInit == 0){
            assignment = new Vector(0);
            assignInit++;
        }
        if(lineInit == 0){
        linePs = new Vector(0);
        lineWiring = new Vector(0);
        for(int i = 0; i<lineAssign.size(); i++){
            linePs.addElement(new Vector(0));
            for(int j = 0; j<((Integer) lineAssign.elementAt(i)).intValue(); j++){
                ((Vector) linePs.elementAt(i)).addElement(DigIn.init());
            }
        }
        }
        else if(mode.equals("grow")){           
        for(int i = 0; i<lineAssign.size(); i++){
            linePs.addElement(new Vector(0));
            for(int j = 0; j<((Integer) lineAssign.elementAt(i)).intValue(); j++){
                ((Vector) linePs.lastElement()).addElement(DigIn.init());
            }
        }
        }
        else{
            reInit(new Integer(4));
        }
        lineInit++;
    }
    public static void posInit(Vector posAssign, String mode){
        if(assignInit == 0){
            assignment = new Vector(0);
            assignInit++;
        }
        if(posInit == 0){
        posSense = new Vector(0);
        posWiring = new Vector(0);
        for(int i = 0; i<posAssign.size(); i++){
            posSense.addElement(new Vector(0));
            for(int j = 0; j<((Integer) posAssign.elementAt(i)).intValue(); j++){
                ((Vector) posSense.elementAt(i)).addElement(AnalogIn.init());
            }
        }
        }
        else if(mode.equals("grow")){           
        for(int i = 0; i<posAssign.size(); i++){
            posSense.addElement(new Vector(0));
            for(int j = 0; j<((Integer) posAssign.elementAt(i)).intValue(); j++){
                ((Vector) posSense.lastElement()).addElement(AnalogIn.init());
            }
        }
        }
        else{
            reInit(new Integer(5));
        }
        posInit++;
    }
    /*
    public static void reInit(Vector typeAssignVals, Integer type){
        int typeCheck = 0;
        if(assignInit == 0){
          assignment = new Vector(0);
          assignInit++;            
        }
            
        for(int i = 0; i<assignment.size(); i++){
            if(((Vector) assignment.elementAt(i)).lastElement().equals(type)){
                for(int j = 0; j<((Vector) assignment.elementAt(i)).size(); j++){
                    if(){
                    for(int k = 0; k<((Vector) ((Vector) assignment.elementAt(i)).elementAt(j)).size(); k++){
                       
                    }
                    }
                }
            }
       }

    }*/
    public void driveAssign(Vector driveRequest, Vector driveJaguarStatus) throws CANTimeoutException{
        driveSize = true;
        if(requestBuffer.driveFlag == true){
            driveRequest = requestBuffer.driveBufferUse();
        }
        for(int i =0; i<Math.max(driveJaguars.size(), driveRequest.size()); i++){
            if(((Vector) driveRequest.elementAt(i)).size() != ((Vector) driveJaguars.elementAt(i)).size()){
                driveSize = false;
            }
            if(driveRequest.size()!=driveJaguars.size()){
                driveSize = false;
            }
        }
        if(driveSize = false){
            System.out.println("Hardware cannot assign drive value; request inconsistent with initialized jaguars");
        }
        else{
            for(int i=0; i<driveJaguars.size(); i++){
                for(int c=0; c<((Vector) driveJaguars.elementAt(i)).size(); c++){
                    if(((Double) ((Vector) driveRequest.elementAt(i)).elementAt(c)) != null && ((Integer)((Vector) driveJaguarStatus.elementAt(i)).elementAt(c)).intValue() != 0){
                       ((CANJaguar) ((Vector) driveJaguars.elementAt(i)).elementAt(c)).setX(((Double) ((Vector) driveRequest.elementAt(i)).elementAt(c)).doubleValue());
                    }
                    else if(((Integer)((Vector) driveJaguarStatus.elementAt(i)).elementAt(c)).intValue() != 0){ //If the value is null, leave jaguars alone
                        
                    }
                    else{ //If it is unsafe to assign (STATUS = 0)
                        for(int j=0; j<driveJaguars.size(); j++){
                            if(c <((Vector) driveJaguars.elementAt(j)).size()){ //Make sure this jaguar exists for all systems before assigning a value
                            ((CANJaguar) ((Vector) driveJaguars.elementAt(j)).elementAt(c)).setX(0); //I assume this lets motor spin freely, will probably bring entire system down via overheat
                            }
                        }
                        
                        
                        System.out.println("Drive Jaguar failure: System");
                        System.out.print(" ");
                        System.out.print(i);
                        System.out.print(" ID ");
                        System.out.print(c);
                        System.out.print(". Compensating by disabling other drive systems with corresponding motors");
                    }
                }
            }
        }
        //else if()        
        //else if(driveRequest.elementAt(i))
    }
    public void armAssign(Vector armRequest, Vector armJaguarStatus) throws CANTimeoutException{
       armSize = true;
        for(int i =0; i<Math.max(armJaguars.size(), armRequest.size()); i++){
            if(((Vector) armRequest.elementAt(i)).size() != ((Vector) armJaguars.elementAt(i)).size()){
                armSize = false;
            }
            if(armRequest.size()!=armJaguars.size()){
                armSize = false;
            }
        }
        if(armSize = false){
            System.out.println("Hardware cannot assign arm value; request inconsistent with initialized jaguars");
        }
        else{
            for(int i=0; i<armJaguars.size(); i++){
                for(int c=0; c<((Vector) armJaguars.elementAt(i)).size(); c++){
                    if(((Double) ((Vector) armRequest.elementAt(i)).elementAt(c)) != null && ((Integer)((Vector) armJaguarStatus.elementAt(i)).elementAt(c))!= new Integer(0)){
                        ((CANJaguar) ((Vector) armJaguars.elementAt(i)).elementAt(c)).setX(((Double) ((Vector) armRequest.elementAt(i)).elementAt(c)).doubleValue());
                    }
                    else if(((Integer)((Vector) armJaguarStatus.elementAt(i)).elementAt(c))!= new Integer(0)){ //If the value is null, leave jaguars alone
                        
                    }
                    else{ //If it is unsafe to assign (STATUS = 0)
                        for(int j=0; j<armJaguars.size(); j++){
                            if(c <((Vector) armJaguars.elementAt(j)).size()){ //Make sure this jaguar exists for all systems before assigning a value
                            ((CANJaguar) ((Vector) armJaguars.elementAt(j)).elementAt(c)).setX(0); //I assume this lets motor spin freely, will probably bring entire system down via overheat
                            }
                        }
                        
                        
                        System.out.println("Arm Jaguar failure: System");
                        System.out.print(" ");
                        System.out.print(i);
                        System.out.print(" ID ");
                        System.out.print(c);
                    }
                }
            }
        }
    }
    public void victorAssign(Vector victorRequest, Vector victorStatus) throws CANTimeoutException{
        victorSize = true;
        for(int i =0; i<Math.max(victors.size(), victorRequest.size()); i++){
            if(((Vector) victorRequest.elementAt(i)).size() != ((Vector) victors.elementAt(i)).size()){
                victorSize = false;
            }
            if(victorRequest.size()!=victors.size()){
                victorSize = false;
            }
        }
        if(victorSize = false){
            System.out.println("Hardware cannot assign value; request inconsistent with initialized victors");
        }
        else{
            for(int i=0; i<victors.size(); i++){
                for(int c=0; c<((Vector) victors.elementAt(i)).size(); c++){
                    if(((Double) ((Vector) victorRequest.elementAt(i)).elementAt(c)) != null && ((Integer)((Vector) victorStatus.elementAt(i)).elementAt(c))!= new Integer(0)){
                    ((Victor) ((Vector) armJaguars.elementAt(i)).elementAt(c)).set(((Double) ((Vector) victorRequest.elementAt(i)).elementAt(c)).doubleValue());
                    }
                    else if(((Integer)((Vector) victorStatus.elementAt(i)).elementAt(c))!= new Integer(0)){ //If the value is null, leave jaguars alone
                        
                    }
                    else{ //If it is unsafe to assign (STATUS = 0)
                        for(int j=0; j<victors.size(); j++){
                            if(c <((Vector) victors.elementAt(j)).size()){ //Make sure this jaguar exists for all systems before assigning a value
                            ((CANJaguar) ((Vector) victors.elementAt(j)).elementAt(c)).setX(0); //I assume this lets motor spin freely, will probably bring entire system down via overheat
                            }
                        }
                        
                        
                        System.out.println("Victor failure: System");
                        System.out.print(" ");
                        System.out.print(i);
                        System.out.print(" ID ");
                        System.out.print(c);
                    }
                }
            }
        }
    }
    public static Vector hardwareReport(){
    for(int i = 0; i<3; i++){ // 0=drive 1=arm 2=sensors
        hardware.addElement(new Vector());
    }
    for(int i = 0; i<driveJaguars.size(); i++){
        ((Vector) hardware.elementAt(0)).addElement(new Integer(((Vector) driveJaguars.elementAt(i)).size()));
        if(i == driveJaguars.size() - 1){
            ((Vector) hardware.elementAt(1)).addElement(new Integer(driveJagFreq)); //drive jaguar freq
        }
    }
    for(int i = 0; i<armJaguars.size(); i++){
        ((Vector) hardware.elementAt(1)).addElement(new Integer(((Vector) armJaguars.elementAt(i)).size()));
        if(i == armJaguars.size() - 1){
            ((Vector) hardware.elementAt(1)).addElement(new Integer(armJagFreq)); //arm jaguar freq
        }    
    }
    for(int i = 0; i<linePs.size() + posSense.size(); i++){
        ((Vector) hardware.elementAt(1)).addElement(new Integer(((Vector) armJaguars.elementAt(i)).size()));
        if(i == armJaguars.size() - 1){
            ((Vector) hardware.elementAt(1)).addElement(new Integer(armJagFreq)); //arm jaguar freq
        }    
    }
    /*for(int i = 0; i<victors.size(); i++){                add sensor loop
        ((Vector) hardware.elementAt(0)).addElement(new Integer(((Vector) victors.elementAt(i)).size()));
    }      
    */

        
        return hardware;
    }
    
    public static Vector assignmentByType(int type){ //ensure it doesnt reach the second return if first occurs
        for(int i = 0; i<assignment.size(); i++){
            if(((Integer) ((Vector) assignment.elementAt(i)).lastElement()).intValue() == type){
            return (Vector) assignment.elementAt(i);
            }   
        }
        return(new Vector(0));
    }
    

    /*
    public Vector driveWiring(){
        return driveWiring;
    }
    public Vector armWiring(){
        return armWiring;
    }
    public Vector victorWiring(){
        return victorWiring;
    }
    */
    
}
