# 🧩 Диаграмма классов UML и подробное объяснение: «Калькулятор-Экстрасенс»

---

## 1. Диаграмма классов (Mermaid classDiagram)

```mermaid
classDiagram
    class SplashActivity {
        -ImageView mainImage
        #onCreate(Bundle savedInstanceState) void
        -navigateToLanguage() void
    }

    class LanguageActivity {
        -TextView timerText
        -CountDownTimer countDownTimer
        -SharedPreferences prefs
        #onCreate(Bundle savedInstanceState) void
        -startSpookyTimer() void
        -selectLanguage(String langCode) void
    }

    class MainActivity {
        -TextView display
        -TextView predictionText
        -TextView auraText
        -PsychicEngine psychicEngine
        -AstralVoice astralVoice
        -SituationalContextEngine situationalEngine
        -MysticalAiEngine mysticalAi
        -TarotDeck tarotDeck
        -HapticPulseEngine haptics
        -MysticalAudioEngine audioEngine
        #onCreate(Bundle savedInstanceState) void
        -onEqualsClick() void
        -toggleTrance() void
        -showTarotDialog() void
        +onGazeZoneDetected(int zoneIndex, float headYaw, float headPitch) void
    }

    class PsychicEngine {
        -Context context
        -List~String~ actionHistory
        -Map~String, String~ learnedPatterns
        +recordAction(String action) void
        +predictNext(String currentInput) String
        +generateMysticalComment(String result) String
    }

    class MysticalAiEngine {
        -Context context
        +analyzeExpressionAndResult(String expression, String resultStr) String
    }

    class SituationalContextEngine {
        -Context context
        -SensorManager sensorManager
        -float currentLux
        +getSituationalComment() String
        +onSensorChanged(SensorEvent event) void
    }

    class TarotDeck {
        -Context context
        +drawRandomCard() TarotCard
    }

    class AstralVoice {
        -Context context
        -TextToSpeech tts
        +onInit(int status) void
        +speak(String text) void
    }

    class MysticalAudioEngine {
        +playGhostWail() void
        +playMagicChime() void
        +playGlitchNoise() void
    }

    class EyeTrackingAnalyzer {
        -FaceDetector faceDetector
        -OnGazeZoneDetectedListener listener
        +analyze(ImageProxy imageProxy) void
        -processFaces(List~Face~ faces) void
    }

    class ParticleSparkView {
        -List~Particle~ particles
        +burst(float originX, float originY) void
        #onDraw(Canvas canvas) void
    }

    class HapticPulseEngine {
        -Vibrator vibrator
        +heartbeat() void
        +trancePulse() void
        +clickShort() void
    }

    SplashActivity --> LanguageActivity : Переход экрана через 4 секунды
    LanguageActivity --> MainActivity : Переход экрана по нажатию или таймеру
    MainActivity *-- PsychicEngine : Содержит движок паттернов
    MainActivity *-- MysticalAiEngine : Содержит движок ИИ
    MainActivity *-- SituationalContextEngine : Содержит и слушает датчики
    MainActivity *-- TarotDeck : Содержит колоду Таро
    MainActivity *-- AstralVoice : Содержит астральный голос (TTS)
    MainActivity *-- MysticalAudioEngine : Содержит синтезатор аудио
    MainActivity *-- ParticleSparkView : Содержит оверлей искр
    MainActivity *-- HapticPulseEngine : Содержит движок вибрации
    MainActivity ..> EyeTrackingAnalyzer : Использует для анализа взгляда
```

---

## 2. Простая аналогия: «Магический цирк» 🎪
- **`SplashActivity`**: "Занавес" — картинка плавно увеличивается из точки за 4 секунды.
- **`LanguageActivity`**: "Страж ворот" — отсчитывает 5 секунд и спрашивает язык.
- **`MainActivity`**: "Конферансье" — главный артист на сцене.
- **`PsychicEngine`**: "Чтец мыслей" — угадывает следующую кнопку.
- **`MysticalAiEngine`**: "Шутник" — комментирует вычисления.
- **`SituationalContextEngine`**: "Зритель в зале" — видит темноту и заряд батареи.
- **`TarotDeck`**: "Гадалка" — достает карты Таро.
- **`AstralVoice`**: "Призрак" — шепчет предсказания.
- **`MysticalAudioEngine`**: "Звукорежиссер" — издает вопль привидения.
- **`ParticleSparkView`**: "Пиротехник" — пускает искры.
- **`HapticPulseEngine`**: "Сердцебиение" — пульсирует вибрацией.

---

## ❓ Вопросы для самопроверки
1. Почему синтез звуков вынесен в `MysticalAudioEngine`, а не написан внутри `MainActivity`?
2. Что произойдет, если убрать `finish()` в `SplashActivity`?
3. В каком классе нужно изменить код, чтобы добавить новую карту Таро?
