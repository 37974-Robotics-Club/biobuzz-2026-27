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

        double shooterRPM = Math.abs(pollenShooter.getCurrentVelocity()) * 60 / 28;


        intake.setState(Intake.IntakeState.UP);

        waitForStart();

        while (opModeIsActive()) {

            intake.setState(Intake.IntakeState.DOWN_IDLE);

            //DRIVETRAIN
            robot.drivetrain.fieldCentricDrive(gamepad1.left_stick_x, -gamepad1.left_stick_y, gamepad1.right_stick_x);
            if (gamepad1.shareWasPressed()) {
                robot.hardware.imu.resetYaw();
            }

            //INTAKE
            if (gamepad1.right_bumper || gamepad2.right_trigger_pressed) {
                intake.setState(Intake.IntakeState.INTAKE);
            } else if (gamepad1.left_bumper || gamepad2.left_trigger_pressed) {
                intake.setState(Intake.IntakeState.OUTTAKE);
            } else {
                intake.setState(Intake.IntakeState.DOWN_IDLE);
            }

            //TEMPORARY PUSHER
            if (gamepad2.triangle) {
                pusher.setState(Pusher.PusherState.FEEDING);
            }

            //SHOOTER
            if (gamepad2.cross) {
                pollenShooter.setTargetVelocity(2400);
            } else {
                pollenShooter.setTargetVelocity(0);
            }
            boolean PSisReady = pollenShooter.isReady();

            pollenShooter.setTurretPower(gamepad2.left_stick_x);
            //HOOD
            if (-gamepad2.left_stick_y < -0.2) {
                pollenShooter.hoodDown();
            } else if (-gamepad2.left_stick_y > 0.2) {
                pollenShooter.hoodUp();
            }
            //TURRET
            pollenShooter.setTurretPower(gamepad2.left_stick_x);

            //RUMBLE
            if (PSisReady && !PSwasReady) {
                gamepad2.rumble(300);
            }
            PSwasReady = PSisReady;

            intake.update();
            pusher.update(PSisReady, gamepad2.right_bumper, gamepad2.left_bumper);

            telemetry.addData("Hood Position", pollenShooter.getHoodPosition());
            telemetry.addData("Vel", pollenShooter.getCurrentVelocity());
            telemetry.addData("RPM", shooterRPM);
            telemetry.update();
        }
    }
}