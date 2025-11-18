package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.drivebase.MecanumDrive;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.hardware.motors.Motor;

import org.firstinspires.ftc.teamcode.commands.TeleOpDrive;
import org.firstinspires.ftc.teamcode.subsystems.NonPedroDrive;


@TeleOp
public class Drive extends CommandOpMode {
    private NonPedroDrive m_robotDrive;
    private Motor fL, fR, bL, bR;
    private GamepadEx m_driverOp;
    private TeleOpDrive m_driveCommand;

    public void initialize(){
        fL = new Motor(hardwareMap, "frontLeft");
        fR = new Motor(hardwareMap, "frontRight");
        bL = new Motor(hardwareMap, "backLeft");
        bR = new Motor(hardwareMap, "backRight");

        // create our drive object
        m_robotDrive = new NonPedroDrive(hardwareMap);

        m_driverOp = new GamepadEx(gamepad1);
        m_driveCommand = new TeleOpDrive(m_robotDrive, () -> m_driverOp.getLeftX(), () -> m_driverOp.getRightX(), () -> m_driverOp.getLeftY());

        //create intake button

        register(m_robotDrive);
        m_robotDrive.setDefaultCommand(m_driveCommand);
    }
}
