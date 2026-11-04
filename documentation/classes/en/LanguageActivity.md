# 📌 Class Documentation: LanguageActivity

### 1. General Information
- **Class Name:** `LanguageActivity`
- **Type:** Activity (Android Screen)
- **Role in Application:** Allows the user to select the app language (English / Русский). Displays a spooky 5-second countdown timer with scale pulsation and haptic vibration, persisting the choice in `SharedPreferences`.
- **Component Interaction:** Launched from `SplashActivity`, navigates to `MainActivity`.

---

### 2. Fields / Variables

| Name | Type | Purpose | Used In |
|------|------|---------|---------|
| `PREFS_NAME` | `String` | SharedPreferences file name (`"PsychicPrefs"`) | Language persistence |
| `KEY_LANG` | `String` | Preference key for language code (`"app_lang"`) | Preference lookup/save |
| `timerText` | `TextView` | Spooky countdown timer display | `startSpookyTimer()` |
| `countDownTimer` | `CountDownTimer` | 5-second countdown timer instance | `startSpookyTimer()` |
| `haptics` | `HapticPulseEngine` | Triggers short vibration ticks | `startSpookyTimer()` |

---

### 3. Methods

**Method Name:** `onCreate`
- Sets layout, initializes `SharedPreferences` (defaulting to English on first launch), starts countdown.

**Method Name:** `startSpookyTimer`
- Runs 5-second timer updating text (`🔮 05`, `⚡ 04`...), pulsing text scale, vibrating, and defaulting to saved/English language on finish.

**Method Name:** `selectLanguage`
- Cancels timer, saves language preference, updates app locale, navigates to `MainActivity`.

---

### 4. Simple Explanation
`LanguageActivity` is like the **gatekeeper of the mystical tower**. It gives you 5 seconds to choose your language; if you don't choose, it picks English for you and opens the door to the calculator!
