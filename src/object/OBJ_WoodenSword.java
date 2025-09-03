package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_WoodenSword extends Entity {

	public OBJ_WoodenSword(GamePanel gp) {
		super(gp);
		// TODO Auto-generated constructor stub
		
		type = type_wsword;
		name = "Wooden Sword";
		down1 = setup("/objects/wooden_sword",gp.tileSize,gp.tileSize);
		attackValue = 1;
		attackArea.width = 36;
		attackArea.height = 36;
		description = "[Wooden Sword]\nMade out of wood\nTake less damage";
		price = 1;
		knockBackPower = 3;
		motion1_duration = 5;
		motion2_duration = 25;
	}

}