package zeev.fraiman.psychiccalculator;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

public class SplashActivity extends AppCompatActivity {

    private ImageView mainImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.enableEdgeToEdge(getWindow());
        setContentView(R.layout.activity_splash);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.splash_main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mainImage = findViewById(R.id.main_image);

        // Инициализация точки масштабирования (из центра в 0)
        mainImage.setScaleX(0f);
        mainImage.setScaleY(0f);
        mainImage.setAlpha(0f);

        // Рост изображения из точки на всю ширину за 4 секунды (4000 мс)
        mainImage.animate()
                .scaleX(1.0f)
                .scaleY(1.0f)
                .alpha(1.0f)
                .setDuration(4000)
                .withEndAction(this::navigateToLanguage)
                .start();
    }

    private void navigateToLanguage() {
        if (!isFinishing() && !isDestroyed()) {
            Intent intent = new Intent(SplashActivity.this, LanguageActivity.class);
            startActivity(intent);
            finish();
        }
    }
}