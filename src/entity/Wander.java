package entity;

import main.GamePanel;
import object.OBJ_Axe;
import object.OBJ_Firecharge;
import object.OBJ_Lantern;
import object.OBJ_Shield;
import object.OBJ_Sword;
import object.OBJ_Tent;
import object.OBJ_WoodenSword;
import object.OBJ_healingP;

public class Wander extends Entity {

    public Wander(GamePanel gp) {
        super(gp);

        direction = "down"; // Default direction (won't actually affect movement)
        speed = 0; // Set speed to 0 since he shouldn't move
        collision = true; // Still has collision

        // Fixed solid area
        solidArea.x = 8;
        solidArea.y = 16;
        solidArea.width = 32;
        solidArea.height = 32;

        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        getImage();
        setDialogue();
        setItem();
    }

    public void getImage() {
        // Only need two frames since he's standing still
        down1 = setup("/npc/Wander1", gp.tileSize, gp.tileSize);
        down2 = setup("/npc/Wander2", gp.tileSize, gp.tileSize);
        
        // Set all directions to use the same sprites since he doesn't move
        up1 = down1;
        up2 = down2;
        left1 = down1;
        left2 = down2;
        right1 = down1;
        right2 = down2;
        
        // Standing sprites (all same)
        upStand = down1;
        downStand = down1;
        leftStand = down1;
        rightStand = down1;
    }

    public void setDialogue() {
        dialogues[0] = "Hello, player!\nWelcome to Wander Trading.\nHow can I help you?";
    }

    public void setItem() {
        inventory.add(new OBJ_healingP(gp));
        inventory.add(new OBJ_Firecharge(gp));
        inventory.add(new OBJ_Sword(gp));
        inventory.add(new OBJ_WoodenSword(gp));
        inventory.add(new OBJ_Axe(gp));
        inventory.add(new OBJ_Shield(gp));
        inventory.add(new OBJ_Tent(gp));
        inventory.add(new OBJ_Lantern(gp));
    }

    @Override
    public void setAction() {
        // Override to prevent any movement
        // Just animate between the two standing sprites
        spriteCounter++;
        if (spriteCounter > 20) { // Slower animation since just standing
            if (spriteNum == 1) {
                spriteNum = 2;
            } else {
                spriteNum = 1;
            }
            spriteCounter = 0;
        }
    }

    @Override 
    public void update() {
        // Only update animation, no movement
        setAction();
        
        // Handle invincibility frames if needed
        if (invincible) {
            invinCounter++;
            if (invinCounter > 40) {
                invincible = false;
                invinCounter = 0;
            }
        }
    }
    
    public void speak() {
    	
    	gp.ui.currentDialogue = dialogues[0]; // Reset to original dialogue
        gp.ui.displayedText = "";
        gp.ui.charIndex = 0;
        gp.ui.textCompleted = false;
    	
    	super.speak();
    	gp.gameState = gp.tradeState;
    	gp.ui.wander = this;
    }
}