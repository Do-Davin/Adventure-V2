package object;

import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;
import entity.Entity;

public class OBJ_Heart extends Entity {

	public OBJ_Heart(GamePanel gp) {
		
		super(gp);
		
		name = "Heart";
		image = setup("/objects/full_heart",gp.tileSize,gp.tileSize);
		image2 = setup("/objects/half_heart",gp.tileSize,gp.tileSize);
		image3 = setup("/objects/empty_heart",gp.tileSize,gp.tileSize);
	}
}