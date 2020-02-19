/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.Watchdog;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.XboxController.Button;
import com.ctre.phoenix.motorcontrol.ControlMode;
import javax.swing.JButton;

import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PWMVictorSPX;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.buttons.Trigger;


/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  
  private PWMVictorSPX right1 = new PWMVictorSPX(0);
  private PWMVictorSPX right2 = new PWMVictorSPX(1);
  private PWMVictorSPX left1 = new PWMVictorSPX(2);
  private PWMVictorSPX left2 = new PWMVictorSPX(3);
  private VictorSP move = new VictorSP(4);
  private PWMVictorSPX intake = new PWMVictorSPX(5);
  private PWMVictorSPX elev = new PWMVictorSPX(6);
  private Victor winch = new Victor(7);
  private SpeedControllerGroup right = new SpeedControllerGroup(right1, right2);
  private SpeedControllerGroup left = new SpeedControllerGroup(left1, left2);
  //private Joystick driverJoystick = new Joystick(0);
  private DifferentialDrive drive = new DifferentialDrive(left, right);
  private XboxController xStick = new XboxController(0);
  DigitalInput forwardLimitSwitchDigitalInput, reverseLimitSwitchDigitalInput;
  Talon motor;
    


  String m_autoSelected;
  SendableChooser auto_chooser;
  Timer autoTimer;
  int auto_choice;
  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */
  @Override
  public void robotInit() {
  auto_chooser = new SendableChooser();
  auto_chooser.addObject("Auto 1", new Integer(1));
  auto_chooser.addObject("Auto 2", new Integer(2));
  auto_chooser.addDefault("Default", new Integer(1));
  SmartDashboard.putData("Auto choices", auto_chooser);
  autoTimer = new Timer();
  CameraServer server = CameraServer.getInstance();
  server.startAutomaticCapture(1);
  server.startAutomaticCapture(0);
  DigitalInput forwardLimitSwitchDigitalInput = new DigitalInput(1)
  DigitalInput reverseLimitSwitchDigitalInput = new DigitalInput(2)
  

  }
  

  @Override
  public void autonomousInit() {
    auto_choice = ((Integer) (auto_chooser.getSelected())).intValue();
    autoTimer.reset();
    autoTimer.start();

  }


  @Override
  public void autonomousPeriodic() {
    switch (auto_choice) {
      case 1:
        forward();
        break;
      case 2:
        auto2();
      default:
        break;
    }
  }

  @Override
  public void teleopInit() {
  }


  @Override
  public void teleopPeriodic() {
  System.out.println(-xStick.getRawAxis(1));
  double power = -xStick.getRawAxis(1);
  double turn = xStick.getRawAxis(4);
  //double in = -xStick.getRawAxis(3);
  //double out = xStick.getRawAxis(4);
  double in = xStick.getTriggerAxis(Hand.kRight);
  double out = xStick.getTriggerAxis(Hand.kLeft);
  double rollerMove = 0;
  double elevator = 0;
  double winchh = 0;
  if (xStick.getRawButton(1) == true) {
    rollerMove = 0.8;
  } else if (xStick.getRawButton(3)) {
    rollerMove = -0.8;
  }
  if (xStick.getRawButton(5) == true) {
    elevator = 0.8;
  } else if (xStick.getRawButton(6)) {
    elevator = -0.8;
  }
  if (xStick.getRawButton(7) == true) {
    winchh = 1.0;
  } else if (xStick.getRawButton(8)) {
    winchh = -1.0;
  }
  intake.set(in - out);
  move.set(rollerMove);
  elev.set(elevator);
  winch.set(winchh);
  drive.arcadeDrive(power * 0.8, turn * 0.8);


  }


  @Override
  public void testInit() {
  }

  @Override
  public void testPeriodic() {
  }

  public void forward()
  {
  System.out.print("forward running");
    if(autoTimer.get() < 3)
    {
      left.set(-0.3);
      right.set(0.3);
    }else
    {
      left.set(0);
      right.set(0);
      autoTimer.stop();
    }
  }
 
  public void auto2()
  {
    if(autoTimer.get() > 2 && autoTimer.get() < 4)
    {
      left.set(-0.3);
      right.set(0.3);
    }else if(autoTimer.get() > 4 )
    {
      left.set(0);
      right.set(0);
      autoTimer.stop();
      autoTimer.reset();
    }
  }
}