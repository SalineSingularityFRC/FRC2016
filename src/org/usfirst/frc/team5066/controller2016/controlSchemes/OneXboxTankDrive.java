package org.usfirst.frc.team5066.controller2016.controlSchemes;

import org.usfirst.frc.team5066.controller2016.ControlScheme;
import org.usfirst.frc.team5066.controller2016.XboxController;
import org.usfirst.frc.team5066.library.SingularityDrive;
import org.usfirst.frc.team5066.library.SpeedMode;
import org.usfirst.frc.team5066.robot.SingularityArm;
import org.usfirst.frc.team5066.robot.SingularityConveyer;

import edu.wpi.first.wpilibj.Joystick;

public class OneXboxTankDrive implements ControlScheme {

	XboxController xbox;
	SpeedMode speedMode;

	boolean isreversed = false;


	public OneXboxTankDrive(Joystick j) {
		xbox = (XboxController) j;
	}

	public OneXboxTankDrive(int port) {
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
		conveyer.setSpeed(xbox.getTriggerRight() - xbox.getTriggerLeft());
	}

	@Override
	public void drive(SingularityDrive sd, boolean squaredInputs) {
		
		//Set speedMode
		if(xbox.getLB()){
			speedMode = SpeedMode.SLOW;
		}
		else if(xbox.getRB()) {
			speedMode = SpeedMode.FAST;
		}
		else{
			speedMode = SpeedMode.NORMAL;
		}

		//Drive
		sd.tank(-1 * xbox.getLS_Y(), -1 * xbox.getRS_Y(), squaredInputs, speedMode,
				SingularityDrive.booleanToggleReverse(xbox.getBackButton(), xbox.getStartButton()));
	}
}
