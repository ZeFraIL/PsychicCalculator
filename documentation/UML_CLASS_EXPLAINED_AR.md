# 🧩 مخطط فئات UML وشرح تفصيلي: تطبيق "الحاسبة الروحانية"

---

## 1. مخطط الفئات (Mermaid classDiagram)

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

    SplashActivity --> LanguageActivity : الانتقال بعد 4 ثوان
    LanguageActivity --> MainActivity : الانتقال عند النقر أو المؤقت
    MainActivity *-- PsychicEngine : يحتوي محرك التنبؤ
    MainActivity *-- MysticalAiEngine : يحتوي محرك الذكاء الاصطناعي
    MainActivity *-- SituationalContextEngine : يحتوي ويستمع للحساسات
    MainActivity *-- TarotDeck : يحتوي بطاقات التاروت
    MainActivity *-- AstralVoice : يحتوي المحرك الصوتي (TTS)
    MainActivity *-- MysticalAudioEngine : يحتوي محرك التأثيرات الصوتية
    MainActivity *-- ParticleSparkView : يحتوي طبقة الشرارات
    MainActivity *-- HapticPulseEngine : يحتوي محرك الاهتزاز
    MainActivity ..> EyeTrackingAnalyzer : يستخدم لتحليل حركة العين
```

---

## 2. تشبيه بسيط: "العرض السحري" 🎪
- **`SplashActivity`**: "الستارة" — تكبر الصورة خلال 4 ثوانٍ.
- **`LanguageActivity`**: "حارس البوابة" — يعد 5 ثوانٍ ويسأل عن اللغة.
- **`MainActivity`**: "المخرج الرئيسي" — يقود العرض على المسرح.
- **`PsychicEngine`**: "قارئ الأفكار" — يتنبأ بالزر التالي.
- **`MysticalAiEngine`**: "الكوميديان" — يعلق على الحسابات.
- **`SituationalContextEngine`**: "المراقب" — يشعر بالإضاءة والبطارية.
- **`TarotDeck`**: "العرافة" — تسحب بطاقات التاروت.
- **`AstralVoice`**: "الشبح" — يهمس بالتوقعات.
- **`MysticalAudioEngine`**: "مهندس الصوت" — يصدر عويل الأرواح.
- **`ParticleSparkView`**: "خبير الألعاب النارية" — يطلق الشرارات السحرية.
- **`HapticPulseEngine`**: "نبض القلب" — ينبض بالاهتزاز.

---

## ❓ أسئلة للمراجعة الذاتية
1. لماذا تم وضع توليد صوت الأرواح في `MysticalAudioEngine` بدلاً من `MainActivity`؟
2. ماذا يحدث إذا حذفנו `finish()` في `SplashActivity`؟
3. ما هي الفئة التي يجب تعديلها لإضافة بطاقة تاروت جديدة؟
