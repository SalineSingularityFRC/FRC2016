package org.usfirst.frc.team5066.robot;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.io.IOException;

import org.usfirst.frc.team5066.controller2016.ControlScheme;
import org.usfirst.frc.team5066.controller2016.controlSchemes.OneXboxArcadeDrive;
import org.usfirst.frc.team5066.controller2016.controlSchemes.OneXboxGTADrive;
import org.usfirst.frc.team5066.controller2016.controlSchemes.OneXboxTankDrive;
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

	public void robotInit() {
		try {
			properties = new SingularityProperties("/home/lvuser/robot.properties");
			loadDefaultProperties();
		} catch (IOException ioe) {
			loadDefaultProperties();
		} finally {
			
			CameraServer server = CameraServer.getInstance();
			server.setQuality(50);
			server.startAutomaticCapture("cam0");
			
			// Implement standard robotics things (input, drive, etc.). We will
			// need to make this use the new controller classes later.
			js = new Joystick(0);
			drive = new SingularityDrive(frontLeftMotor, rearLeftMotor, frontRightMotor, rearRightMotor, 1, SingularityDrive.CANTALON_DRIVE);
			arm = new SingularityArm(6, 7);
			
			frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);

			currentScheme = new OneXboxArcadeDrive(0);
			//currentScheme = new OneXboxTankDrive(0);
			//currentScheme = new OneXboxGTADrive(0);
			
			// the camera name (ex. cam0) can be found through the roborio web
			// interface
			session = NIVision.IMAQdxOpenCamera("cam0", NIVision.IMAQdxCameraControlMode.CameraControlModeController);
			NIVision.IMAQdxConfigureGrab(session);
			NIVision.IMAQdxStartAcquisition(session);
			
			conveyer = new SingularityConveyer(8, 6);

			
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
		/*
		//currentScheme.tankDrive(drive, true);
		
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
		
		//the conveyer for tank drive:
		conveyer.setSpeed(js.getRawAxis(3), js.getRawAxis(2), js.getRawButton(10));
		
		OLD*/
		
		currentScheme.drive(drive, true);
		currentScheme.controlArm(arm);
		currentScheme.controlConveyer(conveyer);
		
		//the conveyer for arcade:
		//conveyer.setSpeed(xbox.getRS_Y() + xbox.getRS_X(), xbox.getRS_Y() - xbox.getRS_X(), false);
		
		//the conveyer for GTADrive:
		//conveyer.setSpeed(xbox.getRS_Y() + xbox.getRS_X(), xbox.getRS_Y() - xbox.getRS_X(), false);
		
		drive.setReducedVelocity(0.5);
		drive.reduceVelocity(js.getRawButton(6));
		
		
		//arm.setSpeed(js.getRawAxis(1));
		
	}

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
		rearLeftMotor = 4;
		frontRightMotor = 1;
		rearRightMotor = 3;
	}

	private void updateCamera(int session, Image frame) {
		NIVision.IMAQdxGrab(session, frame, 1);
		CameraServer.getInstance().setImage(frame);
	}
}
