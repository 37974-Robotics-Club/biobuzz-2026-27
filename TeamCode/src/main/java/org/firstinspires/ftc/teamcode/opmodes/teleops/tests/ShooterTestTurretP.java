
package org.firstinspires.ftc.teamcode.opmodes.teleops.tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.mechanisms.Shooter_PServo;

@TeleOp
public class ShooterTestTurretP extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        DcMotorEx motor = hardwareMap.get(DcMotorEx.class, "flywheel");

        Servo turret = hardwareMap.get(Servo.class, "turret");

        Servo hood = hardwareMap.get(Servo.class, "hood");

        Shooter_PServo shooter = new Shooter_PServo(motor, hood, turret);

        waitForStart();

        while (opModeIsActive()) {

            if (gamepad1.rightBumperWasPressed()) {
                shooter.setTargetVelocity(2000);
            } else {
                shooter.setTargetVelocity(0);
            }

            if (gamepad1.right_bumper) {
                shooter.turretLeft();
            } else if (gamepad1.left_bumper) {
                shooter.turretRight();
            }

            if (gamepad1.dpad_up) {
                shooter.hoodUp();
            } else if (gamepad1.dpad_down) {
                shooter.hoodDown();
            }


            telemetry.addData("Flywheel Velocity", shooter.getCurrentVelocity());
            telemetry.addData("Flywheel State", shooter.getState());
            telemetry.addData("Target Velocity", shooter.getTargetVelocity());
            telemetry.addLine();
            telemetry.update();
        }
    }
}
