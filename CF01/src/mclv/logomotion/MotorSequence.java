/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mclv.logomotion;
import edu.wpi.first.wpilibj.Timer;
import mclv.utils.*;
import mclv.Debug;

/**
 *
 * @author god
 */
public class MotorSequence {
    public double interval;
    public double sequenceInterval;
    public double downSpeed;
    public double upSpeed;
    public int sequenceSt;
    public boolean sequenceQuickTo2;
    public boolean sequenceQuickTo0;
    public double sequenceStart;
    public double lastOutput;
    public MotorSequence(double interval, double downSpeed, double upSpeed){
    this.downSpeed = downSpeed;
    this.upSpeed = upSpeed;
    this.sequenceInterval = interval;
    sequenceStart = Timer.getFPGATimestamp();
    sequenceSt = 0;
    sequenceQuickTo2 = false;
    sequenceQuickTo0 = false;
    }
    public double motorOut(int directionReq){
        Debug.output("driverInput.motorSequence: Start. Current state", new Integer(sequenceSt), ConstantManager.inputDebug);
        double output = 0;
        double timeElapsed = Timer.getFPGATimestamp() - sequenceStart;
        if(sequenceSt == 0 ){ //0 is idle, 1 is moving +, 2 is sequence complete, 3 is moving - (back to 0)
            sequenceQuickTo2 = false;
            sequenceQuickTo0 = false;
            
            if(directionReq == 0){
                sequenceSt = 1;
                sequenceStart = Timer.getFPGATimestamp();
                sequenceInterval = interval;
                output = upSpeed;
            }
            else if(directionReq == 1){
                //System.out.println("driverInput.sequenceSequence: cannot start sequence, already in rest position");
                Debug.output("driverInput.motorSequence: cannot start sequence, already in rest position", null, ConstantManager.inputDebug);
                output = 0;
            }
            else{ //dir ==2
                //System.out.println("driverInput.sequenceSequence: taking no further action");
                Debug.output("driverInput.motorSequence: taking no further action", null, ConstantManager.inputDebug);
                output = 0;
            }
            
        }
        else if(sequenceSt == 1){ //moving to 2 (+)
            if(directionReq == 0){ //(continue moving +)
                output = lastOutput;
            }
            else if(directionReq == 1){ //quick reverse during movement
                sequenceSt = 3;
                if(!sequenceQuickTo2){
                    sequenceInterval = interval - timeElapsed;
                    sequenceStart = Timer.getFPGATimestamp();
                }
                else{
                    sequenceQuickTo2 = false;
                    sequenceInterval = interval  - sequenceInterval + timeElapsed;
                    sequenceStart = Timer.getFPGATimestamp();
                }
                sequenceQuickTo0 = true;
                sequenceSt = 3;
                output = -downSpeed;
            }
            
            if(timeElapsed >= sequenceInterval && sequenceSt == 1){
               sequenceSt = 2;
               output = 0;
               sequenceStart = Timer.getFPGATimestamp();
               
            }
            else{
               output = upSpeed; 
            }
        }
        else if(sequenceSt == 2){
            sequenceQuickTo2 = false;
            sequenceQuickTo0 = false;
            if(directionReq == 0){ //No action, already there
                output = 0;
            }
            else if(directionReq == 1){
                sequenceSt = 3;
                sequenceStart = Timer.getFPGATimestamp();
                sequenceInterval = interval;
                sequenceQuickTo0 = false;
                output = -downSpeed;
            }
            else{ // =3, no input (remain stopped)
                output = 0; 
            }
        }
        else if(sequenceSt == 3){
            if(directionReq == 0){ //quick reverse during movement to 0
                sequenceSt = 1;
                if(!sequenceQuickTo0){
                    sequenceInterval = interval - timeElapsed;
                    sequenceStart = Timer.getFPGATimestamp();
                }
                else{
                    sequenceStart = Timer.getFPGATimestamp();
                    sequenceInterval = interval  - sequenceInterval + timeElapsed;
                    sequenceQuickTo0 = false;
                }
               sequenceSt = 1;
               sequenceQuickTo2 = true;
               output = upSpeed;
            }
            else if(directionReq == 1){
                //continue in same direction
                output = lastOutput;
            }
            
            if(timeElapsed >= sequenceInterval && sequenceSt == 3){
                sequenceSt = 0;
                output = 0;
                sequenceStart = Timer.getFPGATimestamp();
            }
            else{
                output = -downSpeed;
            }
            
        }
        
        lastOutput = output;
        return output;
    }
}
