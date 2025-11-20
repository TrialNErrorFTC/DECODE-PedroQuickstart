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
    private final MecanumDrive mecanum;
    public NonPedroDrive(final HardwareMap hardwareMap){
        frontLeftMotor = new Motor(hardwareMap, "motorFL");
        frontRightMotor = new Motor(hardwareMap, "motorFR");
        backLeftMotor = new Motor(hardwareMap, "motorBL");
        backRightMotor = new Motor(hardwareMap, "motorBR");
        mecanum = new MecanumDrive(true, frontLeftMotor, frontRightMotor, backLeftMotor, backRightMotor);
    }
    public void teleop(final double strafe, final double forward, final double turn){
        mecanum.driveRobotCentric(strafe, forward, turn);
    }
    public Motor getMotor(final String motorName){
        if(Objects.equals(motorName, "frontLeft")){
            return frontLeftMotor;
        }
        if(Objects.equals(motorName, "frontRight")){
            return frontRightMotor;
        }
        if(Objects.equals(motorName, "backLeft")){
            return backLeftMotor;
        }
        if(Objects.equals(motorName, "backRight")){
            return backRightMotor;
        }
        return null;
    }
}
