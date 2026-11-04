package zeev.fraiman.psychiccalculator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class ParticleSparkView extends View {

    private static class Particle {
        float x, y;
        float vx, vy;
        float radius;
        int color;
        int alpha;
        int maxLife;
        int life;

        Particle(float x, float y, float vx, float vy, float radius, int color, int maxLife) {
            this.x = x;
            this.y = y;
            this.vx = vx;
            this.vy = vy;
            this.radius = radius;
            this.color = color;
            this.alpha = 255;
            this.maxLife = maxLife;
            this.life = maxLife;
        }

        boolean update() {
            x += vx;
            y += vy;
            vy += 0.15f; // Легкая гравитация
            life--;
            alpha = (int) (255f * ((float) life / maxLife));
            return life > 0;
        }
    }

    private final List<Particle> particles = new ArrayList<>();
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Random random = new Random();

    private final int[] sparkColors = new int[]{
        Color.parseColor("#FFD700"), // Золото
        Color.parseColor("#BB86FC"), // Фиолетовый
        Color.parseColor("#00FF88"), // Аура зелёный
        Color.parseColor("#E0B0FF"), // Неоновый
        Color.parseColor("#FFFFFF")  // Белый
    };

    public ParticleSparkView(Context context) {
        super(context);
    }

    public ParticleSparkView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void burst(float originX, float originY) {
        int count = 35;
        for (int i = 0; i < count; i++) {
            float angle = (float) (random.nextDouble() * 2 * Math.PI);
            float speed = 4.0f + random.nextFloat() * 12.0f;
            float vx = (float) (Math.cos(angle) * speed);
            float vy = (float) (Math.sin(angle) * speed);
            float radius = 6.0f + random.nextFloat() * 8.0f;
            int color = sparkColors[random.nextInt(sparkColors.length)];
            int maxLife = 20 + random.nextInt(25);

            particles.add(new Particle(originX, originY, vx, vy, radius, color, maxLife));
        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (particles.isEmpty()) return;

        Iterator<Particle> iterator = particles.iterator();
        while (iterator.hasNext()) {
            Particle p = iterator.next();
            if (!p.update()) {
                iterator.remove();
                continue;
            }

            paint.setColor(p.color);
            paint.setAlpha(p.alpha);
            canvas.drawCircle(p.x, p.y, p.radius, paint);
        }

        if (!particles.isEmpty()) {
            postInvalidateOnAnimation();
        }
    }
}