package org.usfirst.frc.team5066.controller2016.controlSchemes;

import org.usfirst.frc.team5066.controller2016.ControlScheme;
import org.usfirst.frc.team5066.controller2016.XboxController;
import org.usfirst.frc.team5066.library.SingularityDrive;
import org.usfirst.frc.team5066.library.SpeedMode;
import org.usfirst.frc.team5066.robot.SingularityArm;
import org.usfirst.frc.team5066.robot.SingularityClimb;
import org.usfirst.frc.team5066.robot.SingularityConveyer;

import com.sun.webkit.Timer;

import edu.wpi.first.wpilibj.Joystick;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class OneXboxArcadeDrive implements ControlScheme {

	XboxController xbox;
	long Timer;
	
	SpeedMode speedMode;
	
	boolean isAutoshooting;
	
	long t;

	boolean isreversed = false;

	public OneXboxArcadeDrive(Joystick j) {
		xbox = (XboxController) j;
	}

	public OneXboxArcadeDrive(int port) {
		xbox = new XboxController(port);
	}

	@Override
	public void controlArm(SingularityArm arm) {
		arm.setSpeed(xbox.getRS_Y());
	}

	@Override
	public void controlConveyer(SingularityConveyer conveyer) {
		Timer = System.nanoTime();
		
		
		
		//Auto-shoot code
		
		if(xbox.getAButton()){
			
			isAutoshooting = true;
		}
		
		  if(isAutoshooting == true){
			
		
			  if( Timer < 200000 && Timer == 200000){
		        conveyer.setSpeed(0.5);
		    }

		    
		      else {
		    	conveyer.setSpeed(0.0);
		        
		    }//Dive conveyer forward for 2 seconds, then turn off and set isAutoshooting to false;
				
			  
			  
		if(xbox.getXButton()){
				
				isAutoshooting = true;
			}
			
			if(isAutoshooting == true){
				
			
				if( Timer < 200000 && Timer == 200000){
			        conveyer.setSpeed(0.0);
			    }

			    
			    else {
			    	
			    	 conveyer.setSpeed(0.0);
			    } // Press x button to cancel auto shooting
			  
			
			
		}
	     
			
		//TODO Add arm move if arm is in the way of shooting
		//TODO add wait until arm is out of the way
		
		if (!isAutoshooting) {
			if (isreversed == false) {
				conveyer.setSpeed(xbox.getTriggerRight() - xbox.getTriggerLeft());
			} 
			else {
				conveyer.setSpeed(xbox.getTriggerLeft() - xbox.getTriggerRight());
			}
		}
		}
	}

	@Override
	public void drive(SingularityDrive sd, boolean squaredInputs) {

		//set isReversed
		if (xbox.getPOV() == 180) {
			isreversed = true;
		} else if (xbox.getPOV() == 0) {
			isreversed = false;
		}
		
		//set speedMode
		if(xbox.getLB()) {
			speedMode = SpeedMode.SLOW;
			SmartDashboard.putString("DB/String 6", "SLOW -- LB pressed");
		} else if(xbox.getRB()) {
			speedMode = SpeedMode.FAST;
			SmartDashboard.putString("DB/String 6", "FAST -- RB pressed");
		} else {
			speedMode = SpeedMode.NORMAL;
			SmartDashboard.putString("DB/String 6", "NORMAL -- nothing pressed");
		}
		
		//drive based on isReversed and speedMode
		if (isreversed == false) {
			sd.arcade(xbox.getLS_Y(), xbox.getLS_X(), squaredInputs, speedMode);
		} else {
			sd.arcade(-1 * xbox.getLS_Y(), -1 * xbox.getLS_X(), squaredInputs, speedMode);
		}
	}

	@Override
	public void climb(SingularityClimb climb) {
		
		
		
		
		
		// TODO Auto-generated method stub
		
	}

}