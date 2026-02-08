package org.firstinspires.ftc.teamcode.subsystems;

import com.seattlesolvers.solverslib.command.SubsystemBase;

import org.firstinspires.ftc.teamcode.robot.RobotHardware;

public class Intake extends SubsystemBase {
    public enum Mode {
        INGEST,
        DISCARD,
        OFF,
        CUSTOM
    }

    public static Mode mode = Mode.OFF;
    public static double INGEST_MOTOR_SPEED = 1.0;
    public static double DISCARD_MOTOR_SPEED = -0.7;
    public static double CUSTOM_POWER = 0.0;

    public void setMode(Mode mode) {
        Intake.mode = mode;
    }

    public static Mode getMode() {
        return Intake.mode;
    }

    public void setPower(double power) {
        CUSTOM_POWER = power;
    }

    public void periodic() {
        RobotHardware robot = RobotHardware.get();

        switch (mode) {
            case OFF:
                robot.intakeMotor.set(0);
                break;
            case INGEST:
                robot.intakeMotor.set(INGEST_MOTOR_SPEED);
                break;
            case DISCARD:
                robot.intakeMotor.set(DISCARD_MOTOR_SPEED);
                break;
            case CUSTOM:
                robot.intakeMotor.set(CUSTOM_POWER);
            default:
                break;
        }
    }
}
