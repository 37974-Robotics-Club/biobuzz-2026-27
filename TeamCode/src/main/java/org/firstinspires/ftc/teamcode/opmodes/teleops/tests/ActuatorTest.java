package org.firstinspires.ftc.teamcode.opmodes.teleops.tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class ActuatorTest extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        DcMotorEx motor = hardwareMap.get(DcMotorEx.class, "motor");
        CRServo axon = hardwareMap.get(CRServo.class, "axon");
        Servo servo = hardwareMap.get(Servo.class, "servo");

        waitForStart();

        while (opModeIsActive()) {

            if (gamepad1.circle) {
                motor.setPower(1);
            }
            else {
                motor.setPower(0);
            }

            if (gamepad1.dpad_down) {
                axon.setPower(1);
            }
            else {
                axon.setPower(0);
            }

            if (gamepad1.triangle) {
                servo.setPosition(1);
            }
            else {
                servo.setPosition(0);
            }

            telemetry.update();
        }
    }
}
