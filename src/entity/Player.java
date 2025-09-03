package entity;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.KeyHandler;
import main.UtilityTool;
import object.OBJ_Arrow;
import object.OBJ_Fireball;
import object.OBJ_Firecharge;
import object.OBJ_Firekey;
import object.OBJ_Icekey;
import object.OBJ_Lantern;
import object.OBJ_Shield;
import object.OBJ_Sword;
import object.OBJ_dunKey;

public class Player extends Entity {

	KeyHandler keyH;

	public final int screenX;
	public final int screenY;

	// Count how many object we interaction with
	public int openDoor = 0;

	// Speed boot
	int getSpeedBoot = 0;
	int speedTimer = 0;
	boolean speedBoosted = false;

	// For Lantern
	public boolean lightUpdated = false;

	public Player(GamePanel gp, KeyHandler keyH) {

		super(gp);
		this.keyH = keyH;

		screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
		screenY = gp.screenHeight / 2 - (gp.tileSize / 2);

		solidArea = new Rectangle();
		solidArea.x = 8;
		solidArea.y = 16;

		// Object interaction
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;

		solidArea.width = 32;
		solidArea.height = 32;

		setDefaultValues();
		getImage();
		getGuardImage();
		setItems();
	}

	// Player coordinate
	public void setDefaultValues() {

		worldX = gp.tileSize * 32;
		worldY = gp.tileSize * 61;
		defaultSpeed = 4;
		speed = defaultSpeed;
		direction = "down";

		// Player state
		// 2 lives = full heart
		// 1 life = half heart

		maxLife = 10; // 10 lives = 5 hearts
		life = maxLife;
		level = 1;
		strength = 1; // the more strength , the more damage;
		dexterity = 1; // the more dex, the less receives;
		exp = 0;
		nextLevelExp = 5;
		coin = 0;
		currentWeapon = null;
		currentShield = null;
		pro = new OBJ_Fireball(gp);
		attack = getAttack(); // total attack base on strength and weapon
		defense = getDefense(); // total defense base on dexterity and shield
	}

	public void setItems() {
	}

	public int getAttack() {
		if (currentWeapon != null) {
			attackArea = currentWeapon.attackArea;
			motion1_duration = currentWeapon.motion1_duration;
			motion2_duration = currentWeapon.motion2_duration;
			return attack = strength * currentWeapon.attackValue;
		} else {
			// No weapon equipped - minimal base attack with bare hands
			attackArea = new Rectangle(0, 0, 0, 0); // No attack area for bare hands
			motion1_duration = 5; // Default bare hand attack timing
			motion2_duration = 20; // Default bare hand attack timing
			return attack = strength; // Just base strength without weapon bonus
		}
	}

	public int getDefense() {
		if (currentShield != null) {
			return dexterity * currentShield.defenseValue;
		} else {
			return dexterity; // Just base dexterity without shield bonus
		}
	}

	public void setDefaultPositions() {

		worldX = gp.tileSize * 32;
		worldY = gp.tileSize * 61;
		direction = "down";

	}
	
	public void restoreLife() {

		life = maxLife;
		invincible = false;
		transparent = false;

	}

	public void getImage() {

		up1 = setup("/player/Steve_up1", gp.tileSize, gp.tileSize);
		up2 = setup("/player/Steve_up2", gp.tileSize, gp.tileSize);
		down1 = setup("/player/Steve_down1", gp.tileSize, gp.tileSize);
		down2 = setup("/player/Steve_down2", gp.tileSize, gp.tileSize);
		left1 = setup("/player/Steve_left1", gp.tileSize, gp.tileSize);
		left2 = setup("/player/Steve_left2", gp.tileSize, gp.tileSize);
		right1 = setup("/player/Steve_right1", gp.tileSize, gp.tileSize);
		right2 = setup("/player/Steve_right2", gp.tileSize, gp.tileSize);
		upStand = setup("/player/Steve_up_stand", gp.tileSize, gp.tileSize);
		downStand = setup("/player/Steve_down_stand", gp.tileSize, gp.tileSize);
		leftStand = setup("/player/Steve_left_stand", gp.tileSize, gp.tileSize);
		rightStand = setup("/player/Steve_right_stand", gp.tileSize, gp.tileSize);
	}

