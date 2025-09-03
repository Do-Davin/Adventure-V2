package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Door extends Entity {
	public static final String objName = "Door";
	public OBJ_Door(GamePanel gp) {
		super(gp);
		// TODO Auto-generated constructor stub
		
		type = type_obstacle;
		name = objName;
		down1 = setup("/tiles/041",gp.tileSize,gp.tileSize);
		collision = true;
		
		solidArea.x = 0;
		solidArea.y = 16;
		solidArea.width = 48;
		solidArea.height = 32;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
	}
}