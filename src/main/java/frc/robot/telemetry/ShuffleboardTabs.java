//Created by Spectrum3847
//Based on Code from FRC# 4141 
package frc.robot.telemetry;

import edu.wpi.first.wpilibj.Notifier;

// Class that wraps all of the interaction with the Shuffleboard

// All decisions about content and layout of the Shuffleboard are consolidated in this file
// to make it easier to change things rather than having to look throughout all of the
// classes for subsystems, commands, etc.

// The ShuffleboardTabs class knows about the subsystems, commands, etc. but generally not vice versa.
public class ShuffleboardTabs {

    private double _heartBeatPeriod = 1;     //How fast we should run the update methods, most values are set by suppliers so they update quickly

    // Tabs

    public ShuffleboardTabs() {
        printLow("Constructing ShuffleboardTabs...");
    }

    public void initialize() {
        printLow("Initializing ShuffleboardTabs...");

        
        _heartBeat.startPeriodic(_heartBeatPeriod);
    }

    //Update values from Shuffleboard, this is run at the _heartbeatperiod
    //We don't need to assign values every program cycle
    private void update() {
    }

    public static void printLow(String msg) {
    Log.println(msg, Log._telemetry, Log.low1);
    }

    public static void printNormal(String msg) {
    Log.println(msg, Log._telemetry, Log.normal2);
    }

    public static void printHigh(String msg) {
    Log.println(msg, Log._telemetry, Log.high3);
    }

    //Controls how often we update values based on shuffleboard not how often data changes are sent to shuffleboard
    class PeriodicRunnable implements java.lang.Runnable {
	    public void run() {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
                update();
        }
    }
    Notifier _heartBeat = new Notifier(new PeriodicRunnable());
}
