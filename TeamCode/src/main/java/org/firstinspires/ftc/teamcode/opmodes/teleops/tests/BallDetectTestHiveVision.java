package org.firstinspires.ftc.teamcode.opmodes.teleops.tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.hivevision.BallDetectorPipeline;
import org.firstinspires.ftc.vision.VisionPortal;
import android.util.Size;
@TeleOp(group = "tests")
public class BallDetectTestHiveVision extends LinearOpMode {

    @Override
    public void runOpMode() {

        BallDetectorPipeline detector = new BallDetectorPipeline();

        VisionPortal portal = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class,"Webcam 1"))
                .setCameraResolution(new Size(1920, 1080))
                .addProcessor(detector)
                .enableLiveView(true)
                .build();
        waitForStart();

        while (opModeIsActive()) {

            BallDetectorPipeline.BallBlob red = detector.bestOf(BallDetectorPipeline.BallColor.RED);
            if (red != null) {
                telemetry.addData("Red ball","X: %.2f | Y: %.2f",red.cxNorm,red.cyNorm);
            } else {
                telemetry.addLine("No red ball detected");
            }

            telemetry.update();
        }
        portal.close();
    }
}
