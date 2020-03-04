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
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PWMVictorSPX;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import jdk.nashorn.api.tree.WhileLoopTree;
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
  
  private PWMVictorSPX right1 = new PWMVictorSPX(0);
  private PWMVictorSPX right2 = new PWMVictorSPX(1);
  private PWMVictorSPX left1 = new PWMVictorSPX(2);
  private PWMVictorSPX left2 = new PWMVictorSPX(3);
  private VictorSP  move = new VictorSP(4);
  private PWMVictorSPX intake = new PWMVictorSPX(5);
  private PWMVictorSPX winch = new PWMVictorSPX(6);
  private Victor elev = new Victor(7);
  private SpeedControllerGroup right = new SpeedControllerGroup(right1, right2);
  private SpeedControllerGroup left = new SpeedControllerGroup(left1, left2);
  //private Joystick driverJoystick = new Joystick(0);
  private DifferentialDrive drive = new DifferentialDrive(left, right);
  private XboxController xStick = new XboxController(0);
  private DigitalInput forwardLimitSwitch, reverseLimitSwitch;
  private AnalogInput m_US; 
    


  String m_autoSelected;
  SendableChooser<Integer> auto_chooser;
  Timer autoTimer;
  int auto_choice;
  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */
  @Override
  public void robotInit() {
  auto_chooser = new SendableChooser<Integer>();
  auto_chooser.addOption("Left", 1);
  auto_chooser.addOption("Right", 2);
  auto_chooser.addOption("Forward", 3);
  auto_chooser.setDefaultOption("Example", 4);
  SmartDashboard.putData("Auto choices", auto_chooser);
  autoTimer = new Timer();
  CameraServer server = CameraServer.getInstance();
  server.startAutomaticCapture(1);
  server.startAutomaticCapture(0);
  forwardLimitSwitch = new DigitalInput(1);
  reverseLimitSwitch = new DigitalInput(2);
  m_US = new AnalogInput(0);

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
        Left();
        break;
      case 2:
        Right();
      case 3:
        forward();
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
  if (xStick.getRawButton(5) == true) {
    rollerMove = 0.8;
  } else if (xStick.getRawButton(6)) {
    rollerMove = -0.8;
  }
  if (xStick.getRawButton(1) == true) {
    elevator = 0.6;
  } else if (xStick.getRawButton(2)) {
    elevator = -0.6;
  
  }
  if (xStick.getRawButton(9) == true) {
    winchh = 1.0;
  } else if (xStick.getRawButton(8)) {
    winchh = -1.0;
  }
  intake.set(in - out);
  move.set(rollerMove);
  elev.set(elevator);
  winch.set(winchh);
  drive.arcadeDrive(power * 0.8, turn * 0.8);
  if (forwardLimitSwitch.get()) // If the forward limit switch is pressed, we want to keep the values between -1 and 0
  rollerMove = 0; //changed from math.min to 0
  else if(reverseLimitSwitch.get()) // If the reversed limit switch is pressed, we want to keep the values between 0 and 1
  rollerMove = 0; //changed from math.max to 0
  double sensorValue = m_US.getVoltage();
    final double scaleFactor = 1/(5./1024.); //scale converting voltage to distance
    double distance = 5*sensorValue*scaleFactor; //convert the voltage to distance
    SmartDashboard.putNumber("DB/Slider 0", distance); //write the value to the LabVIEW DriverStation



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
  if (autoTimer.get() > 0.1 && autoTimer.get() < 1.7)
    {
      left.set(0.5);
      right.set(-0.450);
   
    }else if(autoTimer.get() > 1 )
    {
      move.set(-0.4);
      intake.set(-1);
      autoTimer.stop();
    }
  }
  public void Right()
  {
  System.out.print("Right running");
  if (autoTimer.get() > 0.1 && autoTimer.get() < 2.0)
    {
      left.set(0.5);
      right.set(-0.450);
   
    }else if(autoTimer.get() > 2.0 )
    {
      right.set(-0.2);
      move.set(-0.4);
      intake.set(-1);
      autoTimer.stop();
    }
  }
    public void Left()
    {
    System.out.print("Left running");
    if (autoTimer.get() > 0.1 && autoTimer.get() < 2.0)
      {
        left.set(0.5);
        right.set(-0.450);
     
      }else if(autoTimer.get() > 2.0 )
      {
        left.set(-0.2);
        move.set(-0.4);
        intake.set(-1);
        autoTimer.stop();
      }
  }
 
}
  
 