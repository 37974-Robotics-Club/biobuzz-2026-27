package org.firstinspires.ftc.teamcode.hivevision;

import android.util.Size;

import com.bylazar.camerastream.PanelsCameraStream;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;

@TeleOp(group = "tests")
public class HiveVisionOpmode extends OpMode {

    private BallDetectorPipeline baller;
    private VisionPortal portal;

    @Override
    public void init() {
        telemetry.addLine("Initializing Hive Vision...");
        telemetry.update();

        baller = new BallDetectorPipeline();

        portal = new VisionPortal.Builder()
                .setCamera(
                        hardwareMap.get(
                                WebcamName.class,
                                "Webcam 1"
                        )
                )
                .setCameraResolution(new Size(1920, 1080))
                .addProcessor(baller)
                .enableLiveView(true)
                .setStreamFormat(VisionPortal.StreamFormat.MJPEG)
                .build();

        PanelsCameraStream.INSTANCE.startStream(baller, null);

        telemetry.addLine("Vision initialized");
        telemetry.update();
    }

    @Override
    public void loop() {

        BallBlobPipeline.BallBlob red =
                baller.bestOf(
                        BallBlobPipeline.BallColor.RED
                );

        if (red != null) {
            telemetry.addData(
                    "Red ball",
                    "X: %.2f | Y: %.2f",
                    red.cxNorm,
                    red.cyNorm
            );
        } else {
            telemetry.addLine("No red ball detected");
        }

        telemetry.addData(
                "Camera state",
                portal.getCameraState()
        );

        telemetry.update();
    }

    @Override
    public void stop() {
        PanelsCameraStream.INSTANCE.stopStream();

        if (portal != null) {
            portal.close();
        }
    }
}