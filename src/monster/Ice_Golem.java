package monster;

import java.util.Random;

import entity.Entity;
import main.GamePanel;
import main.Progress;
import object.OBJ_Door;
import object.OBJ_Emerald;
import object.OBJ_Fireball;
import object.OBJ_Firecharge;
import object.OBJ_dunKey;
import object.OBJ_healingP;

public class Ice_Golem extends Entity {

	GamePanel gp;
	public static final String monName = "Ice Golem";

	public Ice_Golem(GamePanel gp) {
		super(gp);
		this.gp = gp;

		type = type_boss;
		name = monName;
		defaultSpeed = 1;
		speed = defaultSpeed;
		maxLife = 50;
		life = maxLife;
		attack = 6;
		boss = true;
		sleep = true;
		
		knockBackPower = 15;

		int size = gp.tileSize * 5;
		solidArea.x = 48; // X-offset 
		solidArea.y = 48; // Y-offset
		solidArea.width = size - 48 * 2; // Hitbox width
		solidArea.height = size - 48; // Hitbox height

		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
		attackArea.width = 170;
		attackArea.height = 170;
		motion1_duration = 25;
		motion2_duration = 50;
		getImage();
		getAttackImage();
		setDialogue();
	}

	public void getImage() {
		int i = 5;

		if(inRange == false) {
			up1 = setup("/monster/Ice_golem_up1", gp.tileSize * i, gp.tileSize * i);
			up2 = setup("/monster/Ice_golem_up2", gp.tileSize * i, gp.tileSize * i);
			down1 = setup("/monster/Ice_golem_down1", gp.tileSize * i, gp.tileSize * i);
			down2 = setup("/monster/Ice_golem_down2", gp.tileSize * i, gp.tileSize * i);
			left1 = setup("/monster/Ice_golem_left1", gp.tileSize * i, gp.tileSize * i);
			left2 = setup("/monster/Ice_golem_left2", gp.tileSize * i, gp.tileSize * i);
			right1 = setup("/monster/Ice_golem_right1", gp.tileSize * i, gp.tileSize * i);
			right2 = setup("/monster/Ice_golem_right2", gp.tileSize * i, gp.tileSize * i);
			upStand = setup("/monster/Ice_golem_upStand", gp.tileSize * i, gp.tileSize * i);
			downStand = setup("/monster/Ice_golem_downStand", gp.tileSize * i, gp.tileSize * i);
			leftStand = setup("/monster/Ice_golem_leftStand", gp.tileSize * i, gp.tileSize * i);
			rightStand = setup("/monster/Ice_golem_rightStand", gp.tileSize * i, gp.tileSize * i);
		}
		if(inRange == true) {
			up1 = setup("/monster/Ice_golem_up1", gp.tileSize * i, gp.tileSize * i);
			up2 = setup("/monster/Ice_golem_up2", gp.tileSize * i, gp.tileSize * i);
			down1 = setup("/monster/Ice_golem_down1", gp.tileSize * i, gp.tileSize * i);
			down2 = setup("/monster/Ice_golem_down2", gp.tileSize * i, gp.tileSize * i);
			left1 = setup("/monster/Ice_golem_left1", gp.tileSize * i, gp.tileSize * i);
			left2 = setup("/monster/Ice_golem_left2", gp.tileSize * i, gp.tileSize * i);
			right1 = setup("/monster/Ice_golem_right1", gp.tileSize * i, gp.tileSize * i);
			right2 = setup("/monster/Ice_golem_right2", gp.tileSize * i, gp.tileSize * i);
			upStand = setup("/monster/Ice_golem_upStand", gp.tileSize * i, gp.tileSize * i);
			downStand = setup("/monster/Ice_golem_downStand", gp.tileSize * i, gp.tileSize * i);
			leftStand = setup("/monster/Ice_golem_leftStand", gp.tileSize * i, gp.tileSize * i);
			rightStand = setup("/monster/Ice_golem_rightStand", gp.tileSize * i, gp.tileSize * i);
		}
		

	}

