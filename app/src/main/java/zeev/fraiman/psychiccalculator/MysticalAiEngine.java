package zeev.fraiman.psychiccalculator;

import android.content.Context;
import java.util.Random;

public class MysticalAiEngine {

    private final Context context;
    private final Random random = new Random();

    public MysticalAiEngine(Context context) {
        this.context = context.getApplicationContext();
    }

    private boolean isRussian() {
        if (context == null) return false;
        String lang = context.getSharedPreferences("PsychicPrefs", Context.MODE_PRIVATE)
                .getString("app_lang", "en");
        return "ru".equalsIgnoreCase(lang);
    }

    public String analyzeExpressionAndResult(String expression, String resultStr) {
        boolean ru = isRussian();

        if (expression == null || expression.isEmpty()) {
            return ru ? "Астральный разум ожидает числового импульса..."
                      : "Astral mind awaits a numerical pulse...";
        }

        // 1. Деление на 0
        if (expression.contains("/ 0") || expression.contains("÷ 0") || "Infinity".equalsIgnoreCase(resultStr) || "NaN".equalsIgnoreCase(resultStr)) {
            String[] zeroErrorsRu = {
                "🌌 Астральные сущности запрещают делить на ноль! Вселенная чуть не схлопнулась!",
                "⚡ Деление на ноль открывает черную дыру в вашей комнате. Возврат в нормальность...",
                "🌀 Ошибка бытия: Духи не нашли ни одного кванта при делении на 0!"
            };
            String[] zeroErrorsEn = {
                "🌌 Astral entities forbid dividing by zero! The universe almost collapsed!",
                "⚡ Division by zero opens a black hole in your room. Returning to normal...",
                "🌀 Error of existence: Spirits found zero quanta when dividing by 0!"
            };
            String[] pool = ru ? zeroErrorsRu : zeroErrorsEn;
            return pool[random.nextInt(pool.length)];
        }

        // 2. Вычитание больших сумм
        if (expression.contains("-")) {
            try {
                String[] parts = expression.split("-");
                if (parts.length >= 2) {
                    double n1 = Double.parseDouble(parts[0].trim());
                    double n2 = Double.parseDouble(parts[1].trim());
                    if (n1 > 3000 && n2 > 2000) {
                        return ru ? "💸 Вижу траты! Остаток " + resultStr + "... Астрал рекомендует экономить!"
                                  : "💸 Expense detected! Remaining " + resultStr + "... Astral advises saving!";
                    }
                }
            } catch (Exception ignored) {}
        }

        // 3. Вычисление 2 + 2
        if (expression.replaceAll("\\s+", "").equals("2+2")) {
            return ru ? "🧙‍♂️ Даже Великие Маги Древности начинали с элементарного 2+2=4!"
                      : "🧙‍♂️ Even Ancient Grand Masters started with simple 2+2=4!";
        }

        // 4. Округлое число 1000, 10000
        try {
            double resVal = Double.parseDouble(resultStr);
            if (resVal > 0 && resVal % 1000 == 0) {
                return ru ? "💎 Идеально круглое число " + (long)resVal + "! Это знак астральной гармонии!"
                          : "💎 Perfectly round number " + (long)resVal + "! A sign of astral harmony!";
            } else if (resVal < 0) {
                return ru ? "🌒 Отрицательное число... Ваш ментальный баланс уйдет в плюс совсем скоро!"
                          : "🌒 Negative number... Your mental balance will turn positive soon!";
            }
        } catch (Exception ignored) {}

        // 5. Обычные ИИ предсказания
        String[] aiInsightsRu = {
            "🤖 ИИ-экстрасенс вычислил: Число " + resultStr + " принесет финансовую удачу!",
            "🔮 Ментальный алгоритм склоняется к сакральной важности числа " + resultStr,
            "🌌 Квантовый разум считает: " + expression + " = " + resultStr + " (с вероятностью 99.9%)",
            "✨ Духи чисел шепчут: " + resultStr + " откроет новые горизонты!"
        };

        String[] aiInsightsEn = {
            "🤖 Psychic AI computed: Number " + resultStr + " brings financial luck!",
            "🔮 Mental algorithm leans toward sacred importance of " + resultStr,
            "🌌 Quantum mind predicts: " + expression + " = " + resultStr + " (99.9% probability)",
            "✨ Number spirits whisper: " + resultStr + " opens new horizons!"
        };

        String[] pool = ru ? aiInsightsRu : aiInsightsEn;
        return pool[random.nextInt(pool.length)];
    }
}