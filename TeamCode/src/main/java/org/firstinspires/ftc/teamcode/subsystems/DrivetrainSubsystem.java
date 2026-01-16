package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class DrivetrainSubsystem extends SubsystemBase {
    public DcMotor frontLeft, frontRight, backLeft, backRight;
   public DrivetrainSubsystem(HardwareMap hardwareMap){
       //initialize all motors with hardware map
       frontLeft = hardwareMap.get(DcMotor.class, "motorFL");
       frontRight = hardwareMap.get(DcMotor.class, "motorFR");
       backLeft = hardwareMap.get(DcMotor.class, "motorBL");
       backRight = hardwareMap.get(DcMotor.class, "motorBR");


       //Reverse the left side motors
       frontLeft.setDirection(DcMotor.Direction.REVERSE);
       backLeft.setDirection(DcMotor.Direction.REVERSE);

   }

   public void drive(double forward, double strafe, double rotation){
       frontLeft.setPower(forward + strafe + rotation);
       frontRight.setPower(forward - strafe - rotation);
       backLeft.setPower(forward - strafe + rotation);
       backRight.setPower(forward + strafe - rotation);
   }


}
