# 📱 "Psychic Calculator" Application Documentation — Level 10/10

---

### 🧾 General Information
- **Project Name:** Psychic Calculator (Калькулятор-Экстрасенс / מחשבון פסיכי)
- **Author(s):** Zeev Fraiman
- **Date:** February 2025
- **Language:** Java (Android SDK 37)
- **IDE:** Android Studio
- **Android Target Version:** `minSdk = 28` | `targetSdk = 37` | `compileSdk = 37`

---

### 🎯 Purpose of the Project
The **"Psychic Calculator"** is an interactive entertainment application combining a traditional mathematical calculator with Artificial Intelligence predictions, Eye Tracking via front camera, Tarot card readings, environmental awareness (light, battery, time of day), and eerie mystical audio-visual effects.

- **Problem Solved:** Transforms a plain, routine calculator into a hilarious, engaging, game-like experience suited for entertainment, illusions, and social media viral content.
- **Importance:** Delivers a unique, innovative user experience (UX) merging advanced mobile technologies (ML Kit, CameraX, Hardware Sensors) with a everyday utility tool.
- **Target Audience:** Tech enthusiasts, content creators, illusionists, students, and anyone looking for humor and a mystical experience on their smartphone.

---

### 📌 Application Requirements

#### **Functional Requirements**
1. **Splash Screen (SplashActivity):** Displays app title in exotic typography, scaling the image `main_image` from a single point to full screen width over exactly 4 seconds, followed by automatic transition.
2. **Language Selection (LanguageActivity):** Choice between English and Russian, storing user preference in `SharedPreferences` (English default on first launch), with a spooky 5-second countdown timer accompanied by vibration and scale animations.
3. **Trance Mode & Eye Tracking:** Real-time front camera frame analysis using CameraX and ML Kit Face Detection. Screen divided into 4 quadrants (Top-Left, Top-Right, Bottom-Left, Bottom-Right) highlighting buttons the user gazes at.
4. **Psychic Prediction Engine (PsychicEngine):** Logs input history, detects input patterns (e.g. `2+`, `5*`, `42`, `13`), and predicts the user's next keypress.
5. **Mystical AI Engine (MysticalAiEngine):** Generates witty, humorous reactions to mathematical expressions (blocking division by zero, analyzing financial expenses, recognizing round numbers).
6. **Situational Context Engine (SituationalContextEngine):** Monitors ambient light levels (darkness/bright sun), battery level & charging state, and special times of day (3:00 AM witching hour, Friday the 13th).
7. **Tarot Cards (TarotDeck & Dialog):** Draws a random astral Tarot card on demand, displaying emoji, card title, arcana meaning, and destiny advice.
8. **Astral Voice (AstralVoice):** TextToSpeech engine configured with deep pitch (0.42) and slow speed (0.60) to deliver dramatic, eerie whispers.
9. **Visual FX (ParticleSparkView & Screen Shake):** Radiates golden and purple particle sparks upon clicking `=`, with screen shake animations on errors or division by zero.
10. **Audio FX (MysticalAudioEngine):** Real-time sound synthesis (AudioTrack PCM) producing ghost wails, magic chimes, and error glitch noises.

#### **Non-Functional Requirements**
- **Performance:** 60 FPS camera frame analysis and particle rendering without lagging or blocking the UI thread.
- **Usability:** Extra-large touch targets (72dp button height, 26sp text size), dark mystical theme.
- **Reliability:** Graceful fallbacks when camera, light sensor, or TTS engines are unavailable.

---

### 🧠 General Architecture
The project follows a modular **MVC / Component-Based Architecture**:
- **View Layer:** `SplashActivity`, `LanguageActivity`, `MainActivity`, `ParticleSparkView`, `dialog_tarot_card.xml`.
- **Engine / Controller Layer:** `PsychicEngine`, `MysticalAiEngine`, `SituationalContextEngine`, `TarotDeck`.
- **Hardware & Media Layer:** `EyeTrackingAnalyzer` (CameraX + ML Kit), `AstralVoice` (TTS), `MysticalAudioEngine` (PCM AudioTrack), `HapticPulseEngine` (Vibrator).

---

