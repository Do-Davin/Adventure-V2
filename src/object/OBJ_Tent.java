package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Tent extends Entity {

	GamePanel gp;
	
	public OBJ_Tent(GamePanel gp) {
		super(gp);
		this.gp = gp;
		
		type = type_consumable;
		name = "Tent";
		down1 = setup("/objects/tent", gp.tileSize, gp.tileSize);
		description = "[tent]\nYou can sleep until\nnext morning.";
		price = 4;
		stackable = true;
	}
	
	@Override
	public boolean use(Entity entity) {
		
		gp.gameState = gp.sleepState;
//		gp.playSE(9);
		gp.player.life = gp.player.maxLife;
		gp.player.mana = gp.player.maxMana;
		gp.player.getSleepImage(down1);
		return true;
	}
}