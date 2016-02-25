package org.usfirst.frc.team5066.controller2016.controlSchemes;

import org.usfirst.frc.team5066.controller2016.ControlScheme;
import org.usfirst.frc.team5066.controller2016.XboxController;
import org.usfirst.frc.team5066.library.SingularityDrive;
import org.usfirst.frc.team5066.library.SpeedMode;
import org.usfirst.frc.team5066.robot.SingularityArm;
import org.usfirst.frc.team5066.robot.SingularityConveyer;

import edu.wpi.first.wpilibj.Joystick;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class OneXboxArcadeDrive implements ControlScheme {

	XboxController xbox;
	SpeedMode speedMode;

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
		conveyer.setSpeed(xbox.getTriggerRight() - xbox.getTriggerLeft());
	}

	@Override
	public void drive(SingularityDrive sd, boolean squaredInputs) {
		
		//set speedMode
		if(xbox.getLB()) {
			speedMode = SpeedMode.SLOW;
			SmartDashboard.putString("DB/String 6", "SLOW -- LB pressed");
		} else if(xbox.getRB()) {
			speedMode = SpeedMode.FAST;
			SmartDashboard.putString("DB/String 6", "FAST -- RB pressed");
		} else {
			speedMode = SpeedMode.NORMAL;
			SmartDashboard.putString("DB/String 6", "NORMAL -- nothing pressed");
		}
		
		sd.arcade(xbox.getLS_Y(), xbox.getLS_X(), squaredInputs, speedMode, xbox.getPOV());
	}

}