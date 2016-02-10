package org.usfirst.frc.team5066.robot;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.usfirst.frc.team5066.controller2016.ControlScheme;
import org.usfirst.frc.team5066.library.SingularityDrive;
import org.usfirst.frc.team5066.library.SingularityProperties;
import org.usfirst.frc.team5066.library.playback.Reader;
import org.usfirst.frc.team5066.library.playback.Recorder;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.Image;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	boolean record, play;
	int frontLeftMotor, rearLeftMotor, frontRightMotor, rearRightMotor;
	Joystick js;
	long initialTime;
	Reader reader;
	Recorder recorder;
	SingularityDrive drive;
	SingularityProperties properties;
	String recordURL, playURL;
	int session;
	Image frame;

	ControlScheme currentScheme;
	
	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	public void robotInit() {
		try {
			properties = new SingularityProperties("/home/lvuser/robot.properties");
			loadDefaultProperties();
		} catch (IOException ioe) {
			loadDefaultProperties();
		} finally {
			// Implement standard robotics things (input, drive, etc.)
			js = new Joystick(0);
			drive = new SingularityDrive(frontLeftMotor, rearLeftMotor, frontRightMotor, rearRightMotor);

			// Set up recordable autonomous mode
			record = false;
			play = false;
			reader = null;
			recorder = null;
			
			frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
			//the camera name (ex. cam0) can be found through the roborio web interface
			session = NIVision.IMAQdxOpenCamera("cam0",
					 NIVision.IMAQdxCameraControlMode.CameraControlModeController);
			NIVision.IMAQdxConfigureGrab(session);
			NIVision.IMAQdxStartAcquisition(session);
		}
	}

	public void disabledPeriodic() {
		if (recorder != null) {
			recorder.close();
			recorder = null;
		}

		if (reader != null) {
			reader.close();
			reader = null;
		}
		
		updateCamera(session, frame);
	}

	public void autonomousInit() {
		if (play) {
			try {
				reader = new Reader(playURL);
				initialTime = System.currentTimeMillis();
			} catch (FileNotFoundException fnfe) {
				reader = null;
			} catch (ParseException pe) {
				reader = null;
			}
		}
	}

	/**
	 * This function is called periodically during autonomous
	 */
	public void autonomousPeriodic() {
		if (reader != null) {
			JSONObject current = reader.getDataAtTime(System.currentTimeMillis() - initialTime);
			drive.mecanum(Double.parseDouble(current.get("x").toString()),
					Double.parseDouble(current.get("y").toString()), Double.parseDouble(current.get("z").toString()));
		}
		
		updateCamera(session, frame);
	}

	/**
	 * This function is called periodically during operator control
	 */
	public void teleopPeriodic() {
		//drive.mecanum(js.getRawAxis(0), -js.getRawAxis(1), js.getRawAxis(4), true);
		
		updateCamera(session, frame);
	}

	public void testInit() {
		if (record) {
			recorder = new Recorder(new String[] { "x", "y", "z" }, new Object[] { 0, 0, 0 }, recordURL);
		}
	}

	/**
	 * This function is called periodically during test mode
	 */
	public void testPeriodic() {
		if (recorder != null) {
			Object[] input = new Object[] { js.getRawAxis(0), -js.getRawAxis(1), js.getRawAxis(4) };

			drive.mecanum((double) input[0], (double) input[1], (double) input[2]);
			recorder.appendData(input);
		} else {
			drive.mecanum(js.getRawAxis(0), -js.getRawAxis(1), js.getRawAxis(4), true);
		}
		
		updateCamera(session, frame);
	}

	private void loadProperties() {
		frontLeftMotor = properties.getInt("frontLeftMotor");
		rearLeftMotor = properties.getInt("rearLeftMotor");
		frontRightMotor = properties.getInt("frontRightMotor");
		rearRightMotor = properties.getInt("rearRightMotor");

		record = properties.getBoolean("record");
		play = properties.getBoolean("play");
		recordURL = properties.getString("recordingURL");
		playURL = properties.getString("playURL");
	}

	private void loadDefaultProperties() {
		frontLeftMotor = 6;
		rearLeftMotor = 5;
		frontRightMotor = 7;
		rearRightMotor = 4;

		record = false;
		play = false;
		recordURL = "/home/lvuser/recording.json";
		playURL = "/home/lvuser/recording.json";
	}
	
	private void updateCamera(int session, Image frame) {
		NIVision.IMAQdxGrab(session, frame, 1);
		CameraServer.getInstance().setImage(frame);
	}
}
