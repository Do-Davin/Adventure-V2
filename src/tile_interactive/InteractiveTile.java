package tile_interactive;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import entity.Entity;
import main.GamePanel;

public class InteractiveTile extends Entity{
	
	GamePanel gp;
	public boolean destructible = false;

	public InteractiveTile(GamePanel gp, int col, int row) {
		super(gp);
		this.gp = gp;	
	}
	
	public boolean isCorrectItem(Entity entity) {
		boolean isCorrectItem = false;
		return isCorrectItem;
	}
	
	public void playSE() {
		
	}
	
	public InteractiveTile getDestroyedForm() {
		
		InteractiveTile tile = null;
		return tile;
		
	}
	
	public void update() {
		
		if(invincible == true) {
			invinCounter++;
			if (invinCounter > 20) {
				invincible = false;
				invinCounter = 0;
			}
		}
	}
}
