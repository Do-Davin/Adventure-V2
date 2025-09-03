package entity;

import main.GamePanel;

public class PlayerDummy extends Entity {

	public static final String npcName = "Dummy";
	public PlayerDummy(GamePanel gp) {
		super(gp);
		// TODO Auto-generated constructor stub
		name = npcName;
		getImage();
	}
	
	public void getImage() {

		up1 = setup("/player/Steve_up1", gp.tileSize, gp.tileSize);
		up2 = setup("/player/Steve_up2", gp.tileSize, gp.tileSize);
		down1 = setup("/player/Steve_down1", gp.tileSize, gp.tileSize);
		down2 = setup("/player/Steve_down2", gp.tileSize, gp.tileSize);
		left1 = setup("/player/Steve_left1", gp.tileSize, gp.tileSize);
		left2 = setup("/player/Steve_left2", gp.tileSize, gp.tileSize);
		right1 = setup("/player/Steve_right1", gp.tileSize, gp.tileSize);
		right2 = setup("/player/Steve_right2", gp.tileSize, gp.tileSize);
		upStand = setup("/player/Steve_up_stand", gp.tileSize, gp.tileSize);
		downStand = setup("/player/Steve_down_stand", gp.tileSize, gp.tileSize);
		leftStand = setup("/player/Steve_left_stand", gp.tileSize, gp.tileSize);
		rightStand = setup("/player/Steve_right_stand", gp.tileSize, gp.tileSize);
	}

}