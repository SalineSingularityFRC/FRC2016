package org.usfirst.frc.team5066.controller2016;

import org.usfirst.frc.team5066.library.SingularityDrive;
import org.usfirst.frc.team5066.robot.SingularityArm;

public interface ControlScheme {
	//contains all control methods that will be called in Robot.java
	//such as moveElevator(), mecanumDrive(), etc.
	
	//implemented by all control scheme classes in the controlSchemes package, which take input from controller classes
	//and are called by the ControlSystem class
	
	public void controlConveyor();
	
	public void arcadeDrive(SingularityDrive sd, boolean squaredInputs);
	
	public void tankDrive(SingularityDrive sd, boolean squaredInputs);

	public void controlArm(SingularityArm arm);
	
	SingularityDrive sd = new SingularityDrive(1,2,3,4);
}