package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
/**
 * Created by 4924_Users on 1/5/2017.
 */

@Autonomous(name = "BlueRevLaunchAndBeacon")
public class BlueRevLaunchAndBeacon extends RevLaunchAndBeaconBase{

    public BlueRevLaunchAndBeacon() {

        launchPositioningPath = new DrivePathSegment[]{

                new DrivePathSegment(8.5f, 0.3f, DrivePathSegment.LINEAR),
                new DrivePathSegment(8.0f, 0.3f, DrivePathSegment.TURN),
                new DrivePathSegment(1.0f),
        };

        beaconPath = new DrivePathSegment[]{

                new DrivePathSegment(0.0f, 0.3f, DrivePathSegment.TURN),
                new DrivePathSegment(-2.0f, 0.3f, DrivePathSegment.LINEAR),
                new DrivePathSegment(38.0f, 0.3f, DrivePathSegment.TURN),
                new DrivePathSegment(13.0f, 0.2f, DrivePathSegment.LINEAR),
                new DrivePathSegment(78.0f, 0.3f, DrivePathSegment.TURN),
                new DrivePathSegment(10.0f, 0.2f, DrivePathSegment.LINEAR),
                new DrivePathSegment(5.0f, 0.1f, DrivePathSegment.LINEAR),
        };
    }

    @Override
    public boolean isRed() {

        return false;
    }
}

