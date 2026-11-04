package zeev.fraiman.psychiccalculator;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.BatteryManager;

import java.util.Calendar;

public class SituationalContextEngine implements SensorEventListener {

    private final Context context;
    private SensorManager sensorManager;
    private float currentLux = 100.0f;

    public SituationalContextEngine(Context context) {
        this.context = context.getApplicationContext();
        sensorManager = (SensorManager) this.context.getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            Sensor lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
            if (lightSensor != null) {
                sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
            }
        }
    }

    private boolean isRussian() {
        String lang = context.getSharedPreferences("PsychicPrefs", Context.MODE_PRIVATE)
                .getString("app_lang", "en");
        return "ru".equalsIgnoreCase(lang);
    }

    public String getSituationalComment() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        boolean ru = isRussian();

        // 1. Проверка на 3:00 ночи (Час дьявола)
        if (hour == 3) {
            return ru ? "🕯️ 3 часа ночи... Час духов! Вы уверены, что хотите считать сейчас?"
                      : "🕯️ 3 AM... Witching hour! Are you sure you want to calculate now?";
        }

        // 2. Проверка на Пятницу 13-е
        if (dayOfWeek == Calendar.FRIDAY && dayOfMonth == 13) {
            return ru ? "👁️ Сегодня Пятница 13-е! Мистическая погрешность вычислений возрастает!"
                      : "👁️ Friday the 13th! Mystical calculation error rate increases!";
        }

        // 3. Проверка на вечер пятницы (время считать бюджет)
        if (dayOfWeek == Calendar.FRIDAY && hour >= 18) {
            return ru ? "🍕 Пятничный вечер! Считаем расходы на кутёж и радости жизни?"
                      : "🍕 Friday evening! Calculating weekend party budget?";
        }

        // 4. Оценка темноты помещения
        if (currentLux < 10.0f) {
            return ru ? "🌑 В комнате тьма... Идеальные условия для спиритического сеанса вычислений!"
                      : "🌑 Pitch darkness... Perfect conditions for a seance of calculation!";
        } else if (currentLux > 8000.0f) {
            return ru ? "☀️ Солнечный свет ослепляет астральный око... Но мы попробуем!"
                      : "☀️ Sunlight blinds the astral eye... But we shall try!";
        }

        // 5. Оценка батареи
        int batteryLevel = getBatteryLevel();
        boolean isCharging = isBatteryCharging();

        if (batteryLevel > 0 && batteryLevel <= 15 && !isCharging) {
            return ru ? "⚡ Моя астральная энергия (батарея) тает! Срочно подпитайте кристаллы в розетку!"
                      : "⚡ Astral energy (battery) is fading! Quickly plug in the crystals!";
        } else if (isCharging && batteryLevel > 95) {
            return ru ? "🔋 Космический аккумулятор переполнен жизненной силой!"
                      : "🔋 Cosmic battery is overflowing with vital force!";
        }

        // Базовые реплики времени суток
        if (hour < 6) {
            return ru ? "🌌 Ночные вычисления... Духи математики бдят вместе с вами."
                      : "🌌 Nighttime calculations... Math spirits keep watch with you.";
        } else if (hour < 12) {
            return ru ? "☕ Утренний астральный рассвет. Разум свеж для великих чисел!"
                      : "☕ Morning astral dawn. Mind is fresh for great numbers!";
        }

        return null;
    }

    private int getBatteryLevel() {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, ifilter);
        if (batteryStatus != null) {
            int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            if (level >= 0 && scale > 0) {
                return (int) ((level / (float) scale) * 100);
            }
        }
        return -1;
    }

    private boolean isBatteryCharging() {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, ifilter);
        if (batteryStatus != null) {
            int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            return status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL;
        }
        return false;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            currentLux = event.values[0];
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    public void unregister() {
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
    }
}