	public void getAttackImage() {

		int i = 5;
		if(inRange == false) {
			aUp1 = setup("/monster/Ice_golem_au1", gp.tileSize * i, gp.tileSize * i * 2); // 16 x 32
			aUp2 = setup("/monster/Ice_golem_au2", gp.tileSize * i, gp.tileSize * i * 2);
			aDown1 = setup("/monster/Ice_golem_ad1", gp.tileSize * i, gp.tileSize * i * 2);
			aDown2 = setup("/monster/Ice_golem_ad2", gp.tileSize * i, gp.tileSize * i * 2);
			aRight1 = setup("/monster/Ice_golem_ar1", gp.tileSize * i * 2, gp.tileSize * i);
			aRight2 = setup("/monster/Ice_golem_ar2", gp.tileSize * i * 2, gp.tileSize * i);
			aLeft1 = setup("/monster/Ice_golem_al1", gp.tileSize * i * 2, gp.tileSize * i);
			aLeft2 = setup("/monster/Ice_golem_al2", gp.tileSize * i * 2, gp.tileSize * i);
		}
		if(inRange == true) {
			aUp1 = setup("/monster/Ice_golem_au1", gp.tileSize * i, gp.tileSize * i * 2); // 16 x 32
			aUp2 = setup("/monster/Ice_golem_au2", gp.tileSize * i, gp.tileSize * i * 2);
			aDown1 = setup("/monster/Ice_golem_ad1", gp.tileSize * i, gp.tileSize * i * 2);
			aDown2 = setup("/monster/Ice_golem_ad2", gp.tileSize * i, gp.tileSize * i * 2);
			aRight1 = setup("/monster/Ice_golem_ar1", gp.tileSize * i * 2, gp.tileSize * i);
			aRight2 = setup("/monster/Ice_golem_ar2", gp.tileSize * i * 2, gp.tileSize * i);
			aLeft1 = setup("/monster/Ice_golem_al1", gp.tileSize * i * 2, gp.tileSize * i);
			aLeft2 = setup("/monster/Ice_golem_al2", gp.tileSize * i * 2, gp.tileSize * i);
		}
		
	}

	public void setAction() {

		if(inRange == false && life < maxLife/2) {
			inRange = true;
			getImage();
			getAttackImage();
			defaultSpeed++;
			speed = defaultSpeed;
			attack *= 2;
		}
		if (getTileDistance(gp.player) < 10) {

			moveTowardPlayer(60);
		} else {
			getRandomDirection(120);
		}

		if (attacking == false) {
			checkAttackOrNot(30, gp.tileSize * 10, gp.tileSize*5);
		}

	}

	public void damageReaction() {

		actionLockCounter = 0;
	}

	// Add this method to reset the boss state
	public void resetBoss() {
	    life = maxLife;
	    alive = true;
	    die = false;
	    sleep = true;
	    inRange = false;
	    attacking = false;
	    onPath = false;
	    knockBack = false;
	    invincible = false;
	    
	    // Reset images to non-enraged state
	    getImage();
	    getAttackImage();
	    
	    // Reset speed and attack
	    defaultSpeed = 1;
	    speed = defaultSpeed;
	    attack = 10;
	}

	public void checkDrop() {
	    // Only proceed if the boss was actually defeated by the player
	    if (life <= 0) {
	        gp.bossBattleOn = false;
	        Progress.GolemDefeated = true;
	        gp.stopMusic();
	     // FIX: Play dungeon music after boss defeat, not overworld
	        if (gp.currentArea == 52) {
	            gp.playMusic(27); // Dungeon music
	        } else {
	            gp.playMusic(0); // Fallback to overworld
	        }
	        
	        // remove door
	        for(int i=0;i<gp.obj[1].length;i++) {
	            if(gp.obj[gp.currentMap][i] != null && gp.obj[gp.currentMap][i].name.equals(OBJ_Door.objName) && gp.obj[gp.currentMap][i].temp) {
	                gp.playSE(16);
	                gp.obj[gp.currentMap][i] = null;
	            }
	        }
	        dropItem(new OBJ_Emerald(gp));
	    }
	}
	
	public void setDialogue() {
		dialogues[0] = "You have come so far huh?";
		dialogues[1] = "HEHEHE.....";
		dialogues[2] = "WELOCOME TO YOUR DOOM !!!";
	}
}