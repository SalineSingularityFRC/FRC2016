package org.usfirst.frc.team5066.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * A class that represents our climb from the 2016 year.
 * 
 * @author Saline Singularity 5066
 *
 */
public class SingularityClimber {
	private CANTalon talon;
	double climberSpeedConstant;

	/**
	 * Constructor for singularity climb.
	 * 
	 * @param l
	 *            <b>int</b> The left side motor channel
	 * @param r
	 *            <b>int</b> The right side motor channel
	 */
	public SingularityClimber(int t, double climberSpeedConstant) {
		talon = new CANTalon(t);

		talon.setPosition(0.0);

		//left.changeControlMode(CANTalon.TalonControlMode.Position);
	}

	/**
	 * Sets the speed of both motors in the same direction. Positive is upward,
	 * negative is downwards.
	 * 
	 * @param speed
	 *            <b>double</b> The desired analog speed of the motors. [-1.0,
	 *            1.0]
	 */
	public void setSpeed(double speed) {
		// Checks for illegal values (and deports them back to where they came,
		// those bastards)
		if (speed < -1) {
			talon.set(-1);
		} else if (speed > 1) {
			talon.set(1);
		} else {
			// sets both motor speeds to move in the same direction
			talon.set(-speed);
		}


		//SmartDashboard.putNumber("left get Position", talon.getPosition());
	}

	/**
	 *
	 * @return The current speed of the Climb motors (right motor)
	 */
	public double getSpeed() {
		return talon.get();
	}
	
	public double getPosition() {
		return talon.getPosition();
	}
}
