package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_DungeonDoor extends Entity {

	public OBJ_DungeonDoor(GamePanel gp) {
		super(gp);
		// TODO Auto-generated constructor stub
		
		type = type_obstacle;
		name = "Dungeon Door";
		down1 = setup("/tiles/041",gp.tileSize,gp.tileSize);
		collision = true;
		
		solidArea.x = 0;
		solidArea.y = 16;
		solidArea.width = 48;
		solidArea.height = 32;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
	}
	
	public void interact() {
		gp.gameState = gp.dialogueState;
		gp.ui.resetDialogueState();
		gp.ui.currentDialogue = "You need a key to open this";
	}

}