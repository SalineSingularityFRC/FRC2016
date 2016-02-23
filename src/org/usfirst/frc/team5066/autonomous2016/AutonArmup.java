package org.usfirst.frc.team5066.autonomous2016;

import org.usfirst.frc.team5066.library.SingularityDrive;
import org.usfirst.frc.team5066.robot.SingularityArm;
import org.usfirst.frc.team5066.robot.SingularityClimb;
import org.usfirst.frc.team5066.robot.SingularityConveyer;

public class AutonArmup implements AutonMode {
	
	SingularityDrive drive;
	SingularityConveyer conveyer;
	SingularityArm arm;
	SingularityClimb climb;
	
	public AutonArmup(SingularityDrive drive, SingularityConveyer conveyer, SingularityArm arm, SingularityClimb climb) {
		this.drive = drive;
		this.conveyer = conveyer;
		this.arm = arm;
		this.climb = climb;
	}
	
	@Override
	public void run(double time) {
		if(time < 1.0 && time > 0.0) {
			arm.setSpeed(1.0);;
		}
	}

}
