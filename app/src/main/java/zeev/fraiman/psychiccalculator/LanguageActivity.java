package zeev.fraiman.psychiccalculator;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

import java.util.Locale;

public class LanguageActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "PsychicPrefs";
    private static final String KEY_LANG = "app_lang";

    private TextView timerText;
    private MaterialButton btnEnglish;
    private MaterialButton btnRussian;

    private CountDownTimer countDownTimer;
    private HapticPulseEngine haptics;
    private SharedPreferences prefs;

    private final String[] spookyIcons = {"🔮 05", "⚡ 04", "👁️ 03", "👻 02", "🔥 01"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.enableEdgeToEdge(getWindow());
        setContentView(R.layout.activity_language);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.lang_main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        if (!prefs.contains(KEY_LANG)) {
            prefs.edit().putString(KEY_LANG, "en").apply();
            setAppLocale("en");
        }

        haptics = new HapticPulseEngine(this);

        timerText = findViewById(R.id.timer_text);
        btnEnglish = findViewById(R.id.btn_lang_en);
        btnRussian = findViewById(R.id.btn_lang_ru);

        btnEnglish.setOnClickListener(v -> selectLanguage("en"));
        btnRussian.setOnClickListener(v -> selectLanguage("ru"));

        startSpookyTimer();
    }

    private void startSpookyTimer() {
        countDownTimer = new CountDownTimer(5000, 1000) {
            int index = 0;

            @Override
            public void onTick(long millisUntilFinished) {
                if (index < spookyIcons.length) {
                    timerText.setText(spookyIcons[index]);
                    index++;
                }

                // Вибрация и пульсация шрифта на каждом тике
                if (haptics != null) haptics.clickShort();
                timerText.setScaleX(1.25f);
                timerText.setScaleY(1.25f);
                timerText.animate().scaleX(1.0f).scaleY(1.0f).setDuration(300).start();
            }

            @Override
            public void onFinish() {
                // Если не выбрали за 5 секунд, берём сохраненный или English по умолчанию
                String savedLang = prefs.getString(KEY_LANG, "en");
                selectLanguage(savedLang);
            }
        }.start();
    }

    private void selectLanguage(String langCode) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        // Сохраняем выбор
        prefs.edit().putString(KEY_LANG, langCode).apply();

        // Применяем локаль
        setAppLocale(langCode);

        // Переходим к главной активности
        Intent intent = new Intent(LanguageActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void setAppLocale(String langCode) {
        Locale locale = new Locale(langCode);
        Locale.setDefault(locale);
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}