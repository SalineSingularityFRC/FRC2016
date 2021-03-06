package org.usfirst.frc.team5066.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * A class that represents our arm from the 2016 year.
 * 
 * @author Saline Singularity 5066
 *
 */
public class SingularityArm {

	private CANTalon talon;

	/**
	 * When true, limit switches will not automatically stop the arm from
	 * moving. The input and effect of the switches must coded elsewhere.
	 */
	private boolean limitSwitchesOverride;
	
	TalonControlMode defaultControlMode = TalonControlMode.PercentVbus;

	private double armSpeed;
	private double armLimit;
	private double armSpeedFAST;

	double lowerLimit;

	/**
	 * Constructor for singularity conveyer.
	 * 
	 * @param lWorm
	 *            <b>int</b> The left side worm motor channel
	 * @param lPlanet
	 *            <b>int</b> The left side planetary motor channel
	 * @param rWorm
	 *            <b>int</b> The right side worm motor channel
	 * @param rPlanet
	 *            <b>int</b> The right side planetary motor channel
	 */

	public SingularityArm(int t, double armSpeed, double armSpeedFAST, double armLimit) {

		talon = new CANTalon(t);
		limitSwitchesOverride = false;
		this.armSpeed = armSpeed;
		this.armSpeedFAST = armSpeedFAST;
		
		this.armLimit = armLimit;
		
		talon.enableBrakeMode(true);
	}
	
	/**
	 * Sets the speed of both motors in the same direction. Positive is forward,
	 * negative is backwards.
	 * 
	 * @param speed
	 *            <b>double</b> The desired analog speed of the motors. [-1.0,
	 *            1.0]
	 * @param fast
	 *            Controls whether the arm is in high-power mode or not. Useful
	 *            for raising the portcullis
	 *            
	 *            Be especially careful! This does not work yet!
	 */
	public void setSpeed(double speed, boolean fast, boolean limitSwitchesOverride) {
		
		
		SmartDashboard.putNumber("Arm Speed", speed);
		
		if(talon.getPosition() < armLimit && limitSwitchesOverride == false && speed > 0.05 /*0.05 is the threshold value*/) {
			
			/*
			//defaultControlMode = rightWorm.getControlMode();
			rightWorm.changeControlMode(TalonControlMode.Position);
			leftWorm.set(armLimit);
			rightPlanet.enableBrakeMode(false);
			leftPlanet.enableBrakeMode(false);
			*/
			
			talon.set(0.0);
			
			
			//rightWorm.changeControlMode(defaultControlMode);
		} else {
			//positive is going up
			/*
			talon.changeControlMode(defaultControlMode);
			
			rightPlanet.enableBrakeMode(true);
			leftPlanet.enableBrakeMode(true);
			*/
			speed = fast ? speed * armSpeedFAST : speed * armSpeed;
			setRawSpeed(speed);
			
			
		}
		
		

		//SmartDashboard.putNumber("rightWorm Position", rightWorm.getPosition());
		//SmartDashboard.putNumber("rightWorm Speed", rightWorm.getSpeed());
	}
	
	/**
	 * Sets the speed of both motors in the same direction. Positive is forward,
	 * negative is backwards.
	 * 
	 * @param speed
	 *            <b>double</b> The desired analog speed of the motors. [-1.0,
	 *            1.0]
	 * @param fast
	 *            Controls whether the arm is in high-power mode or not. Useful
	 *            for raising the portcullis
	 */
	public void setSpeed(double speed, boolean fast) {
		
		//TODO this should be calling the setSpeed method that includes the limit
		
		speed = fast ? speed * armSpeedFAST : speed * armSpeed;
		setRawSpeed(speed);

	}

	/**
	 * Sets the speed of both motors in the same direction. Positive is forward,
	 * negative is backwards.
	 * 
	 * @param speed
	 *            <b>double</b> The desired analog speed of the motors. [-1.0,
	 *            1.0]
	 */
	public void setSpeed(double speed) {

		speed *= armSpeed;
		setRawSpeed(speed);

	}

	/**
	 * Use this method in situations like auton code because it sets the speed
	 * of the arm without taking into account the arm multiplier
	 * 
	 * @param speed
	 *            <b>double</b> The desired raw speed for the arm.
	 */
	public void setRawSpeed(double speed) {

		// clamp
		speed /= -Math.max(1, Math.abs(speed));

		// sets both motor speeds to move in the same direction
		// Note - becuse of the wiring, we actually tell them all to have thee
		// same direction
		talon.set(speed);
		SmartDashboard.putNumber("Arm Speed Value", speed);
		SmartDashboard.putNumber("Arm Encoder", talon.getPosition());
		// code for limit switches
		if (limitSwitchesOverride) {
			if (speed > 0)
				speed = 0;
		}
	}

		// put arm encoder data into smartDash

		/*
		SmartDashboard.putNumber("rightWorm Position", rightWorm.getPosition());
		SmartDashboard.putNumber("rightWorm Speed", rightWorm.getSpeed());
	}

	/**
	 *
	 * @return The current speed of the conveyer motors (right motor)
	 */
	public double getSpeed() {
		return talon.get();
	}
	
	public void zero() {
		talon.setPosition(0.0);
	}
}