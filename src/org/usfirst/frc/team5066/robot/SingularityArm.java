package org.usfirst.frc.team5066.robot;

import org.usfirst.frc.team5066.library.SingularityProperties;
import org.usfirst.frc.team5066.library.SingularityPropertyNotFoundException;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * A class that represents our arm from the 2016 year.
 * 
 * @version 2.0
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
	private double lowerLimit;
	private double armSpeedFAST;

	double upperLimit;
	
	double armPosition;

	SingularityProperties properties = org.usfirst.frc.team5066.robot.Robot.properties;
	
	/**
	 * Constructor for singularity conveyer.
	 * 
	 * @param t
	 *  		  <b>int</b> The motor ID number of the motorcontroller of the arm
	 * @param armSpeed
	 *            <b>double</b> The default speed multiplier of the arm
	 * @param armSpeedFAST
	 *            <b>double</b> The fast mode speed multiplier of the arm
	 * @param lowerLimit
	 *            <b>double</b> The minimum and maxmimum value of the position of the arm. armLimit/2 = arm at 45 degree angle
	 */

	public SingularityArm(int t, double armSpeed, double armSpeedFAST, double lowerLimit, double upperLimit) {

		talon = new CANTalon(t);
		limitSwitchesOverride = false;
		this.armSpeed = armSpeed;
		this.armSpeedFAST = armSpeedFAST;
		
		//this.lowerLimit = armLimit;
		/*
		lowerLimit = -60000;
		upperLimit = 43467;
		*/
		
		this.lowerLimit = lowerLimit;
		this.upperLimit = upperLimit;
		
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
	public void setSpeed(double speed, boolean fast, boolean overrideLimits) {

		speed = fast ? speed * armSpeedFAST : speed * armSpeed;
		setRawSpeed(speed, overrideLimits);
		
		/*SmartDashboard.putNumber("Arm Speed", speed);
		
		if(rightWorm.getPosition() < armLimit && limitSwitchesOverride == false && speed > 0.1) {
			
			//defaultControlMode = rightWorm.getControlMode();
			rightWorm.changeControlMode(TalonControlMode.Position);
			leftWorm.set(armLimit);
			rightPlanet.enableBrakeMode(false);
			leftPlanet.enableBrakeMode(false);
			
			
			//leftPlanet.set(0.0);
			//rightPlanet.set(0.0);
			
			
			//rightWorm.changeControlMode(defaultControlMode);
		} else {
			//positive is going up
			
			rightWorm.changeControlMode(defaultControlMode);
			
			rightPlanet.enableBrakeMode(true);
			leftPlanet.enableBrakeMode(true);
			speed = fast ? speed * armSpeedFAST : speed * armSpeed;
			setRawSpeed(speed);
			
			
		}*/
		
		

		//SmartDashboard.putNumber("rightWorm Position", rightWorm.getPosition());
		//SmartDashboard.putNumber("rightWorm Speed", rightWorm.getSpeed());
	}
	
	
	public double getPosition(){
		return talon.getPosition();
	}
	
	public void setPosition(double p){
		talon.setPosition(p);
	}
	
	/**
	 * Sets the speed of both motors in the same direction. Positive is forward,
	 * negative is backwards.
	 * 
	 * @param speed
	 *            <b>double</b> The desired analog speed of the motors. [-1.0,
	 *            1.0]
	 * @param fast
	 *            Controls whether the arm is in high-power m8ode or not. Useful
	 *            for raising the portcullis
	 */
	public void setSpeed(double speed, boolean fast) {

		
		speed = fast ? speed * armSpeedFAST : speed * armSpeed;
		setRawSpeed(speed, false);

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
		setRawSpeed(speed, false);
		
	}

	/**
	 * Use this method in situations like auton code because it sets the speed
	 * of the arm without taking into account the arm multiplier
	 * 
	 * @param speed
	 *            <b>double</b> The desired raw speed for the arm.
	 */
	public void setRawSpeed(double speed, boolean overrideLimits) {
		
		armPosition = talon.getPosition();
		
		// clamp
		speed /= -Math.max(1, Math.abs(speed));

		// sets both motor speeds to move in the same direction
		// Note - becuse of the wiring, we actually tell them all to have thee
		// same direction
		
		//lower limit
		if(armPosition < lowerLimit && speed > 0 && overrideLimits == false){
			talon.set(0);
			SmartDashboard.putString("Arm Status: ", "Lower limit");
		}
		//upper limit
		else if(armPosition > upperLimit && speed < 0 && overrideLimits == false) {
			talon.set(0);
			SmartDashboard.putString("Arm Status: ", "Upper limit");
		}
		//if not touching a limit
		else{
			talon.set(speed);
			SmartDashboard.putString("Arm Status: ", "Not touching any limit OR overriding limits");
		}
		
		
		SmartDashboard.putNumber("Arm Speed Value", speed);
		SmartDashboard.putNumber("Arm Encoder", armPosition);
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