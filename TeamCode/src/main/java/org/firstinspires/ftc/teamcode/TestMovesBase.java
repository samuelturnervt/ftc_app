package org.firstinspires.ftc.teamcode;

/**
 * Created by 4924_Users on 12/23/2016.
 */

public abstract class TestMovesBase extends AutonomousBase {

    @Override
    public State[] stateList() {

        return new State[] {

                State.STATE_INITIAL,
                State.STATE_START_BEACON_PATH,
                State.STATE_DRIVE,
                State.STATE_STOP
        };
    }
}
