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
		
	    
		left.getSpeed();
		left.getSpeed();
		left.set(0.0);
		
		right.getPosition();
		right.getSpeed();
		right.setPosition(0.0);
		
		
		
		SmartDashboard.getNumber(" left get Position" ,left.getPosition() );
		SmartDashboard.getNumber(" left get Speed" ,left.getSpeed() );
		
		SmartDashboard.getNumber(" right get Position" ,right.getPosition() );
		SmartDashboard.getNumber(" right get Speed" ,right.getSpeed() );
		
		
		
		
		
		
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
	}

	/**
	 *
	 * @return The current speed of the conveyer motors (right motor)
	 */
	public double getSpeed() {
		
		
		return right.get();
	}

}
