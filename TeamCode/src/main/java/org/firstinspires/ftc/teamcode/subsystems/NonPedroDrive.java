package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.hardware.motors.Motor;

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


    /**
     * Drives the robot using mecanum drive kinematics based on joystick inputs.
     * This method is intended to be called continuously during the tele-operated period.
     *
     * @param strafe  The desired sideways movement speed. Positive values strafe right, negative values strafe left. Range: [-1.0, 1.0].
     * @param forward The desired forward/backward movement speed. Positive values move forward, negative values move backward. Range: [-1.0, 1.0].
     * @param turn    The desired rotational speed. Positive values turn clockwise, negative values turn counter-clockwise. Range: [-1.0, 1.0].
     */
    public void teleop(final double strafe, final double forward, final double turn){

        double forwardSpeed = forward; // Remember, Y stick value is reversed
        double strafeSpeed = 1.1 * strafe ; // Counteract imperfect strafing
        double rotationSpeed = turn;

        // Denominator is the largest motor power (absolute value) or 1
        // This ensures all the powers maintain the same ratio,
        // but only if at least one is out of the range [-1, 1]
        double maxPower = Math.max(Math.abs(forwardSpeed) + Math.abs(strafeSpeed) + Math.abs(rotationSpeed), 1);
        double frontLeftPower = (forwardSpeed + strafeSpeed + rotationSpeed) / maxPower;
        double backLeftPower = (forwardSpeed - strafeSpeed + rotationSpeed) / maxPower;
        double frontRightPower = (forwardSpeed - strafeSpeed - rotationSpeed) / maxPower;
        double backRightPower = (forwardSpeed + strafeSpeed - rotationSpeed) / maxPower;

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
