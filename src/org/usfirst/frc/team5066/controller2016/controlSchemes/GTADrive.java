 package org.usfirst.frc.team5066.controller2016.controlSchemes;

import org.usfirst.frc.team5066.controller2016.ControlScheme;
import org.usfirst.frc.team5066.controller2016.LogitechController;
import org.usfirst.frc.team5066.controller2016.XboxController;
import org.usfirst.frc.team5066.library.SingularityDrive;
import org.usfirst.frc.team5066.robot.SingularityArm;
import org.usfirst.frc.team5066.robot.SingularityClimber;
import org.usfirst.frc.team5066.robot.SingularityConveyer;

public class GTADrive implements ControlScheme{
XboxController xbox;
LogitechController logitech;
	
	
	
	public GTADrive(int LogitechController, int xboxPort){
		xbox = new XboxController(xboxPort);
		logitech = new LogitechController(LogitechController);
	}
	
	
	@Override
	public void controlConveyer(SingularityConveyer conveyer) {
		conveyer.setSpeed(xbox.getRS_Y());
		
		// TODO Auto-generated method stub
	}

	@Override
	public void drive(SingularityDrive sd, boolean squaredInputs) {
		sd.arcade(xbox.getTriggerLeft() - xbox.getTriggerRight(), xbox.getLS_X(), squaredInputs, xbox.getPOV());
		
		// TODO Auto-generated method stub
	}

	@Override
	public void controlArm(SingularityArm arm) {
		arm.setSpeed(logitech.getStickY());
		
		// TODO Auto-generated method stub
		
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
