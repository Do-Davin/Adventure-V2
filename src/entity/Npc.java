package entity;

import java.util.Random;

import main.GamePanel;

public class Npc extends Entity {

	public Npc(GamePanel gp) {
		super(gp);

		direction = "down";
		speed = 1;

		// Customize solid area position and size
		solidArea.x = 8; // X-offset within NPC sprite
		solidArea.y = 16; // Y-offset
		solidArea.width = 32; // Hitbox width
		solidArea.height = 32; // Hitbox height

		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;

		getImage();
		setDialogue();
	}

	// Set NPC sprite
	public void getImage() {

		up1 = setup("/npc/npc_up1", gp.tileSize, gp.tileSize);
		up2 = setup("/npc/npc_up2", gp.tileSize, gp.tileSize);
		down1 = setup("/npc/npc_down1", gp.tileSize, gp.tileSize);
		down2 = setup("/npc/npc_down2", gp.tileSize, gp.tileSize);
		left1 = setup("/npc/npc_left1", gp.tileSize, gp.tileSize);
		left2 = setup("/npc/npc_left2", gp.tileSize, gp.tileSize);
		right1 = setup("/npc/npc_right1", gp.tileSize, gp.tileSize);
		right2 = setup("/npc/npc_right2", gp.tileSize, gp.tileSize);
		upStand = setup("/npc/npc_up", gp.tileSize, gp.tileSize);
		downStand = setup("/npc/npc_down", gp.tileSize, gp.tileSize);
		leftStand = setup("/npc/npc_left", gp.tileSize, gp.tileSize);
		rightStand = setup("/npc/npc_right", gp.tileSize, gp.tileSize);
	}

	public void setDialogue() {

		dialogues[0] = "Hello there \nHow are you ?";
		dialogues[1] = "Just awake huh....\nI found you along river \nthen I brought you here";
		dialogues[2] = "This island is crazy \nso you better watch out";
		dialogues[3] = "And i heard there are portals \nthat will bring you to new island";
		dialogues[4] = "I haven't been there...\nbut you should go check it out \nand find out your way out";
		dialogues[5] = "Take my wooden sword overthere \nto protect yourself";
		dialogues[6] = "Ohh... and also try to find my \nLantern in the upper forest\nin case for night time";
		dialogues[7] = "Becareful and Good Luck...";
	}

	public void setAction() {

		if (onPath == true) {

//			 goal position
			int goalCol = 32;
			int goalRow = 61;

			checkCollision();
			if (collisionOn) {
				gp.pFinder.pathList.clear();
				searchPath(goalCol, goalRow);
			} else {
				searchPath(goalCol, goalRow);
			}

		} else {
			actionLockCounter++;

			if (actionLockCounter == 120) {
				Random rand = new Random();
				int i = rand.nextInt(100) + 1; // number from 1 to 100

				if (i <= 20) {
					// Do nothing — keeps current direction and "stands" still
				} else if (i <= 40) {
					direction = "up";
				} else if (i <= 60) {
					direction = "down";
				} else if (i <= 80) {
					direction = "left";
				} else {
					direction = "right";
				}

				actionLockCounter = 0;
			}
		}

	}

	public void speak() {

		// Do this character specific stuff

		super.speak();
	}
}