### 🧩 System Component Diagram
```
[SplashActivity (4 sec Scale)]
       │
       ▼
[LanguageActivity (5 sec Timer & Prefs)]
       │
       ▼
[MainActivity] ───► [EyeTrackingAnalyzer] (CameraX + ML Kit Face Detection)
       │         ───► [PsychicEngine] (Pattern Analysis)
       │         ───► [MysticalAiEngine] (Result Smart Analysis)
       │         ───► [SituationalContextEngine] (Light Sensor + Battery + Clock)
       │         ───► [TarotDeck] (Tarot Cards)
       │         ───► [ParticleSparkView] (Canvas FX)
       │         ───► [MysticalAudioEngine] (AudioTrack PCM Synthesis)
       └─────────► [AstralVoice] (TextToSpeech)
```

---

### 🧩 Detailed Class Description

**📌 Class: SplashActivity**
- **Role:** Startup branding screen.
- **Responsibility:** Animates `main_image` scale from 0 to 1.0 over 4 seconds, transitioning to `LanguageActivity`.

**📌 Class: LanguageActivity**
- **Role:** Interface language selection (English / Русский).
- **Responsibility:** Persists language choice in `SharedPreferences`, runs a 5-second countdown timer with haptics and text pulse, applies locale.

**📌 Class: MainActivity**
- **Role:** Main application controller.
- **Responsibility:** Connects calculator buttons, manages Trance Mode, initializes camera & sensors, displays calculations, and triggers audio/visual feedback.

**📌 Class: EyeTrackingAnalyzer**
- **Role:** Gaze direction and face analysis.
- **Responsibility:** Implements `ImageAnalysis.Analyzer`, processes frames using ML Kit Face Detection, maps Euler angles (Yaw/Pitch) to 4 screen quadrants.

**📌 Class: PsychicEngine**
- **Role:** Predictive pattern engine.
- **Responsibility:** Logs action history, tracks frequency of keypresses, and predicts candidate buttons.

**📌 Class: MysticalAiEngine**
- **Role:** AI commentary engine.
- **Responsibility:** Analyzes expressions/results (division by zero, expenses, round numbers) generating bilingual humorous commentary.

**📌 Class: SituationalContextEngine**
- **Role:** Environmental awareness engine.
- **Responsibility:** Listens to `Sensor.TYPE_LIGHT`, checks battery status/charging, and detects special hours of the day.

**📌 Class: TarotDeck**
- **Role:** Tarot card deck model.
- **Responsibility:** Manages 8 astral Tarot cards with bilingual names, emojis, meanings, and advice.

**📌 Class: AstralVoice**
- **Role:** Mystical speech synthesis.
- **Responsibility:** Wraps `TextToSpeech` with pitch 0.42 and rate 0.60 for spooky whispering.

**📌 Class: MysticalAudioEngine**
- **Role:** Sound effect synthesizer.
- **Responsibility:** Synthesizes PCM audio tracks in real-time (`AudioTrack`) for ghost wails, magic chimes, and glitch noises.

**📌 Class: ParticleSparkView**
- **Role:** Custom visual FX View.
- **Responsibility:** Renders floating particle sparks on Canvas upon equal sign or tarot button taps.

---

### ⚙️ Threading & Concurrency
- **CameraX Analysis Executor:** Processes camera frames on a dedicated background thread (`cameraExecutor = Executors.newSingleThreadExecutor()`) preventing UI freezing.
- **AudioTrack Synthesis Executor:** `MysticalAudioEngine` generates PCM buffers on a separate background thread.
- **Handler & Looper.getMainLooper():** Used for main thread UI updates and delayed prediction highlights.

---

### 💾 Data Storage
- **SharedPreferences (`PsychicPrefs`):** Stores selected language code (`"app_lang"`, e.g., `"en"` or `"ru"`).
- **In-Memory Storage:** Logs input history and pattern frequencies in `PsychicEngine`.

---

### 🔐 Security & Privacy
- **100% Offline Execution:** All processing (face analysis, speech synthesis, AI logic) runs locally on-device without any cloud data transmission.
- **Runtime Permissions:** Camera permission (`CAMERA`) requested dynamically at runtime.

---

### 🧪 Testing
- **Build Verification:** Verified via `:app:assembleDebug` task.
- **Error Handling:** Division by zero caught safely and converted to humorous AI commentary + screen shake.

---

### 🚀 Future Enhancements
1. **Local Gemini Nano Integration (ML Kit Prompt API):** For truly unique LLM-generated responses.
2. **Visual Theme Packs:** Ancient Egyptian theme, Medieval Witchcraft theme.
3. **User Reaction Video Recording:** Ability to record and share the moment the app "guesses" thoughts on social media.
