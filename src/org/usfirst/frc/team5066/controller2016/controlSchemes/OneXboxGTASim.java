package org.usfirst.frc.team5066.controller2016.controlSchemes;

import org.usfirst.frc.team5066.controller2016.ControlScheme;
import org.usfirst.frc.team5066.controller2016.XboxController;
import org.usfirst.frc.team5066.library.SingularityDrive;
import org.usfirst.frc.team5066.robot.SingularityArm;
import org.usfirst.frc.team5066.robot.SingularityClimber;
import org.usfirst.frc.team5066.robot.SingularityConveyer;

import edu.wpi.first.wpilibj.Joystick;

public class OneXboxGTASim implements ControlScheme {
	XboxController xbox;
	
	public OneXboxGTASim(Joystick j) {
		xbox = (XboxController) j;
	}
	
	public OneXboxGTASim(int port) {
		xbox = new XboxController(port);
	}
	@Override
	public void controlArm(SingularityArm arm) {
		arm.setSpeed(xbox.getRS_Y());
	}
	@Override
	public void controlConveyer(SingularityConveyer conveyer) {
		conveyer.setSpeed(xbox.getRS_Y() + xbox.getRS_X(), xbox.getRS_Y() - xbox.getRS_X(), false);
	}
	@Override
	public void drive(SingularityDrive sd, boolean squaredInputs) {
	}

	@Override
	public void controlClimber(SingularityClimber climber) {
		// TODO Auto-generated method stub
		
	}
	
}
