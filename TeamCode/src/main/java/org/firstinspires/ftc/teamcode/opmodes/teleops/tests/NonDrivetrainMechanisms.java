package org.firstinspires.ftc.teamcode.opmodes.teleops.tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.mechanisms.Intake;
import org.firstinspires.ftc.teamcode.mechanisms.Pusher;
import org.firstinspires.ftc.teamcode.mechanisms.Shooter;
import org.firstinspires.ftc.teamcode.robot.Robot;

@TeleOp(group = "tests")
public class NonDrivetrainMechanisms extends LinearOpMode {

    @Override
    public void runOpMode() {
        Robot robot = new Robot(hardwareMap);

        Intake intake = robot.intake;
        Pusher pusher = robot.pusher;
        Shooter pollenShooter = robot.pollenShooter;
        waitForStart();

        while (opModeIsActive()) {

            pusher.update(pollenShooter.isReady(), gamepad1.right_bumper, gamepad1.left_bumper);
            pollenShooter.setTargetVelocity(2000);

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
            telemetry.addLine();
            telemetry.addData("Intake State", intake.getState());
            telemetry.addData("Pusher State", pusher.getState());
            telemetry.addData("Shooter State", pollenShooter.getState());
            telemetry.update();

        }


    }
}