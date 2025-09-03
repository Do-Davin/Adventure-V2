package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Firecharge extends Entity {

	public OBJ_Firecharge(GamePanel gp) {
		super(gp);
		// TODO Auto-generated constructor stub
		
		type = type_firecharge;
		name = "Fire Charge";
		down1 = setup("/objects/fire_charge",gp.tileSize,gp.tileSize);
		defenseValue =1;
		description = "[" + name + "]\nA Contain this to shoot the fire ball.";
		price = 3;
		stackable = true;
	}

}