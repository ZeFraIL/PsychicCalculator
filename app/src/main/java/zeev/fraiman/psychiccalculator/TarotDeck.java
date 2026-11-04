package zeev.fraiman.psychiccalculator;

import android.content.Context;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TarotDeck {

    public static class TarotCard {
        private final String name;
        private final String emoji;
        private final String meaning;
        private final String advice;

        public TarotCard(String name, String emoji, String meaning, String advice) {
            this.name = name;
            this.emoji = emoji;
            this.meaning = meaning;
            this.advice = advice;
        }

        public String getName() { return name; }
        public String getEmoji() { return emoji; }
        public String getMeaning() { return meaning; }
        public String getAdvice() { return advice; }
    }

    private final Context context;
    private final Random random = new Random();

    public TarotDeck(Context context) {
        this.context = context != null ? context.getApplicationContext() : null;
    }

    private boolean isRussian() {
        if (context == null) return false;
        String lang = context.getSharedPreferences("PsychicPrefs", Context.MODE_PRIVATE)
                .getString("app_lang", "en");
        return "ru".equalsIgnoreCase(lang);
    }

    public TarotCard drawRandomCard() {
        List<TarotCard> cards = new ArrayList<>();
        boolean ru = isRussian();

        if (ru) {
            cards.add(new TarotCard("Маг Сложения", "🧙‍♂️", "Аркан Сложения и Роста", "Ваши ресурсы и радости будут умножаться во сто крат!"));
            cards.add(new TarotCard("Шут Деления", "🃏", "Аркан Хаоса и Ошибок", "Не делите на ноль и не распыляйте внимание по мелочам."));
            cards.add(new TarotCard("Дьявол Нуля", "😈", "Аркан Искушения Пустотой", "Опасайтесь нулевых результатов и финансовых дыр."));
            cards.add(new TarotCard("Верховный Интеграл", "📜", "Аркан Абсолютной Точности", "Ваши вычисления безупречны, Космос на вашей стороне!"));
            cards.add(new TarotCard("Колесо Вычислений", "🎡", "Аркан Космической Цикличности", "Сегодня считаем убытки, а завтра разбогатеем!"));
            cards.add(new TarotCard("Астральная Семёрка", "🌟", "Аркан Фортуны и Удачи", "Число 7 охраняет ваш путь к великим открытиям."));
            cards.add(new TarotCard("Звезда Умножения", "✨", "Аркан Синергии и Успеха", "Всё, за что вы возьмётесь, принесёт кратный результат!"));
            cards.add(new TarotCard("Башня Переполнения", "🏰", "Аркан Больших Чисел", "Не оперируйте миллионами без надобности, берегите разум."));
        } else {
            cards.add(new TarotCard("Magician of Addition", "🧙‍♂️", "Arcana of Addition & Growth", "Your resources and joy shall multiply a hundredfold!"));
            cards.add(new TarotCard("Fool of Division", "🃏", "Arcana of Chaos & Errors", "Do not divide by zero or waste focus on trifles."));
            cards.add(new TarotCard("Devil of Zero", "😈", "Arcana of Void Temptation", "Beware of zero results and financial black holes."));
            cards.add(new TarotCard("High Integral", "📜", "Arcana of Absolute Precision", "Your calculations are flawless, the Cosmos is on your side!"));
            cards.add(new TarotCard("Wheel of Calculation", "🎡", "Arcana of Cosmic Cycles", "Calculating losses today, gaining fortune tomorrow!"));
            cards.add(new TarotCard("Astral Seven", "🌟", "Arcana of Fortune & Luck", "The number 7 guards your path to great discoveries."));
            cards.add(new TarotCard("Star of Multiplication", "✨", "Arcana of Synergy & Success", "Everything you undertake shall yield multiplied results!"));
            cards.add(new TarotCard("Tower of Overflow", "🏰", "Arcana of Large Numbers", "Do not operate with millions needlessly, preserve your mind."));
        }

        return cards.get(random.nextInt(cards.size()));
    }
}