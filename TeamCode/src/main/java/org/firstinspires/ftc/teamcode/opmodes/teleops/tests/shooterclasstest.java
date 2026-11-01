
package org.firstinspires.ftc.teamcode.opmodes.teleops.tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.mechanisms.Shooter;

@TeleOp
public class shooterclasstest extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        DcMotorEx motor = hardwareMap.get(DcMotorEx.class, "flywheel");

        CRServo turret = hardwareMap.get(CRServo.class, "turret");

        Servo hood = hardwareMap.get(Servo.class, "hood");

        Shooter shooter = new Shooter(motor, hood, turret);

        waitForStart();

        while (opModeIsActive()) {

            if (gamepad1.circle) {
                shooter.setTargetVelocity(1000);
            } else {
                shooter.setTargetVelocity(0);
            }

            if (gamepad1.dpad_down) {
                turret.setPower(1);
            } else {
                turret.setPower(0);
            }

            if (gamepad1.triangle) {
                hood.setPosition(1);
            } else {
                hood.setPosition(0);
            }


            telemetry.addData("Flywheel Velocity", shooter.getCurrentVelocity());
            telemetry.addData("Flywheel State", shooter.getState());
            telemetry.addData("Target Velocity", shooter.getTargetVelocity());
            telemetry.update();
        }
    }
}
