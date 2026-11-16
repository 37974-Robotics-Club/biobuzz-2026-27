package org.firstinspires.ftc.teamcode.opmodes.teleops;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.mechanisms.Intake;
import org.firstinspires.ftc.teamcode.mechanisms.Pusher;
import org.firstinspires.ftc.teamcode.mechanisms.Shooter;
import org.firstinspires.ftc.teamcode.robot.Robot;

@TeleOp
public class TeleOperation extends LinearOpMode {

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


            intake.setState(Intake.IntakeState.DOWN_IDLE);

            //DRIVETRAIN
            robot.drivetrain.fieldCentricDrive(gamepad1.left_stick_x, -gamepad1.left_stick_y, gamepad1.right_stick_x);
            if (gamepad1.shareWasPressed()) {
                robot.hardware.imu.resetYaw();
            }

            //INTAKE
            if (gamepad1.right_bumper) {
                intake.setState(Intake.IntakeState.INTAKE);
            } else if (gamepad1.left_bumper) {
                intake.setState(Intake.IntakeState.OUTTAKE);
            } else {
                intake.setState(Intake.IntakeState.DOWN_IDLE);
            }

            //TEMPORARY PUSHER
            if (gamepad2.squareWasPressed()) {
                pusher.setState(Pusher.PusherState.FEEDING);
            }

            //SHOOTER
            if (gamepad2.triangle) {
                pollenShooter.setTargetVelocity(2000);
            } else {
                pollenShooter.setTargetVelocity(0);
            }
            boolean PSisReady = pollenShooter.isReady();

            //RUMBLE
            if (PSisReady && !PSwasReady) {
                gamepad1.rumble(500);
            }
            PSwasReady = PSisReady;

            intake.update();
            pusher.update(PSisReady, gamepad2.right_trigger_pressed);
        }
        }
    }