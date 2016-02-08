package org.usfirst.frc.team5066.controller2016.controlSchemes;

import org.usfirst.frc.team5066.controller2016.ControlScheme;
import org.usfirst.frc.team5066.controller2016.XboxController;
import org.usfirst.frc.team5066.library.SingularityDrive;
import org.usfirst.frc.team5066.robot.SingularityArm;

import edu.wpi.first.wpilibj.Joystick;

public class OneXboxArcadeDrive implements ControlScheme{

	XboxController xbox;
	
	public OneXboxArcadeDrive(Joystick j) {
		xbox = (XboxController)j;
	}
	
	public OneXboxArcadeDrive(int port) {
		xbox = new XboxController(port);
	}
	
	@Override
	public void controlArm(SingularityArm arm) {
		arm.setSpeed(xbox.getRS_Y());
	}

	@Override
	public void controlConveyor() {
		
	}

	@Override
	public void arcadeDrive(SingularityDrive sd, boolean squaredInputs) {
		sd.arcade(xbox.getLS_Y(), xbox.getLS_X(), squaredInputs);
	}

	@Override
	public void tankDrive(SingularityDrive sd, boolean squaredInputs) {
		sd.tank(xbox.getLS_Y(), xbox.getRS_Y(), squaredInputs);
	}

	
	
}
