package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Sword extends Entity {

	public OBJ_Sword(GamePanel gp) {
		super(gp);
		// TODO Auto-generated constructor stub
		
		type = type_dsword;
		name = "Diamond Sowrd";
		down1 = setup("/objects/Sword",gp.tileSize,gp.tileSize);
		attackValue = 4;
		attackArea.width = 36;
		attackArea.height = 36;
		description = "[" + name + "]\nA Diamond Sword.";
		price =8;
		knockBackPower = 3;
		motion1_duration = 5;
		motion2_duration = 25;
	}

}