	public void getAttackImage() {
		if (currentWeapon != null) {
			if (currentWeapon.type == type_dsword) {
				aUp1 = setup("/player/Steve_up_a1", gp.tileSize, gp.tileSize * 2);
				aUp2 = setup("/player/Steve_up_a2", gp.tileSize, gp.tileSize * 2);
				aDown1 = setup("/player/Steve_down_a1", gp.tileSize, gp.tileSize * 2);
				aDown2 = setup("/player/Steve_down_a2", gp.tileSize, gp.tileSize * 2);
				aRight1 = setup("/player/Steve_right_a1", gp.tileSize * 2, gp.tileSize);
				aRight2 = setup("/player/Steve_right_a2", gp.tileSize * 2, gp.tileSize);
				aLeft1 = setup("/player/Steve_left_a1", gp.tileSize * 2, gp.tileSize);
				aLeft2 = setup("/player/Steve_left_a2", gp.tileSize * 2, gp.tileSize);
			}
			if (currentWeapon.type == type_wsword) {
				aUp1 = setup("/player/Steve_wood_up1", gp.tileSize, gp.tileSize * 2);
				aUp2 = setup("/player/Steve_wood_up2", gp.tileSize, gp.tileSize * 2);
				aDown1 = setup("/player/Steve_wood_down1", gp.tileSize, gp.tileSize * 2);
				aDown2 = setup("/player/Steve_wood_down2", gp.tileSize, gp.tileSize * 2);
				aRight1 = setup("/player/Steve_wood_right2", gp.tileSize * 2, gp.tileSize);
				aRight2 = setup("/player/Steve_wood_right1", gp.tileSize * 2, gp.tileSize);
				aLeft1 = setup("/player/Steve_wood_left1", gp.tileSize * 2, gp.tileSize);
				aLeft2 = setup("/player/Steve_wood_left2", gp.tileSize * 2, gp.tileSize);
			}
			if (currentWeapon.type == type_axe) {
				aUp1 = setup("/player/Steve_axe_u1", gp.tileSize, gp.tileSize * 2);
				aUp2 = setup("/player/Steve_axe_u2", gp.tileSize, gp.tileSize * 2);
				aDown1 = setup("/player/Steve_axe_d1", gp.tileSize, gp.tileSize * 2);
				aDown2 = setup("/player/Steve_axe_d2", gp.tileSize, gp.tileSize * 2);
				aRight1 = setup("/player/Steve_axe_r1", gp.tileSize * 2, gp.tileSize);
				aRight2 = setup("/player/Steve_axe_r2", gp.tileSize * 2, gp.tileSize);
				aLeft1 = setup("/player/Steve_axe_l1", gp.tileSize * 2, gp.tileSize);
				aLeft2 = setup("/player/Steve_axe_l2", gp.tileSize * 2, gp.tileSize);
			}
		}
	}

	public void getGuardImage() {

		gUp = setup("/player/Steve_up_guard", gp.tileSize, gp.tileSize);
		gDown = setup("/player/Steve_down_guard", gp.tileSize, gp.tileSize);
		gLeft = setup("/player/Steve_left_guard", gp.tileSize, gp.tileSize);
		gRight = setup("/player/Steve_right_guard", gp.tileSize, gp.tileSize);

	}

	public void getSleepImage(BufferedImage image) {
		up1 = image;
		up2 = image;
		upStand = image;
		down1 = image;
		down2 = image;
		downStand = image;
		left1 = image;
		left2 = image;
		leftStand = image;
		right1 = image;
		right2 = image;
		rightStand = image;
	}

