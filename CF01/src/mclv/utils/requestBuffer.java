package mclv.utils;
import java.util.*;
/**
 *
 * @author god
 */
public class requestBuffer {
public static Vector driveBuffer; 
public static Vector armBuffer;
public static Vector victorBuffer;
public static Vector combBuffer;
public static boolean driveFlag = false; //tells whether there is new info
public static boolean armFlag = false;
public static boolean victorFlag = false;
public static boolean combFlag = false;
private static int addRun = 0;

    public static void init(){
        driveBuffer = new Vector(0);
        armBuffer = new Vector(0);
        victorBuffer = new Vector(0);
        combBuffer = new Vector(0);
    }
    public static void add(Vector outputRequest){ //last element is the type
        if(addRun == 0){
            init();
        }
        
        if(((Integer) outputRequest.lastElement()).intValue() == ConstantManager.driveType){
            driveAdd(outputRequest);
        }
        else if(((Integer) outputRequest.lastElement()).intValue() == ConstantManager.armType){
            armAdd(outputRequest);
        }
        else if(((Integer) outputRequest.lastElement()).intValue() == ConstantManager.victorType){
            victorAdd(outputRequest);
        }
        else{
            System.out.print("Buffer cannot add, invalid type");
        }
        
        amalgamate();
        addRun++;
    }
    public static void amalgamate(){
        if(addRun == 0){
            init();
        }
        combBuffer.removeAllElements(); //This is a problem, not useful at all....
        combBuffer.elementAt(0).equals(driveBuffer);
        combBuffer.elementAt(1).equals(armBuffer);
        combBuffer.elementAt(2).equals(victorBuffer);
    }
    public static void clean(){
        if(addRun == 0){
            init();
        }
        driveBuffer.removeAllElements();
        armBuffer.removeAllElements();
        victorBuffer.removeAllElements();
        combBuffer.removeAllElements();
        driveFlag = false;
        armFlag = false;
        victorFlag = false;
        combFlag = false;
    }
    private static void driveAdd(Vector outputRequest){
        driveBuffer.removeAllElements();
        outputRequest.removeElementAt(outputRequest.size() - 1);
        for(int i = 0; i<outputRequest.size(); i++){
            driveBuffer.addElement(outputRequest.elementAt(i));
        }
        driveFlag = true;
    }
    private static void armAdd(Vector outputRequest){
        armBuffer.removeAllElements();
        outputRequest.removeElementAt(outputRequest.size() - 1);
        for(int i = 0; i<outputRequest.size(); i++){
            armBuffer.addElement(outputRequest.elementAt(i));
        }
        armFlag = true;
    }
    private static void victorAdd(Vector outputRequest){
        victorBuffer.removeAllElements();
        outputRequest.removeElementAt(outputRequest.size() - 1);
        for(int i = 0; i<outputRequest.size(); i++){
            victorBuffer.addElement(outputRequest.elementAt(i));
        }
        victorFlag = true;
    }
    public static Vector driveBufferUse(){
        driveFlag = false;
        return driveBuffer;
    }
    public static Vector armBufferUse(){
        armFlag = false;
        return armBuffer;
    }
    public static Vector victorBufferUse(){
        victorFlag = false;
        return victorBuffer;
    }
}
