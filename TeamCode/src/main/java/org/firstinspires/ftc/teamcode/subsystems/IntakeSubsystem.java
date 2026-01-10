package org.firstinspires.ftc.teamcode.subsystems;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_WITHOUT_ENCODER;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.hardware.motors.Motor;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.conditionals.IfElseCommand;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.commands.utility.LambdaCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.MotorEx;
import dev.nextftc.hardware.powerable.SetPower;
import kotlin.time.Instant;

public class IntakeSubsystem extends SubsystemBase {

    DcMotor motor_intake;
    public IntakeSubsystem(HardwareMap hMap){
        motor_intake = hMap.get(DcMotor.class, "motorI");
        motor_intake.setMode(RUN_WITHOUT_ENCODER);
    }

    public void forward(){
        motor_intake.setPower(0.7);
    }
    public void reverse(){
        motor_intake.setPower(-0.7);
    }
    public void stop(){
        motor_intake.setPower(0);
    }
}