	@Override
	public void update() {

		if (knockBack == true) {
			collisionOn = false;
			gp.cChecker.checkTile(this);
			gp.cChecker.checkObject(this, true);
			gp.cChecker.checkEntity(this, gp.npc);
			gp.cChecker.checkEntity(this, gp.monster);
			gp.cChecker.checkEntity(this, gp.iTile);

			if (collisionOn == true) {
				knockBackCounter = 0;
				knockBack = false;
				speed = defaultSpeed;
			} else if (collisionOn == false) {
				switch (knockBackDirection) {
				case "up":
					worldY -= speed;
					break;
				case "down":
					worldY += speed;
					break;
				case "left":
					worldX -= speed;
					break;
				case "right":
					worldX += speed;
					break;
				}
			}

			knockBackCounter++;

			if (knockBackCounter == 10) {
				knockBackCounter = 0;
				knockBack = false;
				speed = defaultSpeed;
			}
		}

		// Handle guarding state first
		if (keyH.shiftPressed && !attacking) { // Can only guard if not attacking
			guarding = true;
			guardCounter++;
		} else {
			guarding = false;
		}

		// Can only attack if not currently guarding
		// In the attack section of update() method:
		if (gp.keyH.fPressed && !guarding) {
			if (currentWeapon != null) { // Only allow attack if player has a weapon
				attacking = true;
				gp.keyH.fPressed = false;
			}

		}
		// Priority: Attack Mode
		else if (attacking == true) {
			attacking();
		}

		//  Movement Input Handling - Only if not attacking
		else if (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed) {

			// Update direction
			if (keyH.upPressed)
				direction = "up";
			else if (keyH.downPressed)
				direction = "down";
			else if (keyH.leftPressed)
				direction = "left";
			else if (keyH.rightPressed)
				direction = "right";

			// Collision checks
			collisionOn = false;
			gp.cChecker.checkTile(this);

			int objIndex = gp.cChecker.checkObject(this, true);
			pickUpObject(objIndex);

			int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
			interactNpc(npcIndex);

			int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
			interactNpc(monsterIndex);
			contactMonster(monsterIndex);

			// CHECK INTERACTIVE TILE COLLISION
			int iTileIndex = gp.cChecker.checkEntity(this, gp.iTile);

			gp.eHandler.checkEvent();
			gp.keyH.enterPressed = false;

			// Only move if no collision
			if (!collisionOn && keyH.enterPressed == false) {
				switch (direction) {
				case "up":
					worldY -= speed;
					break;
				case "down":
					worldY += speed;
					break;
				case "left":
					worldX -= speed;
					break;
				case "right":
					worldX += speed;
					break;
				}
			}

			gp.keyH.enterPressed = false;
			guardCounter = 0;

			// Animate walking frames
			spriteCounter++;
			if (spriteCounter > 10) {
				if (spriteNum == 1)
					spriteNum = 3;
				else if (spriteNum == 3)
					spriteNum = 2;
				else if (spriteNum == 2)
					spriteNum = 1;
				spriteCounter = 0;
			}
		} else {
			// No key is pressed, so reset to standing
			spriteNum = 3;
		}

		// Can only shoot if not guarding
		if (gp.keyH.jPressed == true && pro.alive == false && shotCounter == 30 && !guarding) {
			// Only shoot if have resource
			if (pro.haveResource(this)) {
				pro.set(worldX, worldY, direction, true, this);
				pro.subtractResource(this);

				// check vacancy
				for (int i = 0; i < gp.projectile[1].length; i++) {
					if (gp.projectile[gp.currentMap][i] == null) {
						gp.projectile[gp.currentMap][i] = pro;
						break;
					}
				}
				shotCounter = 0;
				gp.playSE(10);
			}
		}

		if (shotCounter < 30) {
			shotCounter++;
		}

		// Speed boost timer
		if (speedBoosted) {
			speedTimer--;
			if (speedTimer <= 0) {
				speed -= 2;
				speedBoosted = false;
			}
		}

		// Invincibility frames
		if (invincible) {
			invinCounter++;
			if (invinCounter > 60) { // 60 frames = 1 second at 60 FPS
				invincible = false;
				transparent = false;
				invinCounter = 0;
			}
		}
		if (life <= 0) {
			gp.gameState = gp.gameOverState;
			gp.music.stop();
			gp.playSE(26);
		}
	}

