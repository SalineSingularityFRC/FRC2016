package org.usfirst.frc.team5066.controller2016.controlSchemes;

import org.usfirst.frc.team5066.controller2016.ControlScheme;
import org.usfirst.frc.team5066.controller2016.LogitechController;
import org.usfirst.frc.team5066.controller2016.XboxController;
import org.usfirst.frc.team5066.library.SingularityDrive;
import org.usfirst.frc.team5066.robot.SingularityArm;
import org.usfirst.frc.team5066.robot.SingularityConveyer;


public class TwoJoystickTankXboxAssist implements ControlScheme{

	LogitechController Left;
	LogitechController Right;
	XboxController xb;
	
	
	public TwoJoystickTankXboxAssist(int xboxPort, int leftStickPort, int rightStickPort){
	xb = new XboxController(xboxPort);
	Left = new LogitechController(leftStickPort);
	Right = new LogitechController(rightStickPort);
	
	}
	@Override
	public void controlConveyer(SingularityConveyer conveyer) {
		conveyer.setSpeed(xb.getLS_Y(), xb.getRS_Y(), false);
		
	}
	@Override
	public void drive(SingularityDrive sd, boolean squaredInputs) {
		sd.tank(Left.getStickY(), Right.getStickY(), squaredInputs);
		
	}
	@Override
	public void controlArm(SingularityArm arm) {
		arm.setSpeed(xb.getTriggerLeft() - xb.getTriggerRight());
		
	}
	
	
	
	
	
	
	
	
	
	
	
}
