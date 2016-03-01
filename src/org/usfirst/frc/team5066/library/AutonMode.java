package org.usfirst.frc.team5066.library;

import org.usfirst.frc.team5066.robot.SingularityArm;
import org.usfirst.frc.team5066.robot.SingularityConveyer;

public interface AutonMode {
	
	public void run(SingularityDrive drive, SingularityConveyer conveyer, SingularityArm arm);
	
}