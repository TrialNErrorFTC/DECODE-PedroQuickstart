package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.hardware.motors.Motor;

public class ShooterSubsystem extends SubsystemBase {
    private final Motor shooterMotor;

    public ShooterSubsystem(final HardwareMap hardwareMap, final String name){
        shooterMotor = new Motor(hardwareMap, name);
        int p = 10;
        int i = 3;
        shooterMotor.setRunMode(Motor.RunMode.RawPower);
    }
    public void spin(){
        shooterMotor.set(0.7);
    }
    public void reverse(){
        shooterMotor.set(-0.7);
    }
    public void stop(){shooterMotor.set(0);}
}
