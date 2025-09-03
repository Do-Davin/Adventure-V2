package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_healingP extends Entity {

	public OBJ_healingP(GamePanel gp) {
		super(gp);
		// TODO Auto-generated constructor stub
		
		type = type_consumable;
		name = "Healing Potion";
		down1 = setup("/objects/healing_potion",gp.tileSize,gp.tileSize);
		description ="[Healing Potion]\nInstantly max your health.";
		price = 2;
		stackable = true;
	}
	
	public boolean use(Entity entity) {
		entity.life = entity.maxLife;
		
		if(gp.player.life > gp.player.maxLife) {
			gp.player.life = gp.player.maxLife;
		}
		gp.playSE(9);
		return true;
	}

}