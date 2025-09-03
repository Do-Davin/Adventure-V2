package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_GateIce extends Entity {

	public OBJ_GateIce(GamePanel gp) {
		super(gp);
		// TODO Auto-generated constructor stub
		
		type = type_obstacle;
		name = "GateIce";
		down1 = setup("/objects/gate",gp.tileSize,gp.tileSize);
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