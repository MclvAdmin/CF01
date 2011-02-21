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
    public static boolean pwm = true;
    
    public static double straight = 0.5;
    public static double hardTurn = 0.5;
    public static double slightTurn = 0.25;
    
    private static Vector maxList;
    private static int calc;
        public ConstantManager(){
            
        }
        public static int maxTypes(){
            maxList = new Vector(0);
            maxList.addElement(new Integer(driveType));
            maxList.addElement(new Integer(armType));
            maxList.addElement(new Integer(victorType));
            maxList.addElement(new Integer(lineType));
            maxList.addElement(new Integer(posType));
            
            for(int i = 0; i<maxList.size() -1; i++){
                calc = Math.max(((Integer) maxList.elementAt(i)).intValue(), ((Integer) maxList.elementAt(i + 1)).intValue());
            }
            return calc;
        }
        public static int minTypes(){
            maxList = new Vector(0);
            maxList.addElement(new Integer(driveType));
            maxList.addElement(new Integer(armType));
            maxList.addElement(new Integer(victorType));
            maxList.addElement(new Integer(lineType));
            maxList.addElement(new Integer(posType));
            
            for(int i = 0; i<maxList.size() -1; i++){
                calc = Math.min(((Integer) maxList.elementAt(i)).intValue(), ((Integer) maxList.elementAt(i + 1)).intValue());
            }
            return calc;
        }
}
