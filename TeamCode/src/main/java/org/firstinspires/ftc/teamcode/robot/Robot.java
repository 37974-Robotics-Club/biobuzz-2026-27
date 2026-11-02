package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.mechanisms.Drivetrain;
import org.firstinspires.ftc.teamcode.mechanisms.Shooter;

public class Robot {

    /**
     * represents the whole robot by initializing hardware and "compiling" all the mechanisms together
     */
    public final Hardware hardware;
    public final Drivetrain drivetrain;
    public final Shooter shooter;

    public Robot(HardwareMap hardwareMap) {

        hardware = new Hardware();
        hardware.init(hardwareMap);

        drivetrain = new Drivetrain(
                hardware.frontLeft,
                hardware.frontRight,
                hardware.backLeft,
                hardware.backRight,
                hardware.imu
        );

        shooter = new Shooter(
                hardware.flywheel,
                hardware.hood,
                hardware.turret
        );

    }
}