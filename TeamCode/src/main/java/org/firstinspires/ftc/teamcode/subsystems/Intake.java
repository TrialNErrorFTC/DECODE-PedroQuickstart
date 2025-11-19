package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.hardware.motors.Motor;

public class Intake extends SubsystemBase {
    private final Motor intakeMotor;

    public Intake(final HardwareMap hardwareMap, final String name){
       intakeMotor = new Motor(hardwareMap, name);
       int p = 10;
       int i = 3;
       intakeMotor.setRunMode(Motor.RunMode.RawPower);
    }
    public void spin(){
        intakeMotor.set(0.7);
    }
    public void reverse(){
        intakeMotor.set(-0.7);
    }
    public void stop(){intakeMotor.set(0);}

}