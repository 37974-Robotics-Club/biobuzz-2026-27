
package org.firstinspires.ftc.teamcode.opmodes.teleops.tests;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServo;

import org.firstinspires.ftc.teamcode.robot.controllers.RTPAxon;

@TeleOp(name = "Cont. Rotation Axon Test", group = "test")
public class CRAxonTest extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        // Panels telemetry
        TelemetryManager panelsTelemetry =
                PanelsTelemetry.INSTANCE.getTelemetry();

        // Hardware
        CRServo crservo = hardwareMap.crservo.get("rightHorizSlide");

        AnalogInput encoder = hardwareMap.get(
                AnalogInput.class,
                "rightHorizSlideEncoder"
        );

        RTPAxon servo = new RTPAxon(crservo, encoder);

        // Previous button states for edge detection
        boolean prevUp = false;
        boolean prevDown = false;
        boolean prevA = false;
        boolean prevY = false;
        boolean prevX = false;
        boolean prevRB = false;
        boolean prevLB = false;
        boolean prevBack = false;

        telemetry.addLine("Axon initialized.");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {

            // Update controller
            servo.update();

            // Target rotation
            if (gamepad1.dpad_up && !prevUp) {
                servo.changeTargetRotation(15);
            }

            if (gamepad1.dpad_down && !prevDown) {
                servo.changeTargetRotation(-15);
            }

            if (gamepad1.a && !prevA) {
                servo.setTargetRotation(0);
            }

            // P tuning
            if (gamepad1.y && !prevY) {
                servo.setKP(servo.getKP() + 0.001);
            }

            if (gamepad1.x && !prevX) {
                servo.setKP(Math.max(0, servo.getKP() - 0.001));
            }

            // I tuning
            if (gamepad1.right_bumper && !prevRB) {
                servo.setKI(servo.getKI() + 0.0001);
            }

            if (gamepad1.left_bumper && !prevLB) {
                servo.setKI(Math.max(0, servo.getKI() - 0.0001));
            }

            // Reset PID coefficients
            if (gamepad1.back && !prevBack) {
                servo.setKP(0.015);
                servo.setKI(0.0005);
                servo.setKD(0.0025);
                servo.resetPID();
            }

            // Store current button states
            prevUp = gamepad1.dpad_up;
            prevDown = gamepad1.dpad_down;
            prevA = gamepad1.a;
            prevY = gamepad1.y;
            prevX = gamepad1.x;
            prevRB = gamepad1.right_bumper;
            prevLB = gamepad1.left_bumper;
            prevBack = gamepad1.back;

            // Panels telemetry
            panelsTelemetry.debug(
                    "Starting angle: " + servo.STARTPOS,
                    servo.log(),
                    "NTRY: " + servo.ntry
            );

            // Update Panels and Driver Station
            panelsTelemetry.update(telemetry);
        }

        // Stop servo when OpMode ends
        servo.setPower(0);
    }
}
