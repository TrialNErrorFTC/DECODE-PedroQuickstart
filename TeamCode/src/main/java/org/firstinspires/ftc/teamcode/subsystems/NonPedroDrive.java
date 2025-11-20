package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.drivebase.MecanumDrive;
import com.seattlesolvers.solverslib.hardware.motors.Motor;

import java.util.HashMap;
import java.util.Objects;

public class NonPedroDrive extends SubsystemBase {
    private final Motor frontLeftMotor;
    private final Motor frontRightMotor;
    private final Motor backLeftMotor;
    private final Motor backRightMotor;
    public NonPedroDrive(final HardwareMap hardwareMap){
        frontLeftMotor = new Motor(hardwareMap, "motorFL");
        frontRightMotor = new Motor(hardwareMap, "motorFR");
        backLeftMotor = new Motor(hardwareMap, "motorBL");
        backRightMotor = new Motor(hardwareMap, "motorBR");
        backLeftMotor.setInverted(true);
        frontLeftMotor.setInverted(true);
    }
    public void teleop(final double strafe, final double forward, final double turn){

        double y = forward; // Remember, Y stick value is reversed
        double x = 1.1 * strafe ; // Counteract imperfect strafing
        double rx = turn;

        // Denominator is the largest motor power (absolute value) or 1
        // This ensures all the powers maintain the same ratio,
        // but only if at least one is out of the range [-1, 1]
        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        double frontLeftPower = (y + x + rx) / denominator;
        double backLeftPower = (y - x + rx) / denominator;
        double frontRightPower = (y - x - rx) / denominator;
        double backRightPower = (y + x - rx) / denominator;

        frontLeftMotor.set(frontLeftPower);
        backLeftMotor.set(backLeftPower);
        frontRightMotor.set(frontRightPower);
        backRightMotor.set(backRightPower);
    }
    /**
     * Gets a specific motor assigned to the motor name
     * @param motorName the name of the motor to the pull
     */
    public Motor getMotor(final String motorName){
        if(Objects.equals(motorName, "motorFL")){
            return frontLeftMotor;
        }
        if(Objects.equals(motorName, "motorFR")){
            return frontRightMotor;
        }
        if(Objects.equals(motorName, "motorBL")){
            return backLeftMotor;
        }
        if(Objects.equals(motorName, "motorBR")){
            return backRightMotor;
        }
        return null;
    }
}
