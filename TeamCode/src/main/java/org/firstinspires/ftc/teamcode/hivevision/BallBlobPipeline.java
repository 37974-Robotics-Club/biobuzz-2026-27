package org.firstinspires.ftc.teamcode.hivevision;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.firstinspires.ftc.robotcore.external.function.Consumer;
import org.firstinspires.ftc.robotcore.external.function.Continuation;
import org.firstinspires.ftc.robotcore.external.stream.CameraStreamSource;
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.firstinspires.ftc.vision.VisionProcessor;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public abstract class BallBlobPipeline
        implements VisionProcessor, CameraStreamSource {

    public enum BallColor {
        YELLOW, RED, BLUE
    }

    /** One detected ball candidate for the OpMode. */
    public static class BallBlob {
        public final BallColor color;
        public final Rect rect;
        public final double area;
        public final double cx;
        public final double cy;

        public double cxNorm = 0;
        public double cyNorm = 0;
        public double wNorm = 0;
        public double hNorm = 0;

        BallBlob(BallColor color, Rect rect, double area) {
            this.color = color;
            this.rect = rect;
            this.area = area;
            this.cx = rect.x + rect.width / 2.0;
            this.cy = rect.y + rect.height / 2.0;
        }

        void normalize(int frameW, int frameH) {
            cxNorm = cx / frameW;
            cyNorm = cy / frameH;
            wNorm = rect.width / (double) frameW;
            hNorm = rect.height / (double) frameH;
        }
    }

    protected static final boolean USE_MORPH = true;

    private volatile boolean suspend = false;
    private volatile boolean detect = true;

    private final Object lock = new Object();
    private List<BallBlob> detections = new ArrayList<>();

    private final AtomicReference<Bitmap> lastFrame =
            new AtomicReference<>(
                    Bitmap.createBitmap(
                            1, 1,
                            Bitmap.Config.RGB_565
                    )
            );

    protected abstract double expectArea(BallColor color);

    protected abstract void findBlobs(
            Mat input,
            List<BallBlob> out
    );

    /** Returns the most ball-like candidate of the requested color. */
    public BallBlob bestOf(BallColor color) {
        BallBlob best = null;
        double bestCost = Double.MAX_VALUE;

        synchronized (lock) {
            for (BallBlob b : detections) {
                if (b.color != color) {
                    continue;
                }

                double fill = b.area /
                        (double) (b.rect.width * b.rect.height);

                double cost =
                        Math.abs(b.area / expectArea(color) - 1.0)
                                + 4.0 * (1.0 - fill);

                if (cost < bestCost) {
                    bestCost = cost;
                    best = b;
                }
            }
        }

        return best;
    }

    public List<BallBlob> getDetections() {
        synchronized (lock) {
            return new ArrayList<>(detections);
        }
    }

    public void suspendDetection() {
        detect = false;
    }

    public void resumeDetection() {
        detect = true;
    }

    public void suspend() {
        suspend = true;
    }

    public void resume() {
        suspend = false;
    }

    @Override
    public void init(
            int width,
            int height,
            CameraCalibration calibration
    ) {
        Bitmap initialFrame = Bitmap.createBitmap(
                width,
                height,
                Bitmap.Config.RGB_565
        );

        lastFrame.set(initialFrame);
    }

    @Override
    public Mat processFrame(Mat input, long captureTimeNanos) {
        if (suspend) {
            updateStreamFrame(input);
            return input;
        }

        List<BallBlob> out = new ArrayList<>();

        // Always run detection unless processing is suspended.
        findBlobs(input, out);

        for (BallBlob b : out) {
            b.normalize(input.cols(), input.rows());
        }

        synchronized (lock) {
            detections = out;
        }

        // "detect" controls the overlay, not whether detection runs.
        if (!detect) {
            updateStreamFrame(input);
            return input;
        }

        Mat output = input.clone();
        drawOverlay(output, out);
        updateStreamFrame(output);
        output.release();

        return input;
    }

    private void updateStreamFrame(Mat frame) {
        Bitmap bitmap = Bitmap.createBitmap(
                frame.cols(),
                frame.rows(),
                Bitmap.Config.RGB_565
        );

        Utils.matToBitmap(frame, bitmap);

        // Atomically publish the latest frame to Panels.
        lastFrame.set(bitmap);
    }

    @Override
    public void getFrameBitmap(
            Continuation<? extends Consumer<Bitmap>> continuation
    ) {
        continuation.dispatch(
                consumer -> consumer.accept(lastFrame.get())
        );
    }

    @Override
    public void onDrawFrame(
            android.graphics.Canvas canvas,
            int onscreenWidth,
            int onscreenHeight,
            float scaleBmpPxToCanvasPx,
            float scaleCanvasDensity,
            Object userContext
    ) {
        // No custom Canvas drawing required.
    }

    /**
     * Applies morphology and extracts contours from a color mask.
     * Ownership of mask transfers to this method.
     */
    protected void blobsFromMask(
            Mat mask,
            BallColor color,
            double minAreaPx,
            double maxAreaPx,
            double minAspect,
            double maxAspect,
            double minFill,
            double maxFill,
            List<BallBlob> out
    ) {
        if (USE_MORPH) {
            Mat element = Imgproc.getStructuringElement(
                    Imgproc.MORPH_ELLIPSE,
                    new org.opencv.core.Size(5, 5)
            );

            Imgproc.morphologyEx(
                    mask, mask, Imgproc.MORPH_OPEN, element
            );

            Imgproc.morphologyEx(
                    mask, mask, Imgproc.MORPH_CLOSE, element
            );

            element.release();
        }

        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();

        Imgproc.findContours(
                mask,
                contours,
                hierarchy,
                Imgproc.RETR_EXTERNAL,
                Imgproc.CHAIN_APPROX_SIMPLE
        );

        hierarchy.release();
        mask.release();

        for (MatOfPoint contour : contours) {
            double area = Imgproc.contourArea(contour);
            Rect rect = Imgproc.boundingRect(contour);

            contour.release();

            if (area < minAreaPx || area > maxAreaPx) {
                continue;
            }

            if (rect.width <= 0 || rect.height <= 0) {
                continue;
            }

            double aspect = (double) rect.width / rect.height;

            if (aspect < minAspect || aspect > maxAspect) {
                continue;
            }

            double fill = area /
                    (double) (rect.width * rect.height);

            if (fill < minFill || fill > maxFill) {
                continue;
            }

            out.add(new BallBlob(color, rect, area));
        }
    }

    private void drawOverlay(Mat frame, List<BallBlob> blobs) {
        for (BallBlob b : blobs) {
            Scalar color =
                    b.color == BallColor.YELLOW
                            ? new Scalar(0, 255, 255)
                            : b.color == BallColor.RED
                            ? new Scalar(0, 0, 255)
                            : new Scalar(255, 0, 0);

            Imgproc.rectangle(frame, b.rect, color, 2);

            Imgproc.putText(
                    frame,
                    b.color.name(),
                    new Point(b.rect.x, b.rect.y - 6),
                    Imgproc.FONT_HERSHEY_PLAIN,
                    1.0,
                    color,
                    2
            );
        }
    }
}