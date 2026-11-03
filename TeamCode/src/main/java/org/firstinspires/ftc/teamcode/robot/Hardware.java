package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;

public class Hardware {

    /**
     * handles all hardware used by robot
     */

    public DcMotorEx frontRight;
    public DcMotorEx frontLeft;
    public DcMotorEx backRight;
    public DcMotorEx backLeft;
    public DcMotorEx intake;
    public DcMotorEx pollenFlywheel;
    public DcMotorEx nectarFlywheel;
    public Servo pollenHood;
    public Servo nectarHood;
    public CRServo pollenTurret;
    public CRServo nectarTurret;
    public IMU imu;

    public void init(HardwareMap hardwareMap) {

        frontRight = hardwareMap.get(DcMotorEx.class, "frontRight");
        frontLeft = hardwareMap.get(DcMotorEx.class, "frontLeft");
        backRight = hardwareMap.get(DcMotorEx.class, "backRight");
        backLeft = hardwareMap.get(DcMotorEx.class, "backLeft");
        intake = hardwareMap.get(DcMotorEx.class, "intake");
        pollenFlywheel = hardwareMap.get(DcMotorEx.class, "pollenFlywheel");
        nectarFlywheel = hardwareMap.get(DcMotorEx.class, "nectarFlywheel");
        pollenHood = hardwareMap.get(Servo.class, "pollenHood");
        nectarHood = hardwareMap.get(Servo.class, "nectarHood");
        pollenTurret = hardwareMap.get(CRServo.class, "pollenTurret");
        nectarTurret = hardwareMap.get(CRServo.class, "nectarTurret");


        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRight.setDirection(DcMotorSimple.Direction.FORWARD);
        backRight.setDirection(DcMotorSimple.Direction.FORWARD);
        intake.setDirection(DcMotorSimple.Direction.FORWARD);
        pollenFlywheel.setDirection(DcMotorSimple.Direction.FORWARD);
        nectarFlywheel.setDirection(DcMotorSimple.Direction.FORWARD);
        pollenTurret.setDirection(CRServo.Direction.FORWARD);
        nectarTurret.setDirection(CRServo.Direction.FORWARD);


        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        pollenFlywheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        nectarFlywheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        intake.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        pollenFlywheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        nectarFlywheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        imu = hardwareMap.get(IMU.class, "imu");
        IMU.Parameters parameters = new IMU.Parameters(
                new RevHubOrientationOnRobot(
                        RevHubOrientationOnRobot.LogoFacingDirection.LEFT,
                        RevHubOrientationOnRobot.UsbFacingDirection.UP
                )
        );
        imu.initialize(parameters);

    }
}
