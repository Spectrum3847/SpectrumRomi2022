//Created by Spectrum3847
//Based on code by FRC4141
package frc.robot;

import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.lib.gamepads.XboxGamepad;
import frc.lib.util.Logger;
import frc.robot.Robot.RobotState;
import frc.robot.telemetry.Log;

public class Gamepads {
	// Create Joysticks first so they can be used in defaultCommands
	public static XboxGamepad driver = new XboxGamepad(0, .15, .15);
	public static XboxGamepad operator = new XboxGamepad(1, .06, .05);
	public static boolean driverConfigured = false;
	public static boolean operatorConfigured = false;
	public static String name = Log._controls;

	// Configure all the controllers
	public static void configure() {
		configureDriver();
		configureOperator();
	}

	public static void resetConfig() {
		CommandScheduler.getInstance().clearButtons();
		driverConfigured = false;
		operatorConfigured = false;
		configureDriver();
		configureOperator();
		if (!driverConfigured) {
			Logger.println("##### Driver Controller Not Connected #####");
		}

		if (!operatorConfigured) {
			Logger.println("***** Operator Controller Not Connected *****");
		}
	}

	// Configure the driver controller
	public static void configureDriver() {
		// Detect whether the xbox controller has been plugged in after start-up
		if (!driverConfigured) {
			boolean isConnected = driver.isConnected();
			if (!isConnected)
				return;

			// Configure button bindings
			if (Robot.getState() == RobotState.TEST) {
				driverTestBindings();
			} else {
				driverBindings();
			}
			driverConfigured = true;
		}
	}

	// Configure the operator controller
	public static void configureOperator() {
		// Detect whether the xbox controller has been plugged in after start-up
		if (!operatorConfigured) {
			boolean isConnected = operator.isConnected();
			if (!isConnected)
				return;

			// Configure button bindings
			if (Robot.getState() == RobotState.TEST) {
				operatorTestBindings();
			} else {
				operatorBindings();
			}
			operatorConfigured = true;
		}
	}

	public static void driverBindings() {
		// Driver Controls

	}

	public static void operatorBindings() {
		// Intake

	}

	// Configure the button bindings for the driver control in Test Mode
	public static void driverTestBindings() {

	}

	// Configure the button bindings for the driver control in Test Mode
	public static void operatorTestBindings() {

	}

	public static void printLow(String msg) {
		Logger.println(msg, name, Logger.low1);
	}

	public static void printNormal(String msg) {
		Logger.println(msg, name, Logger.normal2);
	}

	public static void printHigh(String msg) {
		Logger.println(msg, name, Logger.high3);
	}

	public static void printCritical(String msg) {
		Logger.println(msg, name, Logger.critical4);
	}
}
