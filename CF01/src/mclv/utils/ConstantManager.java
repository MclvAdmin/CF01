/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mclv.utils;
import java.util.*;

/**
 *
 * @author god
 */
public class ConstantManager {
    public static int driveType = 1;
    public static int armType = 2;
    public static int victorType = 3;
    public static int lineType = 4;
    public static int posType = 5;
    
    public static int driveJagFreq = 1;
    public static int armJagFreq = 1;
    public static int victorFreq = 0; // denotes no checks in monitor (nothing to check) wish i used null :P
    public static int lineFreq = 1;
    public static int posFreq = 1;
    public static boolean pwm = true; //testing this value
    
    public static double straight = 0.5;
    public static double hardTurn = 0.5;
    public static double slightTurn = 0.25;
    public static double failedJagAssign = 0;
    
    public static double jointCoeff = 0.1;
    public static double wristCoeff = 0.1;
    
    public static boolean joyInverted = true;
    
    private static Vector typeList;
    private static int calc;
        public ConstantManager(){
            
        }
        public static int maxTypes(){
            typeList = new Vector(0);
            typeList.addElement(new Integer(driveType));
            typeList.addElement(new Integer(armType));
            typeList.addElement(new Integer(victorType));
            typeList.addElement(new Integer(lineType));
            typeList.addElement(new Integer(posType));
            
            for(int i = 0; i<typeList.size()-1; i++){
                if(i==0){
                calc = Math.max(((Integer) typeList.elementAt(i)).intValue(), ((Integer) typeList.elementAt(i + 1)).intValue());
                }
                else{
                calc = Math.max(calc, ((Integer) typeList.elementAt(i + 1)).intValue());
                }
            }
            return calc;
        }
        public static int minTypes(){
            typeList = new Vector(0);
            typeList.addElement(new Integer(driveType));
            typeList.addElement(new Integer(armType));
            typeList.addElement(new Integer(victorType));
            typeList.addElement(new Integer(lineType));
            typeList.addElement(new Integer(posType));
            
            for(int i = 0; i<typeList.size()-1; i++){
                if(i==0){
                calc = Math.min(((Integer) typeList.elementAt(i)).intValue(), ((Integer) typeList.elementAt(i + 1)).intValue());
                }
                else{
                calc = Math.min(calc, ((Integer) typeList.elementAt(i + 1)).intValue());
                }
            }
            return calc;
        }
}
