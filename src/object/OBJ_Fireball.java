package object;

import java.awt.Color;

import entity.Entity;
import entity.Player;
import entity.Projectile;
import main.GamePanel;

public class OBJ_Fireball extends Projectile {
	
	GamePanel gp;
	
	public OBJ_Fireball(GamePanel gp) {
		super(gp);
		this.gp = gp;
		name = "Fireball";
		speed = 5;
		maxLife = 80;
		life = maxLife;
		attack = 2;
		knockBackPower = 0;
		useCost = 1;
		alive = false;
		getImage();
		// TODO Auto-generated constructor stub
	}
	
	public void getImage() {
		up1 = setup("/projectile/fireball_up1",gp.tileSize,gp.tileSize);
		up2 = setup("/projectile/fireball_up2",gp.tileSize,gp.tileSize);
		down1 = setup("/projectile/fireball_down1",gp.tileSize,gp.tileSize);
		down2 = setup("/projectile/fireball_down2",gp.tileSize,gp.tileSize);
		left1 = setup("/projectile/fireball_left1",gp.tileSize,gp.tileSize);
		left2 = setup("/projectile/fireball_left2",gp.tileSize,gp.tileSize);
		right1 = setup("/projectile/fireball_right1",gp.tileSize,gp.tileSize);
		right2 = setup("/projectile/fireball_right2",gp.tileSize,gp.tileSize);

	}
	
	public boolean haveResource(Entity user) {
	    Player player = (Player)user;
	    boolean haveResource = false;
	    
	    // Check if player has selected firecharge and has at least one
	    if(player.currentBall != null && player.currentBall.type == type_firecharge) {
	        // Count how many firecharges player has
	        int fireChargeCount = 0;
	        for(Entity item : player.inventory) {
	            if(item != null && item.type == type_firecharge) {
	                fireChargeCount++;
	            }
	        }
	        haveResource = fireChargeCount > 0;
	    }
	    return haveResource;
	}
	
	public Color getParticleColor() {
		Color color = new Color(240, 50, 0);
		return color;
	}
	
	public int getParticleSize() {
		int size = 10; 
		return size;
	}
	
	public int getParticleSpeed() {
		int speed = 1;
		return speed;
	}
	
	public int getParticleMaxLife() {
		int maxLife = 20;
		return maxLife;
	}

	public void subtractResource(Entity user) {
	    Player player = (Player)user;
	    // Find and remove one firecharge
	    for(int i = 0; i < player.inventory.size(); i++) {
	    	Entity item = player.inventory.get(i);
	        if(item != null && item.type == type_firecharge) {
	        	if(item.amount >1) {
	        		item.amount--;
	        	}
	        	else {
	        		player.inventory.remove(i);
	        	}
	            break;
	        }
	    }
	}

}