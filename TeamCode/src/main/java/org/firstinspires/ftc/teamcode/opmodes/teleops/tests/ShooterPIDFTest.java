package org.firstinspires.ftc.teamcode.opmodes.teleops.tests;

import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.VoltageSensor;

@Configurable
@TeleOp(group = "tests")
public class ShooterPIDFTest extends OpMode {

    /**
     * Adapted from FTControl docs
     */

    // use Panels
    public static double targetVelocity = 0.0;

    public static double kP = 0.0;
    public static double kI = 0.0;
    public static double kD = 0.0;
    public static double kF = 0.0;

    private DcMotorEx pollenShooter;
    private TelemetryManager panelsTelemetry;
    private VoltageSensor batteryVoltage;

    @Override
    public void init() {
        batteryVoltage = hardwareMap.voltageSensor.iterator().next();

        pollenShooter = hardwareMap.get(DcMotorEx.class, "pollenShooter");

        panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();

        pollenShooter.setPower(0);
        pollenShooter.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }

    @Override
    public void start() {
        pollenShooter.setVelocity(0);
    }

    @Override
    public void loop() {

        pollenShooter.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER,
                new PIDFCoefficients(kP, kI, kD, kF)
        );

        pollenShooter.setVelocity(targetVelocity);

        double currentVelocity = pollenShooter.getVelocity();
        double error = targetVelocity - currentVelocity;


        double shooterRPM = Math.abs(currentVelocity) * 60 / 28;

        telemetry.addData("Shooter RPM", shooterRPM);
        panelsTelemetry.addData("Shooter RPM", shooterRPM);
        panelsTelemetry.addData("Battery Voltage", batteryVoltage.getVoltage());

        // Graph
        panelsTelemetry.addData("Target Velocity", targetVelocity);
        panelsTelemetry.addData("Actual Velocity", currentVelocity);
        panelsTelemetry.addData("Velocity Error", error);

        panelsTelemetry.debug("Target Velocity: " + targetVelocity);
        panelsTelemetry.debug("Actual Velocity: " + currentVelocity);
        panelsTelemetry.debug("Velocity Error: " + error);
        panelsTelemetry.debug("kP: " + kP);
        panelsTelemetry.debug("kI: " + kI);
        panelsTelemetry.debug("kD: " + kD);
        panelsTelemetry.debug("kF: " + kF);

        panelsTelemetry.update(telemetry);
        telemetry.update();
    }

    @Override
    public void stop() {
        if (pollenShooter != null) {
            pollenShooter.setVelocity(0);
            pollenShooter.setPower(0);
        }
    }
}