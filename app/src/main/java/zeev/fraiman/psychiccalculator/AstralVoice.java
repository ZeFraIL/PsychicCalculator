package zeev.fraiman.psychiccalculator;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import java.util.Locale;

public class AstralVoice implements TextToSpeech.OnInitListener {

    private final Context context;
    private TextToSpeech tts;
    private boolean isInitialized = false;
    private boolean isMuted = false;

    public AstralVoice(Context context) {
        this.context = context.getApplicationContext();
        tts = new TextToSpeech(this.context, this);
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            String lang = context.getSharedPreferences("PsychicPrefs", Context.MODE_PRIVATE)
                    .getString("app_lang", "en");
            Locale locale = "ru".equalsIgnoreCase(lang) ? new Locale("ru") : Locale.ENGLISH;
            int result = tts.setLanguage(locale);
            if (result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED) {
                tts.setPitch(0.42f);      // Глубокий потусторонний тембр (громкий шепот)
                tts.setSpeechRate(0.60f);  // Очень медленный, заговорщический астральный темп
                isInitialized = true;
            }
        }
    }

    public void speak(String text) {
        if (isInitialized && !isMuted && text != null && !text.isEmpty()) {
            // Очищаем от эмодзи для корректной озвучки
            String cleanText = text.replaceAll("[^\\p{L}\\p{N}\\p{P}\\p{Z}]", "").trim();
            if (!cleanText.isEmpty()) {
                tts.speak(cleanText, TextToSpeech.QUEUE_FLUSH, null, "AstralVoiceID");
            }
        }
    }

    public boolean isMuted() {
        return isMuted;
    }

    public void setMuted(boolean muted) {
        isMuted = muted;
        if (isMuted && tts != null) {
            tts.stop();
        }
    }

    public void shutdown() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
    }
}