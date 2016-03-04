package org.usfirst.frc.team5066.controller2016.controlSchemes;

import org.usfirst.frc.team5066.controller2016.ControlScheme;
import org.usfirst.frc.team5066.controller2016.LogitechController;
import org.usfirst.frc.team5066.controller2016.XboxController;
import org.usfirst.frc.team5066.library.SingularityDrive;
import org.usfirst.frc.team5066.robot.SingularityArm;
import org.usfirst.frc.team5066.robot.SingularityClimber;
import org.usfirst.frc.team5066.robot.SingularityConveyer;

public class OneStickDrive implements ControlScheme{
	
	LogitechController logitech;
	
	public OneStickDrive(int LogitechController, int xboxPort){
		logitech = new LogitechController(LogitechController);
	}
	
	
	
	@Override
	public void controlConveyer(SingularityConveyer conveyer) {
		conveyer.setSpeed(logitech.getThrottle());
	}

	@Override
	public void drive(SingularityDrive sd, boolean squaredInputs) {
		sd.arcade(logitech.getStickY(), logitech.getStickX(), squaredInputs, logitech.getPOV());
	}

	@Override
	public void controlArm(SingularityArm arm) {
		if(logitech.getRawButton(6)) {
			arm.setRawSpeed(.5);
		}
		else if(logitech.getRawButton(4)) {
			arm.setRawSpeed(-.5);
		}
		else{
			arm.setRawSpeed(0);
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
