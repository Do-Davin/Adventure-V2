package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Axe extends Entity {

	public OBJ_Axe(GamePanel gp) {
		super(gp);
		// TODO Auto-generated constructor stub
		type = type_axe;
		name = "Diamond Axe";
		down1 = setup("/objects/axe",gp.tileSize,gp.tileSize);
		attackValue = 3;
		attackArea.width = 30;
		attackArea.height = 30;
		description = "[Diamond Axe]\nHigh Damage Weapon";
		price = 4;
		knockBackPower = 10;
		motion1_duration = 20;
		motion2_duration = 40;
	}

}