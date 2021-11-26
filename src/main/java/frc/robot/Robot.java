// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.button.Button;
import frc.lib.sim.PhysicsSim;
import frc.lib.util.SpectrumPreferences;
import frc.robot.commands.AutonomousDistance;
import frc.robot.commands.AutonomousTime;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.OnBoardIO;
import frc.robot.subsystems.OnBoardIO.ChannelMode;
import frc.robot.telemetry.Log;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

    // subsystems and hardware are defined here
    public static final Drivetrain drivetrain = new Drivetrain();
    public static final OnBoardIO m_onboardIO = new OnBoardIO(ChannelMode.INPUT, ChannelMode.INPUT);

    public static SpectrumPreferences prefs = SpectrumPreferences.getInstance();

    private final SendableChooser<Command> chooser_ = new SendableChooser<>();
    private Command m_autonomousCommand;

    /**
     * Robot State Tracking Setup
     */
    public enum RobotState {
        DISABLED, AUTONOMOUS, TELEOP, TEST
    }

    public static RobotState s_robot_state = RobotState.DISABLED;

    public static RobotState getState() {
        return s_robot_state;
    }

    public static void setState(final RobotState state) {
        s_robot_state = state;
    }

    /**
     * This function is run when the robot is first started up and should be used
     * for any initialization code.
     */
    @Override
    public void robotInit() {
        printNormal("Start robotInit()");

        Log.initLog(); // Config the Debugger based on FMS state

        // Example of how to use the onboard IO
        Button onboardButtonA = new Button(m_onboardIO::getButtonAPressed);
        onboardButtonA.whenActive(new PrintCommand("Button A Pressed"))
                .whenInactive(new PrintCommand("Button A Released"));

        // Setup SmartDashboard options
        chooser_.setDefaultOption("Auto Routine Distance", new AutonomousDistance(drivetrain));
        chooser_.addOption("Auto Routine Time", new AutonomousTime(drivetrain));
        SmartDashboard.putData(chooser_);

        printNormal("End robotInit()");
    }

    /**
     * This function is called every robot packet, no matter the mode. Use this for
     * items like diagnostics that you want ran during disabled, autonomous,
     * teleoperated and test.
     *
     * <p>
     * This runs after the mode specific periodic functions, but before LiveWindow
     * and SmartDashboard integrated updating.
     */
    @Override
    public void robotPeriodic() {
        // Runs the Scheduler. This is responsible for polling buttons, adding
        // newly-scheduled
        // commands, running already-scheduled commands, removing finished or
        // interrupted commands,
        // and running subsystem periodic() methods. This must be called from the
        // robot's periodic
        // block in order for anything in the Command-based framework to work.
        CommandScheduler.getInstance().run();

        // Ensure the controllers are always configured
        Gamepads.configure();
    }

    /** This function is called once each time the robot enters Disabled mode. */
    @Override
    public void disabledInit() {
        setState(RobotState.DISABLED);
        printNormal("Start disabledInit()");
        Log.initLog(); // Config the Debugger based on FMS state
        CommandScheduler.getInstance().cancelAll(); // Disable any currently running commands
        LiveWindow.setEnabled(false); // Disable Live Window we don't need that data being sent
        LiveWindow.disableAllTelemetry();
        Gamepads.resetConfig(); // Reset Gamepad Configs
        printNormal("End disabledInit()");
    }

    @Override
    public void disabledPeriodic() {
    }

    /**
     * This autonomous runs the autonomous command selected by your
     * {@link RobotContainer} class.
     */
    @Override
    public void autonomousInit() {
        setState(RobotState.AUTONOMOUS);
        printNormal("Start autonomousInit()");
        Log.initLog(); // Config the Debugger based on FMS state
        CommandScheduler.getInstance().cancelAll(); // Disable any currently running commands
        LiveWindow.setEnabled(false); // Disable Live Window we don't need that data being sent
        LiveWindow.disableAllTelemetry();

        // Get selected routine from the SmartDashboard
        m_autonomousCommand = chooser_.getSelected();

        // schedule the autonomous command (example)
        if (m_autonomousCommand != null) {
            m_autonomousCommand.schedule();
        }

        printNormal("End autonomousInit()");
    }

    /** This function is called periodically during autonomous. */
    @Override
    public void autonomousPeriodic() {
    }

    @Override
    public void teleopInit() {
        setState(RobotState.TELEOP);
        printNormal("Start teleopInit()");
        CommandScheduler.getInstance().cancelAll(); // Disable any currently running commands
        Log.initLog(); // Config the Debugger based on FMS state
        Gamepads.resetConfig(); // Reset Gamepad Configs
        LiveWindow.setEnabled(false); // Disable Live Window we don't need that data being sent
        LiveWindow.disableAllTelemetry();
        printNormal("End teleopInit()");
    }

    /** This function is called periodically during operator control. */
    @Override
    public void teleopPeriodic() {
    }

    @Override
    public void testInit() {
        setState(RobotState.TEST);
        // Cancels all running commands at the start of test mode.
        CommandScheduler.getInstance().cancelAll();
        Gamepads.resetConfig();
        ; // Reset Gamepad Configs
        Log.initLog(); // Config the Debugger based on FMS state
    }

    /** This function is called periodically during test mode. */
    @Override
    public void testPeriodic() {
    }

    public void simulationInit() {
        Sim.intialization();
    }

    public void simulationPeriodic() {
        PhysicsSim.getInstance().run();
    }

    /*******************************************************************************
     * VALUE FOR CHANGE BATTERY ON DASHBOARD Should return true if robot is disabled
     * and voltage is less than 12
     ******************************************************************************/
    public static boolean changeBattery() {
        return (Robot.s_robot_state == Robot.RobotState.DISABLED && RobotController.getInputVoltage() < 12);
    }

    // ---------------//
    // Print Methods //
    // ---------------//
    public static void printLow(String msg) {
        Log.println(msg, Log._general, Log.low1);
    }

    public static void printNormal(String msg) {
        Log.println(msg, Log._general, Log.normal2);
    }

    public static void printHigh(String msg) {
        Log.println(msg, Log._general, Log.high3);
    }
}