	public void pickUpObject(int i) {
		if (i != -1) {
			Entity object = gp.obj[gp.currentMap][i];

			if (object.type == type_portal) {
				return; // Skip decorative objects entirely
			} else if (object.type == type_pickup) {
				// Play sound and use the item
				if (canObtainItem(object)) {
					gp.playSE(8);
					object.use(this);
				}
				gp.obj[gp.currentMap][i] = null;
			} else if (object.type == type_obstacle) {
				if (keyH.enterPressed == true) {
					object.interact();
				}
			} else {
				// For all other collectible items (including emeralds)
				if (canObtainItem(object)) {
					gp.playSE(8); // Play collection sound
				}
				gp.obj[gp.currentMap][i] = null;
			}
		}
	}

	public void interactNpc(int i) {

		// Talk using Enter key
		if (gp.keyH.enterPressed == true) {
			if (i != -1) {
				gp.gameState = gp.dialogueState;
				gp.npc[gp.currentMap][i].speak(); // FIXED
			}
		}

		//Attack using F key
		if (gp.keyH.fPressed == true) {
			attacking = true;
			gp.keyH.fPressed = false;
		}

	}

	public void contactMonster(int i) {

		if (i != -1) {
			if (invincible == false && gp.monster[gp.currentMap][i].die == false) { // FIXED
				gp.playSE(2);
				int damage = gp.monster[gp.currentMap][i].attack - defense;
				if (damage < 0) {
					damage = 0;
				}
				life -= damage;
				invincible = true;
				transparent = true;
			}

		}
	}

	public void damageMonster(int i, Entity attacker, int attack, int knockBackPower) {

		if (i != -1) {
			Entity monster = gp.monster[gp.currentMap][i]; // FIXED

			if (!monster.invincible) {
				int hurtSound;
				int deathSound;

				switch (monster.type) {
				case type_zombie:
					hurtSound = new java.util.Random().nextInt(2) + 4; // 4 or 5
					deathSound = 6;
					break;
				case type_skeleton:
					hurtSound = new java.util.Random().nextInt(2) + 12; // 12 or 13
					deathSound = 14;
					break;
				case type_boss:
					hurtSound = new java.util.Random().nextInt(2) + 20;
					deathSound = 22;
					break;
				default:
					hurtSound = 2; // Fallback sound
					deathSound = -1;
				}

				gp.playSE(hurtSound);

				if (knockBackPower > 0) {
					setKnockBack(monster, attacker, knockBackPower);
				}

				if (gp.monster[gp.currentMap][i].offBalance == true) {
					attack *= 5;
				}

				int damage = attack - monster.defense;
				if (damage < 0)
					damage = 0;

				monster.life -= damage;
				monster.invincible = true;
				monster.damageReaction();

				if (monster.life <= 0) {
					monster.die = true;
					if (deathSound != -1)
						gp.playSE(deathSound);
				}
			}
		}
	}

	public void damageInteractiveTile(int i) {

		if (i != -1 && gp.iTile[gp.currentMap][i].destructible == true // FIXED
				&& gp.iTile[gp.currentMap][i].isCorrectItem(this) == true // FIXED
				&& gp.iTile[gp.currentMap][i].invincible == false) { // FIXED

//			gp.iTile[i].playSE();
			gp.iTile[gp.currentMap][i].life--; // FIXED
			gp.iTile[gp.currentMap][i].invincible = true; // FIXED

			// Generate particle
			generateParticle(gp.iTile[gp.currentMap][i], gp.iTile[gp.currentMap][i]); // FIXED

			if (gp.iTile[gp.currentMap][i].life == 0) { // FIXED
				gp.iTile[gp.currentMap][i] = gp.iTile[gp.currentMap][i].getDestroyedForm(); // FIXED
			}
		}
	}

	public void damageProjectile(int i) {

		if (i != -1) {
			Entity pro = gp.projectile[gp.currentMap][i];
			pro.alive = false;
			generateParticle(pro, pro);
		}
	}

