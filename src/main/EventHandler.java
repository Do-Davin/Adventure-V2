package main;

import entity.Entity;

public class EventHandler {

	GamePanel gp;
	EventRect eventRect[][][];
	
	int previousEventX, previousEventY;
	boolean canTouchEvent = false;
	int tempMap , tempCol,tempRow;

	public EventHandler(GamePanel gp) {
		this.gp = gp;

		eventRect = new EventRect[gp.maxMap][gp.maxWorldCol][gp.maxWorldRow];

		int map = 0;
		int col = 0;
		int row = 0;

		while (map < gp.maxMap && col < gp.maxWorldCol && row < gp.maxWorldRow) {
			eventRect[map][col][row] = new EventRect();
			eventRect[map][col][row].x = 23;
			eventRect[map][col][row].y = 23;
			eventRect[map][col][row].width = 2;
			eventRect[map][col][row].height = 2;
			eventRect[map][col][row].eventRectDefaultX = eventRect[map][col][row].x;
			eventRect[map][col][row].eventRectDefaultY = eventRect[map][col][row].y;

			col++;
			if (col == gp.maxWorldCol) {
				col = 0;
				row++;
				
				if(row == gp.maxWorldRow) {
					row = 0;
					map++;
				}
			}
		}

	}

	public void checkEvent() {
		
		// check if player if 1 tile away from event
		int xDis = Math.abs(gp.player.worldX - previousEventX);
		int yDis = Math.abs(gp.player.worldY - previousEventY);
		int distance = Math.max(xDis, yDis);
		
		if (distance > gp.tileSize) {
			canTouchEvent = true;
		}
		
		if (canTouchEvent == true) {
			if (hit(0, 28, 17, "any") == true) {
				damagePit();
			}
			else if (hit(0, 110, 118, "any") == true) {
				gp.playSE(17);
				teleport(1, 30, 32, gp.indoor);
				
			}
			else if (hit(1, 30, 32, "any") == true) {
				gp.playSE(17);
				teleport(0, 110, 118, gp.outside);
			}
			else if(hit(1,30,25,"up") == true) {
				speak(gp.npc[1][0]);
			}
			else if (hit(0, 34, 91, "any") == true) {
				gp.playSE(18);
				gp.startSnowEffect();
				teleport(2, 82, 50, gp.outside);
			}
			else if(hit(2,82,50,"any") == true) {
				gp.playSE(18);
				gp.stopSnowEffect();
				teleport(0,34,91, gp.outside);
			}
			else if (hit(0, 152,117, "any") == true) {
				gp.playSE(18);
				teleport(3, 16, 50, gp.outside);
			}
			else if (hit(3, 16, 50, "any") == true) {
				gp.playSE(18);
				teleport(0, 152,117, gp.outside);
			}
			
			// Enter Final
			else if (hit(0, 89, 96, "any") == true) {
				gp.playSE(18);
				teleport(4, 13, 97, gp.dungeon);
			}
			
			else if (hit(4, 13, 97, "any") == true) {
				gp.playSE(18);
				teleport(0, 89, 96, gp.outside);
			}
			else if (hit(4, 38, 76, "any") == true) {
				Golem();
			}
			else if (hit(4, 38, 57, "any") == true) {
				Ending();
			}
		}		
	}

	public boolean hit(int map, int col, int row, String reqDirection) {

		boolean hit = false;
		
		if(map == gp.currentMap) {	
			gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
			gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;
			
			eventRect[map][col][row].x = col * gp.tileSize + eventRect[map][col][row].x;
			eventRect[map][col][row].y = row * gp.tileSize + eventRect[map][col][row].y;
			
			if (gp.player.solidArea.intersects(eventRect[map][col][row]) && eventRect[map][col][row].eventDone == false) {
				if (gp.player.direction.contentEquals(reqDirection) || reqDirection.contentEquals("any")) {
					hit = true;
					
					previousEventX = gp.player.worldX;
					previousEventY = gp.player.worldY;
				}
			}
			
			gp.player.solidArea.x = gp.player.solidAreaDefaultX;
			gp.player.solidArea.y = gp.player.solidAreaDefaultY;
			eventRect[map][col][row].x = eventRect[map][col][row].eventRectDefaultX;
			eventRect[map][col][row].y = eventRect[map][col][row].eventRectDefaultY;
		}

		return hit;
	}

	public void teleport(int map, int col, int row, int area) {
		gp.gameState = gp.transitionState;
		gp.nextArea = area;
		tempMap = map;
		tempCol =col;
		tempRow = row;
		canTouchEvent = false;
	}

	public void damagePit() {
		gp.player.life -= 1;
		// for one time event
//		eventRect[col][row].eventDone = true;
		// for multi time
		canTouchEvent = false;
	}
	
	public void speak(Entity entity) {
		
		if(gp.keyH.enterPressed == true) {
			gp.gameState = gp.dialogueState;
			entity.speak();
		}
	}
	
	public void Golem() {
	    // Only trigger if boss battle is not already on AND boss is not defeated
	    if(gp.bossBattleOn == false && Progress.GolemDefeated == false) {
	        gp.gameState = gp.cutscenceState;
	        gp.csManager.scenceNum = gp.csManager.Golem;
	    }
	}
	public void Ending() {
	    if (Progress.GolemDefeated) {
	        // Store the player's current position to return to after ending
	        tempMap = gp.currentMap;
	        tempCol = gp.player.worldX / gp.tileSize;
	        tempRow = gp.player.worldY / gp.tileSize;
	        
	        // Trigger the ending cutscene
	        gp.gameState = gp.cutscenceState;
	        gp.csManager.scenceNum = gp.csManager.ending;
	        canTouchEvent = false;
	    }
	}


}