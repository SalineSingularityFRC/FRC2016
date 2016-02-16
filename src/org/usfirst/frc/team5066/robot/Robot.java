package org.usfirst.frc.team5066.robot;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.io.IOException;

import org.usfirst.frc.team5066.controller2016.ControlScheme;
import org.usfirst.frc.team5066.controller2016.controlSchemes.OneXboxArcadeDrive;
import org.usfirst.frc.team5066.library.SingularityDrive;
import org.usfirst.frc.team5066.library.SingularityProperties;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.Image;

public class Robot extends IterativeRobot {
	ControlScheme currentScheme;
	Image frame;
	int frontLeftMotor, rearLeftMotor, frontRightMotor, rearRightMotor, session;
	Joystick js;
	long initialTime;
	SingularityDrive drive;
	SingularityProperties properties;
	SingularityArm arm;
	SingularityConveyer conveyer;
	int driveControllerType;

	/*NOTE
	 * 
	 * Xbox Controller is always port 0
	 * Big Joystick is always port 1
	 * Little joystick is always port 2
	*/
	
	//enum for controller ports
	final int XBOX_PORT = 0;
	final int BIG_JOYSTICK_PORT = 1;
	final int SMALL_JOYSTICK_PORT = 2;
	
	public void robotInit() {
		
		//TODO change this so that default properties are loaded first and then the other properties are applied one by one. If one of them encounters an error, it just keeps the default value and moves on to the next property
		
		try {
			properties = new SingularityProperties("/home/lvuser/robot.properties");
			loadProperties();
		} catch (IOException ioe) {
			loadDefaultProperties();
		} finally {
			// Implement standard robotics things (input, drive, etc.). We will
			// need to make this use the new controller classes later.
			js = new Joystick(0);
			drive = new SingularityDrive(frontLeftMotor, rearLeftMotor, frontRightMotor, rearRightMotor, .5, SingularityDrive.CANTALON_DRIVE);
			arm = new SingularityArm(6, 7);
			
			frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);

			currentScheme = new OneXboxArcadeDrive(this.XBOX_PORT);
			
			// the camera name (ex. cam0) can be found through the roborio web
			// interface
			session = NIVision.IMAQdxOpenCamera("cam0", NIVision.IMAQdxCameraControlMode.CameraControlModeController);
			NIVision.IMAQdxConfigureGrab(session);
			NIVision.IMAQdxStartAcquisition(session);
			
			conveyer = new SingularityConveyer(8, 9);
			
			SmartDashboard.putString("DB/String 1", "" + driveControllerType);
		}
	}

	public void disabledPeriodic() {
		updateCamera(session, frame);
	}

	public void autonomousPeriodic() {
		updateCamera(session, frame);
	}

	public void teleopPeriodic() {
		updateCamera(session, frame);
		
		currentScheme.drive(drive, true);
		currentScheme.controlArm(arm);
		currentScheme.controlConveyer(conveyer);	
		/*
		drive.setReducedVelocity(0.5);
		drive.reduceVelocity(js.getRawButton(6));
		
		if(js.getRawButton(1)){
			drive.setVelocityMultiplier(.8);
		}
		else {
			drive.setVelocityMultiplier(1);
		}
		
		drive.tank(-js.getRawAxis(1), -js.getRawAxis(5), true);
		SmartDashboard.putNumber("Joystick Y", -js.getRawAxis(0));
		SmartDashboard.putNumber("Joystick X", js.getRawAxis(1));
		
		conveyer.setSpeed(js.getRawAxis(3) - js.getRawAxis(2));
		
		//drive.setReduceVelocity(js.getRawButton(6));
		
		
		
		//arm.setSpeed(js.getRawAxis(1));
		 * 
		 * 
		 */
		
		
	}

	public void testPeriodic() {
		updateCamera(session, frame);
		
	}

	private void loadProperties() {
		SmartDashboard.putString("DB/String 0", "No");
		
		//Ports
		frontLeftMotor = properties.getInt("frontLeftMotor");
		rearLeftMotor = properties.getInt("rearLeftMotor");
		frontRightMotor = properties.getInt("frontRightMotor");
		rearRightMotor = properties.getInt("rearRightMotor");
		
		//CANTalon or Talon drive?
		driveControllerType = properties.getInt("driveControllerType");

	}

	private void loadDefaultProperties() {
		SmartDashboard.putString("DB/String 0", "Yes");
		
		//CANTalon or Talon drive?
		driveControllerType = SingularityDrive.CANTALON_DRIVE;
		
		//Ports
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
