package org.firstinspires.ftc.teamcode.opmodes.teleops.tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.robot.controllers.SlewRateLimiter;

@TeleOp(group = "tests")
public class RobotCentric extends LinearOpMode {

    private final SlewRateLimiter driveLimiter = new SlewRateLimiter(2.0);
    private final SlewRateLimiter strafeLimiter = new SlewRateLimiter(2.0);
    private final SlewRateLimiter turnLimiter = new SlewRateLimiter(2.0);

    @Override
    public void runOpMode() throws InterruptedException {

        DcMotorEx frontRight = hardwareMap.get(DcMotorEx.class, "frontRight");
        DcMotorEx frontLeft = hardwareMap.get(DcMotorEx.class, "frontLeft");
        DcMotorEx backRight = hardwareMap.get(DcMotorEx.class, "backRight");
        DcMotorEx backLeft = hardwareMap.get(DcMotorEx.class, "backLeft");


        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            double rawDrive = -gamepad1.left_stick_y;
            double rawStrafe = gamepad1.left_stick_x;
            double rawTurn = gamepad1.right_stick_x;

            double smoothedDrive = driveLimiter.calculate(rawDrive);
            double smoothedStrafe = strafeLimiter.calculate(rawStrafe);
            double smoothedTurn = turnLimiter.calculate(rawTurn);

            smoothedStrafe *= 1.1;

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
    }
}