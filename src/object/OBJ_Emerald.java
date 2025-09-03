package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Emerald extends Entity{

	public OBJ_Emerald(GamePanel gp) {
		super(gp);
		// TODO Auto-generated constructor stub
		this.gp = gp;
		
		type = type_pickup;
		name = "Emerald";
		down1 = setup("/objects/emerald",gp.tileSize,gp.tileSize);
		description = "["+ name +"]\nValuable Item for Trading!";
		stackable = true;
	}
	
}