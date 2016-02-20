package org.usfirst.frc.team5066.controller2016.controlSchemes;

import org.usfirst.frc.team5066.controller2016.ControlScheme;
import org.usfirst.frc.team5066.controller2016.XboxController;
import org.usfirst.frc.team5066.library.SingularityDrive;
import org.usfirst.frc.team5066.robot.SingularityArm;
import org.usfirst.frc.team5066.robot.SingularityConveyer;

import edu.wpi.first.wpilibj.Joystick;

public class OneXboxGTADrive implements ControlScheme{

	XboxController xbox;
	
	boolean isreversed = false;
	
	public OneXboxGTADrive(Joystick j) {
		xbox = (XboxController) j;
	}
	
	public OneXboxGTADrive(int port) {
		xbox = new XboxController(port);
	}
	
	@Override
	public void controlArm(SingularityArm arm) {
		if (xbox.getAButton()) {
			arm.setSpeed(1);
		} else if (xbox.getYButton()) {
			arm.setSpeed(-1);
		} else {
			arm.setSpeed(0);
		}
	}

	@Override
	public void controlConveyer(SingularityConveyer conveyer) {
		if (isreversed == true) {
			conveyer.setSpeed(xbox.getRS_Y() + xbox.getRS_X(), xbox.getRS_Y() - xbox.getRS_X(), false);
		} else {
			conveyer.setSpeed(xbox.getRS_Y() + xbox.getRS_X(), xbox.getRS_Y() - xbox.getRS_X(), true);
		}
	}

	@Override
	public void drive(SingularityDrive sd, boolean squaredInputs) {
		if (xbox.getPOV() == 180) {
			isreversed = true;
		} else if (xbox.getPOV() == 0) {
			isreversed = false;
		}
		if (isreversed) {
			sd.arcade(xbox.getTriggerRight() - xbox.getTriggerLeft(), xbox.getLS_X(), squaredInputs);
		} else {
			sd.arcade(xbox.getTriggerLeft() - xbox.getTriggerRight(), -1 * xbox.getLS_X(), squaredInputs);
		}
	}

}
