package org.firstinspires.ftc.teamcode.subsystems;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.subsystems.Subsystem;

public class LimelightHelper implements Subsystem {
    public static final LimelightHelper INSTANCE = new LimelightHelper();
    private Limelight3A limelight;

    private LimelightHelper() {}



    // goals for limelight
    public void initialize() {
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.setPollRateHz(100); // This sets how often we ask Limelight for data (100 times per second)
    }

    public Limelight3A getLimelight() {
        return limelight;
    }

    public Command start = new InstantCommand(() -> limelight.start()).requires(this);
    public Command stop = new InstantCommand(() -> limelight.stop()).requires(this);

}
