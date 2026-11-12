package org.firstinspires.ftc.teamcode.opmodes.teleops.tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.mechanisms.Intake;
import org.firstinspires.ftc.teamcode.mechanisms.Pusher;
import org.firstinspires.ftc.teamcode.mechanisms.Shooter;
import org.firstinspires.ftc.teamcode.robot.Robot;

@TeleOp
public class NonDrivetrainMechanisms extends LinearOpMode {

    @Override
    public void runOpMode() {
        Robot robot = new Robot(hardwareMap);
        Pusher pusher = new Pusher(robot.hardware.pusher);
        Shooter shooter = new Shooter(robot.hardware.pollenFlywheel,
                robot.hardware.pollenHood,
                robot.hardware.pollenTurret);
        waitForStart();

        while (opModeIsActive()) {

            pusher.update(shooter.isReady(), gamepad1.right_bumper);
            shooter.setTargetVelocity(2000);

            if (gamepad1.a) {
                robot.intake.setState(Intake.IntakeState.INTAKE);
            } else if (gamepad1.b) {
                robot.intake.setState(Intake.IntakeState.OUTTAKE);
            } else {
                robot.intake.setState(Intake.IntakeState.DOWN_IDLE);
            }
            if (gamepad2.share) {
                robot.hardware.imu.resetYaw();
            }

            telemetry.addData("Heading", robot.hardware.imu.getRobotYawPitchRollAngles()
                    .getYaw(org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES));
            telemetry.addLine();
            telemetry.addData("Intake State", robot.intake.getState());
            telemetry.addData("Pusher State", pusher.getState());
            telemetry.addData("Shooter State", shooter.getState());
            telemetry.update();

        }


    }
}