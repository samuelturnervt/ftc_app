package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by 4924_Users on 1/4/2017.
 */

@TeleOp(name = "EvolutionFullHolonomic")
public class EvolutionFullHolonomic extends TeleopBase {

    private boolean throwing;
    private double throwStartTime;
    private double throwInterval = 0.0;
    private double switchModeStartTime;
    private final double THROW_INPUT_DELAY = 0.7;
    private final double BOUNCE_DELAY = 0.2;
    private float driveCoeff;

    public boolean gyroCorrecting = false;

    @Override
    public void init() {

        super.init();

        collectionMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        driveDirection = -1;
        driveCoeff = 1;
        turningGyro.calibrate();

        throwing = false;
    }

    private void setPowerForFullHolonomic(float x, float y, int heading, float leftTurnPower, float rightTurnPower, int driveDirection) {

        x *= driveDirection;
        y *= driveDirection;

        int headingDifference = steadyHeading - heading;

        if (isTurningLeft || isTurningRight) {

            powerLevels.frontLeftPower = y - x - leftTurnPower + rightTurnPower;
            powerLevels.backLeftPower = y + x - leftTurnPower + rightTurnPower;
            powerLevels.backRightPower = y - x + leftTurnPower - rightTurnPower;
            powerLevels.frontRightPower = y + x + leftTurnPower - rightTurnPower;

        } else {

            if (gyroCorrecting) {

                if (steadyHeading - heading >= 0) {

                    headingDifference = steadyHeading - heading;
                }

                if (heading - steadyHeading >= 0) {

                    headingDifference = 0 - heading + steadyHeading;
                }

                if (headingDifference < 0) {

                    powerLevels.frontLeftPower = (y - x) - Math.abs(headingDifference / 10);
                    powerLevels.backLeftPower = (y + x) - Math.abs(headingDifference / 10);
                    powerLevels.backRightPower = y - x;
                    powerLevels.frontRightPower = y + x;

                } else {

                    powerLevels.frontLeftPower = y - x;
                    powerLevels.backLeftPower = y + x;
                    powerLevels.backRightPower = (y - x) - (headingDifference / 10);
                    powerLevels.frontRightPower = (y + x) - (headingDifference / 10);
                }

            } else {

                powerLevels.frontLeftPower = y - x;
                powerLevels.backLeftPower = y + x;
                powerLevels.backRightPower = y - x;
                powerLevels.frontRightPower = y + x;
            }
        }

        setCoeffPowerLevels(driveCoeff);
    }

    private void setCoeffPowerLevels(float driveCoeff) {

        powerLevels.frontLeftPower *= driveCoeff;
        powerLevels.backLeftPower *= driveCoeff;
        powerLevels.frontRightPower *= driveCoeff;
        powerLevels.backRightPower *= driveCoeff;
    }

    private boolean d1LeftBumperIsPressed() { return gamepad1.left_bumper; }

    private boolean d1RightBumperIsPressed() { return gamepad1.right_bumper; }

    private boolean d1StartIsPressed() { return gamepad1.start; }

    private boolean d2StartIsPressed() { return gamepad2.start; }

    private boolean d1BackIsPressed() { return gamepad1.back; }

    @Override
    public void loop() {
        int currentHeading = turningGyro.getHeading();

        float x = -gamepad1.left_stick_x;
        float y = -gamepad1.left_stick_y;

        isTurningLeft = leftTriggerValue() > 0.01f;
        isTurningRight = rightTriggerValue() > 0.01f;

        if (d1DPadDownIsPressed()) {

            driveDirection = -1;

        } else if (d1DPadUpIsPressed()) {

            driveDirection = 1;
        }

        if (d1XIsPressed()) {

            leftBeaconServoOut();

        } else if (d1YIsPressed()) {

            leftBeaconServoIn();
        }

        if (d1AIsPressed() && !d1StartIsPressed()) {

            rightBeaconServoOut();

        } else if (d1BIsPressed()) {

            rightBeaconServoIn();
        }

        if (d2DPadUpIsPressed()) {

            raiseThrowingArm();

        } else if (d2DPadDownIsPressed()) {

            lowerThrowingArm();

        } else {

            stopMovingThrowingArm();
        }

        if (collectionIn()) {

            collectionIntake();

        } else if (collectionOut()) {

            collectionRelease();

        } else {

            collectionOff();
        }

        if (d2XIsPressed()) {

            openGateLow();

        } else if (d2YIsPressed()) {

            openGateHigh();

        } else if (d2AIsPressed()) {

            closeGate();
        }

        if (d1DPadLeftIsPressed() && d2DPadLeftIsPressed()) {

            lockShovel();
        }

        if (d1DPadRightIsPressed() && d2DPadRightIsPressed()) {

            unlockShovel();
        }

        if (d1BackIsPressed() && ((time.time() - switchModeStartTime) > BOUNCE_DELAY)) {

            gyroCorrecting = !gyroCorrecting;
            switchModeStartTime = time.time();
        }

        if (d2BIsPressed() && ((time.time() - throwStartTime) > THROW_INPUT_DELAY) && !d2StartIsPressed()) {

            throwing = true;
            throwInterval = 0.3;
            throwStartTime = time.time();
        }

        if (throwing && ((time.time() - throwStartTime) < throwInterval)) {

            throwingArmPowerLevel = 1.0f;

        } else if (throwing) {

            throwing = false;
            throwingArmPowerLevel = 0.0f;
        }

        if (d1LeftBumperIsPressed()) {

            driveCoeff = 1f;
        }

        if (d1RightBumperIsPressed()) {

            driveCoeff = 0.4f;
        }

        if (Math.abs(x) > 0.01 || Math.abs(y) > 0.01) {

            headingSet = true;
        }

        if ((Math.abs(x) < 0.01 && Math.abs(y) < 0.01)) {

            headingSet = false;
            steadyHeading = currentHeading;
        }

        if (isTurningLeft || isTurningRight) {

            steadyHeading = currentHeading;
        }

        telemetry.addData("x", x);
        telemetry.addData("y", y);
        telemetry.addData("Steady Angle", steadyHeading);

        // clip servo values
        rightBeaconServoPosition = Range.clip(rightBeaconServoPosition, -1.0f, 1.0f);
        leftBeaconServoPosition = Range.clip(leftBeaconServoPosition, -1.0f, 1.0f);

        rightBeaconServo.setPosition(rightBeaconServoPosition);
        leftBeaconServo.setPosition(leftBeaconServoPosition);
        collectionGateServo.setPosition(gateServoPosition);
        shovelLockServo.setPosition(shovelLockServoPosition);

        if (headingSet || isTurningLeft || isTurningRight) {

            setPowerForFullHolonomic(x, y, currentHeading, leftTriggerValue(), rightTriggerValue(), driveDirection);

        } else {

            TurnOffAllDriveMotors();
        }

        setMotorPowerLevels(powerLevels);

        throwingArm.setPower(throwingArmPowerLevel);
        collectionMotor.setPower(collectionPowerLevel);
    }
}

