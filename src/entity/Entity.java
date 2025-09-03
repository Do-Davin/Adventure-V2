package entity;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Arc2D.Float;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

import ai.Node;
import main.GamePanel;
import main.UtilityTool;

public class Entity {

	protected GamePanel gp;

	public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2, upStand, downStand, leftStand,
			rightStand;
	public BufferedImage aUp1, aUp2, aDown1, aDown2, aRight1, aRight2, aLeft1, aLeft2, gUp, gDown, gLeft, gRight;
	public BufferedImage image, image2, image3;
	public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
	public Rectangle attackArea = new Rectangle(0, 0, 0, 0);
	public int solidAreaDefaultX, solidAreaDefaultY; // For Object interaction attributes
	public boolean collisionOn = false;
	boolean attackSoundPlayed = false;
	public String dialogues[] = new String[50];
	public Entity attacker;

	// STATE
	public int worldX, worldY;
	public String direction = "down";
	public int spriteNum = 1;
	public int dialogueIndex = 0;
	public boolean invincible = false;
	public boolean collision = false;
	public boolean attacking = false;
	public boolean alive = true;
	public boolean die = false;
	public boolean hpBarOn = false;
	public boolean onPath = false;
	public boolean knockBack = false;
	public String knockBackDirection;
	public boolean guarding = false;
	public boolean transparent = false;
	public boolean offBalance = false;
	public boolean inRange = false;
	public boolean boss;
	public boolean sleep = false;
	public boolean temp = false;

	public boolean drawing = true;

	// COUNTER
	public int actionLockCounter = 0;
	public int invinCounter = 0;
	public int spriteCounter = 0;
	int dieCounter;
	public int hpBarCounter = 0;
	public int shotCounter;
	protected int knockBackCounter = 0;
	public int guardCounter = 0;
	int offBalanceCounter = 0;
	public int pathRecalcCounter = 0;
	public int PATH_RECALC_INTERVAL = 60;

	// CHARACTER ATTRIBUTES
	public String name;
	public int defaultSpeed;
	public int maxLife;
	public int mana;
	public int maxMana;
	public int life;
	public int speed;
	public int level;
	public int strength;
	public int dexterity;
	public int attack;
	public int defense;
	public int exp;
	public int nextLevelExp;
	public int coin;
	public int motion1_duration;
	public int motion2_duration;
	public Entity currentWeapon;
	public Entity currentShield;
	public Entity currentBall;
	public Entity currentLight;
	public Projectile pro;

	// Item Attributes
	public ArrayList<Entity> inventory = new ArrayList<>();
	public final int maxinventorySize = 21;
	public int attackValue;
	public int defenseValue;
	public String description = "";
	public int useCost;
	public int price;
	public int knockBackPower = 0;
	public boolean stackable = false;
	public int amount = 1;
	public int lightRadius;

	public int type; // 0 = player , 1 = npc, 2 = monster
	public final int type_player = 0;
	public final int type_npc = 1;
	public final int type_zombie = 2;
	public final int type_dsword = 3;
	public final int type_wsword = 4;
	public final int type_shield = 5;
	public final int type_consumable = 6;
	public final int type_firecharge = 7;
	public final int type_skeleton = 8;
	public final int type_axe = 9;
	public final int type_pickup = 10;
	public final int type_light = 11;
	public final int type_portal = 12;
	public final int type_obstacle = 13;
	public final int type_boss = 14;

	public Entity(GamePanel gp) {
		this.gp = gp;
	}

	public void interact() {

	}

	public int getScreenX() {
		int screenX = worldX - gp.player.worldX + gp.player.screenX;
		return screenX;
	}

	public int getScreenY() {
		int screenY = worldY - gp.player.worldY + gp.player.screenY;
		return screenY;
	}

	public int getLeftX() {
		return worldX + solidArea.x;
	}

	public int getRightX() {
		return worldX + solidArea.x + solidArea.width;
	}

	public int getTopY() {
		return worldY + solidArea.y;
	}

	public int getBottomY() {
		return worldY + solidArea.y + solidArea.height;
	}

	public int getCol() {
		return (worldX + solidArea.x) / gp.tileSize;
	}

	public int getRow() {
		return (worldY + solidArea.y) / gp.tileSize;
	}

	public int getXdistance(Entity target) {
		int xDistance = Math.abs(getCenterX() - target.getCenterX());
		return xDistance;
	}

