package org.usfirst.frc.team5066.robot;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.usfirst.frc.team5066.controller2016.ControlScheme;
import org.usfirst.frc.team5066.controller2016.controlSchemes.OneXboxArcadeDrive;
import org.usfirst.frc.team5066.library.SingularityDrive;
import org.usfirst.frc.team5066.library.SingularityProperties;
import org.usfirst.frc.team5066.library.playback.Reader;
import org.usfirst.frc.team5066.library.playback.Recorder;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */

/* Reverted to Robot.java from master tag  51a2c4c */
public class Robot extends IterativeRobot {
	CameraServer cameraServer;
	int frontLeftMotor, rearLeftMotor, frontRightMotor, rearRightMotor;
	Joystick js;
	long initialTime;
	
	SingularityDrive drive;

	ControlScheme currentScheme;
	
	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	public void robotInit() {
		
		currentScheme = new OneXboxArcadeDrive(0);
		/*
		cameraServer = CameraServer.getInstance();
		cameraServer.setQuality(50);
		cameraServer.startAutomaticCapture();
		*/
		/*
		try {
			loadProperties();
		} catch (IOException ioe) {
			loadDefaultProperties();
		} finally {
		
		*/
		loadDefaultProperties();
			// Implement standard robotics things (input, drive, etc.)
			js = new Joystick(0);
			drive = new SingularityDrive(frontLeftMotor, rearLeftMotor, frontRightMotor, rearRightMotor);

			/*
			// Set up recordable autonomous mode
		}
		*/
	}

	public void disabledPeriodic() {
		/*
		if (recorder != null) {
			recorder.close();
			recorder = null;
		}

		if (reader != null) {
			reader.close();
			reader = null;
		}
		*/
	}

	public void autonomousInit() {
		
	}

	/**
	 * This function is called periodically during autonomous
	 */
	public void autonomousPeriodic() {
		
	}

	/**
	 * This function is called periodically during operator control
	 */
	public void teleopPeriodic() {
		currentScheme.arcadeDrive(drive, true);
		//drive.mecanum(js.getRawAxis(0), -js.getRawAxis(1), js.getRawAxis(4), true);
	}

	public void testInit() {
		
	}

	/**
	 * This function is called periodically during test mode
	 */
	public void testPeriodic() {
		
	}

	private void loadProperties() {
		/*
		frontLeftMotor = properties.getInt("frontLeftMotor");
		rearLeftMotor = properties.getInt("rearLeftMotor");
		frontRightMotor = properties.getInt("frontRightMotor");
		rearRightMotor = properties.getInt("rearRightMotor");

		record = properties.getBoolean("record");
		play = properties.getBoolean("play");
		recordURL = properties.getString("recordingURL");
		playURL = properties.getString("playURL");
		*/
	}

	private void loadDefaultProperties() {
		frontLeftMotor = 10;
		rearLeftMotor = 2;
		frontRightMotor = 1;
		rearRightMotor = 3;
		
		/*
		record = false;
		play = false;
		recordURL = "/home/lvuser/recording.json";
		playURL = "/home/lvuser/recording.json";
		*/
	}
}
