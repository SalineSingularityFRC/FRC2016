package org.usfirst.frc.team5066.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * A class that represents our arm from the 2016 year.
 * 
 * @author Saline Singularity 5066
 *
 */
public class SingularityArm {

	private CANTalon leftWorm, leftPlanet, rightWorm, rightPlanet;

	/**
	 * When true, limit switches will not automatically stop the arm from
	 * moving. The input and effect of the switches must coded elsewhere.
	 */
	private boolean limitSwitchesOverride;

	private double armSpeed;

	private double armSpeedFAST;

	double lowerLimit;

	double position;
	
	TalonControlMode controlMode;
	
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

	public SingularityArm(int lWorm, int lPlanet, int rWorm, int rPlanet, double armSpeed, double armSpeedFAST) {

		leftWorm = new CANTalon(lWorm);
		leftPlanet = new CANTalon(lPlanet);
		rightWorm = new CANTalon(rWorm);
		rightPlanet = new CANTalon(rPlanet);
		limitSwitchesOverride = false;
		this.armSpeed = armSpeed;
		this.armSpeedFAST = armSpeedFAST;
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
	public void setSpeed(double speed, boolean fast, boolean limitSwitchesOverride) {
		/*if(rightPlanet.getPosition() < 0) {
			rightPlanet.setPosition(0);
		} else {
		*/
			speed = fast ? speed * armSpeedFAST : speed * armSpeed;
			setRawSpeed(speed);
		//}
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


		// code for limit switches
		if (limitSwitchesOverride) {
			if (speed > 0)
				speed = 0;
		}
		
		if(Math.abs(speed) > 0.07){
			// sets both motor speeds to move in the same direction
			// Note - becuse of the wiring, we actually tell them all to have thee
			// same direction
			leftWorm.set(speed);
			leftPlanet.set(speed);
			rightWorm.set(speed);
			rightPlanet.set(speed);
			
			position = rightPlanet.getPosition();
		} else {
			controlMode = rightPlanet.getControlMode();
			rightPlanet.changeControlMode(TalonControlMode.Position);
			rightPlanet.set(position);
			rightPlanet.changeControlMode(controlMode);			
			
			rightWorm.set(rightPlanet.get());
			leftWorm.set(rightPlanet.get());
			leftPlanet.set(rightPlanet.get());

		}

		// put arm encoder data into smartDash
		/*SmartDashboard.putNumber("leftWorm Position", leftWorm.getPosition());
		SmartDashboard.putNumber("leftWorm Speed", leftWorm.getSpeed());*/

		SmartDashboard.putNumber("rightPlanet Position", rightPlanet.getPosition());
		SmartDashboard.putNumber("rightPlanet Speed", rightPlanet.getSpeed());

	}

	/**
	 *
	 * @return The current speed of the conveyer motors (right motor)
	 */
	public double getSpeed() {
		return rightPlanet.get();
	}
	
	private double getBrakeConstant() {
		if(rightWorm.getPosition() <= armBrakeThreshold) {
			return brakeConstant;
		}
		else{
			return 0;
		}
	}
}