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

    private double slowSpeedConstant, normalSpeedConstant, fastSpeedConstant;

    private SpeedController m_frontLeftMotor, m_rearLeftMotor, m_frontRightMotor, m_rearRightMotor;

    private final static double DEFAULT_VELOCITY_MULTIPLIER = 1.0;
    private double velocityMultiplier = 1.0;

    private boolean buttonPressed = false;
    private double reducedVelocity;

    private final static double DEFAULT_MINIMUM_THRESHOLD = 0.09;

    public static boolean isreverse = false;
    private static boolean reverseB = false;

    // Talon type enum
    public static final int CANTALON_DRIVE = 0;
    public static final int TALON_SR_DRIVE = 1;

    private static final int DEFAULT_TALON_TYPE = CANTALON_DRIVE;
    private final static double DEFAULT_SLOW_SPEED_CONSTANT = 0.4;
    private final static double DEFAULT_NORMAL_SPEED_CONSTANT = 0.8;
    private final static double DEFAULT_FAST_SPEED_CONSTANT = 1.0;

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
     *
     */
    public SingularityDrive(int frontLeftMotor, int rearLeftMotor, int frontRightMotor, int rearRightMotor,
            int talonType, double slowSpeedConstant, double normalSpeedConstant, double fastSpeedConstant) {

        if (talonType == CANTALON_DRIVE) {
            m_frontLeftMotor = new CANTalon(frontLeftMotor);
            m_rearLeftMotor = new CANTalon(rearLeftMotor);
            m_frontRightMotor = new CANTalon(frontRightMotor);
            m_rearRightMotor = new CANTalon(rearRightMotor);

        } else if (talonType == TALON_SR_DRIVE) {
            m_frontLeftMotor = new Talon(frontLeftMotor);
            m_rearLeftMotor = new Talon(rearLeftMotor);
            m_frontRightMotor = new Talon(frontRightMotor);
            m_rearRightMotor = new Talon(rearRightMotor);
        } else {
            SmartDashboard.putNumber("INVALID VALUE FOR TALON TYPE.      value=", talonType);
        }

        this.velocityMultiplier = normalSpeedConstant;
        this.talonType = talonType;
        this.slowSpeedConstant = slowSpeedConstant;
        this.normalSpeedConstant = normalSpeedConstant;
        this.fastSpeedConstant = fastSpeedConstant;
    }

    /**
     * Constructor for {@link org.usfirst.frc.team5066.library.SingularityDrive
     * SingularityDrive}. Takes in integers to use for motor ports.
     * 
     * <b>Uses CANTalons by default.</b>
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
        this(frontLeftMotor, rearLeftMotor, frontRightMotor, rearRightMotor, DEFAULT_TALON_TYPE,
                DEFAULT_SLOW_SPEED_CONSTANT, DEFAULT_NORMAL_SPEED_CONSTANT, DEFAULT_FAST_SPEED_CONSTANT);
    }

    /**
     * A utility method. It restricts a value between -1 and 1 (an alternative
     * but less readable implementation is
     * {@code return velocityMultiplier / Math.max(Math.abs(val), 1);}
     * 
     * @param val
     *            The number to restrict
     * @return The value in the range [-1.0, 1.0]
     */
    private double clamp(double val) {
        if (val > 1.0) {
            return 1.0;
        } else if (val < -1.0) {
            return -1.0;
        } else {
            return val;
        }
    }

    /**
     * Sets the velocityMultiplier
     * 
     * @param velocityMultiplier
     *            The new value
     */
    public void setVelocityMultiplier(double velocityMultiplier) {
        this.velocityMultiplier = this.clamp(velocityMultiplier);
    }

    /**
     * Accesses the velocityMultiplier
     * 
     * @return The current velocityMultiplier
     */
    public double getVelocityMultiplier() {
        return this.velocityMultiplier;
    }

    /**
     * Sets whether the reduced velocity button was pressed
     * 
     * @param reduceVelocityButton
     *            Whether the button was pressed
     */
    public void reduceVelocity(boolean reduceVelocityButton) {
        this.buttonPressed = reduceVelocityButton;
    }

    /**
     * Sets the reducedVelocity
     * 
     * @param reducedVelocity
     *            Why are you reading this? See above (but kudos for noticing
     *            this)
     */
    public void setReducedVelocity(double reducedVelocity) {
        this.reducedVelocity = reducedVelocity;
    }

    /**
     * Tests (in a somewhat primitive fashion) whether a control value is
     * 
     * @param velocity
     * @return
     */
    private double threshold(double velocity) {
        if (Math.abs(velocity) <= DEFAULT_MINIMUM_THRESHOLD) {
            return 0;
        }
        return velocity;
    }

    //DO WE EVEN USE THIS?????
    /*
     * // reverse drive method for booleans. You have to hold the button to //
     * reverse. This method is used in control schemes to plug-in to SingDrive.
     * public static int booleanHoldReverse(boolean reverse) { if (reverse) {
     * return 180; } else { return 0; } } // In the next method, you can toggle
     * using 2 boolean buttons.
     * 
     * public static int booleanToggleReverse(boolean forward, boolean reverse)
     * { if (reverse) reverseB = true; else if (forward) reverseB = false; if
     * (reverseB) return 180; else return 0; }
     */

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
     * @param speedMode
     *            The enum value corrresponding to the current speed mode: slow,
     *            normal, or fast
     * @param reverse
     *            The value (180 or 0) to control reverse drive. 180 is reverse,
     *            0 is forward
     */
    public void arcade(double translation, double rotation, boolean squaredInputs, SpeedMode speedMode, int reverse) {
        double translationVelocity = translation, rotationVelocity = rotation;

        setVelocityMultiplerBasedOnSpeedMode(speedMode);

        // Do squared inputs if necessary
        if (squaredInputs) {
            translationVelocity *= Math.abs(translation);
            rotationVelocity *= Math.abs(rotation);
        }

        // Flip the chassis if necessary
        if (reverse == 180)
            isreverse = true;
        else if (reverse == 0)
            isreverse = false;
        if (isreverse) {
            translationVelocity = -translationVelocity;
            rotationVelocity = -rotationVelocity;
        }

        // Guard against illegal values
        double maximum = Math.max(1, Math.abs(translationVelocity) + Math.abs(rotationVelocity));

        if (buttonPressed) {
            maximum /= reducedVelocity;
        }

        translationVelocity = threshold(translationVelocity);
        rotationVelocity = threshold(rotationVelocity);

        // Set the motors
        m_frontLeftMotor.set(this.velocityMultiplier * ((-translationVelocity + rotationVelocity) / maximum));
        m_rearLeftMotor.set(this.velocityMultiplier * ((-translationVelocity + rotationVelocity) / maximum));
        m_frontRightMotor.set(this.velocityMultiplier * ((translationVelocity + rotationVelocity) / maximum));
        m_rearRightMotor.set(this.velocityMultiplier * ((translationVelocity + rotationVelocity) / maximum));
    }

    /**
     * Drive the robot in arcade mode (forward and backwards translate, left and
     * right rotate).
     * 
     * @param translation
     *            How much to translate by. Forwards is positive.
     * @param rotation
     *            How much to rotate by. Clockwise is positive (I think. You
     *            might want to test this)
     * @param squaredInputs
     *            Whether or not to use squared inputs
     * @param reverse
     *            Whether or not to flip the chassis around
     */
    public void arcade(double translation, double rotation, boolean squaredInputs, int reverse) {
        this.arcade(translation, rotation, squaredInputs, SpeedMode.NORMAL, reverse);
    }

    /**
     * Choose how much to multiply the speed by based on the speed mode
     * 
     * @param speedMode
     *            Which speed mode to use. Use constants found in
     *            {@link org.usfirst.frc.team5066.library.SingularityDrive this}
     *            class
     */
    private void setVelocityMultiplerBasedOnSpeedMode(SpeedMode speedMode) {
        switch (speedMode) {
            case SLOW:
                velocityMultiplier = this.slowSpeedConstant;
                SmartDashboard.putString("DB/String 8", "Using slow speed constant");
                break;
            case NORMAL:
                velocityMultiplier = this.normalSpeedConstant;
                SmartDashboard.putString("DB/String 8", "Using normal speed constant");
                break;
            case FAST:
                velocityMultiplier = this.fastSpeedConstant;
                SmartDashboard.putString("DB/String 8", "Using fast speed constant");
                break;
        }
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
        // Just do the arcade without squared inputs at normal speed mode
        this.arcade(translation, rotation, false, SpeedMode.NORMAL, 0);
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
    public void mecanum(double horizontal, double vertical, double rotation, double rotationMultiplier,
            boolean squaredInputs) {

        double translationVelocity, direction, maximum, rotationVelocity;

        // Do squared inputs if necessary
        if (squaredInputs) {
            horizontal *= Math.abs(horizontal);
            vertical *= Math.abs(vertical);
            rotation *= Math.abs(rotation);
        }

        // Use the Pythagorean theorem to find the speed of translation
        translationVelocity = this.velocityMultiplier * Math.sqrt(Math.pow(horizontal, 2) + Math.pow(vertical, 2));
        rotationVelocity = this.velocityMultiplier * rotation * rotationMultiplier;

        // Use trigonometry to find the direction of travel
        direction = Math.PI / 4 + Math.atan2(vertical, horizontal);

        // Guard against illegal inputs
        maximum = Math.max(Math.max(Math.abs(Math.sin(direction)), Math.abs(Math.cos(direction))) * translationVelocity
                + Math.abs(rotationVelocity), 1);

        if (buttonPressed) {
            maximum *= 1 / reducedVelocity;
        }

        translationVelocity = threshold(translationVelocity);
        rotationVelocity = threshold(rotationVelocity);

        // Set the motors' speeds
        m_frontLeftMotor.set((translationVelocity * Math.sin(direction) + rotationVelocity) / maximum);
        m_rearLeftMotor.set((translationVelocity * -Math.cos(direction) + rotationVelocity) / maximum);
        m_frontRightMotor.set((translationVelocity * Math.cos(direction) + rotationVelocity) / maximum);
        m_rearRightMotor.set((translationVelocity * -Math.sin(direction) + rotationVelocity) / maximum);
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
    public void tank(double left, double right, boolean squaredInputs, SpeedMode speedMode) {
        double leftVelocity = left, rightVelocity = right;

        this.setVelocityMultiplerBasedOnSpeedMode(speedMode);

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

        SmartDashboard.putNumber("Reduced Velocity - Left", leftVelocity);
        SmartDashboard.putNumber("Reduced Velocity - Right", rightVelocity);

        leftVelocity = threshold(leftVelocity);
        rightVelocity = threshold(rightVelocity);

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
    public void tank(double left, double right, SpeedMode speedMode) {
        // Just ignore squared inputs
        this.tank(left, right, false, speedMode);
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
     * @param squaredInputs
     *            Whether or not to use squared inputs
     */
    public void tank(double left, double right, boolean squaredInputs) {
        // Just ignore squared speedMode
        this.tank(left, right, true, SpeedMode.NORMAL);
    }
}