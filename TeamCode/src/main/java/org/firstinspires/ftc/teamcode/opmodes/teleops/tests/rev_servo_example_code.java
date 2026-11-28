package org.firstinspires.ftc.teamcode.opmodes.teleops;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;


@TeleOp

public class rev_servo_example_code extends LinearOpMode {
    private CRServo test_servo;


    @Override
    public void runOpMode() {
        test_servo = hardwareMap.get(CRServo.class, "axon");

        test_servo.setPower(0);

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            if (gamepad1.y) {
                //move to position 0
                test_servo.setPower(0);

            } else if (gamepad1.x || gamepad1.b) {
                //move to position 0.5
                test_servo.setPower(-1);

            } else if (gamepad1.a) {
                //move to position 1
                test_servo.setPower(1);
            }
            telemetry.addData("Status", "Running");
            telemetry.update();

        }   }
    }