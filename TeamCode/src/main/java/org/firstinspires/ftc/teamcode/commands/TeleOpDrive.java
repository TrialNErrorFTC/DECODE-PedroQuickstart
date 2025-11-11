package org.firstinspires.ftc.teamcode.commands;

import com.seattlesolvers.solverslib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.NonPedroDrive;

import java.util.function.DoubleSupplier;

public class TeleOpDrive extends CommandBase {
    private NonPedroDrive m_drive;
    private DoubleSupplier m_strafe;
    private DoubleSupplier m_rotation;
    private DoubleSupplier m_forward;
    public TeleOpDrive(NonPedroDrive subsystem, DoubleSupplier strafe, DoubleSupplier rotation, DoubleSupplier forward){
        m_drive = subsystem;
        m_strafe = strafe;
        m_rotation = rotation;
        m_forward = forward;
        addRequirements(m_drive);
    }

    public void execute(){
        m_drive.teleop(m_strafe.getAsDouble(), m_forward.getAsDouble(), m_rotation.getAsDouble());
    }
}
