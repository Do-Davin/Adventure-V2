package monster;

import java.util.Random;

import entity.Entity;
import main.GamePanel;
import object.OBJ_Emerald;
import object.OBJ_Firecharge;
import object.OBJ_healingP;

public class Zombie extends Entity {
    GamePanel gp;
    
    public Zombie(GamePanel gp) {
        super(gp);
        this.gp = gp;
        
        type = type_zombie;
        name = "Zombie";
        defaultSpeed = 1;
        speed = defaultSpeed;
        maxLife = 6;
        life = maxLife;
        attack = 2;
        defense = 0;
        
        solidArea.x = 8;
        solidArea.y = 16;
        solidArea.width = 32;
        solidArea.height = 32;

        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        getImage();
    }
    
    public void getImage() {
        up1 = setup("/monster/zombie_up1", gp.tileSize, gp.tileSize);
        up2 = setup("/monster/zombie_up2", gp.tileSize, gp.tileSize);
        down1 = setup("/monster/zombie_down1", gp.tileSize, gp.tileSize);
        down2 = setup("/monster/zombie_down2", gp.tileSize, gp.tileSize);
        left1 = setup("/monster/zombie_left1", gp.tileSize, gp.tileSize);
        left2 = setup("/monster/zombie_left2", gp.tileSize, gp.tileSize);
        right1 = setup("/monster/zombie_right1", gp.tileSize, gp.tileSize);
        right2 = setup("/monster/zombie_right2", gp.tileSize, gp.tileSize);
        upStand = setup("/monster/zombie_up", gp.tileSize, gp.tileSize);
        downStand = setup("/monster/zombie_down", gp.tileSize, gp.tileSize);
        leftStand = setup("/monster/zombie_left", gp.tileSize, gp.tileSize);
        rightStand = setup("/monster/zombie_right", gp.tileSize, gp.tileSize);
    }
    
    public void setAction() {
        // Don't calculate actions if dead, dying, or in knockback
        if (!alive || die || knockBack) {
            return;
        }
        
        int tileDist = getTileDistance(gp.player);
        
        // Simple chasing logic - if player is close, chase them
        if (tileDist < 8) {
            onPath = true;
            
            // Get player's position
            int goalCol = (gp.player.worldX + gp.player.solidArea.x) / gp.tileSize;
            int goalRow = (gp.player.worldY + gp.player.solidArea.y) / gp.tileSize;
            
            // Search path to player
            searchPath(goalCol, goalRow);
            
            // If pathfinding fails, use simple movement
            if (gp.pFinder.pathList == null || gp.pFinder.pathList.isEmpty()) {
                moveTowardPlayer(30);
            }
        } else {
            onPath = false;
            // Random movement when player is far
            getRandomDirection(120);
        }
    }
    
    public void damageReaction() {
        actionLockCounter = 0;
        // Always try to chase when hit
        onPath = true;
    }
    
    // Simple movement toward player without pathfinding
    public void moveTowardPlayer(int interval) {
        actionLockCounter++;
        
        if(actionLockCounter > interval) {
            // Simple direction calculation based on player position
            if(Math.abs(gp.player.worldX - worldX) > Math.abs(gp.player.worldY - worldY)) {
                if(gp.player.worldX < worldX) {
                    direction = "left";
                } else {
                    direction = "right";
                }
            } else {
                if(gp.player.worldY < worldY) {
                    direction = "up";
                } else {
                    direction = "down";
                }
            }
            actionLockCounter = 0;
        }
    }
    
    public void checkDrop() {
        int i = new Random().nextInt(100) + 1; // Generates 1 to 100

        if (i <= 75) {
            dropItem(new OBJ_Emerald(gp)); // 75% chance
        } else {
            dropItem(new OBJ_healingP(gp)); // 25% chance
        }
    }

    
    // Override to ensure proper behavior after knockback
    @Override
    public void setKnockBack(Entity target, Entity attacker, int knockBackPower) {
        super.setKnockBack(target, attacker, knockBackPower);
        // Clear any pathfinding when knocked back
        if (gp.pFinder.pathList != null) {
            gp.pFinder.pathList.clear();
        }
        onPath = false;
    }

}