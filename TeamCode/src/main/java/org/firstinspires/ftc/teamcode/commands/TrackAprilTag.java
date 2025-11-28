package org.firstinspires.ftc.teamcode.commands;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import com.qualcomm.hardware.limelightvision.LLResult;

import org.firstinspires.ftc.teamcode.subsystems.DriveTrain;
import org.firstinspires.ftc.teamcode.subsystems.LimelightHelper;

import dev.nextftc.core.commands.Command;

public class TrackAprilTag extends Command {
    double Kp = 0.035;
    double min_command = 0.05;

    public TrackAprilTag() {
        requires(LimelightHelper.INSTANCE, DriveTrain.INSTANCE);
        setInterruptible(true); // this is the default, so you don't need to specify
    }

    @Override
    public boolean isDone() {
        return false; // whether or not the command is done
    }
    // "tx" value from the Limelight.
    private double limelight_aim_proportional() {
        // kP (constant of proportionality)
        // this is a hand-tuned number that determines the aggressiveness of our proportional control loop
        // if it is too high, the robot will oscillate around.
        // if it is too low, the robot will never reach its target
        // if the robot never turns in the correct direction, kP should be inverted.
        double kP = .035;

        double tx = 0, ty = 0, ta = 0;
        LLResult result = LimelightHelper.INSTANCE.getLimelight().getLatestResult();


        if (result != null && result.isValid()) {
             tx = result.getTx(); // How far left or right the target is (degrees)
             ty = result.getTy(); // How far up or down the target is (degrees)
             ta = result.getTa(); // How big the target looks (0%-100% of the image)
            telemetry.addData("Tx", tx);
            telemetry.addData("Ty", ty);
            telemetry.addData("Ta", ta);
        } else {
            telemetry.addLine("Not available");
        }
        telemetry.update();


       // tx ranges from (-hifov/2) to (hfov/2) in degrees. If your target is on the rightmost edge of
        // your limelight 3 feed, tx should return roughly 31 degrees.

        double targetingAngularPower = tx * kP;

        //invert since tx is positive when the target is to the right of the crosshair
        targetingAngularPower *= -1.0;

        return targetingAngularPower;
    }
    @Override
    public void start() {
        // executed when the command begins
        //create P controller with limelight as the input and a double
        //apply to drive mode
        DriveTrain.INSTANCE.drive(() -> (double) 0, () -> (double) 0, this::limelight_aim_proportional);
    }

    @Override
    public void update() {
        // executed on every update of the command
    }

    @Override
    public void stop(boolean interrupted) {
        // executed when the command ends
    }
}