# План добавления 2 новых активностей (SplashActivity и LanguageActivity)

Создание полноценного стартового сценария приложения с 4-секундной магия-анимацией изображения и 5-секундным «жутким» отсчетом таймера при выборе языка интерфейса.

## Реализуемый сценарий запуска

```
[Запуск приложения] 
       │
       ▼
[SplashActivity] 
 (Заголовок эзотерическим шрифтом + main_image вырастает из точки за 4 секунды)
       │
       ▼ (Автоматический переход)
[LanguageActivity]
 (Выбор: English / Русский | Сохранение в SharedPreferences)
 (Жуткий 5-секундный обратный отсчет со свечением и вибрацией)
       │
       ▼ (По нажатию или истечению 5 секунд)
[MainActivity] (Главный Калькулятор-Экстрасенс)
```

---

## Proposed Changes

### Resources & Drawables

#### [NEW] [main_image.xml](file:///C:/Users/zeevf/AndroidStudioProjects/PsychicCalculator/app/src/main/res/drawable/main_image.xml)
- Мистический векторный рисунок (хрустальный магический шар / око предсказателя) для анимации на стартовом экране.

---

### New Activities & Layouts

#### [NEW] [SplashActivity.java](file:///C:/Users/zeevf/AndroidStudioProjects/PsychicCalculator/app/src/main/java/zeev/fraiman/psychiccalculator/SplashActivity.java)
- Полноэкранный стартовый экран.
- Анимация `main_image`: увеличение масштаба из 0 в 1.0 за 4000 мс (4 секунды).
- Автоматический переход в `LanguageActivity`.

#### [NEW] [activity_splash.xml](file:///C:/Users/zeevf/AndroidStudioProjects/PsychicCalculator/app/src/main/res/layout/activity_splash.xml)
- Макет со стилизованным заголовком и центральным `ImageView` `main_image`.

#### [NEW] [LanguageActivity.java](file:///C:/Users/zeevf/AndroidStudioProjects/PsychicCalculator/app/src/main/java/zeev/fraiman/psychiccalculator/LanguageActivity.java)
- Экран выбора языка интерфейса:
  - Кнопки: `English` и `Русский`.
  - Запоминание выбора в `SharedPreferences` (по умолчанию `en` при первом запуске).
  - 5-секундный `CountDownTimer` с пульсирующим жутковатым отображением времени (`🔮 05`, `⚡ 04`, `👁️ 03`...) и вибрацией на каждом тике.
  - Автоматический переход в `MainActivity` по клику или истечении 5 секунд.

#### [NEW] [activity_language.xml](file:///C:/Users/zeevf/AndroidStudioProjects/PsychicCalculator/app/src/main/res/layout/activity_language.xml)
- Макет с мистическим заголовком, кнопками выбора языка и индикатором обратного отсчёта.

---

### Manifest Update

#### [MODIFY] [AndroidManifest.xml](file:///C:/Users/zeevf/AndroidStudioProjects/PsychicCalculator/app/src/main/AndroidManifest.xml)
- Назначение `SplashActivity` как главной `LAUNCHER` активности.
- Регистрация `LanguageActivity`.

---

## Verification Plan

### Automated Tests
- Запуск `:app:assembleDebug` для верификации сборки всех активностей и ресурсов.

### Manual Verification
- Проверка старта приложения с `SplashActivity`.
- Проверка 4-секундной плавности анимации роста `main_image`.
- Проверка работы 5-секундного обратного отсчёта в `LanguageActivity`.
- Проверка сохранения выбранного языка в `SharedPreferences`.
