package main;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import entity.PlayerDummy;
import monster.Ice_Golem;
import object.OBJ_Door;

public class CutscenceManager {

	GamePanel gp;
	Graphics2D g2;
	public int scenceNum;
	public int scencePhase;
	int counter = 0;
	float alpha = 0f;
	int y;
	
	public final int NA = 0;
	public final int Golem =1;
	public final int ending =2;
	
	public CutscenceManager(GamePanel gp) {
		this.gp = gp;
	}
	
	public void draw(Graphics2D g2) {
		this.g2 = g2;
		
		switch(scenceNum) {
		case Golem: scence_Golem() ;break;
		case ending: scence_Ending();break;
		}
	}
	
	public void scence_Golem() {
		
		if (gp.player.life <= 0) {
	        scenceNum = NA;
	        scencePhase = 0;
	        gp.gameState = gp.playState;
	        gp.bossBattleOn = false;
	        
	        // FIX: Stop boss music and play appropriate area music
	        gp.stopMusic();
	        if (gp.currentArea == 50) {
	            gp.playMusic(0); // Overworld
	        } else if (gp.currentArea == 52) {
	            gp.playMusic(27); // Dungeon
	        }
	        return;
	    }
	    if(scencePhase == 0) {
	        gp.bossBattleOn = true;
	        for(int i = 0; i< gp.obj[1].length ;i++) {
	            if(gp.obj[gp.currentMap][i] == null) {
	                gp.obj[gp.currentMap][i] = new OBJ_Door(gp);
	                gp.obj[gp.currentMap][i].worldX = gp.tileSize*38;
	                gp.obj[gp.currentMap][i].worldY = gp.tileSize*77;
	                gp.obj[gp.currentMap][i].temp = true;
	                gp.playSE(17);
	                break;
	            }
	        }
	        // search vacant slot for dummy
	        for(int i =0 ; i< gp.npc[1].length;i++) {
	            if(gp.npc[gp.currentMap][i] == null) {
	                gp.npc[gp.currentMap][i] = new PlayerDummy(gp);
	                gp.npc[gp.currentMap][i].worldX = gp.player.worldX;
	                gp.npc[gp.currentMap][i].worldY = gp.player.worldY;
	                gp.npc[gp.currentMap][i].direction = gp.player.direction;
	                break;
	            }
	        }
	        gp.player.drawing = false;
	        scencePhase++;
	    }
	    else if(scencePhase == 1) {
	        gp.player.worldY -= 2;
	        
	        if(gp.player.worldY < gp.tileSize*62) {
	            scencePhase++;
	        }
	    }
	    else if(scencePhase == 2) {
	        // search the boss
	        for(int i=0; i< gp.monster[1].length;i++) {
	            if(gp.monster[gp.currentMap][i] != null && gp.monster[gp.currentMap][i].name.equals(Ice_Golem.monName)) {
	                gp.monster[gp.currentMap][i].sleep = false;
	                // Set the first dialogue
	                gp.ui.currentDialogue = gp.monster[gp.currentMap][i].dialogues[0];
	                gp.ui.resetDialogueState();
	                scencePhase++;
	                break;
	            }
	        }
	    }
	    else if (scencePhase == 3) {
	        if (gp.ui.currentDialogue != null) {
	            gp.ui.drawDialogueScreen();

	            if (gp.ui.textCompleted && gp.keyH.enterPressed) {
	                gp.keyH.enterPressed = false;

	                for (int i = 0; i < gp.monster[gp.currentMap].length; i++) {
	                    if (gp.monster[gp.currentMap][i] != null &&
	                        gp.monster[gp.currentMap][i].name.equals(Ice_Golem.monName)) {

	                        Ice_Golem golem = (Ice_Golem) gp.monster[gp.currentMap][i];

	                        if (golem.dialogueIndex < golem.dialogues.length - 1) {
	                            golem.dialogueIndex++;
	                            gp.ui.currentDialogue = golem.dialogues[golem.dialogueIndex];
	                            gp.ui.resetDialogueState();
	                        } else {
	                            // Dialogue finished
	                            gp.ui.currentDialogue = null;
	                            gp.ui.displayedText = "";
	                            gp.ui.textCompleted = false;
	                            scencePhase++;
	                        }
	                        break;
	                    }
	                }
	            }
	        } else {
	            // Failsafe: if dialogue is null, move on
	            scencePhase++;
	        }
	    }

	    else if(scencePhase == 4) {
	        // return to player
	        for(int i=0;i<gp.npc[1].length;i++) {
	            if(gp.npc[gp.currentMap][i] != null && gp.npc[gp.currentMap][i].name.equals(PlayerDummy.npcName)) {
	                gp.player.worldX = gp.npc[gp.currentMap][i].worldX;
	                gp.player.worldY = gp.npc[gp.currentMap][i].worldY;
	                
	                // delete dummy
	                gp.npc[gp.currentMap][i] = null;
	                break;
	            }
	        }
	        // draw player
	        gp.player.drawing = true;
	        
	        // reset
	        scenceNum = NA;
	        scencePhase = 0;
	        gp.gameState = gp.playState;
	        
	        gp.stopMusic();
	        gp.playMusic(28);
	    }
	}
	public void scence_Ending() {
	    if(scencePhase == 0) {
	        gp.stopMusic();
	        gp.playSE(25);
	        scencePhase++;
	    }
	    if(scencePhase == 1) {
	        if(counterReached(300) == true) {
	            scencePhase++;
	        }
	    }
	    if (scencePhase == 2) {
	        alpha += 0.005f;
	        if (alpha >= 1f) {
	            alpha = 1f;
	            scencePhase++;
	        }
	        drawBlackBackground(alpha);
	    }
	    else if (scencePhase == 3) {
	        drawBlackBackground(1f);
	        String text = "The End";
	        g2.setFont(g2.getFont().deriveFont(48f));
	        FontMetrics fm = g2.getFontMetrics();
	        int textWidth = fm.stringWidth(text);
	        int textHeight = fm.getHeight();

	        int x = (gp.screenWidth - textWidth) / 2;
	        int y = (gp.screenHeight - textHeight) / 2 + fm.getAscent();

	        g2.setColor(Color.white);
	        g2.drawString(text, x, y);

	        if (counterReached(180)) {
	            scencePhase++;
	        }
	    }
	    else if (scencePhase == 4) {
	        // RETURN PLAYER TO OVERWORLD (world02)
	        gp.currentMap = 0; // Overworld map
	        gp.player.worldX = gp.tileSize * 32; // Original X position
	        gp.player.worldY = gp.tileSize * 61; // Original Y position
	        
	        // Reset area and music
	        gp.currentArea = gp.outside; // 50 - overworld
	        gp.nextArea = gp.outside;    // 50 - overworld
	        gp.stopMusic();
	        gp.playMusic(0); // Overworld music
	        
	        // Reset cutscene state
	        scenceNum = NA;
	        scencePhase = 0;
	        gp.gameState = gp.playState;
	    }
	}
	
	public boolean counterReached( int target) {
		boolean counterReach = false;
		counter++;
		if(counter> target) {
			counterReach = true;
			counter = 0;
		}
		return counterReach;
	}
	
	public void drawBlackBackground(float alpha) {
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,alpha));
		g2.setColor(Color.black);
		g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1f));
	}
}