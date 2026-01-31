package org.firstinspires.ftc.teamcode.opmodes;

import com.pedropathing.control.PIDFController;
import com.pedropathing.follower.Follower;
import com.pedropathing.ftc.FTCCoordinates;
import com.pedropathing.geometry.Pose;
import com.pedropathing.math.MathFunctions;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.ScheduleCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.command.button.Button;
import com.seattlesolvers.solverslib.command.button.GamepadButton;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.teamcode.commands.DefaultDrive;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.subsystems.DrivetrainSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.LimelightSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ShooterSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.TransferSubsystem;

@TeleOp
public class test extends CommandOpMode {

    double targetHeading = Math.toRadians(180); // Radians
    private ShooterSubsystem m_shooter;
    private IntakeSubsystem m_intake;
    private TransferSubsystem m_transfer;
    private DrivetrainSubsystem m_drivetrain;
    private LimelightSubsystem m_limelight;
    private double Tx;
    //blue side pose
    private Follower follower;
    private PIDFController controller;
    double targetVelocity = 2100;

    /**
     * This method is executed once when the OpMode is initialized. {}
     * It handles the setup of all necessary components for driver control,
     * such as subsystems and button bindings for commands.
     */


    @Override
    public void initialize() {
        GamepadEx driverOp = new GamepadEx(gamepad1);
        GamepadEx driverOp2 = new GamepadEx(gamepad2);
        m_shooter = new ShooterSubsystem(hardwareMap);
        m_intake = new IntakeSubsystem(hardwareMap);
        m_transfer = new TransferSubsystem(hardwareMap);
        m_drivetrain = new DrivetrainSubsystem(hardwareMap);
        m_limelight = new LimelightSubsystem(hardwareMap);

        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(new Pose(72, 72, Math.toRadians(90)));

        InstantCommand m_initialize = new InstantCommand(m_shooter::initializeServos);
        InstantCommand m_offCommand = new InstantCommand(m_transfer::off_position);


//       TeleOpDrive m_driveCommand = new TeleOpDrive(m_drivetrain, () -> {
//                           return driverOp.getLeftY() * 0.7;
//                       },
//                       () -> {
//                           return driverOp.getLeftX() * 0.7;
//                       },
//                       () -> {
//                           return driverOp.getRightX() * 0.7;
//                       },
//                       follower
//               );

        DefaultDrive m_driveCommand = new DefaultDrive(m_drivetrain,
                () -> {
                    return driverOp.getLeftY() * 0.7;
                },
                () -> {
                    return driverOp.getLeftX() * 0.7;
                },
                () -> {
                    return driverOp.getRightX() * 0.7;
                }

        );

        SequentialCommandGroup m_threeBallShoot2 = new SequentialCommandGroup(
                new InstantCommand(m_transfer::shoot_position),
                new WaitCommand(400),
                new ParallelCommandGroup(
                        new InstantCommand(m_transfer::transfer),
                        new InstantCommand(m_intake::forward)
                ),
                new WaitCommand(3000),
                new ParallelCommandGroup(
                        new InstantCommand(m_transfer::stop),
                        new InstantCommand(m_intake::stop)
                ),
                new InstantCommand(m_transfer::off_position)

        );

//                new InstantCommand(m_intake::reverse).withTimeout(1000),
//                new InstantCommand(m_intake::forward).withTimeout(3000),
//                new InstantCommand(m_intake::stop),
//                new InstantCommand(m_transfer::off_position)
        m_offCommand.addRequirements(m_transfer);
        m_drivetrain.setDefaultCommand(m_driveCommand);
        m_offCommand.schedule();
        m_initialize.schedule();

        // Binds the 'A' button to a command that incrementally moves the shooter servos up.
        Button moveUpButton = new GamepadButton(
                driverOp, GamepadKeys.Button.A
        ).whenPressed(new InstantCommand(
                () -> {
                    m_shooter.setServos(m_shooter.servoRight.getPosition() - 0.1);
                }
        ));

        // Binds the 'A' button to a command that incrementally moves the shooter servos up.
        Button increaseVelo = new GamepadButton(
                driverOp2, GamepadKeys.Button.A
        ).whenPressed(new InstantCommand(
                () -> {
                    m_shooter.motorShooter.setVelocity(m_shooter.motorShooter.getVelocity() + 50);
                    targetVelocity += 50;
                }
        ));

        // Binds the 'A' button to a command that incrementally moves the shooter servos up.
        Button decreaseVelo = new GamepadButton(
                driverOp2, GamepadKeys.Button.B
        ).whenPressed(new InstantCommand(
                () -> {
                    m_shooter.motorShooter.setVelocity(m_shooter.motorShooter.getVelocity() - 50);
                    targetVelocity -= 50;
                }
        ));

        // Binds the 'B' button to a command that incrementally moves the shooter servos down.
        Button moveDownButton = new GamepadButton(
                driverOp, GamepadKeys.Button.B
        ).whenPressed(new InstantCommand(
                () -> {
                    m_shooter.setServos(m_shooter.servoRight.getPosition() + 0.1);
                }
        ));

        //Binds the 'X' button to a command that stops the shooter.
        Button highSpeed = new GamepadButton(
                driverOp, GamepadKeys.Button.DPAD_UP
        ).toggleWhenPressed(new InstantCommand(m_shooter::highSpeed));

        Button stopShooter = new GamepadButton(
                driverOp, GamepadKeys.Button.DPAD_DOWN
        ).toggleWhenPressed(new InstantCommand(m_shooter::stop));

        //Binds the 'Y' button to a command that reverse the intake.
        Button reverseButton = new GamepadButton(
                driverOp, GamepadKeys.Button.Y
        ).whenPressed(new InstantCommand(
                m_intake::reverse
        )).whenReleased(new InstantCommand(
                m_intake::stop
        ));

        //Binds the 'Right Bumper' button to a command that runs the intake.
        Button intakeButton = new GamepadButton(
                driverOp, GamepadKeys.Button.RIGHT_BUMPER
        ).whenPressed(new ScheduleCommand(
                new InstantCommand(m_intake::forward),
                new InstantCommand(m_transfer::transfer)
        )).whenReleased(new ScheduleCommand(
                new InstantCommand(m_intake::stop),
                new InstantCommand(m_transfer::stop)
        ));

//        //Binds the 'Left Bumper' button to a command that moves the transfer to the shoot position.
        Button shootButton = new GamepadButton(
                driverOp, GamepadKeys.Button.LEFT_BUMPER
        ).whenPressed(new InstantCommand(
                m_transfer::shoot_position
        )).whenReleased(new InstantCommand(
                m_transfer::off_position
        ));

//        Button increaseFlywheelSpeed = new GamepadButton(
//                driverOp, GamepadKeys.Button.DPAD_LEFT
//        ).whenPressed(
//                new InstantCommand(
//                        ()-> {
//                            m_shooter.motorShooter.set(m_shooter.motorShooter.get() + 0.1);
//                        }
//                )
//        );
//        Button decreaseFlywheelSpeed = new GamepadButton(
//                driverOp, GamepadKeys.Button.DPAD_RIGHT
//        ).whenPressed(
//                new InstantCommand(
//                        ()-> {
//                            m_shooter.motorShooter.set(m_shooter.motorShooter.get() - 0.1);
//                        }
//                )
//        );

        Button shootButton2 = new GamepadButton(
                driverOp, GamepadKeys.Button.X
        ).toggleWhenPressed(m_threeBallShoot2);

    }

