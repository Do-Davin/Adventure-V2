package monster;

import java.util.Random;

import entity.Entity;
import main.GamePanel;
import object.OBJ_Emerald;
import object.OBJ_Fireball;
import object.OBJ_Firecharge;
import object.OBJ_healingP;

public class Skeleton_Wither extends Entity {

	GamePanel gp;

	public Skeleton_Wither(GamePanel gp) {
		super(gp);
		this.gp = gp;

		type = type_skeleton;
		name = "Skeleton Wither";
		defaultSpeed = 1;
		speed = defaultSpeed;
		maxLife = 6;
		life = maxLife;
		attack = 5;
		knockBackPower = 5;

		solidArea.x = 8; // X-offset 
		solidArea.y = 16; // Y-offset
		solidArea.width = 32; // Hitbox width
		solidArea.height = 32; // Hitbox height

		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
		attackArea.width = 48;
		attackArea.height = 48;
		motion1_duration = 40;
		motion2_duration = 85;
		getImage();
		getAttackImage();
	}

	public void getImage() {

		up1 = setup("/monster/wither_u1", gp.tileSize, gp.tileSize);
		up2 = setup("/monster/wither_u2", gp.tileSize, gp.tileSize);
		down1 = setup("/monster/wither_d1", gp.tileSize, gp.tileSize);
		down2 = setup("/monster/wither_d2", gp.tileSize, gp.tileSize);
		left1 = setup("/monster/wither_l1", gp.tileSize, gp.tileSize);
		left2 = setup("/monster/wither_l2", gp.tileSize, gp.tileSize);
		right1 = setup("/monster/wither_r1", gp.tileSize, gp.tileSize);
		right2 = setup("/monster/wither_r2", gp.tileSize, gp.tileSize);
		upStand = setup("/monster/wither_us", gp.tileSize, gp.tileSize);
		downStand = setup("/monster/wither_ds", gp.tileSize, gp.tileSize);
		leftStand = setup("/monster/wither_ls", gp.tileSize, gp.tileSize);
		rightStand = setup("/monster/wither_rs", gp.tileSize, gp.tileSize);

	}

	public void getAttackImage() {

		aUp1 = setup("/monster/wither_au1", gp.tileSize, gp.tileSize * 2); // 16 x 32
		aUp2 = setup("/monster/wither_au2", gp.tileSize, gp.tileSize * 2);
		aDown1 = setup("/monster/wither_ad1", gp.tileSize, gp.tileSize * 2);
		aDown2 = setup("/monster/wither_ad2", gp.tileSize, gp.tileSize * 2);
		aRight1 = setup("/monster/wither_ar1", gp.tileSize * 2, gp.tileSize);
		aRight2 = setup("/monster/wither_ar2", gp.tileSize * 2, gp.tileSize);
		aLeft1 = setup("/monster/wither_al1", gp.tileSize * 2, gp.tileSize);
		aLeft2 = setup("/monster/wither_al2", gp.tileSize * 2, gp.tileSize);
	}

	public void setAction() {

		boolean contactPlayer = gp.cChecker.checkPlayer(this);

		if (contactPlayer && shotCounter >= 30 && !gp.player.invincible) {
			damagePlayer(attack);
			shotCounter = 0;
		}

		if (onPath == true) {

			checkStopChasingOrNot(gp.player, 15, 100);

			checkCollision();
			if (collisionOn) {
				gp.pFinder.pathList.clear();
				searchPath(getGoalCol(gp.player), getGoalRow(gp.player));
			} else {
				searchPath(getGoalCol(gp.player), getGoalRow(gp.player));
			}
		} else {
			checkStartChasingOrNot(gp.player, 5, 100);
			getRandomDirection(120);
		}

		if (attacking == false) {
			checkAttackOrNot(30, gp.tileSize * 4, gp.tileSize);
		}

	}

	public void damageReaction() {

		actionLockCounter = 0;
		onPath = true;
	}

	public void checkDrop() {
	    int i = new Random().nextInt(2); // Generates 0 or 1

	    if (i == 0) {
	        dropItem(new OBJ_Emerald(gp)); // 50% chance
	    } else {
	        dropItem(new OBJ_Firecharge(gp)); // 50% chance
	    }
	}

}