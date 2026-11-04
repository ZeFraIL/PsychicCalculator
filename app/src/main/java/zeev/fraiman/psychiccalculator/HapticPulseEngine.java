package zeev.fraiman.psychiccalculator;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.os.VibratorManager;

public class HapticPulseEngine {

    private Vibrator vibrator;

    public HapticPulseEngine(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            VibratorManager vm = (VibratorManager) context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE);
            if (vm != null) {
                vibrator = vm.getDefaultVibrator();
            }
        } else {
            vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        }
    }

    public void heartbeat() {
        if (vibrator != null && vibrator.hasVibrator()) {
            long[] pattern = {0, 40, 100, 50}; // Двойной импульс «сердцебиения»
            vibrator.vibrate(VibrationEffect.createWaveform(pattern, -1));
        }
    }

    public void trancePulse() {
        if (vibrator != null && vibrator.hasVibrator()) {
            long[] pattern = {0, 80, 50, 80, 50, 150};
            vibrator.vibrate(VibrationEffect.createWaveform(pattern, -1));
        }
    }

    public void clickShort() {
        if (vibrator != null && vibrator.hasVibrator()) {
            vibrator.vibrate(VibrationEffect.createOneShot(25, VibrationEffect.DEFAULT_AMPLITUDE));
        }
    }
}