package org.usfirst.frc.team5066.controller2016.controlSchemes;

import org.usfirst.frc.team5066.controller2016.ControlScheme;
import org.usfirst.frc.team5066.controller2016.XboxController;
import org.usfirst.frc.team5066.library.SingularityDrive;
import org.usfirst.frc.team5066.robot.SingularityArm;
import org.usfirst.frc.team5066.robot.SingularityConveyer;

import edu.wpi.first.wpilibj.Joystick;

public class OneXboxArcadeDrive implements ControlScheme{

	XboxController xbox;
	
	public OneXboxArcadeDrive(Joystick j) { //This method causes a casting error... so don't use it
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
		sd.arcade(xbox.getLS_Y(), xbox.getLS_X(), squaredInputs);
	}


	
	
}
