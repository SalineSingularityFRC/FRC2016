package org.usfirst.frc.team5066.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * A class that represents our conveyer belt from the 2016 year.
 * 
 * @author Saline Singularity 5066
 *
 */
public class SingularityConveyer {

	CANTalon left, right;

	/**
	 * Constructor for singularity conveyer.
	 * 
	 * @param l
	 *            <b>int</b> The left side motor channel
	 * @param r
	 *            <b>int</b> The right side motor channel
	 */
	public SingularityConveyer(int l, int r) {

		left = new CANTalon(l);
		right = new CANTalon(r);
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

		// Checks for illegal values (and deports them back to where they came,
		// those bastards)
		if ((speed < -1)) {
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
		
		SmartDashboard.putNumber("left encoder speed", left.getSpeed());
		SmartDashboard.putNumber("right Encoder speed", right.getSpeed());
	}

	/**
	 * 			
	 * @return The current speed of the conveyer motors (right motor)
	 */
	public double getSpeed() {
		return right.get();
	}
	 /**  @param leftspeed
		 *            <b>double</b> The desired analog speed of the left motor. [-1.0,
		 *            1.0]
		 *             @param rightspeed
		 *            <b>double</b> The desired analog speed of the right motor. [-1.0,
		 *            1.0]
		 **/
	//same as the setSpeed method above, but with two different speeds for inputs
	public void setSpeed(double leftSpeed, double rightSpeed, boolean reverse) {
		double leftS = leftSpeed, rightS = -rightSpeed;
		if (reverse) {
			leftS = -leftSpeed;
			rightS = rightSpeed;
		}
		SmartDashboard.putNumber("DB/String 2", leftS);
		SmartDashboard.putNumber("DB/String 3", rightS);
		if (leftS < -1) {
			left.set(-1);
		} else if (leftS > 1) {
			left.set(1);
		} else {
			left.set(leftS);
		} if (rightS < -1) {
			right.set(-1);
		} else if (rightS > 1) {
			right.set(1);
		} else {
			right.set(rightS);
		}
	}

}
