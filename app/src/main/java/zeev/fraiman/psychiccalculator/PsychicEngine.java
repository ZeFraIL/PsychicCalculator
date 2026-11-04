package zeev.fraiman.psychiccalculator;

import android.content.Context;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class PsychicEngine {

    private final Context context;
    private final List<String> actionHistory = new ArrayList<>();
    private final Map<String, String> learnedPatterns = new HashMap<>();
    private final Map<String, Integer> frequencyMap = new HashMap<>();
    private final Random random = new Random();

    public PsychicEngine(Context context) {
        this.context = context.getApplicationContext();
    }

    private boolean isRussian() {
        if (context == null) return false;
        String lang = context.getSharedPreferences("PsychicPrefs", Context.MODE_PRIVATE)
                .getString("app_lang", "en");
        return "ru".equalsIgnoreCase(lang);
    }

    public void recordAction(String action) {
        actionHistory.add(action);
        Integer currentCount = frequencyMap.get(action);
        frequencyMap.put(action, (currentCount == null ? 0 : currentCount) + 1);

        if (actionHistory.size() >= 2) {
            String prev = actionHistory.get(actionHistory.size() - 2);
            String patternKey = prev + "->" + action;
            boolean ru = isRussian();
            learnedPatterns.put(patternKey, (ru ? "Чувствую привычный шаг: " : "Sensing habitual step: ") + action);
        }
    }

    public void recordResult(String expression, String result) {
        boolean ru = isRussian();
        learnedPatterns.put(expression, (ru ? "Память предков помнит: " : "Ancestral memory recalls: ") + result);
    }

    public String predictNext(String currentInput) {
        boolean ru = isRussian();

        if (currentInput == null || currentInput.isEmpty()) {
            return ru ? "Космос ждет вашего первого шага..." : "Cosmos awaits your first step...";
        }

        String cleaned = currentInput.trim();

        // Проверяем паттерны
        if (cleaned.endsWith("2+")) {
            return ru ? "Вижу число 3..." : "I sense a 3...";
        } else if (cleaned.endsWith("5*")) {
            return ru ? "Духи говорят: 5 или 10!" : "Spirits say: 5 or 10!";
        } else if (cleaned.endsWith("1+1")) {
            return ru ? "Сакральный ответ: 2" : "Sacred answer: 2";
        } else if (cleaned.endsWith("7")) {
            return ru ? "Счастливая семерка витает в эфире!" : "Lucky 7 floats in the ether!";
        } else if (cleaned.endsWith("42")) {
            return ru ? "Ответ на Главный Вопрос Вселенной" : "Answer to the Ultimate Question of the Universe";
        } else if (cleaned.endsWith("13")) {
            return ru ? "Осторожно... Роковое число!" : "Beware... The fateful number!";
        }

        // Проверяем изученные паттерны
        if (!actionHistory.isEmpty()) {
            String lastAction = actionHistory.get(actionHistory.size() - 1);
            for (Map.Entry<String, String> entry : learnedPatterns.entrySet()) {
                if (entry.getKey().startsWith(lastAction + "->")) {
                    return entry.getValue();
                }
            }
        }

        // Частотный предсказатель
        String mostFrequent = getMostFrequentAction();
        if (mostFrequent != null && random.nextBoolean()) {
            return (ru ? "Аура подсказывает кнопку: " : "Aura suggests button: ") + formatActionName(mostFrequent);
        }

        // Случайные астральные подсказки
        String[] mysticalPredictionsRu = {
            "Чувствую операцию сложения (+)...",
            "Вижу знак равенства (=) в ближайшем будущем",
            "Число 7 витает в эфире...",
            "Астральный совет: время умножить на 2",
            "Духи чисел шепчут про заветную кнопку",
            "Ментальный импульс направлен на '='..."
        };

        String[] mysticalPredictionsEn = {
            "Sensing addition (+) operation...",
            "I see an equals (=) sign in the near future",
            "Number 7 floats in the ether...",
            "Astral advice: time to multiply by 2",
            "Number spirits whisper about the sacred button",
            "Mental pulse directed at '='..."
        };

        String[] pool = ru ? mysticalPredictionsRu : mysticalPredictionsEn;
        return pool[random.nextInt(pool.length)];
    }

    public String generateMysticalComment(String result) {
        boolean ru = isRussian();
        double val = 0;
        try {
            val = Double.parseDouble(result);
        } catch (NumberFormatException ignored) {}

        if (val == 42) {
            return ru ? "🌌 Ответ на Главный Вопрос Вселенной, Жизни и Всего Остального!"
                      : "🌌 Answer to the Ultimate Question of Life, the Universe, and Everything!";
        } else if (val == 13) {
            return ru ? "⚡ Роковое число! Будьте внимательны к знакам судьбы."
                      : "⚡ Fateful number! Pay attention to the signs of destiny.";
        } else if (val == 7 || val == 777) {
            return ru ? "✨ Космическая удача снизошла на этот результат!"
                      : "✨ Cosmic luck descended upon this result!";
        }

        String[] commentsRu = {
            "Сакральное число судьбы: " + result,
            "Духи математики шепчут: " + result,
            "В параллельной вселенной это было бы " + (val + 1),
            "Энергия чисел подтверждает: " + result,
            "Это число видел Нострадамус в астральном сне",
            "Астральный резонанс равен " + result + "!"
        };

        String[] commentsEn = {
            "Sacred number of destiny: " + result,
            "Spirits of mathematics whisper: " + result,
            "In a parallel universe this would be " + (val + 1),
            "Number energy confirms: " + result,
            "Nostradamus saw this number in an astral dream",
            "Astral resonance equals " + result + "!"
        };

        String[] pool = ru ? commentsRu : commentsEn;
        return pool[random.nextInt(pool.length)];
    }

    private String getMostFrequentAction() {
        if (frequencyMap.isEmpty()) return null;
        return Collections.max(frequencyMap.entrySet(), Map.Entry.comparingByValue()).getKey();
    }

    private String formatActionName(String action) {
        if (action.startsWith("number_")) {
            return action.replace("number_", "");
        } else if (action.startsWith("op_")) {
            return action.replace("op_", "");
        }
        return action;
    }
}