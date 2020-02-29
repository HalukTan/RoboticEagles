/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.XboxController.Button;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpinotepadlibj.PWMVictorSPX;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.buttons.Trigger;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;



/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  
  //Robots Motors for the wheels
  private PWMVictorSPX motor1 = new PWMVictorSPX(0);
  private PWMVictorSPX motor2 = new PWMVictorSPX(1);
  private PWMVictorSPX motor3 = new PWMVictorSPX(2);
  private PWMVictorSPX motor4 = new PWMVictorSPX(3);
  //Controller groups
  private SpeedControllerGroup right = new SpeedControllerGroup(motor1, motor2);
  private SpeedControllerGroup left = new SpeedControllerGroup(motor3, motor4);
  //private Joystick driverJoystick = new Joystick(0); (use this if you are using logitech controller)
  //Drive chain
  private DifferentialDrive drive = new DifferentialDrive(left, right);
  private XboxController xStick = new XboxController(0);
    



  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */
  @Override
  public void robotInit() {
  //Camera
  CameraServer server = CameraServer.getInstance();
  server.startAutomaticCapture(0);

  }
  

  @Override
  public void autonomousInit() {


  }


  @Override
  public void autonomousPeriodic() {
    
  }

  @Override
  public void teleopInit() {
  }


  @Override
  public void teleopPeriodic() {
  //power turn for drive
  double power = -xStick.getRawAxis(1);
  double turn = xStick.getRawAxis(4);
  double action = 0;
 //Program for button
 if (xStick.getRawButton(1) == true) {
    button = 1;
  } else if (xStick.getRawButton(2)) {
    button = -1;
  }
  motorname.set(action);
  //make robot go go
  drive.arcadeDrive(power * 0.8, turn * 0.8);



  }


  @Override
  public void testInit() {
  }

  @Override
  public void testPeriodic() {
  }

 
}