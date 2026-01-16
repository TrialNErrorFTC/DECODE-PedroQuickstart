package org.firstinspires.ftc.teamcode.opmodes;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.seattlesolvers.solverslib.command.CommandScheduler;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.LimelightSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ShooterSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.TransferSubsystem;

public class SixBallBlue extends OpMode {
    public static Pose startPose = new Pose(12.763678696158333, 130.6298020954598, Math.toRadians(135));
    public static Pose preloadPose = new Pose(29.010, 114.622, Math.toRadians(135));
    public static Pose pickup1StartPose = new Pose(40.618, 83.299, Math.toRadians(180));
    public static Pose pickup1EndPose = new Pose(17.973806752037255, 82.82479627473806, Math.toRadians(180));
    public static Pose shoot1Pose = new Pose(57.69033760186264, 84.52968568102445, Math.toRadians(135));
    private ShooterSubsystem m_shooter;
    private IntakeSubsystem m_intake;
    private TransferSubsystem m_transfer;
    private LimelightSubsystem m_limelight;

    @Override
    public void init() {
        m_shooter = new ShooterSubsystem(hardwareMap);
        m_intake = new IntakeSubsystem(hardwareMap);
        m_transfer = new TransferSubsystem(hardwareMap);
        m_limelight = new LimelightSubsystem(hardwareMap);

        CommandScheduler.getInstance().reset();

        Follower follower = Constants.createFollower(hardwareMap);
    }

    @Override
    public void loop() {

    }
}
