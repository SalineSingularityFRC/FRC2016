package org.usfirst.frc.team5066.autonomous2016;

import org.usfirst.frc.team5066.library.SingularityDrive;
import org.usfirst.frc.team5066.robot.SingularityArm;
import org.usfirst.frc.team5066.robot.SingularityClimb;
import org.usfirst.frc.team5066.robot.SingularityConveyer;

public class Autonomous2016 {

	SingularityDrive drive;
	SingularityConveyer conveyer;
	SingularityArm arm;
	SingularityClimb climb;

	public Autonomous2016(SingularityDrive drive, SingularityConveyer conveyer, SingularityArm arm, SingularityClimb climb) {
		this.drive = drive;
		this.conveyer = conveyer;
		this.arm = arm;
		this.climb = climb;
	}

	public AutonMode getAutonomousMode(String modeName){
		//switch statement for each value of modeName --> returns the corresponding AutonRunner
		switch (modeName){
		case "forward":
			return new AutonForward(drive, conveyer, arm, climb);
			
		case"backward":
			return new AutonBackward(drive, conveyer, arm, climb);
			
		case"armup":
			return new AutonArmup(drive, conveyer, arm, climb);
			
		case"armdown":
			return new AutonArmdown(drive, conveyer, arm, climb);
				
		
		
		
	
		
		
		
		default:
			//default auton mode
	        return new AutonForward(drive, conveyer, arm, climb);
			
		}
	}
	}

	    
	
	
	
	
	
	
	
	
