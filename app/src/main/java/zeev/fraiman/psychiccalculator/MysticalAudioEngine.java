package zeev.fraiman.psychiccalculator;

import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioTrack;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MysticalAudioEngine {

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public void playGhostWail() {
        executor.execute(() -> {
            int sampleRate = 22050;
            int numSamples = sampleRate * 1; // 1 секунда
            short[] sample = new short[numSamples];

            for (int i = 0; i < numSamples; i++) {
                double t = (double) i / sampleRate;
                // Частотная модуляция от 300Гц к 550Гц и назад (призрачный вой)
                double freq = 350.0 + 180.0 * Math.sin(2.0 * Math.PI * 1.5 * t);
                double angle = 2.0 * Math.PI * freq * t;
                // Плавное нарастание и затухание громкости
                double envelope = Math.sin(Math.PI * t);
                sample[i] = (short) (Math.sin(angle) * 12000 * envelope);
            }

            playBuffer(sample, sampleRate);
        });
    }

    public void playMagicChime() {
        executor.execute(() -> {
            int sampleRate = 22050;
            int numSamples = (int) (sampleRate * 0.6);
            short[] sample = new short[numSamples];

            double[] freqs = {880.0, 1100.0, 1320.0, 1760.0};
            for (int i = 0; i < numSamples; i++) {
                double t = (double) i / sampleRate;
                double val = 0;
                for (double f : freqs) {
                    val += Math.sin(2.0 * Math.PI * f * t) * Math.exp(-6.0 * t);
                }
                sample[i] = (short) (val * 4000);
            }

            playBuffer(sample, sampleRate);
        });
    }

    public void playGlitchNoise() {
        executor.execute(() -> {
            int sampleRate = 22050;
            int numSamples = (int) (sampleRate * 0.4);
            short[] sample = new short[numSamples];

            for (int i = 0; i < numSamples; i++) {
                double t = (double) i / sampleRate;
                double freq = 150.0 + (Math.random() * 800.0);
                double val = (Math.sin(2.0 * Math.PI * freq * t) > 0 ? 1 : -1) * Math.exp(-4.0 * t);
                sample[i] = (short) (val * 9000);
            }

            playBuffer(sample, sampleRate);
        });
    }

    private void playBuffer(short[] buffer, int sampleRate) {
        try {
            int bufferSize = AudioTrack.getMinBufferSize(
                    sampleRate,
                    AudioFormat.CHANNEL_OUT_MONO,
                    AudioFormat.ENCODING_PCM_16BIT
            );

            AudioTrack audioTrack = new AudioTrack.Builder()
                    .setAudioAttributes(new AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_GAME)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .build())
                    .setAudioFormat(new AudioFormat.Builder()
                            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                            .setSampleRate(sampleRate)
                            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                            .build())
                    .setBufferSizeInBytes(Math.max(bufferSize, buffer.length * 2))
                    .setTransferMode(AudioTrack.MODE_STATIC)
                    .build();

            audioTrack.write(buffer, 0, buffer.length);
            audioTrack.play();

            Thread.sleep(1200);
            audioTrack.release();
        } catch (Exception ignored) {}
    }

    public void shutdown() {
        executor.shutdown();
    }
}