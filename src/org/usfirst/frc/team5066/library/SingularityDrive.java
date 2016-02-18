package org.usfirst.frc.team5066.library;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Class for driving around a simple robot. It is based on the
 * {@link edu.wpi.first.wpilibj.RobotDrive RobotDrive} class, which is provided
 * by <b>WPILib</b>
 * 
 * @author Saline Singularity 5066
 *
 */
public class SingularityDrive {
	private SpeedController m_frontLeftMotor, m_rearLeftMotor, m_frontRightMotor, m_rearRightMotor;
	
	private final static double DEFAULT_VELOCITY_MULTIPLIER = 1.0;
	private double velocityMultiplier = 1.0;
	
	private boolean buttonPressed = false;
	private double reducedVelocity;
	
	//Talon type enum
	public static final int CANTALON_DRIVE = 0;
	public static final int TALON_SR_DRIVE = 1;
	
	private static final int DEFAULT_TALON_TYPE = CANTALON_DRIVE;
	
	private int talonType;
	

	/**
	 * Constructor for {@link org.usfirst.frc.team5066.library.SingularityDrive
	 * SingularityDrive}. Takes in integers to use for motor ports. Allows the
	 * velocityMultiplier to be changed
	 * 
	 * @param frontLeftMotor
	 *            Channel for front left motor
	 * @param rearLeftMotor
	 *            Channel for rear left motor
	 * @param frontRightMotor
	 *            Channel for front right motor
	 * @param rearRightMotor
	 *            Channel for rear right motor
	 * @param velocityMultiplier
	 *            Limits the velocity by a factor of this.
	 * @param talonType
	 *            takes an enum value to determine the type of talons being used
	 *            in the drive
	 *
	 */
	public SingularityDrive(int frontLeftMotor, int rearLeftMotor, int frontRightMotor, int rearRightMotor, double m_velocityMultiplier, int talonType){
		this.talonType = talonType;

		if (talonType == CANTALON_DRIVE) {
			m_frontLeftMotor = new CANTalon(frontLeftMotor);
			m_rearLeftMotor = new CANTalon(rearLeftMotor);
			m_frontRightMotor = new CANTalon(frontRightMotor);
			m_rearRightMotor = new CANTalon(rearRightMotor);
			
			
		}
		else if(talonType == TALON_SR_DRIVE) {
			m_frontLeftMotor = new Talon(frontLeftMotor);
			m_rearLeftMotor = new Talon(rearLeftMotor);
			m_frontRightMotor = new Talon(frontRightMotor);
			m_rearRightMotor = new Talon(rearRightMotor);
		}
		else{
			SmartDashboard.putNumber("INVALID VALUE FOR TALON TYPE.      value=", talonType);
		}
		this.velocityMultiplier = m_velocityMultiplier;
	}
	
	/**
	 * Constructor for {@link org.usfirst.frc.team5066.library.SingularityDrive
	 * SingularityDrive}. Takes in integers to use for motor ports.
	 * 
	 * @param frontLeftMotor
	 *            Channel for front left motor
	 * @param rearLeftMotor
	 *            Channel for rear left motor
	 * @param frontRightMotor
	 *            Channel for front right motor
	 * @param rearRightMotor
	 *            Channel for rear right motor
	 */
	public SingularityDrive(int frontLeftMotor, int rearLeftMotor, int frontRightMotor, int rearRightMotor) {
		this(frontLeftMotor, rearLeftMotor, frontRightMotor, rearRightMotor, DEFAULT_VELOCITY_MULTIPLIER, DEFAULT_TALON_TYPE);
	}
	
