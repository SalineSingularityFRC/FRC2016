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

	public OneXboxTankDrive(Joystick j) {
		xbox = (XboxController) j;
	}

	public OneXboxTankDrive(int port) {
		xbox = new XboxController(port);
	}

	@Override
	public void controlArm(SingularityArm arm) {
		arm.setSpeed(xbox.getTriggerRight() - xbox.getTriggerLeft());
		//TODO same as conveyor
	}

	@Override
	public void controlConveyer(SingularityConveyer conveyer) {
		conveyer.setSpeed(xbox.getTriggerRight() - xbox.getTriggerLeft());
	}

	@Override
	public void drive(SingularityDrive sd, boolean squaredInputs) {
		
		if(xbox.getLB()){
			speedMode = SpeedMode.SLOW;
		}
		else if(xbox.getRB()) {
			speedMode = SpeedMode.FAST;
		}
		else{
			speedMode = SpeedMode.NORMAL;
		}
		
		sd.tank(-1 * xbox.getLS_Y(), -1 * xbox.getRS_Y(), squaredInputs, speedMode);
	}
}
