package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_dunKey extends Entity{

	public OBJ_dunKey(GamePanel gp) {
		super(gp);
		this.gp = gp;
		// TODO Auto-generated constructor stub
		type = type_consumable;
		name = "Dungeon Key";
		down1 = setup("/objects/dunKey",gp.tileSize,gp.tileSize);
		description = "[Dungeon Key]\nKey to open door for\n Dungeon mansion.";
	}
	
	public boolean use(Entity entity) {
		
		int objIndex = getDetected(entity,gp.obj, "Dungeon Door");
		
		if(objIndex != 999) {
			gp.playSE(16);
			gp.obj[gp.currentMap][objIndex] = null;
			return true;
		}
		else {
			return false;
		}
	}
}