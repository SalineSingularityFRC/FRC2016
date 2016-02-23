package org.usfirst.frc.team5066.autonomous2016;

import org.usfirst.frc.team5066.library.SingularityDrive;
import org.usfirst.frc.team5066.robot.SingularityArm;
import org.usfirst.frc.team5066.robot.SingularityClimb;
import org.usfirst.frc.team5066.robot.SingularityConveyer;

public class AutonBackward implements AutonMode {
	
	SingularityDrive drive;
	SingularityConveyer conveyer;
	SingularityArm arm;
	SingularityClimb climb;
	
	public AutonBackward(SingularityDrive drive, SingularityConveyer conveyer, SingularityArm arm, SingularityClimb climb) {
		this.drive = drive;
		this.conveyer = conveyer;
		this.arm = arm;
		this.climb = climb;
	}
	
	@Override
	public void run(double time) {
		if(time < 2.0 && time > 0.0) {
			drive.setVelocityMultiplier(-0.5);
		}
	}

}
