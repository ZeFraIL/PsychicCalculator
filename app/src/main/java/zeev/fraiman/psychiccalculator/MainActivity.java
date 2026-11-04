package zeev.fraiman.psychiccalculator;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements SensorEventListener, EyeTrackingAnalyzer.OnGazeZoneDetectedListener {

    private static final int CAMERA_PERMISSION_REQUEST = 1001;

    private TextView display;
    private TextView predictionText;
    private TextView auraText;
    private MaterialButton buttonTrance;
    private MaterialButton buttonTarot;
    private MaterialButton buttonVoiceToggle;
    private PreviewView previewView;
    private ParticleSparkView particleView;

    private final Map<String, MaterialButton> buttons = new HashMap<>();

    private PsychicEngine psychicEngine;
    private AstralVoice astralVoice;
    private SituationalContextEngine situationalEngine;
    private MysticalAiEngine mysticalAi;
    private TarotDeck tarotDeck;
    private HapticPulseEngine haptics;
    private MysticalAudioEngine audioEngine;

    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Random random = new Random();

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor magnetometer;

    private ProcessCameraProvider cameraProvider;
    private ExecutorService cameraExecutor;

    private String currentInput = "";
    private boolean isNewNumber = true;
    private boolean isInTrance = false;

    private float[] accelValues = new float[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        android.content.SharedPreferences prefs = getSharedPreferences("PsychicPrefs", Context.MODE_PRIVATE);
        String langCode = prefs.getString("app_lang", "en");
        setAppLocale(langCode);

        WindowCompat.enableEdgeToEdge(getWindow());
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeViews();
        initializeSensors();
        initializeEngines();

        checkCameraPermissionAndStart();
        showToast(getString(R.string.welcome_msg));
        speakIfEnabled(getString(R.string.welcome_msg));

        handler.postDelayed(this::checkSituationalContext, 2000);
    }

    private void initializeViews() {
        display = findViewById(R.id.display);
        predictionText = findViewById(R.id.prediction_text);
        auraText = findViewById(R.id.aura_text);
        buttonTrance = findViewById(R.id.button_trance);
        buttonTarot = findViewById(R.id.button_tarot);
        buttonVoiceToggle = findViewById(R.id.button_voice_toggle);
        previewView = findViewById(R.id.preview_view);
        particleView = (ParticleSparkView) findViewById(R.id.particle_view);

        // Цифры
        buttons.put("0", findViewById(R.id.button_0));
        buttons.put("1", findViewById(R.id.button_1));
        buttons.put("2", findViewById(R.id.button_2));
        buttons.put("3", findViewById(R.id.button_3));
        buttons.put("4", findViewById(R.id.button_4));
        buttons.put("5", findViewById(R.id.button_5));
        buttons.put("6", findViewById(R.id.button_6));
        buttons.put("7", findViewById(R.id.button_7));
        buttons.put("8", findViewById(R.id.button_8));
        buttons.put("9", findViewById(R.id.button_9));

        // Операции
        buttons.put("+", findViewById(R.id.button_plus));
        buttons.put("-", findViewById(R.id.button_minus));
        buttons.put("*", findViewById(R.id.button_multiply));
        buttons.put("/", findViewById(R.id.button_divide));
        buttons.put("=", findViewById(R.id.button_equals));
        buttons.put("C", findViewById(R.id.button_clear));

        // Клики кнопок с безопасной проверкой на null
        for (int i = 0; i <= 9; i++) {
            final int digit = i;
            MaterialButton btn = buttons.get(String.valueOf(digit));
            if (btn != null) {
                btn.setOnClickListener(v -> onNumberClick(digit));
            }
        }

        setOpClickListener("+", v -> onOperationClick("+"));
        setOpClickListener("-", v -> onOperationClick("-"));
        setOpClickListener("*", v -> onOperationClick("*"));
        setOpClickListener("/", v -> onOperationClick("/"));
        setOpClickListener("=", v -> onEqualsClick());
        setOpClickListener("C", v -> onClearClick());

        buttonTrance.setOnClickListener(v -> toggleTrance());
        buttonTarot.setOnClickListener(v -> showTarotDialog());
        buttonVoiceToggle.setOnClickListener(v -> toggleVoice());

        // Проксимити отклик (вибрация при нажатии)
        for (MaterialButton btn : buttons.values()) {
            btn.setOnTouchListener((view, motionEvent) -> {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    if (haptics != null) haptics.clickShort();
                }
                return false;
            });
        }
    }

    private void initializeSensors() {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

            if (accelerometer != null) {
                sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
            }
            if (magnetometer != null) {
                sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI);
            }
        }
    }

    private void initializeEngines() {
        psychicEngine = new PsychicEngine(this);
        astralVoice = new AstralVoice(this);
        situationalEngine = new SituationalContextEngine(this);
        mysticalAi = new MysticalAiEngine(this);
        tarotDeck = new TarotDeck(this);
        haptics = new HapticPulseEngine(this);
        audioEngine = new MysticalAudioEngine();
    }

    private void checkCameraPermissionAndStart() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST);
        } else {
            startCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera();
            } else {
                showToast("Режим транса ограничен: нет доступа к камере");
            }
        }
    }

    private void startCamera() {
        cameraExecutor = Executors.newSingleThreadExecutor();
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() -> {
            try {
                cameraProvider = cameraProviderFuture.get();
                bindCameraUseCases();
            } catch (ExecutionException | InterruptedException e) {
                updatePredictionText("👁️ Ошибка астрального ока камеры");
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void bindCameraUseCases() {
        if (cameraProvider == null) return;

        Preview preview = new Preview.Builder().build();
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
                .build();

        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                .setTargetResolution(new Size(320, 240))
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build();

        imageAnalysis.setAnalyzer(cameraExecutor, new EyeTrackingAnalyzer(this));
        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        try {
            cameraProvider.unbindAll();
            cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis);
        } catch (Exception ignored) {}
    }

    @Override
    public void onGazeZoneDetected(int zoneIndex, float headYaw, float headPitch) {
        if (!isInTrance) return;

        handler.post(() -> {
            String[] zoneNames = {"Верхний левый", "Верхний правый", "Нижний левый", "Нижний правый", "Центр"};
            updatePredictionText("🔮 Взгляд: " + zoneNames[zoneIndex]);

            switch (zoneIndex) {
                case EyeTrackingAnalyzer.ZONE_TOP_LEFT:
                    highlightButtonGroup("7", "8", "4", "5");
                    break;
                case EyeTrackingAnalyzer.ZONE_TOP_RIGHT:
                    highlightButtonGroup("9", "/", "6", "*");
                    break;
                case EyeTrackingAnalyzer.ZONE_BOTTOM_LEFT:
                    highlightButtonGroup("1", "2", "0", "C");
                    break;
                case EyeTrackingAnalyzer.ZONE_BOTTOM_RIGHT:
                    highlightButtonGroup("3", "+", "=", "-");
                    break;
                case EyeTrackingAnalyzer.ZONE_CENTER:
                    makePrediction();
                    break;
            }
        });
    }

    private void highlightButtonGroup(String... keys) {
        for (String key : keys) {
            MaterialButton btn = buttons.get(key);
            if (btn != null) {
                btn.setStrokeColor(ContextCompat.getColorStateList(this, R.color.mystical_purple_glow));
                btn.setStrokeWidth(4);
            }
        }

        handler.postDelayed(() -> {
            for (String key : keys) {
                MaterialButton btn = buttons.get(key);
                if (btn != null) {
                    btn.setStrokeWidth(0);
                }
            }
        }, 800);
    }

    private void onNumberClick(int digit) {
        psychicEngine.recordAction("number_" + digit);

        if (isNewNumber) {
            currentInput = String.valueOf(digit);
            isNewNumber = false;
        } else {
            currentInput += digit;
        }

        display.setText(currentInput);
        makePrediction();
    }

    private void onOperationClick(String op) {
        psychicEngine.recordAction("op_" + op);
        currentInput += " " + op + " ";
        display.setText(currentInput);
        isNewNumber = false;
        predictNextNumber();
    }

    private void onEqualsClick() {
        if (currentInput.isEmpty()) return;

        try {
            String result = calculateResult(currentInput);
            display.setText(result);
            psychicEngine.recordResult(currentInput, result);

            // Искры магии и аудиоэффект перезвона
            MaterialButton eqBtn = buttons.get("=");
            if (eqBtn != null && particleView != null) {
                int[] location = new int[2];
                eqBtn.getLocationOnScreen(location);
                float rx = location[0] + eqBtn.getWidth() / 2f;
                float ry = location[1] + eqBtn.getHeight() / 2f;
                particleView.burst(rx, ry);
            }
            if (audioEngine != null) audioEngine.playMagicChime();

            // Реакция через MysticalAiEngine
            String aiComment = mysticalAi.analyzeExpressionAndResult(currentInput, result);
            updatePredictionText(aiComment);

            // Тряска экрана если ошибка или особенное вычисление
            if (aiComment.contains("запрещают") || aiComment.contains("схлопнулась")) {
                shakeDisplay();
                if (audioEngine != null) audioEngine.playGlitchNoise();
            }

            currentInput = result;
            isNewNumber = true;
            if (haptics != null) haptics.heartbeat();
        } catch (Exception e) {
            display.setText("Ошибка");
            shakeDisplay();
            if (audioEngine != null) audioEngine.playGlitchNoise();
            updatePredictionText("⚡ Астральные сущности перегрузили вычисления!");
        }
    }

    private void shakeDisplay() {
        display.animate()
                .translationX(25f)
                .setDuration(40)
                .withEndAction(() -> display.animate()
                        .translationX(-25f)
                        .setDuration(40)
                        .withEndAction(() -> display.animate()
                                .translationX(0f)
                                .setDuration(40)
                                .start())
                        .start())
                .start();
    }

    private void onClearClick() {
        currentInput = "";
        display.setText("0");
        updatePredictionText("");
        isNewNumber = true;
        psychicEngine.recordAction("clear");
    }

    private void toggleTrance() {
        isInTrance = !isInTrance;
        if (isInTrance) {
            buttonTrance.setText(R.string.trance_mode_active);
            buttonTrance.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.mystical_purple));
            auraText.setText(R.string.aura_trance);
            auraText.setTextColor(ContextCompat.getColor(this, R.color.aura_purple));
            if (haptics != null) haptics.trancePulse();
            if (audioEngine != null) audioEngine.playGhostWail();
            showToast("🔮 Режим Транса Активирован!");
            speakIfEnabled("Режим транса активирован. Смотрите в астрал.");
        } else {
            buttonTrance.setText(R.string.trance_mode);
            buttonTrance.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.mystical_purple_dark));
            auraText.setText(R.string.aura_default);
            auraText.setTextColor(ContextCompat.getColor(this, R.color.aura_green));
        }
    }

    private void toggleVoice() {
        if (astralVoice != null) {
            boolean nextMutedState = !astralVoice.isMuted();
            astralVoice.setMuted(nextMutedState);
            if (nextMutedState) {
                buttonVoiceToggle.setText(R.string.btn_voice_off);
                buttonVoiceToggle.setTextColor(ContextCompat.getColor(this, R.color.aura_red));
                showToast("🔇 Голос Астрала умолк");
            } else {
                buttonVoiceToggle.setText(R.string.btn_voice_on);
                buttonVoiceToggle.setTextColor(ContextCompat.getColor(this, R.color.aura_green));
                showToast("🔊 Голос Астрала пробудился");
                astralVoice.speak("Голос астрала пробудился");
            }
        }
    }

    private void showTarotDialog() {
        if (tarotDeck == null) return;

        if (particleView != null) {
            particleView.burst(particleView.getWidth() / 2f, particleView.getHeight() / 2f);
        }
        if (audioEngine != null) {
            audioEngine.playMagicChime();
        }

        TarotDeck.TarotCard card = tarotDeck.drawRandomCard();
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_tarot_card, null);

        TextView emojiTv = view.findViewById(R.id.tarot_emoji);
        TextView titleTv = view.findViewById(R.id.tarot_title);
        TextView meaningTv = view.findViewById(R.id.tarot_meaning);
        TextView adviceTv = view.findViewById(R.id.tarot_advice);
        MaterialButton closeBtn = view.findViewById(R.id.tarot_close_button);

        emojiTv.setText(card.getEmoji());
        titleTv.setText(card.getName());
        meaningTv.setText(card.getMeaning());
        adviceTv.setText(card.getAdvice());

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        closeBtn.setOnClickListener(v -> dialog.dismiss());
        dialog.show();

        if (haptics != null) haptics.trancePulse();
        speakIfEnabled(card.getName() + ". " + card.getAdvice());
    }

    private void makePrediction() {
        handler.postDelayed(() -> {
            String prediction = psychicEngine.predictNext(currentInput);
            updatePredictionText("👁️ " + prediction);

            if (prediction.contains("7")) highlightButton("7");
            else if (prediction.contains("3")) highlightButton("3");
            else if (prediction.contains("сложения") || prediction.contains("+")) highlightButton("+");
            else if (prediction.contains("умножить") || prediction.contains("*")) highlightButton("*");
            else if (prediction.contains("=") || prediction.contains("равенства")) highlightButton("=");
        }, 400);
    }

    private void predictNextNumber() {
        String[] mysticalNumbers = {"7", "42", "13", "666", "777", "3", "5"};
        String predicted = mysticalNumbers[random.nextInt(mysticalNumbers.length)];
        updatePredictionText("✨ Вижу в астрале... число " + predicted + "?");
        highlightButton(predicted);
    }

    private void checkSituationalContext() {
        if (situationalEngine != null) {
            String sitComment = situationalEngine.getSituationalComment();
            if (sitComment != null && predictionText.getText().length() == 0) {
                updatePredictionText(sitComment);
            }
        }
    }

    private void updatePredictionText(String text) {
        predictionText.setText(text);
        speakIfEnabled(text);
    }

    private void speakIfEnabled(String text) {
        if (astralVoice != null && text != null && !text.isEmpty()) {
            astralVoice.speak(text);
        }
    }

    private void highlightButton(String key) {
        MaterialButton button = buttons.get(key);
        if (button == null) return;

        button.setStrokeColor(ContextCompat.getColorStateList(this, R.color.mystical_purple_glow));
        button.setStrokeWidth(6);

        handler.postDelayed(() -> button.setStrokeWidth(0), 1200);
    }

    private String calculateResult(String expression) {
        String[] parts = expression.trim().split("\\s+");
        if (parts.length == 3) {
            double n1 = Double.parseDouble(parts[0]);
            String op = parts[1];
            double n2 = Double.parseDouble(parts[2]);
            double res;
            switch (op) {
                case "+": res = n1 + n2; break;
                case "-": res = n1 - n2; break;
                case "*": res = n1 * n2; break;
                case "÷":
                case "/": res = n2 != 0 ? n1 / n2 : 0; break;
                default: return expression;
            }

            if (res == (long) res) {
                return String.valueOf((long) res);
            }
            return String.valueOf(res);
        }
        return expression;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            accelValues = event.values.clone();
        }

        float totalMotion = Math.abs(accelValues[0]) + Math.abs(accelValues[1]) + Math.abs(accelValues[2]);
        if (totalMotion > 16.0f && !isInTrance) {
            auraText.setText(R.string.aura_high);
            auraText.setTextColor(ContextCompat.getColor(this, R.color.aura_purple));
        } else if (!isInTrance) {
            auraText.setText(R.string.aura_default);
            auraText.setTextColor(ContextCompat.getColor(this, R.color.aura_green));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    private void setAppLocale(String langCode) {
        java.util.Locale locale = new java.util.Locale(langCode);
        java.util.Locale.setDefault(locale);
        android.content.res.Resources resources = getResources();
        android.content.res.Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }

    private void setOpClickListener(String key, View.OnClickListener listener) {
        MaterialButton btn = buttons.get(key);
        if (btn != null) {
            btn.setOnClickListener(listener);
        }
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
        if (situationalEngine != null) {
            situationalEngine.unregister();
        }
        if (astralVoice != null) {
            astralVoice.shutdown();
        }
        if (audioEngine != null) {
            audioEngine.shutdown();
        }
        if (cameraExecutor != null) {
            cameraExecutor.shutdown();
        }
    }
}