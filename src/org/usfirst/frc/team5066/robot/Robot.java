package org.usfirst.frc.team5066.robot;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.json.simple.JSONObject;
import org.usfirst.frc.team5066.controller2016.ControlScheme;
import org.usfirst.frc.team5066.controller2016.XboxController;
import org.usfirst.frc.team5066.controller2016.controlSchemes.GTADrive;
import org.usfirst.frc.team5066.controller2016.controlSchemes.RegularDrive;
import org.usfirst.frc.team5066.library.SingularityDrive;
import org.usfirst.frc.team5066.library.SingularityProperties;
import org.usfirst.frc.team5066.library.SingularityPropertyNotFoundException;
import org.usfirst.frc.team5066.library.playback.Reader;
import org.usfirst.frc.team5066.library.playback.Recorder;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.Image;

public class Robot extends IterativeRobot {
	
	//Holds the current control scheme
	ControlScheme currentScheme;
	
	//used for vision
	Image frame;
	int session;
	
	//Holds the integer port id's for the motors. The values are assigned when properties are laoded.
	int frontLeftMotor, rearLeftMotor, frontRightMotor, rearRightMotor;
	int armLeftWorm, armLeftPlanetary, armRightWorm, armRightPlanetary;
	int leftConveyerMotor, rightConveyerMotor;
	
	//Holds the drive speed constants (loaded from properties file) used in the 3-speed drive
	double slowSpeedConstant;
	double normalSpeedConstant;
	double fastSpeedConstant;
	
	//Holds the arm speed constants (loaded from properties file) used in the 3-speed drive
	double armSpeedConstant;
	double armSpeedConstantFAST;

	//The arm limit (loaded from the properties file) that is passed to the arm object
	double armLimit;
	
	
	Joystick js;
	XboxController xbox;
	
	//Holds the object representing the properties file
	SingularityProperties properties;
	
	//Variables for the subsystems
	SingularityDrive drive;
	SingularityArm arm;
	SingularityConveyer conveyor;
	SingularityClimber climber;
	
	//
	int driveControllerType;
	
	//Used in drive mode toggling
	private boolean aButtonWasPressed;
	
	//Used when testing the arm - setting 'true' causes only the arm to be driveable
	boolean armOnly;
	
	//Setting to 'true' will disable the camera
	boolean noCamera;
	
	/*
	 * Constants for the joysticks. Based on the DriverStation configuration
	 * In order for this code to work, the Joysticks must have these settings in Driver Station
	 * 
	 * NOTE
	 * 
	 * Xbox Controller is always port 0 Big Joystick is always port 1 Little
	 * joystick is always port 2
	 */
	final int XBOX_PORT = 0;
	final int BIG_JOYSTICK_PORT = 1;
	final int SMALL_JOYSTICK_PORT = 2;

