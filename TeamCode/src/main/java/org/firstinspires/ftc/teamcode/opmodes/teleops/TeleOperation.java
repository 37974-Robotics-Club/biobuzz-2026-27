package org.firstinspires.ftc.teamcode.opmodes.teleops;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.mechanisms.Intake;
import org.firstinspires.ftc.teamcode.mechanisms.Pusher;
import org.firstinspires.ftc.teamcode.robot.Robot;

@TeleOp
public class TeleOperation extends LinearOpMode {

    @Override
    public void runOpMode() {
        Robot robot = new Robot(hardwareMap);
        Intake intake = new Intake(robot.hardware.intake, robot.hardware.intakeDrop);
        Pusher pusher = new Pusher(robot.hardware.pusher);

        waitForStart();

        while (opModeIsActive()) {
            intake.setState(Intake.IntakeState.DOWN_IDLE);

            robot.drivetrain.fieldCentricDrive(gamepad1.left_stick_x, -gamepad1.left_stick_y, gamepad1.right_stick_x);

            if (gamepad1.a) {
                intake.setState(Intake.IntakeState.INTAKE);
            } else if (gamepad1.b) {
                intake.setState(Intake.IntakeState.OUTTAKE);
            } else {
                intake.setState(Intake.IntakeState.DOWN_IDLE);
            }

            if (gamepad1.dpad_up) {
                pusher.setState(Pusher.PusherState.FEEDING);
            } else {
                pusher.setState(Pusher.PusherState.IDLE);
            }
            if (gamepad1.share) {
                robot.hardware.imu.resetYaw();
            }

            intake.update();

            pusher.update(robot.pollenShooter.isReady(), gamepad1.dpad_up);
            if (robot.pollenShooter.isReady()) {
                gamepad1.rumble(500);
            }


            telemetry.addData("Heading", robot.hardware.imu.getRobotYawPitchRollAngles()
                    .getYaw(org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES));
            telemetry.addLine("gp1 - share to reset heading");
            telemetry.update();
        }


        }
    }