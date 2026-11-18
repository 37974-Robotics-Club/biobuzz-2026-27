package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.robot.Config;
import org.firstinspires.ftc.teamcode.robot.SlewRateLimiter;

public class Drivetrain {

    private final SlewRateLimiter driveLimiter = new SlewRateLimiter(4.0);
    private final SlewRateLimiter strafeLimiter = new SlewRateLimiter(4.0);
    private final SlewRateLimiter turnLimiter = new SlewRateLimiter(4.0);

    private final DcMotorEx frontLeft;
    private final DcMotorEx frontRight;
    private final DcMotorEx backLeft;
    private final DcMotorEx backRight;
    private final IMU imu;
    private final GoBildaPinpointDriver pinpoint;

    public Drivetrain(DcMotorEx frontLeft, DcMotorEx frontRight, DcMotorEx backLeft, DcMotorEx backRight, IMU imu, GoBildaPinpointDriver pinpoint) {
        this.frontLeft = frontLeft;
        this.frontRight = frontRight;
        this.backLeft = backLeft;
        this.backRight = backRight;
        this.imu = imu;
        this.pinpoint = pinpoint;
    }

    public void update() {
        if (pinpoint != null) {
            pinpoint.update();
        }
    }

    public void resetHeading() {
        if (pinpoint != null) {
            pinpoint.setPosition(new Pose2D(DistanceUnit.INCH, 0, 0, AngleUnit.DEGREES, 0));
        }
        if (imu != null) {
            imu.resetYaw();
        }
    }

    public double getHeading(AngleUnit unit) {
        if (pinpoint != null) {
            return pinpoint.getHeading(unit);
        } else if (imu != null) {
            return imu.getRobotYawPitchRollAngles().getYaw(unit);
        }
        return 0;
    }

    public GoBildaPinpointDriver getPinpoint() {
        return pinpoint;
    }

    public void robotCentricDrive(double rawStrafe, double rawDrive, double rawTurn) {

        double smoothedDrive = driveLimiter.calculate(rawDrive);
        double smoothedStrafe = strafeLimiter.calculate(rawStrafe);
        double smoothedTurn = turnLimiter.calculate(rawTurn);

        smoothedStrafe *= Config.STRAFE_COMPENSATION;

        double denominator = Math.max(Math.abs(smoothedDrive) + Math.abs(smoothedStrafe) + Math.abs(smoothedTurn), 1.0);
        double speedLimit = 0.8;

        double frontLeftPower = ((smoothedDrive + smoothedStrafe + smoothedTurn) / denominator) * speedLimit;
        double backLeftPower = ((smoothedDrive - smoothedStrafe + smoothedTurn) / denominator) * speedLimit;
        double frontRightPower = ((smoothedDrive - smoothedStrafe - smoothedTurn) / denominator) * speedLimit;
        double backRightPower = ((smoothedDrive + smoothedStrafe - smoothedTurn) / denominator) * speedLimit;

        frontLeft.setPower(frontLeftPower);
        backLeft.setPower(backLeftPower);
        frontRight.setPower(frontRightPower);
        backRight.setPower(backRightPower);
    }

    public void fieldCentricDrive(double strafe, double forward, double rotation) {

        double heading = getHeading(AngleUnit.DEGREES);
        double rotatedX = strafe * Math.cos(-heading) - forward * Math.sin(-heading);
        double rotatedY = strafe * Math.sin(-heading) + forward * Math.cos(-heading);

        robotCentricDrive(rotatedX, rotatedY, rotation);
    }
}