package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Chest extends Entity{

	GamePanel gp;
	Entity loot;
	boolean opened = false;
	public OBJ_Chest(GamePanel gp,Entity loot) {
		super(gp);
		this.gp = gp;
		this.loot = loot;
		// TODO Auto-generated constructor stub
		
		type = type_obstacle;
		name = "Chest";
		image = setup("/objects/chest1",gp.tileSize,gp.tileSize);
		image2 = setup("/objects/chest2",gp.tileSize,gp.tileSize);
		down1 = image;
		collision = true;
		
		solidArea.x = 4;
		solidArea.y = 16;
		solidArea.width =40;
		solidArea.height = 32;
		solidAreaDefaultX= solidArea.x;
		solidAreaDefaultY = solidArea.y;
		
	}
	
	public void interact() {
		gp.gameState = gp.dialogueState;
		gp.ui.resetDialogueState();
		if(opened == false) {
			StringBuilder sb = new StringBuilder();
//			sb.append("You got a "+ loot.name+" !");
			
			if(gp.player.inventory.size() == gp.player.maxinventorySize) {
				sb.append("\n...But you can't carry more !");
			}
			else {
				sb.append("You got a "+ loot.name+" !");
				gp.playSE(19);
				gp.player.inventory.add(loot);
				down1 = image2;
				opened = true;
			}
			gp.ui.currentDialogue = sb.toString();
		}
		else {
			gp.ui.currentDialogue = "It's empty !";
		}
	}

}