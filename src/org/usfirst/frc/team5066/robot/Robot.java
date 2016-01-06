package org.usfirst.frc.team5066.robot;

import java.io.FileNotFoundException;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.usfirst.frc.team5066.library.SingularityDrive;
import org.usfirst.frc.team5066.library.playback.Reader;
import org.usfirst.frc.team5066.library.playback.Recorder;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	boolean record, play;
	Joystick js;
	long initialTime;
	Reader reader;
	Recorder recorder;
	SingularityDrive drive;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	public void robotInit() {
		js = new Joystick(0);
		drive = new SingularityDrive(6, 5, 7, 4);

		record = true;
		play = true;

		reader = null;
		recorder = null;
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
	}

	public void autonomousInit() {
		if (play) {
			try {
				reader = new Reader("/home/lvuser/recording.json");
				initialTime = System.currentTimeMillis();
			} catch (FileNotFoundException fnfe) {
				reader = null;
				fnfe.printStackTrace();
			} catch (ParseException pe) {
				reader = null;
				pe.printStackTrace();
			}
		}
	}

	/**
	 * This function is called periodically during autonomous
	 */
	public void autonomousPeriodic() {
		if (reader != null) {
			JSONObject current = reader.getDataAtTime(System.currentTimeMillis() - initialTime);
			drive.mecanum(Double.parseDouble(current.get("x").toString()), Double.parseDouble(current.get("y").toString()), Double.parseDouble(current.get("z").toString()));
		}
	}

	/**
	 * This function is called periodically during operator control
	 */
	public void teleopPeriodic() {
		drive.mecanum(js.getRawAxis(0), js.getRawAxis(1), js.getRawAxis(4), true);
	}

	public void testInit() {
		if (record) {
			recorder = new Recorder(new String[] { "x", "y", "z" }, new Object[] { 0, 0, 0 }, "/home/lvuser/recording.json");
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
		}
	}

}
