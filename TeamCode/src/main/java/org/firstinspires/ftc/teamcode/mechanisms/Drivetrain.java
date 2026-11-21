package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.robot.Config;
import org.firstinspires.ftc.teamcode.robot.controllers.SlewRateLimiter;

public class Drivetrain {

    private final SlewRateLimiter driveLimiter = new SlewRateLimiter(16.0);
    private final SlewRateLimiter strafeLimiter = new SlewRateLimiter(16.0);
    private final SlewRateLimiter turnLimiter = new SlewRateLimiter(16.0);

    private final DcMotorEx frontLeft;
    private final DcMotorEx frontRight;
    private final DcMotorEx backLeft;
    private final DcMotorEx backRight;
    private final IMU imu;

    public Drivetrain(DcMotorEx frontLeft, DcMotorEx frontRight, DcMotorEx backLeft, DcMotorEx backRight, IMU imu) {
        this.frontLeft = frontLeft;
        this.frontRight = frontRight;
        this.backLeft = backLeft;
        this.backRight = backRight;
        this.imu = imu;
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

        double heading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
        double rotatedX = strafe * Math.cos(-heading) - forward * Math.sin(-heading);
        double rotatedY = strafe * Math.sin(-heading) + forward * Math.cos(-heading);

        robotCentricDrive(rotatedX, rotatedY, rotation);
    }
}