package object;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import entity.Entity;
import main.GamePanel;

public class OBJ_Portal extends Entity {

    public OBJ_Portal(GamePanel gp) {
        super(gp);
        
        name = "Portal";
        type = type_portal;
        collision = false;
        
        getImage();
    }
    
    public void getImage() {
        down1 = setup("/objects/portal1", gp.tileSize, gp.tileSize);
        down2 = setup("/objects/portal2", gp.tileSize, gp.tileSize); 
        up1 = setup("/objects/portal3", gp.tileSize, gp.tileSize);
        up2 = setup("/objects/portal4", gp.tileSize, gp.tileSize);
    }
    
    public void update() {
        // Animate the portal
        spriteCounter++;
        if (spriteCounter > 10) {
            if (spriteNum == 1) {
                spriteNum = 2;
            } else if (spriteNum == 2) {
                spriteNum = 3;
            } else if (spriteNum == 3) {
                spriteNum = 4;
            } else if (spriteNum == 4) {
                spriteNum = 1;
            }
            spriteCounter = 0;
        }
    }
    
    @Override
    public void draw(Graphics2D g2) {
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        // Only draw if on screen
        if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
            worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
            worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
            worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
            
            BufferedImage image = null;
            
            // Custom 4-frame animation for portal ONLY
            switch(spriteNum) {
                case 1: image = down1; break;
                case 2: image = down2; break;
                case 3: image = up1; break;
                case 4: image = up2; break;
            }
            
            if (image != null) {
                // Handle transparency if invincible (like other entities)
                if (invincible == true) {
                    changeAlpha(g2, 0.5f);
                }
                
                g2.drawImage(image, screenX, screenY, null);
                
                // Reset alpha if changed
                if (invincible == true) {
                    changeAlpha(g2, 1f);
                }
            }
        }
    }
}