	public int getYdistance(Entity target) {
		int yDistance = Math.abs(getCenterY() - target.getCenterY());
		return yDistance;
	}

	public int getTileDistance(Entity target) {
		int tileDistance = (getXdistance(target) + getYdistance(target)) / gp.tileSize;
		return tileDistance;
	}

	public int getGoalCol(Entity target) {
		int goalCol = (target.worldX + target.solidArea.x) / gp.tileSize;
		return goalCol;
	}

	public int getGoalRow(Entity target) {
		int goalRow = (target.worldY + target.solidArea.y) / gp.tileSize;
		return goalRow;
	}

	public int getCenterX() {
		int centerX = worldX + left1.getWidth() / 2;
		return centerX;
	}

	public int getCenterY() {
		int centerY = worldY + up1.getHeight() / 2;
		return centerY;
	}

	public void setAction() {
	}

	public void damageReaction() {

	}

	public void speak() {
		// Reset dialogue state before starting new dialogue
		gp.ui.resetDialogueState();

		if (dialogues[dialogueIndex] == null) {
			dialogueIndex = 0;
		}

		// Set the current dialogue text
		gp.ui.currentDialogue = dialogues[dialogueIndex];
		dialogueIndex++;

		// NPC will face to us during we're talking to each other
		switch (gp.player.direction) {
		case "up":
			direction = "down";
			break;
		case "down":
			direction = "up";
			break;
		case "right":
			direction = "left";
			break;
		case "left":
			direction = "right";
			break;
		}
	}

	public boolean use(Entity entity) {

		return false;
	}

	public Color getParticleColor() {
		Color color = null;
		return color;
	}

	public int getParticleSize() {
		int size = 0;
		return size;
	}

	public int getParticleSpeed() {
		int speed = 0;
		return speed;
	}

	public int getParticleMaxLife() {
		int maxLife = 0;
		return maxLife;
	}

	public void generateParticle(Entity generator, Entity target) {

		Color color = generator.getParticleColor();
		int size = generator.getParticleSize();
		int speed = generator.getParticleSpeed();
		int maxLife = generator.getParticleMaxLife();

		Particle p1 = new Particle(gp, target, color, size, speed, maxLife, -2, -1);
		Particle p2 = new Particle(gp, target, color, size, speed, maxLife, 2, -1);
		Particle p3 = new Particle(gp, target, color, size, speed, maxLife, -2, 1);
		Particle p4 = new Particle(gp, target, color, size, speed, maxLife, 2, 1);
		gp.particleList.add(p1);
		gp.particleList.add(p2);
		gp.particleList.add(p3);
		gp.particleList.add(p4);
	}

	public void checkCollision() {

		collisionOn = false;
		gp.cChecker.checkTile(this);
		gp.cChecker.checkObject(this, false);
		gp.cChecker.checkEntity(this, gp.monster);
		gp.cChecker.checkEntity(this, gp.npc);
		gp.cChecker.checkEntity(this, gp.iTile);
		boolean contactPlayer = gp.cChecker.checkPlayer(this);

		if (this.type == type_zombie && contactPlayer == true) {
			damagePlayer(attack);
		}
	}

	public void checkDrop() {

	}

	public void dropItem(Entity droppedItem) {

		for (int i = 0; i < gp.obj[1].length; i++) {
			if (gp.obj[gp.currentMap][i] == null) {
				gp.obj[gp.currentMap][i] = droppedItem;
				gp.obj[gp.currentMap][i].worldX = worldX;
				gp.obj[gp.currentMap][i].worldY = worldY;
				break;
			}
		}
	}

	public void checkShootOrNot(int rate, int shotInterval) {
		int i = new Random().nextInt(rate);
		if (i == 0 && pro.alive == false && shotCounter == shotInterval) {

			pro.set(worldX, worldY, direction, true, this);

			for (int ii = 0; ii < gp.projectile[1].length; ii++) {
				if (gp.projectile[gp.currentMap][ii] == null) {
					gp.projectile[gp.currentMap][ii] = pro;
					break;
				}
			}
		}
	}

