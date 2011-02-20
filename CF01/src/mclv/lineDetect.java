/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mclv;
import java.util.*;
/**
 *
 * @author god
 */
public class lineDetect {
    public static int lineDetectRun = 0;
    private static Integer lineSt;
    private static Vector lineData;
    private static boolean leftSense;
    private static boolean midSense;
    private static boolean rightSense;
    public lineDetect(){}
    public void acquire(){
        if(lineDetectRun == 0){
            
        }
    lineDetectRun++;
    }
    private void feed(Vector sensorStates){
        if(((Boolean) sensorStates.elementAt(0)) == null || ((Boolean) sensorStates.elementAt(1)) == (null) || ((Boolean) sensorStates.elementAt(2)) == (null)){
            System.out.println("error retrieving line sensor states");      
        }
        
    }
    private void analyze(){
        
    }
}
