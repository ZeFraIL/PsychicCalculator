# 🧩 UML Class Diagram & Detailed Explanation: "Psychic Calculator" App

---

## 1. Class Diagram (Mermaid classDiagram)

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

    SplashActivity --> LanguageActivity : Screen transition after 4 seconds
    LanguageActivity --> MainActivity : Screen transition on tap or timer
    MainActivity *-- PsychicEngine : Holds and manages pattern engine
    MainActivity *-- MysticalAiEngine : Holds and manages AI engine
    MainActivity *-- SituationalContextEngine : Holds and listens to sensors
    MainActivity *-- TarotDeck : Holds Tarot card deck
    MainActivity *-- AstralVoice : Holds TTS speech engine
    MainActivity *-- MysticalAudioEngine : Holds ghost audio engine
    MainActivity *-- ParticleSparkView : Holds particle spark View overlay
    MainActivity *-- HapticPulseEngine : Holds vibrator engine
    MainActivity ..> EyeTrackingAnalyzer : Uses for camera gaze analysis
```

---

## 2. Diagram Legend

| Symbol / Element | Meaning |
|---|---|
| **Class Box** | Represents one class in Java (`MainActivity`, `PsychicEngine`, etc.). |
| **Top Section** | Class name. |
| **Middle Section** | Fields / Variables stored in the class. |
| **Bottom Section** | Methods / Actions the class performs. |
| **`-` (Minus)** | `private` visibility — accessible only within this class. |
| **`+` (Plus)** | `public` visibility — accessible from other classes. |
| **`#` (Hash)** | `protected` visibility — used in Android lifecycle methods. |
| **`-->` Arrow** | Screen navigation via `Intent`. |
| **`*--` Composition** | Holds and owns the inner engine object. |
| **`..>` Dependency** | "Uses" — calls services of another class. |

---

## 3. 📖 Explanation in Simple Words

### Everyday Analogy: "Magic Circus Show" 🎪
- **`SplashActivity`**: "The Opening Gong" — lights swell up over 4 seconds declaring the show start.
- **`LanguageActivity`**: "The Gatekeeper" — counts down 5 seconds asking for your language.
- **`MainActivity`**: "The Ringmaster" — stands on stage directing all performers.
- **`PsychicEngine`**: "The Mind Reader" — predicts your next move.
- **`MysticalAiEngine`**: "The Stand-up Comedian" — makes hilarious remarks about your math.
- **`SituationalContextEngine`**: "The Crowd Observer" — watches light, battery, and 3 AM hours.
- **`TarotDeck`**: "The Fortune Teller" — draws astral Tarot cards.
- **`AstralVoice`**: "The Whispering Phantom" — speaks predictions in deep whispers.
- **`MysticalAudioEngine`**: "The Sound Engineer" — synthesizes ghost wails and magic chimes.
- **`ParticleSparkView`**: "The Pyrotechnician" — shoots magic sparks across the screen.
- **`HapticPulseEngine`**: "The Heartbeat" — pulses vibration in your hand.

---

## ❓ Self-Check Questions
1. Why is ghost sound generation placed in `MysticalAudioEngine` instead of `MainActivity`?
2. What happens if we omit `finish()` in `SplashActivity`?
3. Which class in the diagram needs modification if we want to add a new Tarot card?
4. Which class processes front camera frames for gaze detection?