	public void getRandomDirection(int interval) {

		actionLockCounter++;

		if (actionLockCounter > interval) {
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

	public void checkStartChasingOrNot(Entity target, int distance, int rate) {
		if (getTileDistance(target) < distance) {
			int i = new Random().nextInt(rate);
			if (i == 0) {
				onPath = true;
			}
		}
	}

	public void checkStopChasingOrNot(Entity target, int distance, int rate) {
		if (getTileDistance(target) > distance) {
			int i = new Random().nextInt(rate);
			if (i == 0) {
				onPath = false;
			}
		}
	}

	public void checkAttackOrNot(int rate, int straight, int horizontal) {

		boolean targetInRange = false;
		int xDis = getXdistance(gp.player);
		int yDis = getYdistance(gp.player);

		switch (direction) {
		case "up":
			if (gp.player.getCenterY() < getCenterY() && yDis < straight && xDis < horizontal) {
				targetInRange = true;
			}
			break;
		case "down":
			if (gp.player.getCenterY() > getCenterY() && yDis < straight && xDis < horizontal) {
				targetInRange = true;
			}
			break;

		case "left":
			if (gp.player.getCenterX() < getCenterX() && xDis < straight && yDis < horizontal) {
				targetInRange = true;
			}
			break;

		case "right":
			if (gp.player.getCenterX() > getCenterX() && xDis < straight && yDis < horizontal) {
				targetInRange = true;
			}
			break;
		}

		if (targetInRange == true) {
			// check if it initiates an attack
			int i = new Random().nextInt(rate);
			if (i == 0) {
				attacking = true;
				spriteNum = 1;
				spriteCounter = 0;
				shotCounter = 0;
			}
		}
	}

	public void moveTowardPlayer(int interval) {

		actionLockCounter++;
		if (actionLockCounter > interval) {

			if (getXdistance(gp.player) > getYdistance(gp.player)) {
				if (gp.player.getCenterX() < getCenterX()) {

					direction = "left";
				} else {
					direction = "right";
				}

			} else if (getXdistance(gp.player) < getYdistance(gp.player)) {
				if (gp.player.getCenterY() < getCenterY()) {
					direction = "up";
				} else {
					direction = "down";
				}
			}
			actionLockCounter = 0;
		}
	}

	public String getOppositeDirection(String direction) {

		String oppositeDir = " ";

		switch (direction) {

		case "up":
			oppositeDir = "down";
			break;
		case "down":
			oppositeDir = "up";
			break;
		case "left":
			oppositeDir = "right";
			break;
		case "right":
			oppositeDir = "left";
			break;
		}
		return oppositeDir;
	}

	public void attacking() {
	    if (spriteNum == 2 && !attackSoundPlayed) {
	        if (this.name != null && this.name.equals("Ice Golem")) {
	            gp.playSE(23);
	        } else {
	            gp.playSE(3);
	        }
	        attackSoundPlayed = true;
	    }

	    spriteCounter++;

	    if (spriteCounter <= motion1_duration) {
	        spriteNum = 1;
	    }
	    if (spriteCounter > motion1_duration && spriteCounter <= motion2_duration) {
	        spriteNum = 2;

	        // Save the current WorldX,Y, solidArea
	        int currentX = worldX;
	        int currentY = worldY;
	        int solidAreaWidth = solidArea.width;
	        int solidAreaHeight = solidArea.height;

	        // Adjust players world X,Y for attack Area
	        switch (direction) {
	        case "up":
	            worldY -= attackArea.height;
	            break;
	        case "down":
	            worldY += attackArea.height; // FIXED: was attackArea.width
	            break;
	        case "left":
	            worldX -= attackArea.width;
	            break;
	        case "right":
	            worldX += attackArea.width; // FIXED: was attackArea.height
	            break;
	        }
	        
	        // attack area become solid area
	        solidArea.width = attackArea.width;
	        solidArea.height = attackArea.height;

	        if (type == type_skeleton || type == type_zombie || type == type_boss) {
	            if (gp.cChecker.checkPlayer(this) == true) {
	                damagePlayer(attack);
	            }
	        } else {
	            // Check monster collision with the attack area
	            int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
	            gp.player.damageMonster(monsterIndex, this, attack, currentWeapon.knockBackPower);

	            int iTileIndex = gp.cChecker.checkEntity(this, gp.iTile);
	            gp.player.damageInteractiveTile(iTileIndex);

	            int proIndex = gp.cChecker.checkEntity(this, gp.projectile);
	            gp.player.damageProjectile(proIndex);
	        }

	        worldX = currentX;
	        worldY = currentY;
	        solidArea.width = solidAreaWidth;
	        solidArea.height = solidAreaHeight; // FIXED: was attackArea.height
	    }
	    if (spriteCounter > motion2_duration) {
	        spriteNum = 1;
	        spriteCounter = 0;
	        attacking = false;
	        attackSoundPlayed = false;
	    }
	}

	// In Entity.java - replace the entire update() method with this:
	public void update() {
	    if(sleep == false) {
	        if(knockBack == true) {
	            // Handle knockback movement
	            checkCollision();
	            
	            if(collisionOn == true) {
	                // If we hit something during knockback, stop immediately
	                knockBackCounter = 0;
	                knockBack = false;
	                speed = defaultSpeed;
	                collisionOn = false; // IMPORTANT: Reset collision state
	            }
	            else {
	                // Move during knockback
	                switch(knockBackDirection) {
	                    case "up": worldY -= speed; break;
	                    case "down": worldY += speed; break;
	                    case "left": worldX -= speed; break;
	                    case "right": worldX += speed; break;
	                }
	            }
	            
	            knockBackCounter++;
	            if(knockBackCounter >= 10) { // Fixed: should be >= not ==
	                knockBackCounter = 0;
	                knockBack = false;
	                speed = defaultSpeed;
	                collisionOn = false; // Reset collision after knockback
	            }
	        }
	        else if(attacking == true) {
	            attacking();
	        }
	        else {
	            setAction();
	            checkCollision();

	            if (!attacking && !collisionOn && !direction.equals("stand")) {
	                switch (direction) {
	                    case "up": worldY -= speed; break;
	                    case "down": worldY += speed; break;
	                    case "left": worldX -= speed; break;
	                    case "right": worldX += speed; break;
	                }
	            }
	            
	            // Animate only if moving
	            if (!attacking) {
	                if (!direction.equals("stand") && !collisionOn) {
	                    spriteCounter++;
	                    if (spriteCounter > 10) {
	                        if (spriteNum == 1) spriteNum = 3;
	                        else if (spriteNum == 3) spriteNum = 2;
	                        else if (spriteNum == 2) spriteNum = 1;
	                        spriteCounter = 0;
	                    }
	                } else {
	                    // Set to standing frame
	                    spriteNum = 3;
	                }
	            }
	        }
	        
	        if (invincible == true) {
	            invinCounter++;
	            if (invinCounter > 40) {
	                invincible = false;
	                invinCounter = 0;
	            }
	        }
	        if (shotCounter < 30) {
	            shotCounter++;
	        }
	        
	        if(offBalance == true) {
	            offBalanceCounter++;
	            if(offBalanceCounter > 60) {
	                offBalance = false;
	                offBalanceCounter = 0;
	            }
	        }
	    }
	}

	public void damagePlayer(int attack) {
		if (!gp.player.invincible) {

			int damage = attack - gp.player.defense;

			// get opposite direction
			String canGuard = getOppositeDirection(direction);
			if (gp.player.guarding == true && gp.player.direction.equals(canGuard)) {

				// parry
				if (gp.player.guardCounter < 10) {
					damage = 0;
					gp.playSE(24);
					setKnockBack(this, gp.player, knockBackPower);
					offBalance = true;
					spriteCounter = -60;
				} else {
					damage = 0;
					gp.playSE(24);
				}

			} else {
				gp.playSE(2);
				if (damage < 0)
					damage = 0;// Ensure minimum damage
			}

			if (damage != 0) {
				gp.player.transparent = true;
				setKnockBack(gp.player, this, knockBackPower);
			}
			gp.player.life -= damage;
			gp.player.invincible = true;
		}
	}

	public void setKnockBack(Entity target, Entity attacker, int knockBackPower) {

		this.attacker = attacker;
		target.knockBackDirection = attacker.direction;
		target.speed += knockBackPower;
		target.knockBack = true;
	}

	public boolean inCamera() {
		boolean inCamera = false;
		if (worldX + gp.tileSize * 5 > gp.player.worldX - gp.player.screenX
				&& worldX - gp.tileSize < gp.player.worldX + gp.player.screenX
				&& worldY + gp.tileSize * 5 > gp.player.worldY - gp.player.screenY
				&& worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {

			inCamera = true;
		}
		return inCamera;
	}

	public void draw(Graphics2D g2) {
	    if (!alive)
	        return;

	    BufferedImage image = null;

	    if (inCamera() == true) {

	        int tempScreenX = getScreenX();
	        int tempScreenY = getScreenY();

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
	                // For large entities like Ice Golem, adjust positioning based on entity size
	                if (name != null && name.equals("Ice Golem")) {
	                    // Ice Golem is 5x5 tiles, so we need different positioning
	                	tempScreenY = getScreenY() - (aUp1.getHeight() - gp.tileSize) / 2; // Adjust for attack sprite height difference
	                } else {
	                    // Normal entities (1x1 tile)
	                    tempScreenY = getScreenY() - (aUp1.getHeight() - gp.tileSize) / 2;
	                }
	                if (spriteNum == 1) {
	                    image = aUp1;
	                }
	                if (spriteNum == 2) {
	                    image = aUp2;
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
	                // For Ice Golem, keep the Y position the same since attack sprite extends downward
	                if (name != null && name.equals("Ice Golem")) {
	                    // No Y adjustment needed for downward attack
	                    tempScreenY = getScreenY();
	                } else {
	                    // Normal entities
	                    tempScreenY = getScreenY() - (aDown1.getHeight() - gp.tileSize) / 2;
	                }
	                if (spriteNum == 1) {
	                    image = aDown1;
	                }
	                if (spriteNum == 2) {
	                    image = aDown2;
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
	                // For Ice Golem, adjust X positioning
	                if (name != null && name.equals("Ice Golem")) {
	                    // Ice Golem is 5x5 tiles, attack sprite extends leftward
	                    tempScreenX = getScreenX() - gp.tileSize * 5; // Adjust for 5-tile width attack sprite
	                } else {
	                    // Normal entities
	                    tempScreenX = getScreenX() - (aLeft1.getWidth() - gp.tileSize) / 2;
	                }
	                if (spriteNum == 1) {
	                    image = aLeft1;
	                }
	                if (spriteNum == 2) {
	                    image = aLeft2;
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
	                // For Ice Golem, keep X position the same since attack sprite extends rightward
	                if (name != null && name.equals("Ice Golem")) {
	                    // No X adjustment needed for rightward attack
	                    tempScreenX = getScreenX();
	                } else {
	                    // Normal entities
	                    tempScreenX = getScreenX() - (aRight1.getWidth() - gp.tileSize) / 2;
	                }
	                if (spriteNum == 1) {
	                    image = aRight1;
	                }
	                if (spriteNum == 2) {
	                    image = aRight2;
	                }
	            }
	            break;
	        }

	        // Image transparent when receive damage
	        if (invincible == true) {
	            hpBarOn = true;
	            hpBarCounter = 0;
	            changeAlpha(g2, 0.5f);
	        }

	        if (die == true) {
	            deadAnimation(g2);
	        }

	        g2.drawImage(image, tempScreenX, tempScreenY, null);

	        // Reset Alpha
	        changeAlpha(g2, 1f);
	    }
	}

	public void deadAnimation(Graphics2D g2) {
		dieCounter++;

		int i = 5;
		if (dieCounter <= i) {
			changeAlpha(g2, 0f);
		}
		if (dieCounter > i && dieCounter <= i * 2) {
			changeAlpha(g2, 1f);
		}
		if (dieCounter > i * 2 && dieCounter <= i * 3) {
			changeAlpha(g2, 0f);
		}
		if (dieCounter > i * 3 && dieCounter <= i * 4) {
			changeAlpha(g2, 1f);
		}
		if (dieCounter > i * 4 && dieCounter <= i * 5) {
			changeAlpha(g2, 0f);
		}
		if (dieCounter > i * 5 && dieCounter <= i * 6) {
			changeAlpha(g2, 1f);
		}
		if (dieCounter > i * 6 && dieCounter <= i * 7) {
			changeAlpha(g2, 0f);
		}
		if (dieCounter > i * 7 && dieCounter <= i * 8) {
			changeAlpha(g2, 1f);
		}

		if (dieCounter > i * 8) {
			alive = false;
		}
	}

	public void changeAlpha(Graphics2D g2, float value) {
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, value));
	}

	public BufferedImage setup(String imagePath, int width, int height) {

		UtilityTool uTool = new UtilityTool();
		BufferedImage image = null;

		try {
			image = ImageIO.read(getClass().getResourceAsStream(imagePath + ".png"));
			image = uTool.scaleImage(image, width, height);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return image;
	}

	public void searchPath(int goalCol, int goalRow) {
		int startCol = (worldX + solidArea.x) / gp.tileSize;
		int startRow = (worldY + solidArea.y) / gp.tileSize;

		gp.pFinder.setNodes(startCol, startRow, goalCol, goalRow, this);

		if (gp.pFinder.search()) {
			// Only proceed if path was found successfully
			if (!gp.pFinder.pathList.isEmpty()) {
				// Follow exact path coordinates
				int nextCol = gp.pFinder.pathList.get(0).col;
				int nextRow = gp.pFinder.pathList.get(0).row;

				// Calculate exact target position
				int nextX = nextCol * gp.tileSize;
				int nextY = nextRow * gp.tileSize;

				// Check if we've reached the current path node
				if (Math.abs(worldX - nextX) < speed && Math.abs(worldY - nextY) < speed) {
					// Remove reached node from path
					gp.pFinder.pathList.remove(0);
				}

				// Direct movement toward exact path coordinates
				if (worldX < nextX) {
					direction = "right";
				} else if (worldX > nextX) {
					direction = "left";
				} else if (worldY < nextY) {
					direction = "down";
				} else if (worldY > nextY) {
					direction = "up";
				}

				// If path is empty, we've reached the goal
				if (gp.pFinder.pathList.isEmpty()) {
					onPath = false;
				}
			} else {
				// Path found but empty (shouldn't happen normally)
				onPath = false;
			}
		} else {
			// No path found to target
			onPath = false;
			// Optional: Add some fallback behavior here
			getRandomDirection(120); // Wander randomly if can't reach player
		}
	}

	public void followPath() {
		if (gp.pFinder.pathList == null || gp.pFinder.pathList.isEmpty()) {
			onPath = false;
			return;
		}

		// Get the next node in the path
		Node nextNode = gp.pFinder.pathList.get(0);

		// Calculate the center of the next node in pixel coordinates
		int nextNodeCenterX = nextNode.col * gp.tileSize + gp.tileSize / 2;
		int nextNodeCenterY = nextNode.row * gp.tileSize + gp.tileSize / 2;

		// Calculate the center of the entity in pixel coordinates
		int entityCenterX = worldX + solidArea.x + solidArea.width / 2;
		int entityCenterY = worldY + solidArea.y + solidArea.height / 2;

		// Calculate distances
		int distX = nextNodeCenterX - entityCenterX;
		int distY = nextNodeCenterY - entityCenterY;

		// Determine direction based on which axis has greater distance
		if (Math.abs(distX) > Math.abs(distY)) {
			// Move horizontally
			if (distX > 0) {
				direction = "right";
			} else {
				direction = "left";
			}
		} else {
			// Move vertically
			if (distY > 0) {
				direction = "down";
			} else {
				direction = "up";
			}
		}

		// Check if we've reached the current node (generous tolerance)
		int tolerance = gp.tileSize / 2;
		if (Math.abs(distX) < tolerance && Math.abs(distY) < tolerance) {
			// Remove this node from the path
			gp.pFinder.pathList.remove(0);

			// If path is now empty, stop following
			if (gp.pFinder.pathList.isEmpty()) {
				onPath = false;
			}
		}
	}

	public int getDetected(Entity user, Entity target[][], String targetName) {

		int index = 999;

		// Check the surrounding object
		int nextWorldX = user.getLeftX();
		int nextWorldY = user.getTopY();

		switch (user.direction) {
		case "up":
			nextWorldY = user.getTopY() - 1;
			break;
		case "down":
			nextWorldY = user.getBottomY() + 1;
			break;
		case "left":
			nextWorldX = user.getLeftX() - 1;
			break;
		case "right":
			nextWorldX = user.getRightX() + 1;
			break;
		}

		int col = nextWorldX / gp.tileSize;
		int row = nextWorldY / gp.tileSize;

		for (int i = 0; i < target[1].length; i++) {
			if (target[gp.currentMap][i] != null) {
				if (target[gp.currentMap][i].getCol() == col && target[gp.currentMap][i].getRow() == row
						&& target[gp.currentMap][i].name.equals(targetName)) {

					index = i;
					break;
				}
			}
		}
		return index;
	}

	public void die() {
		alive = false;
		die = true;
		// Clear pathfinding resources if this entity was using them
		if (onPath && gp != null && gp.pFinder != null) {
			gp.pFinder.clearPath();
		}
	}
	
	public boolean isValidPosition(int col, int row) {
	    return col >= 0 && col < gp.maxWorldCol && row >= 0 && row < gp.maxWorldRow;
	}

}