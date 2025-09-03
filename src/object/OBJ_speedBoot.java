package object;

import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;
import entity.Entity;

public class OBJ_speedBoot extends Entity {

	public OBJ_speedBoot(GamePanel gp) {
		super(gp);
		
		name = "speed_boot";
		down1 = setup("/objects/speed_boot",gp.tileSize,gp.tileSize);

	}
	
}