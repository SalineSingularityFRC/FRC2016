package org.usfirst.frc.team5066.controller2016.controlSchemes;

import org.usfirst.frc.team5066.controller2016.ControlScheme;
import org.usfirst.frc.team5066.controller2016.XboxController;
import org.usfirst.frc.team5066.library.SingularityDrive;
import org.usfirst.frc.team5066.robot.SingularityArm;
import org.usfirst.frc.team5066.robot.SingularityConveyer;

import edu.wpi.first.wpilibj.Joystick;

public class OneXboxTankDrive implements ControlScheme{
	
	XboxController xbox;
	
	boolean isreversed = false;
	
	double armSpeed = 1;
	
	public OneXboxTankDrive(Joystick j) {
		xbox = (XboxController) j;
	}
	
	public OneXboxTankDrive(int port) {
		xbox = new XboxController(port);
	}
	
	@Override
	public void controlArm(SingularityArm arm) {
		if (xbox.getAButton()) {
			arm.setSpeed(armSpeed);
		} else if (xbox.getYButton()) {
			arm.setSpeed(-1 * armSpeed);
		} else {
			arm.setSpeed(0);
		}
	}
	
	@Override
	public void controlConveyer(SingularityConveyer conveyer) {
		if (isreversed == false) {
			conveyer.setSpeed(xbox.getTriggerRight(), xbox.getTriggerLeft(), xbox.getR3());
		} else {
			conveyer.setSpeed(xbox.getTriggerLeft(), xbox.getTriggerRight(), !xbox.getR3());
		}
	}

	@Override
	public void drive(SingularityDrive sd, boolean squaredInputs) {
		
		if (xbox.getPOV() == 180) {
			isreversed = true;
		} else if (xbox.getPOV() == 0) {
			isreversed = false;
		}
		
		if (isreversed == false) {
			sd.tank(-1 * xbox.getLS_Y(), -1 * xbox.getRS_Y(), squaredInputs);
		} else {
			sd.tank(xbox.getLS_Y(), xbox.getRS_Y(), squaredInputs);
		}
	}
}