	/*
	 * Options for using recordable autonomous mode.
	 */
	boolean record, play;
	long initialTime;
	Reader reader;
	Recorder recorder;
	String recordingURL;

	
	public void robotInit() {
		noCamera = false;
		
		//sets the properties variable to the current SingularityProperties object
		try {
			properties = new SingularityProperties("/home/lvuser/robot.properties");
		} catch (Exception e) {
			properties = new SingularityProperties();
			// TODO is getInstance() necessary?
			// DriverStation.getInstance();
			DriverStation.reportError("It looks like there was an error finding the properties file... probably. \n",
					true);
		} finally {
			// This must always come before loadProperties
			setDefaultProperties();

			// LoadProperties should no longer throw errors.
			// It also includes automatic fallback to default properties for
			// each property individually if a file property is not found, as
			// long as they have been set already.
			loadProperties();

			// Implement standard robotics things (input, drive, etc.).
			js = new Joystick(0);
			drive = new SingularityDrive(frontLeftMotor, rearLeftMotor, frontRightMotor, rearRightMotor,
					this.driveControllerType, slowSpeedConstant, normalSpeedConstant, fastSpeedConstant);
			arm = new SingularityArm(6, armSpeedConstant, armSpeedConstantFAST, armLimit);
			conveyor = new SingularityConveyer(8, 6);
			climber = new SingularityClimber(11, 0.69); // Might be 11 or 12

			xbox = new XboxController(1);

			// currentScheme = new OneXboxArcadeDrive(this.XBOX_PORT);

			currentScheme = new RegularDrive(0, 1);
			aButtonWasPressed = false;

			SmartDashboard.putString("DB/String 1", "" + driveControllerType);

			// Camera setup code
			try {
				// the camera name (ex. cam0) can be found through the roborio
				// web interface

				frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);

				session = NIVision.IMAQdxOpenCamera("cam0",
						NIVision.IMAQdxCameraControlMode.CameraControlModeController);

				NIVision.IMAQdxConfigureGrab(session);
				NIVision.IMAQdxStartAcquisition(session);

			} catch (Exception e) {
				e.printStackTrace();
				noCamera = true;
				DriverStation.reportError("No Camera found on code startup", false);
			}
			SmartDashboard.putString("recordingURL", recordingURL);
		}
	}

	public void disabledInit() {
		// Closes all readers and recorder (allows files to close and/or save
		if (recorder != null) {
			recorder.close();
			recorder = null;
		}
		if (reader != null) {
			reader.close();
			reader = null;
		}
	}

	public void disabledPeriodic() {
		// Keeps the camera going even if the robot is not enabled
		if(noCamera = false) {
			updateCamera(session, frame);
		}
	}

	public void autonomousInit() {
		// Recordable autonomous 
		if (play) {
			try {
				reader = new Reader(recordingURL);
				initialTime = System.currentTimeMillis();
			} catch (Exception e) {
				reader = null;
				e.printStackTrace();
			}
		}
	}

	public void autonomousPeriodic() {
		// Recordable autonomous
		if (reader != null) {
			JSONObject current = reader.getDataAtTime(System.currentTimeMillis() - initialTime);
			drive.arcade((double) current.get("v"), (double) current.get("omega"), true, 0);
			arm.setRawSpeed((double) current.get("arm"));
			conveyor.setSpeed((double) current.get("intake"));
		}

		// Keeps the camera going so the driver can always see what the robot
		// can
		updateCamera(session, frame);

	}

	public void teleopPeriodic() {

		if (armOnly) {
			currentScheme.controlArm(arm);
		} else {

			currentScheme.drive(drive, true);
			currentScheme.controlConveyer(conveyor);
			currentScheme.controlClimber(climber);
		}
		
		toggleDriveMode();
		SmartDashboard.putString("Drive Mode", currentScheme instanceof GTADrive ? "GTA Drive" : "Regular Drive");

		drive.reduceVelocity(xbox.getRB());
		drive.setReducedVelocity(0.5);
		
		updateCamera(session, frame);
	}

	public void testInit() {
		if (record) {
			// This initializes the recorder. The former parameter is the keys,
			// and the latter is the defaults to use.
			recorder = new Recorder(new String[] { "v", "omega", "arm", "intake" }, new Object[] { 0.0, 0.0, 0.0, 0.0 },
					recordingURL);
		}
	}

	public void testPeriodic() {
		if (recorder != null) {
			Object[] input = new Object[] { xbox.getLS_Y(), xbox.getRS_X(), js.getY(),
					xbox.getTriggerLeft() - xbox.getTriggerRight() };

			// Do stuff to drive with the inputs.
			drive.arcade((double) input[0], (double) input[1], true, 0);
			arm.setRawSpeed((double) input[2]);
			conveyor.setSpeed((double) input[3]);

			recorder.appendData(input);
		}

		// Yeah, you go camera!
		updateCamera(session, frame);
	}

	/**
	 * Toggles the drive mode when the A buttton on the xbox controller is pressed and wait until it is released to be able to use again
	 */
	private void toggleDriveMode() {
		if (xbox.getAButton()) {
			if (!aButtonWasPressed) {
				if (currentScheme instanceof RegularDrive) {
					currentScheme = new GTADrive(0, 1);
				} else {
					currentScheme = new RegularDrive(0, 1);
				}
			}
			aButtonWasPressed = true;
		} else {
			aButtonWasPressed = false;
		}
	}

	/**
	 * sets all fields to the proper values loaded from properties
	 */
	private void loadProperties() {
		try {
			// Ports
			frontLeftMotor = properties.getInt("frontLeftMotor");
			rearLeftMotor = properties.getInt("rearLeftMotor");
			frontRightMotor = properties.getInt("frontRightMotor");
			rearRightMotor = properties.getInt("rearRightMotor");

			// CANTalon or Talon drive?
			driveControllerType = properties.getInt("driveControllerType");

			armLeftWorm = properties.getInt("armLeftWorm");
			armLeftPlanetary = properties.getInt("armLeftPlanetary");
			armRightWorm = properties.getInt("armRightWorm");
			armRightPlanetary = properties.getInt("armRightPlanetary");

			slowSpeedConstant = properties.getDouble("slowSpeedConstant");
			normalSpeedConstant = properties.getDouble("normalSpeedConstant");
			fastSpeedConstant = properties.getDouble("fastSpeedConstant");

			armSpeedConstant = properties.getDouble("armSpeedConstant");
			armSpeedConstantFAST = properties.getDouble("armSpeedConstantFAST");

			armLimit = properties.getDouble("armLimit");

			play = properties.getBoolean("play");
			record = properties.getBoolean("record");
			recordingURL = properties.getString("recordingURL");
			
			armOnly = properties.getBoolean("armOnly");

		} catch (SingularityPropertyNotFoundException e) {
			DriverStation.reportError(
					"The property \"" + e.getPropertyName()
							+ " was not found --> CODE CRASHED!!!!!! \n _POSSIBLE CAUSES:\n - Property missing in file and defaults"
							+ "\n - Typo in property name in code or file\n - using a different properties file than the one that actually contains the property ou are looking for",
					false);
			e.printStackTrace();
		}

		SmartDashboard.putString("DB/String 9",
				"slow: " + slowSpeedConstant + " | normal: " + normalSpeedConstant + " | fast: " + fastSpeedConstant);

	}

	/**
	 * Sets all default properties
	 */
	private void setDefaultProperties() {
		// Drive ports
		properties.addDefaultProp("frontLeftMotor", 10);
		properties.addDefaultProp("rearLeftMotor", 4);
		properties.addDefaultProp("frontRightMotor", 1);
		properties.addDefaultProp("rearRightMotor", 3);

		// Arm ports
		properties.addDefaultProp("armLeftWorm", 2);
		properties.addDefaultProp("armLeftPlanetary", 9);
		properties.addDefaultProp("armRightWorm", 7);
		properties.addDefaultProp("armRightPlanetary", 5);

		// Conveyer Ports
		properties.addDefaultProp("leftConveyerMotor", 8);
		properties.addDefaultProp("rightConveyerMotor", 6);

		// Speed Constants
		properties.addDefaultProp("slowSpeedConstant", 0.4);
		properties.addDefaultProp("normalSpeedConstant", 0.8);
		properties.addDefaultProp("fastSpeedConstant", 1.0);

		properties.addDefaultProp("play", true);
		properties.addDefaultProp("record", false);
		properties.addDefaultProp("recordingURL", "/home/lvuser/default.json");

		properties.addDefaultProp("armSpeedConstant", 0.5);
		properties.addDefaultProp("armSpeedConstantFAST", 1.0);

		properties.addDefaultProp("armLimit", -4700.0);
		properties.addDefaultProp("armOnly", false);
	}

	//updates the camera
	private void updateCamera(int session, Image frame) {
		if (noCamera = false) {
			try {
				NIVision.IMAQdxGrab(session, frame, 1);
				CameraServer.getInstance().setImage(frame);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
