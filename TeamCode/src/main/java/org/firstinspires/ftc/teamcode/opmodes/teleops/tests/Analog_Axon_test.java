package org.firstinspires.ftc.teamcode.opmodes.teleops.tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.mechanisms.Shooter;
import org.firstinspires.ftc.teamcode.robot.Robot;

@TeleOp
public class Analog_Axon_test extends LinearOpMode {

    @Override
    public void runOpMode() {
        Robot robot = new Robot(hardwareMap);
        Shooter pollenShooter = robot.pollenShooter;

        waitForStart();

        while (opModeIsActive()) {
            double turretPower = gamepad2.left_stick_x;

            pollenShooter.setTurretPower(turretPower);

            telemetry.addData("Turret Input Power", "%.2f", turretPower);
            telemetry.addData("Turret Feedback Voltage", "%.3f V", pollenShooter.getTurretVoltage());
            telemetry.addLine();
            telemetry.addData("Controls", "P2 Left Stick X to rotate turret");
            telemetry.update();
        }
    }
}