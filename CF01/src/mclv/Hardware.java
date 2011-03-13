/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mclv;
import mclv.device.*;
import mclv.utils.*;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import java.util.*;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Compressor;
//import edu.wpi.first.wpilibj.RobotDrive;
//import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Watchdog;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Relay.Value;
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
    private static Vector hardwareHistoric;
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
    private static int init = 0;
    private static int assignInit = 0;
    
    public static final int driveJagFreq = ConstantManager.driveJagFreq; //for now, in future will check straight from constant manager
    public static final int armJagFreq = ConstantManager.armJagFreq;
    public static final int victorFreq = ConstantManager.victorFreq;
    public static final int lineFreq = ConstantManager.lineFreq;
    public static final int posFreq = ConstantManager.posFreq;
 
    public static final int driveType = ConstantManager.driveType;
    public static final int armType = ConstantManager.armType;
    public static final int victorType = ConstantManager.victorType;
    public static final int lineType = ConstantManager.lineType;
    public static final int posType = ConstantManager.posType;
    
    private static int typeMatch;
    public static Compressor comp;
    public static double globalStart;
    public static double lastOn;
    
    public static DigitalInput lineLeft;
    public static DigitalInput lineMid;
    public static DigitalInput lineRight;
   
    public static boolean compOn = true; //Should the compressor be on as required by the 
    //Sensor vals here
   
    // Make visualization of vectors
    
    public Hardware(){}
    
    public static void init(Vector hardwareAssign, String mode){
            assignment = new Vector(ConstantManager.maxTypes() - ConstantManager.minTypes() + 1); // yup; drivetype is the min value
            assignInit++;
        if(init == 0){
            comp = new Compressor(9,1); //Compressor(int pressureSwitchChannel, int compressorRelayChannel) OR Compressor(int pressureSwitchSlot, int pressureSwitchChannel, int compresssorRelaySlot, int compressorRelayChannel)
            hardware = new Vector(0);
            hardwareHistoric = new Vector(0);
            hardwareHistoric.addElement(new Integer(init));
            globalStart = Timer.getFPGATimestamp();
            lastOn = globalStart;
            comp.start();
            lineLeft = new DigitalInput(1);
            lineMid = new DigitalInput(2);
            lineRight = new DigitalInput(3);
        }
        
        if(mode.equals("fresh")){
            Debug.output("Hardware.init: printing pertinant constants and initializing", null, ConstantManager.hardwareDebug);
            hardware = new Vector(0); //may interact poorly with other threads, lock
            while(hardware.size() < (ConstantManager.maxTypes() - ConstantManager.minTypes() + 1)){
                hardware.addElement(new Vector(0));
            }
            
            for(int i = 0; i<hardwareAssign.size(); i++){ //Type of hardware ... should be -1?
                /*if(hardware.size() != ConstantManager.maxTypes() - ConstantManager.minTypes() + 1){ //+1 since the bottom index is 0
                System.out.println("Hardware.init: adding new vector to hardware element at index:");
                System.out.println(i);
                ((Vector) hardware.elementAt(i)).addElement(new Vector(0));
                System.out.println("Hardware.init: will continue through index = ");
                System.out.println(hardwareAssign.size() -1);
                }*/
                while(((Vector) hardware.elementAt(i)).size() < (((Vector) hardwareAssign.elementAt(i)).size() -1)){ // -1 for tag!
                        //System.out.println("Hardware.init: expanding harware system element at index:");
                        Debug.output("Hardware.init: expanding harware system element at index", new Integer(i), ConstantManager.hardwareDebug);
                        ((Vector) hardware.elementAt(i)).addElement(new Vector(0)); // system vector
                    }
                Debug.output("Hardware.init: final hardware size", new Integer(((Vector) hardwareAssign.elementAt(i)).size()), ConstantManager.hardwareDebug);
                
                for(int j = 0; j<((Vector) hardwareAssign.elementAt(i)).size() -1; j++){//-1 for last tag value.... Systems of type

                    //System.out.println("Hardware.init: Elements at current index");
                    Debug.output("Hardware.init: Elements at current index", ((Vector) hardwareAssign.elementAt(i)).elementAt(j), ConstantManager.hardwareDebug);
                    //System.out.println(((Integer) ((Vector) hardwareAssign.elementAt(i)).elementAt(j)).intValue());
                    
                    for(int h = 0; h<((Integer) ((Vector) hardwareAssign.elementAt(i)).elementAt(j)).intValue(); h++) //
                        if(((Integer) ((Vector) hardwareAssign.elementAt(i)).lastElement()).intValue() == ConstantManager.driveType){
                            Debug.output("Hardware.init: asking for drive assignment", null, ConstantManager.hardwareDebug);
                            ((Vector) ((Vector) hardware.elementAt(driveType -ConstantManager.minTypes())).elementAt(j)).addElement(DeviceSelect.selectInit(ConstantManager.driveType)); //get debug info from DeviceSelect
                        }
                        else if(((Integer) ((Vector) hardwareAssign.elementAt(i)).lastElement()).intValue() == ConstantManager.armType){
                            Debug.output("Hardware.init: asking for arm assignment", null, ConstantManager.hardwareDebug);
                            //((Vector) hardware.elementAt(armType -ConstantManager.minTypes())).addElement(DeviceSelect.selectInit(ConstantManager.armType));
                            ((Vector) ((Vector) hardware.elementAt(armType -ConstantManager.minTypes())).elementAt(j)).addElement(DeviceSelect.selectInit(ConstantManager.armType));
                        }
                        else if(((Integer) ((Vector) hardwareAssign.elementAt(i)).lastElement()).intValue() == ConstantManager.victorType){
                            //System.out.println("Init asking for victor assignment");
                            ((Vector) hardware.elementAt(victorType -ConstantManager.minTypes())).addElement(DeviceSelect.selectInit(ConstantManager.victorType));
                        }
                        else if(((Integer) ((Vector) hardwareAssign.elementAt(i)).lastElement()).intValue() == ConstantManager.pneuType){
                            Debug.output("Hardware.Init asking for pneumatics assignment", null, ConstantManager.hardwareDebug);
                            ((Vector) ((Vector) hardware.elementAt(ConstantManager.pneuType -ConstantManager.minTypes())).elementAt(j)).addElement(DeviceSelect.selectInit(ConstantManager.pneuType));
                        }
                        else if(((Integer) ((Vector) hardwareAssign.elementAt(i)).lastElement()).intValue() == ConstantManager.lineType){
                            Debug.output("Hardware.Init asking for line assignment", null, ConstantManager.hardwareDebug);
                            ((Vector) hardware.elementAt(lineType -ConstantManager.minTypes())).addElement(DeviceSelect.selectInit(ConstantManager.lineType));
                        }
                        else if(((Integer) ((Vector) hardwareAssign.elementAt(i)).lastElement()).intValue() == ConstantManager.posType){
                            System.out.println("Init asking for position sensor assignment");
                            ((Vector) hardware.elementAt(posType -ConstantManager.minTypes())).addElement(DeviceSelect.selectInit(ConstantManager.posType));
                        }
                }
            }
                Debug.output("Hardware.init: final report", null, ConstantManager.hardwareDebug);
                Debug.output("Hardware.init: run", new Integer(init), ConstantManager.hardwareDebug);
                Debug.output("Hardware.init: mode", mode, ConstantManager.hardwareDebug);
                Debug.output("Hardware.init: final hardware vector", hardware, ConstantManager.hardwareDebug);
                /*
                System.out.println("Hardware.init: hardware types:");
                System.out.println(hardware.size());
                System.out.println("Hardware.init: drive systems:");
                System.out.println(((Vector) hardware.elementAt(0)).size());
                System.out.println("Hardware.init: drive system 1 size:");
                System.out.println(((Vector) ((Vector) hardware.elementAt(0)).elementAt(0)).size());
                System.out.println("Hardware.init: drive system 2 size:");
                System.out.println(((Vector) ((Vector) hardware.elementAt(0)).elementAt(1)).size());
                */
                
                hardwareAssign.addElement(new Integer(init));
                hardwareHistoric = hardwareAssign;
                hardwareAssign.removeElementAt(hardwareAssign.size() -1);

            }
          else if(mode.equals("reinit")){
                hardware = new Vector(ConstantManager.maxTypes()); //may interact poorly with other threads, lock
                if(init > ((Integer) hardwareHistoric.lastElement()).intValue() && init != 0){ //Make the assings the historic values. works for last assigned not all assigned
                    hardwareAssign = hardwareHistoric;
                    hardwareAssign.removeElementAt(hardwareAssign.size() -1);
                }
                else{
                    System.out.println("attempting to reinit hardware illegally");
                }
                //Reinit devices
                CANJag.reInit();
                DigIn.reInit();
                AnalogIn.reInit();
                VictorMclv.reInit();
                for(int i = 0; i<hardwareAssign.size(); i++){
                    hardware.setElementAt(new Vector(0), i);
                    for(int j = 0; j<((Vector) hardwareAssign.elementAt(i)).size() -1; j++){//-1 for last tag value
                        for(int h = 0; h<((Integer) ((Vector) hardwareAssign.elementAt(i)).elementAt(j)).intValue(); h++)
                        if(((Integer) ((Vector) hardwareAssign.elementAt(i)).lastElement()).intValue() == driveType){
                            ((Vector) hardware.elementAt(driveType -ConstantManager.minTypes())).addElement(DeviceSelect.selectInit(ConstantManager.driveType));
                        }
                        else if(((Integer) ((Vector) hardwareAssign.elementAt(i)).lastElement()).intValue() == armType){
                            ((Vector) hardware.elementAt(armType -ConstantManager.minTypes())).addElement(DeviceSelect.selectInit(ConstantManager.armType));    
                        }
                        else if(((Integer) ((Vector) hardwareAssign.elementAt(i)).lastElement()).intValue() == victorType){
                            ((Vector) hardware.elementAt(victorType -ConstantManager.minTypes())).addElement(DeviceSelect.selectInit(ConstantManager.victorType));
                        }
                        else if(((Integer) ((Vector) hardwareAssign.elementAt(i)).lastElement()).intValue() == lineType){
                            ((Vector) hardware.elementAt(lineType -ConstantManager.minTypes())).addElement(DeviceSelect.selectInit(ConstantManager.lineType));
                        }
                        else if(((Integer) ((Vector) hardwareAssign.elementAt(i)).lastElement()).intValue() == posType){
                            ((Vector) hardware.elementAt(posType -ConstantManager.minTypes())).addElement(DeviceSelect.selectInit(ConstantManager.posType));
                        }
                    }
                }
            }
        for(int i = 0; i<assignment.size(); i++){
            
        }
        init++;
    }
    public static void assign(Vector hardwareAssign){ //needs safety functionality from monitor (maybe a request goes in first that is either confirmed or denied). Intermediate method? Also, needs driver flag
        if(init == 0){
            Debug.output("Hardware.assign: run init first. no action taken.", null, ConstantManager.hardwareDebug);       
        }
        else if(hardwareAssign.lastElement().getClass() == (new Vector(0)).getClass()){ //that should do it!
            Debug.output("Hardware.assign: recognized multiple type assignment request, sending each individually to assignSingleType", null, ConstantManager.hardwareDebug);
            if(hardware.size() > hardwareAssign.size()){
                Debug.output("Hardware.assign: NOTE: full assignment set not given", null, ConstantManager.hardwareDebug);//create method for assignable types vs. unassignable
            }
            for(int hardIndex = 0; hardIndex<Math.min(hardware.size(), hardwareAssign.size()); hardIndex++){
                if(((Vector) hardwareAssign.elementAt(hardIndex)).lastElement().getClass() == (new Integer(0)).getClass()){
                    Debug.output("Hardware.assign: sending assignments to assignSingleType for type", new Integer(hardIndex + ConstantManager.minTypes()), ConstantManager.hardwareDebug);
                    Hardware.assignSingleType(((Vector) hardwareAssign.elementAt(hardIndex)));
                }
                else{
                    Debug.output("Hardware.assign: Error: incorrect assignment format; no further action taken on type:", new Integer(hardIndex + ConstantManager.minTypes()), ConstantManager.hardwareDebug);
                }
                
            }
        }
        else if((hardwareAssign.lastElement().getClass() == (new Integer(0)).getClass())){
            Debug.output("Hardware.assign: recognized single type assignment parameter, sending to assignSingleType", null, ConstantManager.hardwareDebug);
            Hardware.assignSingleType(hardwareAssign);
        }
    }
    public static void assignSingleType(Vector hardwareTypeAssign){
        Vector hardwareHolder = new Vector(0);
        for(int hardIndex = 0; hardwareHolder.size()<hardwareTypeAssign.size(); hardIndex++){
           hardwareHolder.addElement(hardwareTypeAssign.elementAt(hardIndex));
        }
        hardwareTypeAssign.removeElementAt(hardwareTypeAssign.size() - 1); //removes last element which is a tag value anyway
        assignSingleTypeByType(hardwareTypeAssign,((Integer) hardwareHolder.lastElement()).intValue());
    }
    
    public static void assignSingleTypeByType(Vector hardwareTypeAssign, int type){
        for(int typeSystem = 0; typeSystem<hardwareTypeAssign.size(); typeSystem++){
            for(int typeSystemMember = 0; typeSystemMember<((Vector) hardwareTypeAssign.elementAt(typeSystem)).size(); typeSystemMember++){
             decideAssign(type, typeSystem, typeSystemMember, ((Double)  ((Vector) hardwareTypeAssign.elementAt(typeSystem)).elementAt(typeSystemMember)).doubleValue());  
            }
        }
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
    public static void driveAssign(Vector driveRequest, Vector driveJaguarStatus) throws CANTimeoutException{
        System.out.println("starting drive assignment");
        System.out.println(((Double) ((Vector) driveRequest.elementAt(0)).elementAt(1)).doubleValue());
        System.out.println(((Double) ((Vector) driveRequest.elementAt(1)).elementAt(1)).doubleValue());
        System.out.println(((Integer) ((Vector) driveJaguarStatus.elementAt(0)).elementAt(0)).intValue());
        driveSize = true;
        if(requestBuffer.driveFlag == true){
            System.out.println("Using requestBuffer");
            driveRequest = requestBuffer.driveBufferUse();
        }
        /*
        for(int i =0; i<Math.max(((Vector) hardware.elementAt(ConstantManager.driveType - ConstantManager.minTypes())).size(), driveRequest.size()); i++){
            System.out.println("driveAssign index:");
            System.out.println(i);
            if(((Vector) driveRequest.elementAt(i)).size() != ((Vector) ((Vector) hardware.elementAt(ConstantManager.driveType - ConstantManager.minTypes())).elementAt(i)).size()){
                driveSize = false;
            }
            if(driveRequest.size()!=((Vector) hardware.elementAt(ConstantManager.driveType - ConstantManager.minTypes())).size()){
                driveSize = false;
            }
        }*/ //DEBUG!
        if(driveSize = false){
            System.out.println("Hardware cannot assign drive value; request inconsistent with initialized jaguars");
        }
        else if(driveSize = true){
            
            System.out.println("Starting drive assignment loop");
            System.out.println("drive hardware index: ");
            System.out.println(ConstantManager.driveType - ConstantManager.minTypes());
            System.out.println("maximum for next for variable; drivetype hardware vector size");
            System.out.println(((Vector) hardware.elementAt(ConstantManager.driveType - ConstantManager.minTypes())).size());
            
            for(int i=0; i<((Vector) hardware.elementAt(ConstantManager.driveType - ConstantManager.minTypes())).size(); i++){
                System.out.println("System Index");
                System.out.println(i);
                System.out.println("next loop max; motors in system");
                System.out.println(((Vector) ((Vector) hardware.elementAt(ConstantManager.driveType - ConstantManager.minTypes())).elementAt(i)).size());
                for(int c=0; c<((Vector) ((Vector) hardware.elementAt(ConstantManager.driveType - ConstantManager.minTypes())).elementAt(i)).size() -1; c++){
                    System.out.println("Jaguar Index");
                    System.out.println(c);
                    if(((Vector) driveRequest.elementAt(i)).elementAt(c) != null && ((Integer)((Vector) driveJaguarStatus.elementAt(i)).elementAt(c)).intValue() != 0){
                       //((CANJag) ((Vector) ((Vector) ((Vector) hardware.elementAt(ConstantManager.driveType - ConstantManager.minTypes())).elementAt(i)).elementAt(c)).elementAt(0)).assign(((Double) ((Vector) driveRequest.elementAt(i)).elementAt(c)).doubleValue());
                       System.out.println("Hardware.driveAssign: drive assignments valid and jaguar status good. Assigning argument values");
                        decideAssign(ConstantManager.driveType, i, c,((Double) ((Vector) driveRequest.elementAt(i)).elementAt(c)).doubleValue());
                       //System.out.println(((Double) ((Vector) driveRequest.elementAt(i)).elementAt(c)).doubleValue());
                    }
                    else if(((Integer)((Vector) driveJaguarStatus.elementAt(i)).elementAt(c)).intValue() != 0){ //If the value is null, leave jaguars alone
                        System.out.println("jaguars left alone");
                    }
                    else{ //If it is unsafe to assign (STATUS = 0)
                        System.out.println("assigning to failed drive jaguar and counterparts:");
                        for(int j=0; j<((Vector) hardware.elementAt(ConstantManager.driveType - ConstantManager.minTypes())).size(); j++){
                            if(c <((Vector) ((Vector) hardware.elementAt(ConstantManager.driveType - ConstantManager.minTypes())).elementAt(j)).size()){ //Make sure this jaguar exists for all systems before assigning a value
                            //((CANJag) ((Vector) ((Vector) ((Vector) hardware.elementAt(ConstantManager.driveType - ConstantManager.minTypes())).elementAt(j)).elementAt(c)).elementAt(0)).assign(0); //I assume this lets motor spin freely, will probably bring entire system down via overheat
                            decideAssign(ConstantManager.driveType, j, c, ConstantManager.failedJagAssign);
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
    public static void armAssign(Vector armRequest, Vector armJaguarStatus) throws CANTimeoutException{
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
    /*public static Vector hardwareReport(){
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
    for(int i = 0; i<victors.size(); i++){                add sensor loop
        ((Vector) hardware.elementAt(0)).addElement(new Integer(((Vector) victors.elementAt(i)).size()));
    }      
    

        
        return hardware;
    }
    */
    public static Vector assignmentByType(int type){ //ensure it doesnt reach the second return if first occurs
        return(((Vector) assignment.elementAt(type - ConstantManager.minTypes())));
    }
    private static void decideAssign(int type, int system, int memberId, double output){ //decide which type to cast the jaguar as; pwm or CANJag?
        if(type == ConstantManager.driveType || type == ConstantManager.armType){
            if(ConstantManager.pwm){ //if pwm is true
                Debug.output("Hardware.decideAssign: assigning values to Pwm", new Double(output), ConstantManager.hardwareDebug);
                ((Pwm) ((Vector) ((Vector) ((Vector) hardware.elementAt(type - ConstantManager.minTypes())).elementAt(system)).elementAt(memberId)).elementAt(0)).assign(output);
            }
            else{ //assign to the proper jag
                Debug.output("Hardware.decideAssign: assigning values to CANJag", new Double(output), ConstantManager.hardwareDebug);
                ((CANJag) ((Vector) ((Vector) ((Vector) hardware.elementAt(type - ConstantManager.minTypes())).elementAt(system)).elementAt(memberId)).elementAt(0)).assign(output);
            }              
        }
        else if(type == ConstantManager.victorType){
            Debug.output("Hardware.decideAssign: assigning values to VictorMclv", new Double(output), ConstantManager.hardwareDebug);
            ((VictorMclv) ((Vector) ((Vector) ((Vector) hardware.elementAt(type - ConstantManager.minTypes())).elementAt(system)).elementAt(memberId)).elementAt(0)).assign(output);
        }
        else if(type == ConstantManager.pneuType){
            Debug.output("Hardware.decideAssign: assigning values to SolenoidMclv", new Double(output), ConstantManager.hardwareDebug);
            if(output == 1){
               ((SolenoidMclv) ((Vector) ((Vector) ((Vector) hardware.elementAt(type - ConstantManager.minTypes())).elementAt(system)).elementAt(memberId)).elementAt(0)).assign(true);  
            }
            else if(output == 0){
               ((SolenoidMclv) ((Vector) ((Vector) ((Vector) hardware.elementAt(type - ConstantManager.minTypes())).elementAt(system)).elementAt(memberId)).elementAt(0)).assign(false); 
            }
            else{
                Debug.output("Hardware.decideAssign: invalid value to SolenoidMclv", new Double(output), 3);
            }
            
        }
        else if(type == ConstantManager.lineType){
            Debug.output("Hardware.decideAssign: cannot assign to lineType", null, ConstantManager.hardwareDebug);
        }
        else if(type == ConstantManager.posType){
            Debug.output("Hardware.decideAssign: cannot assign to posType", null, ConstantManager.hardwareDebug);
        } 
        
        
    }
    
}
