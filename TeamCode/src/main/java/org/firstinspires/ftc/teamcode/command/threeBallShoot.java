package org.firstinspires.ftc.teamcode.command;

import static org.firstinspires.ftc.teamcode.cmd.Commandlet.waitFor;

import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;

import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Transfer;

public class threeBallShoot extends SequentialCommandGroup {

    /**
     * Command Group for shooting three balls (no power).
     *
     * @param intake   The intake to turn on
     * @param transfer The transfer to turn on
     */
    public threeBallShoot(Intake intake, Transfer transfer) {
        addCommands(
                new InstantCommand(() -> intake.setMode(Intake.Mode.INGEST)),
                waitFor(400),

                //set the shoot position
                new InstantCommand(transfer::shoot_position),
                waitFor(2000),
                //turn off intake
                new InstantCommand(() -> intake.setMode(Intake.Mode.OFF)),

                //push to off position
                new InstantCommand(() -> transfer.off_position())

        );
        addRequirements();
    }
}
