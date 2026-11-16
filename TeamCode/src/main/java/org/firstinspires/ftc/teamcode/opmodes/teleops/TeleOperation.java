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
        Intake intake = new Intake(robot.hardware.intake, robot.hardware.intakeDrop);
        Pusher pusher = new Pusher(robot.hardware.pusher);
        Shooter pollenShooter = new Shooter(
                robot.hardware.pollenFlywheel,
                robot.hardware.pollenHood,
                robot.hardware.pollenTurret);

        waitForStart();

        while (opModeIsActive()) {
            intake.update();
            pusher.update(pollenShooter.isReady(), gamepad2.right_trigger_pressed);


            intake.setState(Intake.IntakeState.DOWN_IDLE);

            robot.drivetrain.fieldCentricDrive(gamepad1.left_stick_x, -gamepad1.left_stick_y, gamepad1.right_stick_x);

            if (gamepad1.right_trigger_pressed || gamepad1.right_bumper) {
                intake.setState(Intake.IntakeState.INTAKE);
            } else if (gamepad1.left_trigger_pressed || gamepad1.left_bumper) {
                intake.setState(Intake.IntakeState.OUTTAKE);
            } else {
                intake.setState(Intake.IntakeState.DOWN_IDLE);
            }

            if (gamepad1.share) {
                robot.hardware.imu.resetYaw();
            }

            if (gamepad2.square) {
                pusher.setState(Pusher.PusherState.FEEDING);            }

            if (gamepad2.triangle) {
                pollenShooter.setTargetVelocity(2000);
            } else {
                pollenShooter.setTargetVelocity(0);
            }

            if (pollenShooter.isReady()) {
                gamepad1.rumble(500);
            }


            telemetry.addData("Heading", robot.hardware.imu.getRobotYawPitchRollAngles()
                    .getYaw(org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES));
            telemetry.addLine("gp1 - share to reset heading");
            telemetry.addData("vel", pollenShooter.getCurrentVelocity());
            telemetry.update();
        }


        }
    }