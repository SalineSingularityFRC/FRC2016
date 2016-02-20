package org.usfirst.frc.team5066.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.SpeedController;

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

	/**
	 * Constructor for singularity conveyer.
	 * 
	 * @param lWorm
	 *            <b>int</b> The left side worm motor channel
     * @param lPlanet
     * 			  <b>int</b> The left side planetary motor channel
	 * @param rWorm
	 *            <b>int</b> The right side worm motor channel
	 * @param rPlanet
	 *            <b>int</b> The right side planetary motor channel
	 */
	public SingularityArm(int lWorm, int lPlanet, int rWorm, int rPlanet) {
		leftWorm = new CANTalon(lWorm);
		leftPlanet = new CANTalon(lPlanet);
		rightWorm = new CANTalon(rWorm);
		rightPlanet = new CANTalon(rPlanet);
		limitSwitchesOverride = false;
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
			speed = -1;
		} else if (speed > 1) {
			speed = 1;
		}

		// sets both motor speeds to move in the same direction
		leftWorm.set(speed);
		leftPlanet.set(speed);
		rightWorm.set(speed);
		rightPlanet.set(-speed);		

		// code for limit switches
		if (limitSwitchesOverride) {
			if (speed > 0)
				speed = 0;
		}
	}

	/**
	 *
	 * @return The current speed of the conveyer motors (right motor)
	 */
	public double getSpeed() {
		return rightWorm.get();
	}
}