	/**
	 * Constructor for {@link org.usfirst.frc.team5066.library.SingularityDrive
	 * SingularityDrive}. Takes in {@link edu.wpi.first.wpilibj.SpeedController
	 * SpeedControllers} as arguments.
	 * 
	 * @param frontLeftMotor
	 *            SpeedController for front left motor
	 * @param rearLeftMotor
	 *            SpeedController for rear left motor
	 * @param frontRightMotor
	 *            SpeedController for front right motor
	 * @param rearRightMotor
	 *            SpeedController for rear right motor
	 */
	public SingularityDrive(SpeedController frontLeftMotor, SpeedController rearLeftMotor,
			SpeedController frontRightMotor, SpeedController rearRightMotor, double velocityMultiplier) {
		m_frontLeftMotor = frontLeftMotor;
		m_rearLeftMotor = rearLeftMotor;
		m_frontRightMotor = frontRightMotor;
		m_rearRightMotor = rearRightMotor;
		this.velocityMultiplier = velocityMultiplier;
	}
	
	public SingularityDrive(SpeedController frontLeftMotor, SpeedController rearLeftMotor,
			SpeedController frontRightMotor, SpeedController rearRightMotor) {
		this(frontLeftMotor, rearLeftMotor, frontRightMotor, rearRightMotor, DEFAULT_VELOCITY_MULTIPLIER);
	}
	
	private double clamp(double velocityMultiplier){
		if(velocityMultiplier > 1.0){
			return 1.0;
		} else if(velocityMultiplier < -1.0){
			return -1.0;
		} else {
			return velocityMultiplier;
		}
	}

	public void setVelocityMultiplier(double velocityMultiplier){
		this.velocityMultiplier = this.clamp(velocityMultiplier);
	}
	
	public double getVelocityMultiplier(){
		return this.velocityMultiplier;
	}
	
	public void reduceVelocity(boolean reduceVelocityButton){
		this.buttonPressed = reduceVelocityButton;
	}
	
	public void setReducedVelocity(double reducedVelocity){
		this.reducedVelocity = reducedVelocity;
	}
	
	/**
	 * So called "arcade drive" method for driving a robot around. Drives much
	 * like one would expect a vehicle to move with a joy stick.
	 * 
	 * @param translation
	 *            Speed and direction at which to translate forwards
	 * @param rotation
	 *            Speed and direction at which to rotate clockwise
	 * @param squaredInputs
	 *            Whether or not to square the magnitude of the input values in
	 *            order to provide for finer motor control at lower velocities
	 */
	public void arcade(double translation, double rotation, boolean squaredInputs) {
		double translationVelocity = translation, rotationVelocity = rotation;
		// Do squared inputs if necessary
		if (squaredInputs) {
			translationVelocity *= Math.abs(translation);
			rotationVelocity *= Math.abs(rotation);
		}

		// Guard against illegal values
		double maximum = Math.max(1, Math.abs(translationVelocity) + Math.abs(rotationVelocity));
		
		if (buttonPressed) {
			maximum *= 1/reducedVelocity;
		}
		
		// Set the motors
		m_frontLeftMotor.set(this.velocityMultiplier * ((-translationVelocity + rotationVelocity) / maximum));
		m_rearLeftMotor.set(this.velocityMultiplier * (-translationVelocity + rotationVelocity) / maximum);
		m_frontRightMotor.set(this.velocityMultiplier * (translationVelocity + rotationVelocity) / maximum);
		m_rearRightMotor.set(this.velocityMultiplier * (translationVelocity + rotationVelocity) / maximum);
	}

	/**
	 * So called "arcade drive" method for driving a robot around. Drives much
	 * like one would expect a vehicle to move with a joy stick. This method
	 * does not assume squared inputs.
	 * 
	 * @param translation
	 *            Speed and direction at which to translate forwards
	 * @param rotation
	 *            Speed and direction at which to rotate clockwise
	 */
	public void arcade(double translation, double rotation) {
		// Just do the arcade without squared inputs
		this.arcade(translation, rotation, false);
		
	

	}

