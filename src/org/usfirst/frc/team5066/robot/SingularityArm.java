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

	private SpeedController left, right;
	private double maxSpeed;
	
	/**
	 * When true, limit switches will not automatically stop the arm from moving. 
	 * The input and effect of the switches must coded elsewhere.
	 */
	private boolean limitSwitchesOverride;
	
	/**
	 * Constructor for singularity conveyer.
	 * 
	 * @param l
	 *            <b>int</b> The left side motor channel
	 * @param r
	 *            <b>int</b> The right side motor channel
	 * @param maxSpeed
	 *            <b>double</b> The maximum allowed speed of the conveyor. Any input values <br/>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
	 *            outside the range from -maximum to maximum will be replaced with the maximum speed
	 */
	public SingularityArm(int l, int r, double maxSpeed){
		left = new CANTalon(l);
		right = new CANTalon(r);
		this.maxSpeed = maxSpeed;
		limitSwitchesOverride = false;
	}
	
	/**
	 * Sets the speed of both motors in the same direction. Positive is forward,
	 * negative is backwards.
	 * 
	 * @param speed <b>double</b> The desired analog speed of the motors. [-1.0, 1.0]
	 */
	public void setSpeed(double speed){
		// Checks for illegal values (and deports them back to where they came, those bastards)
				if ((speed < -maxSpeed)) {
					left.set(maxSpeed);
					right.set(-maxSpeed);
				} else if (speed > maxSpeed) {
					left.set(-maxSpeed);
					right.set(maxSpeed);
				} else {
					// sets both motor speeds to move in the same direction
					left.set(-speed);
					right.set(speed);
				}
	}
	
	/**
	 *
	 * @return The current speed of the conveyer motors (right motor)
	 */
	public double getSpeed(){
		return right.get();
	}
}
