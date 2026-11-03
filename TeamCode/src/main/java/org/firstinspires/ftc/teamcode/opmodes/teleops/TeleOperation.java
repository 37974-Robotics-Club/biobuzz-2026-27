package org.firstinspires.ftc.teamcode.opmodes.teleops;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.robot.Robot;

@TeleOp
public class TeleOperation extends LinearOpMode {

    @Override
    public void runOpMode() {
        Robot robot = new Robot(hardwareMap);
        waitForStart();

        while (opModeIsActive()) {

            robot.drivetrain.fieldCentricDrive(gamepad1.left_stick_x, -gamepad1.left_stick_y, gamepad1.right_stick_x);
            // robot centric available
        }

        if (gamepad1.share) {
            robot.hardware.imu.resetYaw();
        }

        telemetry.addData("Heading", robot.hardware.imu.getRobotYawPitchRollAngles()
                .getYaw(org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES));
        telemetry.update();
        }
    }