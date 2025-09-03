package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Icekey extends Entity{

	public OBJ_Icekey(GamePanel gp) {
		super(gp);
		this.gp = gp;
		// TODO Auto-generated constructor stub
		type = type_consumable;
		name = "Ice_trailKey";
		down1 = setup("/objects/ice_trailkey",gp.tileSize,gp.tileSize);
		description = "[Ice Trail Key]\nKey to open gate for\n Frozen Island.";
	}
	
	public boolean use(Entity entity) {
		
		int objIndex = getDetected(entity,gp.obj, "GateIce");
		
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