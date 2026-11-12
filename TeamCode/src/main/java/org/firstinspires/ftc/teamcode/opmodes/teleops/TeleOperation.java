package org.firstinspires.ftc.teamcode.opmodes.teleops;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.mechanisms.Intake;
import org.firstinspires.ftc.teamcode.robot.Robot;

@TeleOp
public class TeleOperation extends LinearOpMode {

    @Override
    public void runOpMode() {
        Robot robot = new Robot(hardwareMap);
        Intake intake = new Intake(robot.hardware.intake, robot.hardware.intakeDrop);
        waitForStart();

        while (opModeIsActive()) {
            intake.setState(Intake.IntakeState.DOWN_IDLE);

            robot.drivetrain.fieldCentricDrive(gamepad1.left_stick_x, -gamepad1.left_stick_y, gamepad1.right_stick_x);
            // robot centric available

            if (gamepad1.a) {
                intake.setState(Intake.IntakeState.INTAKE);
            } else if (gamepad1.b) {
                intake.setState(Intake.IntakeState.OUTTAKE);
            } else {
                intake.setState(Intake.IntakeState.DOWN_IDLE);
            }
            if (gamepad2.share) {
                robot.hardware.imu.resetYaw();
            }

            telemetry.addData("Heading", robot.hardware.imu.getRobotYawPitchRollAngles()
                    .getYaw(org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES));
            telemetry.update();
        }


        }
    }