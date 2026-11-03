package org.firstinspires.ftc.teamcode.opmodes.teleops.tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.mechanisms.Pusher;

@TeleOp(group = "tests")
public class PusherTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {

        Pusher pusher = new Pusher(hardwareMap.get(DcMotorEx.class, "pusher"));

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            if (gamepad1.circle) {
                pusher.setState(Pusher.PusherState.LAUNCH);

            } else if (gamepad1.square) {
                pusher.setState(Pusher.PusherState.PULL);
            } else {
                pusher.setState(Pusher.PusherState.IDLE);
            }
            telemetry.addData("State", pusher.getState());
            telemetry.addData("Gamepad 1 square", gamepad1.square);
            telemetry.addData("Gamepad 1 circle", gamepad1.circle);
            telemetry.update();

        }
    }
}