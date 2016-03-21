package org.usfirst.frc.team5066.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Solenoid;

/**
 * A class that represents our climb from the 2016 year.
 * 
 * @author Saline Singularity 5066
 *
 */
public class SingularityClimber {
	private CANTalon talon;
	private Solenoid solenoid;
	private double climberSpeedConstant;

	/**
	 * {@code SingularityClimber} constructer
	 * @param talonPort Which port the winch is on
	 * @param solenoidPort Which port the solenoid is on
	 * @param climberSpeedConstant Constant to scale the winch
	 */
	public SingularityClimber(int talonPort, int solenoidPort, double climberSpeedConstant) {
		talon = new CANTalon(talonPort);
		talon.setPosition(0.0);
		this.climberSpeedConstant = climberSpeedConstant;

		solenoid = new Solenoid(solenoidPort);
	}

	/**
	 * Sets the speed of the winch. Positive is upward, negative is downwards.
	 * 
	 * @param speed
	 *            <b>double</b> The desired analog speed of the motor. [-1.0,
	 *            1.0]
	 */
	public void setSpeed(double speed) {
		speed *= climberSpeedConstant;
		talon.set(speed / Math.max(Math.abs(speed), 1));
	}

	/**
	 * Whether or not pressure should be applied to the solenoid
	 * 
	 * @param release
	 *            Parameter to set to the solenoid
	 */
	public void releaseSolenoid(boolean release) {
		solenoid.set(release);
	}

	/**
	 *
	 * @return The current speed of the winch
	 */
	public double getSpeed() {
		return talon.get();
	}

	/**
	 * 
	 * @return The current position of the winch
	 */
	public double getPosition() {
		return talon.getPosition();
	}

	/**
	 * 
	 * @return The current climber speed constant
	 */
	public double getClimberSpeedConstant() {
		return climberSpeedConstant;
	}

	/**
	 * 
	 * @param climberSpeedConstant
	 *            What to set the climber speed constant to
	 */
	public void setClimberSpeedConstant(double climberSpeedConstant) {
		this.climberSpeedConstant = climberSpeedConstant;
	}
}
