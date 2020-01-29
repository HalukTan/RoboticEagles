/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;

import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PWMVictorSPX;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.SpeedControllerGroup;


/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  
  private PWMVictorSPX right1 = new PWMVictorSPX(0);
  private PWMVictorSPX right2 = new PWMVictorSPX(2);
  private PWMVictorSPX left1 = new PWMVictorSPX(1);
  private PWMVictorSPX left2 = new PWMVictorSPX(3);
  private SpeedControllerGroup right = new SpeedControllerGroup(right1, right2);
  private SpeedControllerGroup left = new SpeedControllerGroup(left1, left2);
  private Joystick driverJoystick = new Joystick(0);
  private DifferentialDrive drive = new DifferentialDrive(left, right);

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
        auto1();
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
  double power = -driverJoystick.getRawAxis(1);
  double turn = driverJoystick.getRawAxis(2);
  drive.arcadeDrive(power * 0.6, turn *0.6);

  }


  @Override
  public void testInit() {
  }

  @Override
  public void testPeriodic() {
  }

  public void auto1()
  {
  System.out.print("auto 1 running");
    if(autoTimer.get() < 2)
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
