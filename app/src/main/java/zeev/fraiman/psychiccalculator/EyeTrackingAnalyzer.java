package zeev.fraiman.psychiccalculator;

import android.annotation.SuppressLint;
import android.media.Image;
import androidx.annotation.NonNull;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;

import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;

import java.util.List;

public class EyeTrackingAnalyzer implements ImageAnalysis.Analyzer {

    public interface OnGazeZoneDetectedListener {
        void onGazeZoneDetected(int zoneIndex, float headYaw, float headPitch);
    }

    public static final int ZONE_TOP_LEFT = 0;
    public static final int ZONE_TOP_RIGHT = 1;
    public static final int ZONE_BOTTOM_LEFT = 2;
    public static final int ZONE_BOTTOM_RIGHT = 3;
    public static final int ZONE_CENTER = 4;

    private final FaceDetector faceDetector;
    private final OnGazeZoneDetectedListener listener;

    public EyeTrackingAnalyzer(OnGazeZoneDetectedListener listener) {
        this.listener = listener;

        FaceDetectorOptions options = new FaceDetectorOptions.Builder()
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
                .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
                .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                .build();

        this.faceDetector = FaceDetection.getClient(options);
    }

    @Override
    @SuppressLint("UnsafeOptInUsageError")
    public void analyze(@NonNull ImageProxy imageProxy) {
        Image mediaImage = imageProxy.getImage();
        if (mediaImage == null) {
            imageProxy.close();
            return;
        }

        InputImage image = InputImage.fromMediaImage(
                mediaImage,
                imageProxy.getImageInfo().getRotationDegrees()
        );

        faceDetector.process(image)
                .addOnSuccessListener(this::processFaces)
                .addOnFailureListener(e -> {})
                .addOnCompleteListener(task -> imageProxy.close());
    }

    private void processFaces(List<Face> faces) {
        if (faces == null || faces.isEmpty() || listener == null) {
            return;
        }

        Face face = faces.get(0);
        float headYaw = face.getHeadEulerAngleY();   // Поворот влево (-)/вправо (+)
        float headPitch = face.getHeadEulerAngleX(); // Наклон вниз (-)/вверх (+)

        // Определяем квадрант взгляда/наклона головы (для передней камеры значения инвертированы по X)
        int zoneIndex;

        if (Math.abs(headYaw) < 6.0f && Math.abs(headPitch) < 6.0f) {
            zoneIndex = ZONE_CENTER;
        } else if (headYaw > 4.0f && headPitch > 4.0f) {
            zoneIndex = ZONE_TOP_LEFT;
        } else if (headYaw < -4.0f && headPitch > 4.0f) {
            zoneIndex = ZONE_TOP_RIGHT;
        } else if (headYaw > 4.0f && headPitch < -4.0f) {
            zoneIndex = ZONE_BOTTOM_LEFT;
        } else if (headYaw < -4.0f && headPitch < -4.0f) {
            zoneIndex = ZONE_BOTTOM_RIGHT;
        } else if (headYaw > 4.0f) {
            zoneIndex = ZONE_TOP_LEFT;
        } else if (headYaw < -4.0f) {
            zoneIndex = ZONE_TOP_RIGHT;
        } else if (headPitch < -4.0f) {
            zoneIndex = ZONE_BOTTOM_RIGHT;
        } else {
            zoneIndex = ZONE_TOP_LEFT;
        }

        listener.onGazeZoneDetected(zoneIndex, headYaw, headPitch);
    }
}