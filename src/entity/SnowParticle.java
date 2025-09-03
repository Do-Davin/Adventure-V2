package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;
import main.GamePanel;

public class SnowParticle extends Entity {
    private int size;
    private float opacity;
    private float wind;
    private Random random;
    
    public SnowParticle(GamePanel gp) {
        super(gp);
        random = new Random();
        
        // Random size between 2-6 pixels
        size = random.nextInt(5) + 4;
        
        // Random starting position across the screen width
        worldX = random.nextInt(gp.screenWidth);
        worldY = -size; // Start above the screen
        
        // Random horizontal movement (wind effect)
        wind = (random.nextFloat() - 0.5f) * 0.5f;
        
        // Random fall speed
        speed = random.nextInt(2) + 1;
        
        // Semi-transparent white for snow
        opacity = random.nextFloat() * 0.5f + 0.5f;
        
        alive = true;
    }

    public int getSize() { return size; }
    public float getOpacity() { return opacity; }
    public float getWind() { return wind; }
    public Random getRandom() { return random; }

    public void setSize(int size) { this.size = size; }
    public void setOpacity(float opacity) { this.opacity = opacity; }
    public void setWind(float wind) { 
        if(wind < 0){
            this.wind = wind; 
        }
    }
    public void setRandom(Random random) { this.random = random; }

    public void update() {
        // Move downward with gravity effect
        worldY += speed;
        
        // Add slight horizontal movement (wind)
        worldX += wind;
        
        // If particle goes off screen, reset it
        if (worldY > gp.screenHeight || worldX < -size || worldX > gp.screenWidth) {
            reset();
        }
    }
    
    private void reset() {
        worldX = random.nextInt(gp.screenWidth);
        worldY = -size;
        wind = (random.nextFloat() - 0.5f) * 0.5f;
    }
    
    public void draw(Graphics2D g2) {
        if (!alive) return;
        
        // Set color with opacity
        g2.setColor(new Color(0.7f, 0.85f, 1f, opacity));
        
        // Draw snowflake (simple circle for now)
        g2.fillOval((int)worldX, (int)worldY, size, size);
    }
}