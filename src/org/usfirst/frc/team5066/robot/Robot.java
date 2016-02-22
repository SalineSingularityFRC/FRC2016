package org.usfirst.frc.team5066.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.io.IOException;

import org.usfirst.frc.team5066.controller2016.ControlScheme;
import org.usfirst.frc.team5066.controller2016.controlSchemes.OneXboxArcadeDrive;
import org.usfirst.frc.team5066.controller2016.controlSchemes.TwoJoystickTankXboxAssist;
import org.usfirst.frc.team5066.controller2016.controlSchemes.OneXboxTankDrive;
import org.usfirst.frc.team5066.library.SingularityDrive;
import org.usfirst.frc.team5066.library.SingularityProperties;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.Image;

public class Robot extends IterativeRobot {
	
	Command autonomousCommand;
	SendableChooser autochooser;
	
	
	ControlScheme currentScheme;
	Image frame;
	int frontLeftMotor, rearLeftMotor, frontRightMotor, rearRightMotor;
	int armLeftWorm, armLeftPlanetary, armRightWorm, armRightPlanetary;
	int leftConveyerMotor, rightConveyerMotor;
	int session;
	
	double slowSpeedConstant;
	double normalSpeedConstant;
	double fastSpeedConstant;
	
	Joystick js;
	long initialTime;
	SingularityDrive drive;
	SingularityProperties properties;
	SingularityArm arm;
	SingularityConveyer conveyer;
	int driveControllerType;
	
	CANTalon talon;
	
	double position;
	
	Ultrasonic googleUltron;

	/*
	 * NOTE
	 * 
	 * Xbox Controller is always port 0 Big Joystick is always port 1 Little
	 * joystick is always port 2
	 */

	// enum for controller ports
	final int XBOX_PORT = 0;
	final int BIG_JOYSTICK_PORT = 1;
	final int SMALL_JOYSTICK_PORT = 2;

	public void robotInit() {

		// TODO change this so that default properties are loaded first and then
		// the other properties are applied one by one. If one of them
		// encounters an error, it just keeps the default value and moves on to
		// the next property

  
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
		//	SmartDashboard.putData("Autonomous Chooser", autochooser);
	
		try {
			properties = new SingularityProperties("/home/lvuser/robot.properties");
			//TODO switch back to loadProperties()!!!!!!!!!!!!!!!!!!!!!!
			loadDefaultProperties();
		} catch (Exception e) {
			loadDefaultProperties();
			e.printStackTrace();
		} finally {

			// Implement standard robotics things (input, drive, etc.). We will
			// need to make this use the new controller classes later.
			js = new Joystick(0);
			drive = new SingularityDrive(frontLeftMotor, rearLeftMotor, frontRightMotor, rearRightMotor, this.driveControllerType, slowSpeedConstant, normalSpeedConstant, fastSpeedConstant);
			arm = new SingularityArm(2, 9, 7, 5, .25);
			conveyer = new SingularityConveyer(8, 6);

			currentScheme = new OneXboxArcadeDrive(this.XBOX_PORT);

			SmartDashboard.putString("DB/String 1", "" + driveControllerType);
			
			talon = new CANTalon(0);
			
			googleUltron = new Ultrasonic(0, 1);

			//Camera setup code
			try {

				CameraServer server = CameraServer.getInstance();
				server.setQuality(50);
				server.startAutomaticCapture("cam0");

				// the camera name (ex. cam0) can be found through the roborio
				// web interface

				frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
				

				session = NIVision.IMAQdxOpenCamera("cam0",
						NIVision.IMAQdxCameraControlMode.CameraControlModeController);

				NIVision.IMAQdxConfigureGrab(session);
				NIVision.IMAQdxStartAcquisition(session);


			} catch (Exception e) {
				e.printStackTrace();
			}
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
	
	public void teleopInit(){
		
		talon.changeControlMode(CANTalon.TalonControlMode.Position);
		
		talon.set(0);
		
		talon.reverseSensor(true);
		
		SmartDashboard.putNumber("Position to go to", 0);
	}

	public void teleopPeriodic() {

		//TODO try removing/ throttling this line to speed up robot response to controls
		/*updateCamera(session, frame);

		currentScheme.drive(drive, true);
		currentScheme.controlArm(arm);
		currentScheme.controlConveyer(conveyer);

		drive.setReducedVelocity(0.5);*/
		
		position = SmartDashboard.getNumber("Position to go to", 0);
		
		SmartDashboard.putNumber("Position", talon.getPosition());
		SmartDashboard.putNumber("Velocity", talon.getSpeed());
		
		talon.set(position);
		
		SmartDashboard.putNumber("Sensor", googleUltron.getRangeInches());
		
	}
	
	public void disabledInit(){
		talon.set(0);
	}

	public void testPeriodic() {
		updateCamera(session, frame);

	}

	private void loadProperties() {
		SmartDashboard.putString("DB/String 0", "No");

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
		
		SmartDashboard.putString("DB/String 9", "slow: " + slowSpeedConstant + " | normal: " + normalSpeedConstant + " | fast: " + fastSpeedConstant);

	}

	private void loadDefaultProperties() {
		SmartDashboard.putString("DB/String 0", "Yes  -- Defaults were loaded");

		SmartDashboard.putString("DB/String 9", "Defaults loaded");

		driveControllerType = SingularityDrive.CANTALON_DRIVE;

		// Ports
		frontLeftMotor = 10;
		rearLeftMotor = 4;
		frontRightMotor = 1;
		rearRightMotor = 3;

		// TODO add arm motors
		armLeftWorm = 2;
		armLeftPlanetary = 9;
		armRightWorm = 7;
		armRightPlanetary = 5;

		// TODO add these variables to the initialization of the conveyer and
		// arm objects
		leftConveyerMotor = 8;
		rightConveyerMotor = 6;
		
		slowSpeedConstant = 0.4;
		normalSpeedConstant = 0.8;
		fastSpeedConstant = 1.0;
		
	}

	private void updateCamera(int session, Image frame) {
		try {
			NIVision.IMAQdxGrab(session, frame, 1);
			CameraServer.getInstance().setImage(frame);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