    public double getHeadingError() {
        if (follower.getCurrentPath() == null) {
            return 0;
        }

        double headingError = MathFunctions.getTurnDirection(follower.getPose().getHeading(), targetHeading) * MathFunctions.getSmallestAngleDifference(follower.getPose().getHeading(), targetHeading);
        return headingError;
    }

    public void run() {
        super.run();
        follower.update();

        m_drivetrain.odo.update();
        m_limelight.limelight.updateRobotOrientation(m_drivetrain.odo.getHeading(AngleUnit.DEGREES));
        controller.setCoefficients(follower.constants.coefficientsHeadingPIDF);
        controller.updateError(getHeadingError());

        if (m_limelight.hasValidTarget()) {
            m_limelight.result = m_limelight.limelight.getLatestResult();
            Tx = m_limelight.result.getTx();
            Pose3D botpose_mt2 = m_limelight.result.getBotpose_MT2();
            if (botpose_mt2 != null) {
                Position position = botpose_mt2.getPosition();
                double distance = Math.hypot(( position.x + 1.48), (position.y - 1.41));
                telemetry.addData("X", position.x);
                telemetry.addData("Y", position.y);
                telemetry.addData("Distance", distance);
            }
        } else {
            telemetry.addLine("Limelight: No Targets found");
        }
        telemetry.addData("Shooter Velocity", m_shooter.motorShooter.getVelocity() * 60 / 28);
        telemetry.addData("Shooter Target Velocity", targetVelocity);
        telemetry.addData("Current Heading(Degrees)", m_drivetrain.odo.getHeading(AngleUnit.DEGREES));
        telemetry.addData("Shooter Hood Position", m_shooter.servoRight.getPosition());
        telemetry.addData("Transfer Position", m_transfer.servo_transfer.getPosition());
        telemetry.addData("Shooter Power", m_shooter.motorShooter.getVelocity());
        telemetry.addData("Heading", m_drivetrain.odo.getHeading(AngleUnit.DEGREES));
        telemetry.update();
    }
}
