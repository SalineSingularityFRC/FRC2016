package org.usfirst.frc.team5066.controller2016.controlSchemes;

import org.usfirst.frc.team5066.controller2016.ControlScheme;
import org.usfirst.frc.team5066.controller2016.XboxController;
import org.usfirst.frc.team5066.library.SingularityDrive;
import org.usfirst.frc.team5066.robot.SingularityArm;
import org.usfirst.frc.team5066.robot.SingularityConveyer;

import edu.wpi.first.wpilibj.Joystick;

public class OneXboxArcadeDrive implements ControlScheme{
	
	XboxController xbox;
	
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
		if (isreversed == false) {
			conveyer.setSpeed(xbox.getTriggerRight() - xbox.getTriggerLeft());
		} else {
			conveyer.setSpeed(xbox.getTriggerLeft() - xbox.getTriggerRight());
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
			sd.arcade(xbox.getLS_Y(), xbox.getLS_X(), squaredInputs);
		} else {
			sd.arcade(-1 * xbox.getLS_Y(), -1 * xbox.getLS_X(), squaredInputs);
		}
	}	
}