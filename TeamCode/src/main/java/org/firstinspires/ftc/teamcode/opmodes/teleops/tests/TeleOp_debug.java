package org.firstinspires.ftc.teamcode.opmodes.teleops.tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.mechanisms.Intake;
import org.firstinspires.ftc.teamcode.mechanisms.Pusher;
import org.firstinspires.ftc.teamcode.mechanisms.Shooter;
import org.firstinspires.ftc.teamcode.robot.Robot;

@TeleOp
public class TeleOp_debug extends LinearOpMode {

    @Override
    public void runOpMode() {
        Robot robot = new Robot(hardwareMap);

        Intake intake = robot.intake;
        Pusher pusher = robot.pusher;
        Shooter pollenShooter = robot.pollenShooter;

        boolean PSwasReady = false;



        intake.setState(Intake.IntakeState.UP);

        waitForStart();

        while (opModeIsActive()) {

            double shooterRPM = Math.abs(pollenShooter.getCurrentVelocity()) * 60 / 28;

            intake.setState(Intake.IntakeState.DOWN_IDLE);

            //DRIVETRAIN
            double dpadDrive = 0.0;
            double dpadStrafe = 0.0;
            if (gamepad1.dpad_up) dpadDrive += 1.0;
            if (gamepad1.dpad_down) dpadDrive -= 1.0;
            if (gamepad1.dpad_right) dpadStrafe += 1.0;
            if (gamepad1.dpad_left) dpadStrafe -= 1.0;

            if (dpadDrive != 0 || dpadStrafe != 0) {
                robot.drivetrain.robotCentricDrive(dpadStrafe, dpadDrive, gamepad1.right_stick_x);
            } else {
                robot.drivetrain.fieldCentricDrive(gamepad1.left_stick_x, -gamepad1.left_stick_y, gamepad1.right_stick_x);
            }

            if (gamepad1.shareWasPressed()) {
                robot.hardware.imu.resetYaw();
            }

            //INTAKE
            if (gamepad1.right_trigger > 0.25 || gamepad2.right_trigger > 0.25) {
                intake.setState(Intake.IntakeState.INTAKE);
            } else if (gamepad1.left_trigger > 0.25 || gamepad2.left_trigger > 0.25) {
                intake.setState(Intake.IntakeState.OUTTAKE);
            } else {
                intake.setState(Intake.IntakeState.DOWN_IDLE);
            }

            //SHOOTER
            if (gamepad2.cross) {
                pollenShooter.setTargetVelocity(2600);
            } else {
                pollenShooter.setTargetVelocity(0);
            }
            boolean PSisReady = pollenShooter.isReady();

            //PUSHER
            if (gamepad2.right_bumper && PSisReady) {
                pusher.setState(Pusher.PusherState.FEEDING);
            } else if (gamepad2.left_bumper) {
                pusher.setState(Pusher.PusherState.REVERSE);
            } else {
                pusher.setState(Pusher.PusherState.IDLE);
            }

            //HOOD
            if (-gamepad2.left_stick_y < -0.2) {
                pollenShooter.hoodDown();
            } else if (-gamepad2.left_stick_y > 0.2) {
                pollenShooter.hoodUp();
            }
            //TURRET
            pollenShooter.setTurretPower(gamepad2.left_stick_x);

            //RUMBLE (trigger on transition to ready state)
            //boolean justBecameReady = PSisReady && !PSwasReady;
            if (pollenShooter.isReady()) {
                gamepad2.rumble(300);
            }
            //PSwasReady = PSisReady;

            intake.update();

            telemetry.addData("Drive, Strafe", "P1 left stick");
            telemetry.addData("Turn", "P1 right stick x");
            telemetry.addData("Robot-centric drive", "P1 d-pads");
            telemetry.addLine();
            telemetry.addData("Hood Position", pollenShooter.getHoodPosition());
            telemetry.addData("Vel", pollenShooter.getCurrentVelocity());
            telemetry.addData("RPM", shooterRPM);
            telemetry.addLine();
            telemetry.addData("PSisReady", PSisReady);
            //telemetry.addData("JustBecameReady", justBecameReady);
            telemetry.addLine();
            telemetry.addData("Intake", "P1 right bumper, P2 right trigger");
            telemetry.addData("Outtake", "P1 left bumper, P2 left trigger");
            telemetry.addData("Run shooter", "P2 Cross");
            telemetry.addData("Pusher Up", "P2 triangle + shooter at target speed");
            telemetry.addData("Pusher Down", "P2 circle");
            telemetry.addData("Turret (in theory)", "P2 left stick x");
            telemetry.addData("Hood", "P2 left stick y");
            telemetry.update();
        }
    }
}