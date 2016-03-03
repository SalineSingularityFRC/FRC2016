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
	private CANTalon left, right;
	double climberSpeedConstant;

	/**
	 * Constructor for singularity climb.
	 * 
	 * @param l
	 *            <b>int</b> The left side motor channel
	 * @param r
	 *            <b>int</b> The right side motor channel
	 */
	public SingularityClimber(int l, int r, double climberSpeedConstant) {
		left = new CANTalon(l);
		right = new CANTalon(r);

		left.setPosition(0.0);
		right.setPosition(0.0);

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
			left.set(1);
			right.set(-1);
		} else if (speed > 1) {
			left.set(-1);
			right.set(1);
		} else {
			// sets both motor speeds to move in the same direction
			left.set(-speed);
			right.set(speed);
		}

		//left.set(right.getPosition());

		SmartDashboard.putNumber("left get Position", left.getPosition());
		SmartDashboard.putNumber("right get Position", right.getPosition());
	}

	/**
	 *
	 * @return The current speed of the Climb motors (right motor)
	 */
	public double getSpeed() {
		return right.get();
	}
	
	public double getPosition() {
		return right.getPosition();
	}
}