	/**
	 * A method for driving a robot with Mecanum wheels (allowing full
	 * translation and rotation). This function uses the algorithm found on
	 * <a href=
	 * "http://thinktank.wpi.edu/resources/346/ControllingMecanumDrive.pdf">this
	 * pdf</a>, which was created by FRC team 2022.
	 * 
	 * @param horizontal
	 *            Velocity at which to translate horizontally
	 * @param vertical
	 *            Velocity at which to translate vertically
	 * @param rotation
	 *            Velocity at which to rotate
	 * @param translationMultiplier
	 *            Constant to reduce the translation speed
	 * @param rotationMultiplier
	 *            Constant to reduce the rotation speed
	 * @param squaredInputs
	 *            Whether or not to square the magnitude of the input values in
	 *            order to provide for finer motor control at lower velocities
	 */
	public void mecanum(double horizontal, double vertical, double rotation,
			double rotationMultiplier, boolean squaredInputs) {

		double translationSpeed, direction, maximum, rotationVelocity;

		// Do squared inputs if necessary
		if (squaredInputs) {
			horizontal *= Math.abs(horizontal);
			vertical *= Math.abs(vertical);
			rotation *= Math.abs(rotation);
		}

		// Use the Pythagorean theorem to find the speed of translation
		translationSpeed = this.velocityMultiplier * Math.sqrt(Math.pow(horizontal, 2) + Math.pow(vertical, 2));

		rotationVelocity = this.velocityMultiplier * rotation * rotationMultiplier;

		// Use trigonometry to find the direction of travel
		direction = Math.PI / 4 + Math.atan2(vertical, horizontal);

		// Guard against illegal inputs
		maximum = Math.max(Math.max(Math.abs(Math.sin(direction)), Math.abs(Math.cos(direction))) * translationSpeed
				+ Math.abs(rotationVelocity), 1);
		
		if (buttonPressed) {
			maximum *= 1/reducedVelocity;
		}

		// Set the motors' speeds
		m_frontLeftMotor.set((translationSpeed * Math.sin(direction) + rotationVelocity) / maximum);
		m_rearLeftMotor.set((translationSpeed * -Math.cos(direction) + rotationVelocity) / maximum);
		m_frontRightMotor.set((translationSpeed * Math.cos(direction) + rotationVelocity) / maximum);
		m_rearRightMotor.set((translationSpeed * -Math.sin(direction) + rotationVelocity) / maximum);
	}

	/**
	 * A method for driving a robot with Mecanum wheels (allowing full
	 * translation and rotation). This function uses the algorithm found on
	 * <a href=
	 * "http://thinktank.wpi.edu/resources/346/ControllingMecanumDrive.pdf">this
	 * pdf</a>, which was created by FRC team 2022. This method does not assume
	 * squared inputs.
	 * 
	 * @param horizontal
	 *            Velocity at which to translate horizontally
	 * @param vertical
	 *            Velocity at which to translate vertically
	 * @param rotation
	 *            Velocity at which to rotate
	 * @param translationMultiplier
	 *            Constant to reduce the translation speed
	 * @param rotationMultiplier
	 *            Constant to reduce the rotation speed
	 */
	public void mecanum(double horizontal, double vertical, double rotation, double translationMultiplier,
			double rotationMultiplier) {
		// Just ignore squared inputs
		this.mecanum(horizontal, vertical, rotation, rotationMultiplier, false);
	}

	/**
	 * A method for driving a robot with Mecanum wheels (allowing full
	 * translation and rotation). This function uses the algorithm found on
	 * <a href=
	 * "http://thinktank.wpi.edu/resources/346/ControllingMecanumDrive.pdf">this
	 * pdf</a>, which was created by FRC team 2022. This method uses maximum
	 * magnitudes of 0.8 for the translation and rotation variables.
	 * 
	 * @param horizontal
	 *            Velocity at which to translate horizontally
	 * @param vertical
	 *            Velocity at which to translate vertically
	 * @param rotation
	 *            Velocity at which to rotate
	 * @param squaredInputs
	 *            Whether or not to square the magnitude of the input values in
	 *            order to provide for finer motor control at lower velocities
	 */
	public void mecanum(double horizontal, double vertical, double rotation, boolean squaredInputs) {
		// Set default values for multipliers
		this.mecanum(horizontal, vertical, rotation, 0.8, squaredInputs);
	}

