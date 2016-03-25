package org.usfirst.frc.team5066.controller2016.controlSchemes;

import org.usfirst.frc.team5066.controller2016.ControlScheme;
import org.usfirst.frc.team5066.controller2016.LogitechController;
import org.usfirst.frc.team5066.controller2016.XboxController;
import org.usfirst.frc.team5066.library.SingularityDrive;
import org.usfirst.frc.team5066.robot.SingularityArm;
import org.usfirst.frc.team5066.robot.SingularityClimber;
import org.usfirst.frc.team5066.robot.SingularityConveyer;

public class RegularDrive implements ControlScheme{
	
	XboxController xbox;
	LogitechController logitech;
	
	public RegularDrive(int LogitechController, int xboxPort){
		xbox = new XboxController(xboxPort);
		logitech = new LogitechController(LogitechController);
	}
	
	
	
	@Override
	public void controlConveyer(SingularityConveyer conveyer) {
		if(xbox.getRB())
			conveyer.setSpeed(0.5 * (xbox.getTriggerRight() - xbox.getTriggerLeft()));
		else
			conveyer.setSpeed(1.0 * (xbox.getTriggerRight() - xbox.getTriggerLeft()));
			
	}

	@Override
	public void drive(SingularityDrive sd, boolean squaredInputs) {
		sd.arcade(xbox.getLS_Y(),xbox.getRS_X(), squaredInputs, xbox.getPOV());
	}

	@Override
	public void controlArm(SingularityArm arm) {
		arm.setSpeed(logitech.getStickY(), logitech.getTrigger(), logitech.getRawButton(12));
		if(logitech.getRawButton(7)) {
			arm.zero();
		}
	}



	@Override
	public void controlClimber(SingularityClimber climber) {
		
		if(logitech.getStickBackLeft()){
			climber.setSpeed(-1);
		} else if(logitech.getStickFrontLeft()){
			climber.setSpeed(1);
		} else {
			climber.setSpeed(0);
		}
	}

}
