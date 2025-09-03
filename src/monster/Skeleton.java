package monster;

import java.util.Random;

import entity.Entity;
import main.GamePanel;
import object.OBJ_Arrow;
import object.OBJ_Emerald;
import object.OBJ_Firecharge;
import object.OBJ_healingP;

public class Skeleton extends Entity {

    GamePanel gp;
    boolean lockedDirection = false;

    public Skeleton(GamePanel gp) {
        super(gp);
        this.gp = gp;

        type = type_skeleton;
        name = "Skeleton";
        defaultSpeed = 1;
        speed = defaultSpeed;
        maxLife = 6;
        life = maxLife;
        pro = new OBJ_Arrow(gp);

        solidArea.x = 8;
        solidArea.y = 16;
        solidArea.width = 32;
        solidArea.height = 32;

        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        getImage();
        getAttackImage();
    }

    public void getImage() {
        up1 = setup("/monster/su1", gp.tileSize, gp.tileSize);
        up2 = setup("/monster/su2", gp.tileSize, gp.tileSize);
        down1 = setup("/monster/sd1", gp.tileSize, gp.tileSize);
        down2 = setup("/monster/sd2", gp.tileSize, gp.tileSize);
        left1 = setup("/monster/sl1", gp.tileSize, gp.tileSize);
        left2 = setup("/monster/sl2", gp.tileSize, gp.tileSize);
        right1 = setup("/monster/sr1", gp.tileSize, gp.tileSize);
        right2 = setup("/monster/sr2", gp.tileSize, gp.tileSize);
        upStand = setup("/monster/su", gp.tileSize, gp.tileSize);
        downStand = setup("/monster/sd", gp.tileSize, gp.tileSize);
        leftStand = setup("/monster/sl", gp.tileSize, gp.tileSize);
        rightStand = setup("/monster/sr", gp.tileSize, gp.tileSize);
    }

    public void getAttackImage() {
        aUp1 = setup("/monster/sua1", gp.tileSize, gp.tileSize);
        aUp2 = setup("/monster/sua2", gp.tileSize, gp.tileSize);
        aDown1 = setup("/monster/sda1", gp.tileSize, gp.tileSize);
        aDown2 = setup("/monster/sda2", gp.tileSize, gp.tileSize);
        aLeft1 = setup("/monster/sla1", gp.tileSize, gp.tileSize);
        aLeft2 = setup("/monster/sla2", gp.tileSize, gp.tileSize);
        aRight1 = setup("/monster/sra1", gp.tileSize, gp.tileSize);
        aRight2 = setup("/monster/sra2", gp.tileSize, gp.tileSize);
    }

    @Override
    public void update() {
        // Don't process if in knockback - let parent class handle it
        if (knockBack) {
            super.update();
            return;
        }

        // Handle attack animation first if we're attacking
        if (attacking) {
            attackAnimation();
        } else {
            // Call parent update for normal behavior when not attacking
            super.update();
        }

        // Skeleton-specific logic (only if not attacking and not in knockback)
        if (!attacking && !knockBack) {
            boolean contactPlayer = gp.cChecker.checkPlayer(this);

            if (contactPlayer && shotCounter >= 30) {
                damagePlayer(attack);
                shotCounter = 0;
            }

            int xDist = gp.player.worldX - worldX;
            int yDist = gp.player.worldY - worldY;
            int tileDist = (Math.abs(xDist) + Math.abs(yDist)) / gp.tileSize;

            if (tileDist > 15) {
                onPath = false;
                attacking = false;
                lockedDirection = false;
            }
            else if (tileDist <= 7) {
                onPath = false;
                if (gp.pFinder.pathList != null) {
                    gp.pFinder.pathList.clear();
                }

                if (!lockedDirection) {
                    double angle = Math.atan2(yDist, xDist);
                    double degrees = Math.toDegrees(angle);
                    if (degrees < 0) degrees += 360;

                    if (degrees >= 45 && degrees < 135) direction = "down";
                    else if (degrees >= 135 && degrees < 225) direction = "left";
                    else if (degrees >= 225 && degrees < 315) direction = "up";
                    else direction = "right";

                    lockedDirection = true;
                }

                if (!attacking && shotCounter >= 30 && !pro.alive) {
                    attacking = true;
                    spriteNum = 1;
                    spriteCounter = 0;
                }
            }
            else {
                onPath = true;
                lockedDirection = false;
            }
        }
    }

    public void attackAnimation() {
        spriteCounter++;

        // Simple 2-frame attack animation
        if (spriteCounter <= 20) {
            spriteNum = 1; // Draw bow
        } 
        else if (spriteCounter <= 35) {
            spriteNum = 2; // Release arrow
            
            if (spriteCounter == 21 && !pro.alive && shotCounter >= 30) {
                // Calculate position in front of the skeleton based on direction
                int arrowX = worldX;
                int arrowY = worldY;
                
                switch (direction) {
                    case "up":
                        arrowY = worldY - gp.tileSize;
                        break;
                    case "down":
                        arrowY = worldY + gp.tileSize;
                        break;
                    case "left":
                        arrowX = worldX - gp.tileSize;
                        break;
                    case "right":
                        arrowX = worldX + gp.tileSize;
                        break;
                }
                
                // Center the arrow on the tile
                arrowX += (gp.tileSize - pro.up1.getWidth()) / 2;
                arrowY += (gp.tileSize - pro.up1.getHeight()) / 2;
                
                pro.set(arrowX, arrowY, direction, true, this);
                
                // Add to projectile array
                for (int i = 0; i < gp.projectile[gp.currentMap].length; i++) {
                    if (gp.projectile[gp.currentMap][i] == null) {
                        gp.projectile[gp.currentMap][i] = pro;
                        break;
                    }
                }
                
                shotCounter = 0;
                gp.playSE(11);
            }
        }
        else {
            // End attack
            attacking = false;
            spriteNum = 3; // Standing frame
            spriteCounter = 0;
            lockedDirection = false;
        }
    }

    public void setAction() {
        // Don't move while shooting or in knockback
        if (attacking || knockBack) {
            return;
        }

        if (onPath) {
            int goalCol = (gp.player.worldX + gp.player.solidArea.x) / gp.tileSize;
            int goalRow = (gp.player.worldY + gp.player.solidArea.y) / gp.tileSize;
            
            // Only search path if coordinates are valid
            if (goalCol >= 0 && goalCol < gp.maxWorldCol && goalRow >= 0 && goalRow < gp.maxWorldRow) {
                searchPath(goalCol, goalRow);
            } else {
                onPath = false;
            }
        }
    }

    public void damageReaction() {
        actionLockCounter = 0;
        onPath = true;
        
        // Clear any existing path to force recalculation
        if (gp.pFinder.pathList != null) {
            gp.pFinder.pathList.clear();
        }
    }
    
    // Override to handle knockback properly
    @Override
    public void setKnockBack(Entity target, Entity attacker, int knockBackPower) {
        super.setKnockBack(target, attacker, knockBackPower);
        // Clear pathfinding when knocked back
        if (gp.pFinder.pathList != null) {
            gp.pFinder.pathList.clear();
        }
        onPath = false;
        attacking = false; // Stop attacking during knockback
        lockedDirection = false;
    }
    
    public void checkDrop() {
        int i = new Random().nextInt(100) + 1; // Generates 1 to 100

        if (i <= 75) {
            dropItem(new OBJ_Emerald(gp)); // 75% chance
        } else {
            dropItem(new OBJ_healingP(gp)); // 25% chance
        }
    }
}