	public void selectItem() {
		int itemIndex = gp.ui.getItemIndexOnSlot(gp.ui.playerSlotCol, gp.ui.playerSlotRow);

		if (itemIndex < inventory.size()) {
			Entity selectedItem = inventory.get(itemIndex);

			if (selectedItem.type == type_dsword || selectedItem.type == type_wsword || selectedItem.type == type_axe) {
				currentWeapon = selectedItem;
				attack = getAttack();
				getAttackImage(); // Call this only when a weapon is actually equipped
			}

			if (selectedItem.type == type_firecharge) {
				currentBall = selectedItem;
			}

			if (selectedItem.type == type_shield) {
				currentShield = selectedItem;
				defense = getDefense();
			}

			if (selectedItem.type == type_light) {
				if (currentLight == selectedItem) {
					currentLight = null;
				} else {
					currentLight = selectedItem;
				}
				lightUpdated = true;
			}

			if (selectedItem.type == type_consumable) {
				if (selectedItem.use(this) == true) {
					if (selectedItem.amount > 1) {
						selectedItem.amount--;
					} else {
						inventory.remove(itemIndex);
					}
				}
			}
		}
	}

	public int searchItemInInventory(String itemName) {

		int itemIndex = 999;

		for (int i = 0; i < inventory.size(); i++) {
			if (inventory.get(i).name.equals(itemName)) {
				itemIndex = i;
				break;
			}
		}
		return itemIndex;
	}

	public boolean canObtainItem(Entity item) {

		boolean canObtain = false;

		if (item.type == type_portal) {
			return false;
		}
		// check if item is stackable
		if (item.stackable == true) {

			int index = searchItemInInventory(item.name);
			// if have the same item that can be stack, then stack it
			if (index != 999) {
				inventory.get(index).amount++;
				canObtain = true;
			} else { // new item so need to check vacancy
				if (inventory.size() != maxinventorySize) {
					inventory.add(item);
					canObtain = true;
				}
			}
		} else { // not stackable
			if (inventory.size() != maxinventorySize) {
				inventory.add(item);
				canObtain = true;
			}
		}
		return canObtain;
	}

	@Override
	public void draw(Graphics2D g2) {

		int tempScreenX = screenX;
		int tempScreenY = screenY;
		BufferedImage image = null;
		
		switch (direction) {
		case "up":
			if (attacking == false) {
				if (spriteNum == 1) {
					image = up1;
				}
				if (spriteNum == 2) {
					image = up2;
				}
				if (spriteNum == 3) {
					image = upStand;
				}
			}
			if (attacking == true) {
				if (currentWeapon != null) {
					tempScreenY = screenY - gp.tileSize; // adjust y when attack upward
					if (spriteNum == 1) {
						image = aUp1;
					}
					if (spriteNum == 2) {
						image = aUp2;
					}
				}
			}
			if (guarding == true) {
				if (currentShield != null) {
					image = gUp;
				}
			}
			break;
		case "down":
			if (attacking == false) {
				if (spriteNum == 1) {
					image = down1;
				}
				if (spriteNum == 2) {
					image = down2;
				}
				if (spriteNum == 3) {
					image = downStand;
				}
			}
			if (attacking == true) {
				if (spriteNum == 1) {
					image = aDown1;
				}
				if (spriteNum == 2) {
					image = aDown2;
				}
			}
			if (guarding == true) {
				if (currentShield != null) {
					image = gDown;
				}
			}
			break;
		case "left":
			if (attacking == false) {
				if (spriteNum == 1) {
					image = left1;
				}
				if (spriteNum == 2) {
					image = left2;
				}
				if (spriteNum == 3) {
					image = leftStand;
				}
			}
			if (attacking == true) {
				tempScreenX = screenX - gp.tileSize;
				if (spriteNum == 1) {
					image = aLeft1;
				}
				if (spriteNum == 2) {
					image = aLeft2;
				}
			}
			if (guarding == true) {
				if (currentShield != null) {
					image = gLeft;
				}
			}
			break;
		case "right":
			if (attacking == false) {
				if (spriteNum == 1) {
					image = right1;
				}
				if (spriteNum == 2) {
					image = right2;
				}
				if (spriteNum == 3) {
					image = rightStand;
				}
			}
			if (attacking == true) {
				if (spriteNum == 1) {
					image = aRight1;
				}
				if (spriteNum == 2) {
					image = aRight2;
				}
			}
			if (guarding == true) {
				if (currentShield != null) {
					image = gRight;
				}
			}
			break;
		}

		// Image transparent when receive damage
		if (transparent == true) {
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
		}

		if (drawing == true) {
			g2.drawImage(image, tempScreenX, tempScreenY, null);
		}
		// Reset Alpha
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
	}
}