	/**
	 * A method for driving a robot with Mecanum wheels (allowing full
	 * translation and rotation). This function uses the algorithm found on
	 * <a href=
	 * "http://thinktank.wpi.edu/resources/346/ControllingMecanumDrive.pdf">this
	 * pdf</a>, which was created by FRC team 2022. This method uses maximum
	 * magnitudes of 0.8 for the translation and rotation variables and does not
	 * assume squared inputs.
	 * 
	 * @param horizontal
	 *            Velocity at which to translate horizontally
	 * @param vertical
	 *            Velocity at which to translate vertically
	 * @param rotation
	 *            Velocity at which to rotate
	 */
	public void mecanum(double horizontal, double vertical, double rotation) {
		// Ignore squared inputs and use default values for multipliers
		this.mecanum(horizontal, vertical, rotation, 0.8, false);
	}

	/**
	 * Method for driving the robot like a tank. It uses two sticks: one for the
	 * left pair of wheels and one for the right pair of wheels.
	 * 
	 * @param left
	 *            Velocity at which to rotate the left set of wheels
	 *            (counterclockwise i.e. forwards)
	 * @param right
	 *            Velocity at which to rotate the right set of wheels (clockwise
	 *            i.e. forwards)
	 * @param squaredInputs
	 *            Whether or not to square the magnitude of the input values in
	 *            order to provide for finer motor control at lower velocities
	 */
	public void tank(double left, double right, boolean squaredInputs) {
		double leftVelocity = left, rightVelocity = right;

		// Do squared inputs if necessary
		if (squaredInputs) {
			leftVelocity *= Math.abs(left);
			rightVelocity *= Math.abs(right);
		}
		SmartDashboard.putNumber("Post-sqaring inputs - Left Velocity", leftVelocity);
		SmartDashboard.putNumber("Post-sqaring inputs - Right Velocity", rightVelocity);

		
		// Guard against illegal inputs
		leftVelocity /= Math.max(1, Math.abs(leftVelocity));
		rightVelocity /= Math.max(1, Math.abs(rightVelocity));
		
		SmartDashboard.putNumber("Clamped Value - Left Velocity", leftVelocity);
		SmartDashboard.putNumber("Clamped Value - Right Velocity", rightVelocity);

		
		if (buttonPressed) {
			leftVelocity *= reducedVelocity;
			rightVelocity *= reducedVelocity;
		}
		
		SmartDashboard.putNumber("Reduced Velocity - Left", leftVelocity);
		SmartDashboard.putNumber("Reduced Velocity - Right", rightVelocity);
		

		// Set the motors' speeds
		m_frontLeftMotor.set(this.velocityMultiplier * leftVelocity);
		m_rearLeftMotor.set(this.velocityMultiplier * leftVelocity);
		m_frontRightMotor.set(this.velocityMultiplier * -rightVelocity);
		m_rearRightMotor.set(this.velocityMultiplier * -rightVelocity);
	}

	/**
	 * Method for driving the robot like a tank. It uses two sticks: one for the
	 * left pair of wheels and one for the right pair of wheels. This method
	 * does not assume squared inputs.
	 * 
	 * @param left
	 *            Velocity at which to rotate the left set of wheels
	 *            (counterclockwise i.e. forwards)
	 * @param right
	 *            Velocity at which to rotate the right set of wheels (clockwise
	 *            i.e. forwards)
	 */
	public void tank(double left, double right) {
		// Just ignore squared inputs
		this.tank(left, right, false);
	}
}
