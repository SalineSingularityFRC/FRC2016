package org.usfirst.frc.team5066.robot;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.io.IOException;

import org.usfirst.frc.team5066.controller2016.ControlScheme;
import org.usfirst.frc.team5066.controller2016.controlSchemes.OneXboxArcadeDrive;
import org.usfirst.frc.team5066.library.SingularityDrive;
import org.usfirst.frc.team5066.library.SingularityProperties;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.Image;

public class Robot extends IterativeRobot {
	
	Command autonomousCommand;
	SendableChooser autochooser;
	
	
	ControlScheme currentScheme;
	Image frame;
	int frontLeftMotor, rearLeftMotor, frontRightMotor, rearRightMotor, session;
	Joystick js;
	long initialTime;
	SingularityDrive drive;
	SingularityProperties properties;
	SingularityArm arm;
	SingularityConveyer sc;
	
	
    
    
	public void robotInit() {
		//    setup auto chooser in SmartDashboard 
//	autochooser = new SendableChooser();
//	autochooser.addDefault("default programm",  object);
//	autochooser.addObject("object programm111", object);
//	autochooser.addObject("object programm222", object);
//	autochooser.addObject("object programm333", object);
//	autochooser.addObject("object programm444", object);
//	autochooser.addObject("object programm555", object);
//	autochooser.addObject("object programm666", object);
//	autochooser.addObject("object programm777", object);
//		
	//	SmartDashboard.putData("Autonomou Chooser", autochooser);
	
		
	
		try {
			properties = new SingularityProperties("/home/lvuser/robot.properties");
			loadDefaultProperties();
		} catch (IOException ioe) {
			loadDefaultProperties();
		} finally {
			// Implement standard robotics things (input, drive, etc.). We will
			// need to make this use the new controller classes later.
			js = new Joystick(0);
			drive = new SingularityDrive(frontLeftMotor, rearLeftMotor, frontRightMotor, rearRightMotor, .5, SingularityDrive.CANTALON_DRIVE);
			arm = new SingularityArm(6, 7);
			sc = new SingularityConveyer(8,9);
			
			frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);

			//currentScheme = new OneXboxArcadeDrive(js);
			
			// the camera name (ex. cam0) can be found through the roborio web
			// interface
			session = NIVision.IMAQdxOpenCamera("cam0", NIVision.IMAQdxCameraControlMode.CameraControlModeController);
			NIVision.IMAQdxConfigureGrab(session);
			NIVision.IMAQdxStartAcquisition(session);
			
		}
	}

	public void disabledPeriodic() {
		updateCamera(session, frame);
	}
    public void autonomousInit(){
    	
    	// Use SmartDashboard to setup autonomous chooser
    	
    	autonomousCommand = (Command) autochooser.getSelected();
		autonomousCommand.start();
		
    	
    	
    	
    }
	public void autonomousPeriodic() {
		
		
		
		
		//Autonomous part
		
		
		Scheduler.getInstance(); // Schedule all the autonomous for SmartDashboard
		
		updateCamera(session, frame);
	}

	public void teleopPeriodic() {
		updateCamera(session, frame);
		
		//currentScheme.tankDrive(drive, true);
		
		drive.setReducedVelocity(0.5);
		drive.reduceVelocity(js.getRawButton(6));
		
		if(js.getRawButton(1)){
			drive.setVelocityMultiplier(.8);
		}
		else {
			drive.setVelocityMultiplier(1);
		}
		
		drive.tank(-js.getRawAxis(1), -js.getRawAxis(0), true);
		SmartDashboard.putNumber("Joystick Y", -js.getRawAxis(0));
		SmartDashboard.putNumber("Joystick X", js.getRawAxis(1));
		
		sc.setSpeed(js.getRawAxis(3)-js.getRawAxis(2));
		
		//drive.setReduceVelocity(js.getRawButton(6));

		
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
		rearLeftMotor = 2;
		frontRightMotor = 1;
		rearRightMotor = 3;
	}

	private void updateCamera(int session, Image frame) {
		NIVision.IMAQdxGrab(session, frame, 1);
		CameraServer.getInstance().setImage(frame);
	}
}
