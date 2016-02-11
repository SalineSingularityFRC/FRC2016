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
	int frontLeftMotor, rearLeftMotor, frontRightMotor, rearRightMotor;
	Joystick js;
	long initialTime;
	SingularityDrive drive;
	SingularityProperties properties;
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
			
			frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
			
			//the camera name (ex. cam0) can be found through the roborio web interface
			session = NIVision.IMAQdxOpenCamera("cam0",
					 NIVision.IMAQdxCameraControlMode.CameraControlModeController);
			NIVision.IMAQdxConfigureGrab(session);
			NIVision.IMAQdxStartAcquisition(session);
		}
	}

	public void disabledPeriodic() {
		updateCamera(session, frame);
	}

	public void autonomousInit() {
		//Yep
	}

	/**
	 * This function is called periodically during autonomous
	 */
	public void autonomousPeriodic() {
		updateCamera(session, frame);
	}

	/**
	 * This function is called periodically during operator control
	 */
	public void teleopPeriodic() {
		
		updateCamera(session, frame);
	}

	public void testInit() {
		//Testy
	}

	/**
	 * This function is called periodically during test mode
	 */
	public void testPeriodic() {
		updateCamera(session, frame);
	}

	private void loadProperties() {
		frontLeftMotor = properties.getInt("frontLeftMotor");
		rearLeftMotor = properties.getInt("rearLeftMotor");
		frontRightMotor = properties.getInt("frontRightMotor");
		rearRightMotor = properties.getInt("rearRightMotor");
	}

	private void loadDefaultProperties() {
		frontLeftMotor = 10;
		rearLeftMotor = 2;
		frontRightMotor = 1;
		rearRightMotor = 3;
	}
	
	private void updateCamera(int session, Image frame) {
		NIVision.IMAQdxGrab(session, frame, 1);
		CameraServer.getInstance().setImage(frame);
	}
}
