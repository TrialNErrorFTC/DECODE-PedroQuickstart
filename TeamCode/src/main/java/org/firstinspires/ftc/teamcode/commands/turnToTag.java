package org.firstinspires.ftc.teamcode.commands;

import com.seattlesolvers.solverslib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.DrivetrainSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.LimelightSubsystem;

public class turnToTag extends CommandBase {
    private final LimelightSubsystem m_limelight;
    private final DrivetrainSubsystem m_drive;

    public turnToTag(DrivetrainSubsystem drive, LimelightSubsystem limelight){
        m_drive = drive;
        m_limelight = limelight;
    }

    @Override
    public void execute() {
        if (m_limelight.hasValidTarget()){
            double tx = m_limelight.getTx();
            double ImuYaw = m_drive.getHeading();
            double turn = tx - ImuYaw;

            while (Math.abs(turn) > 1){
                m_drive.drive(0, 0, turn);
            }
        }
    }
}
