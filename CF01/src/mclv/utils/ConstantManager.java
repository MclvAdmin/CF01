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
    public static int pneuType = 4;
    public static int lineType = 5;
    public static int posType = 6;
    
    public static int driveJagFreq = 1;
    public static int armJagFreq = 1;
    public static int victorFreq = 0; // denotes no checks in monitor (nothing to check) wish i used null :P
    public static int lineFreq = 1;
    public static int posFreq = 1;
    public static boolean pwm = true; //testing this value
    public static double compCutoff = 0.3;
    public static double compDelay = 0.05; //This is the amount of time after last assigned value that the compressor will wait (so we don't have compressors switching on/off)
    
    public static double straight = 0.5;
    public static double hardTurn = 0.5;
    public static double slightTurn = 0.25;
    public static double failedJagAssign = 0;
    
    public static double jointCoeff = 0.1;
    public static double wristCoeff = 0.1;
    
    public static boolean joyInverted = true;
    public static int atk3ButtonMax = 12;
    public static int joyIndex = 0;
    public static int joySquareSide = 1; 
    public static int joySquareButton = 2; 
    public static int joySqrtSide = 1; 
    public static int joySqrtButton = 3; 
    public static boolean joyMix = true;
    public static double joyMixMargin = 2.5;
    public static double joyMixNullRange = 1;
    public static boolean armInverted = false;
    
    public static double mainIntervalLength = 0.3;
    public static double mainSpeed = 0.4;
    public static double mainManualSpeed = 1;
    public static int mainPlusButton = 1;
    public static int mainMinusButton = 2;
    public static boolean mainSeqInv = false;
    
    public static double wristIntervalLength = 0.1;
    public static double wristSpeed = 0.07;
    public static double wristManualSpeedDown = .05;
    public static double wristManualSpeedUp = 1;
    public static int wristPlusButton = 4;
    public static int wristMinusButton = 3;
    public static boolean wristSeqInv = false;
    
    public static double clawIntervalLength = 3;
    public static double clawSpeed = .75;
    public static double clawManualSpeed = 1;
    public static int clawPlusButton = 6;
    public static int clawMinusButton = 5;
    public static boolean clawSeqInv = false;
    
    public static boolean armSequenceEnabled = false;
    
    public static boolean debug = true;
    public static int debugVerbose = 2; //verbosity from 0-3, 0 is none 3 is most. Higher verb rating for messages represents higher priority
    public static int deviceDebug = 1;
    public static int compDebug = 3;
    public static int driveDebug = 2;
    public static int armDebug = 2;
    public static int hardwareDebug = 2;
    public static int inputDebug = 1;
    
    
    private static Vector typeList;
    private static int calc;
        public ConstantManager(){
            
        }
        public static int maxTypes(){
            typeList = new Vector(0);
            typeList.addElement(new Integer(driveType));
            typeList.addElement(new Integer(armType));
            typeList.addElement(new Integer(victorType));
            typeList.addElement(new Integer(pneuType));
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
            typeList.addElement(new Integer(pneuType));
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
