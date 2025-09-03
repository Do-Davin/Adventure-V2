package main;

import java.awt.BasicStroke;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import object.OBJ_Emerald;
import object.OBJ_Heart;
import entity.Entity;

import javax.imageio.ImageIO;

import javax.sound.sampled.*;

public class UI {

	GamePanel gp;
	Graphics2D g2;
	public Font PressStart2P;
	BufferedImage full_heart, half_heart, empty_heart, emeral;
	public boolean messageOn = false;
	public String message = "";
	int messageCounter = 0;
	int counter = 0;
	public Entity wander;

	// For dialogue attributes
	public String currentDialogue = "";
	BufferedImage dialogueBoxImage;
	BufferedImage checkBoxImage;
	BufferedImage checkmarkImage;
	BufferedImage longBoxImage;
	BufferedImage pointImage;
	BufferedImage bgImage;
	BufferedImage InImage;
	BufferedImage DeImage;

	// For dialogue text appear letter by letter
	public String displayedText = ""; // Text currently shown
	public int charIndex = 0;
	public int textTimer = 0;
	int textSpeed = 3; // Delay between characters (smaller = faster)
	public boolean textCompleted = false;

	// For TITLE STATE attributes
	public int commandNum = 0;

	// FOR SUB SCREEN ATTRIBUTE
	int subState = 0;

	public int playerSlotCol = 0;
	public int playerSlotRow = 0;
	public int npcSlotCol = 0;
	public int npcSlotRow = 0;

	public UI(GamePanel gp) {
		this.gp = gp;

		try {
			bgImage = ImageIO.read(getClass().getResourceAsStream("/background/background.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			InImage = ImageIO.read(getClass().getResourceAsStream("/dialogues/inventory.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			DeImage = ImageIO.read(getClass().getResourceAsStream("/dialogues/D_Image.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			InputStream is = getClass().getResourceAsStream("/font/PressStart2P-Regular.ttf");
			PressStart2P = Font.createFont(Font.TRUETYPE_FONT, is);
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}

		try {
			dialogueBoxImage = ImageIO.read(getClass().getResourceAsStream("/dialogues/dialogue_wood.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			checkBoxImage = ImageIO.read(getClass().getResourceAsStream("/dialogues/checkbox.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			longBoxImage = ImageIO.read(getClass().getResourceAsStream("/dialogues/longBox.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			pointImage = ImageIO.read(getClass().getResourceAsStream("/dialogues/point.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			checkmarkImage = ImageIO.read(getClass().getResourceAsStream("/dialogues/checkmark.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		Entity heart = new OBJ_Heart(gp);
		full_heart = heart.image;
		half_heart = heart.image2;
		empty_heart = heart.image3;

		Entity greenEmeral = new OBJ_Emerald(gp);
		emeral = greenEmeral.down1;
	}

	public void playBlip() {
		try {
			// Input typing sound later
			AudioInputStream audioIn = AudioSystem
					.getAudioInputStream(getClass().getResourceAsStream("/sound/typing.wav"));
			Clip clip = AudioSystem.getClip();
			clip.open(audioIn);
			clip.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void showMessage(String text) {

		message = text;
		messageOn = true;

	}

	public void draw(Graphics2D g2) {

		this.g2 = g2;

		g2.setFont(PressStart2P);
		g2.setColor(Color.white);

		// TITLE STATE
		if (gp.gameState == gp.titleState) {
			drawTitleScreen();
		}

		// PLAY STATE
		if (gp.gameState == gp.playState) {
			// Do playState stuff later

			drawPlayerlife();
			drawMonsterLife();
		}

		// PAUSE STATE
		if (gp.gameState == gp.pauseState) {
			drawPauseScreen();
			drawPlayerlife();
		}

		// DIALOGUE STATE
		if (gp.gameState == gp.dialogueState) {
			drawDialogueScreen();
			drawPlayerlife();
		}

		// OPTIONS STATE
		if (gp.gameState == gp.optionsState) {
			drawOptionsScreen();
		}

		// GAMEOVER STATE
		if (gp.gameState == gp.gameOverState) {
			drawGameOverScreen();
		}

		if (gp.gameState == gp.characterState) {
			drawCharacterScreen(); // for character state if want in the future
			drawInventory(gp.player, true);
		}

		// transition state
		if (gp.gameState == gp.transitionState) {
			drawTransition();
		}

		// TRADE STATE
		if (gp.gameState == gp.tradeState) {
			drawTradeScreen();
		}

		// SLEEP STATE
		if (gp.gameState == gp.sleepState) {
			drawSleepScreen();
		}
	}

	public void drawGameOverScreen() {

		g2.setColor(new Color(0, 0, 0, 150));
		g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

		int x;
		int y;
		String text;
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 50f));

		text = "Game Over";

		// Shadow
		g2.setColor(Color.black);
		x = getXforCenteredText(text);
		y = gp.tileSize * 4;
		g2.drawString(text, x, y);

		// Main
		g2.setColor(Color.white);
		g2.drawString(text, x - 4, y - 4);

		// Retry
		g2.setFont(g2.getFont().deriveFont(25f));
		text = "Retry";
		x = getXforCenteredText(text);
		y += gp.tileSize * 4;
		g2.drawString(text, x, y);
		if (commandNum == 0) {
			g2.drawString(">", x - 40, y);
			gp.stopMusic();
		}

		// Back to the title screen
		text = "Quit";
		x = getXforCenteredText(text);
		y += 55;
		g2.drawString(text, x, y);
		if (commandNum == 1) {
			g2.drawString(">", x - 40, y);
		}

	}

	public void drawTitleScreen() {

		g2.drawImage(bgImage, 0, 0, gp.screenWidth, gp.screenHeight, null);

		// TITLE NAME
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 56F));
		String text = "VV_Adventure";
		int x = getXforCenteredText(text);
		int y = gp.tileSize * 3;

		// SHADOW
		g2.setColor(Color.gray);
		g2.drawString(text, x + 4, y + 4);

		// MAIN COLOR
		g2.setColor(Color.white);
		g2.drawString(text, x, y);

		// MENU
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 26F));

		// IF WE ARE USING drawToTempScreen()
		// ================= drawToTempScreen() =================
		String[] menu = { "NEW GAME", "LOAD GAME", "QUIT" };

		y += gp.tileSize * 4; // starting y for menu

		for (int i = 0; i < menu.length; i++) {
			text = menu[i];
			x = getXforCenteredText(text);

			if (i == commandNum) {
				g2.drawString("> " + text + " <", x - gp.tileSize, y);
			} else {
				g2.drawString(text, x, y);
			}

			y += gp.tileSize * 1.5;
		}
	}

	public void drawPlayerlife() {

//		gp.player.life = 3;
		int x = gp.tileSize / 2;
		int y = gp.tileSize / 2;

		int i = 0;
		// Empty heart
		while (i < gp.player.maxLife / 2) {
			g2.drawImage(empty_heart, x, y, null);
			i++;
			x += gp.tileSize;
		}
		// Reset
		x = gp.tileSize / 2;
		y = gp.tileSize / 2;

		i = 0;

		// Current life
		while (i < gp.player.life) {
			g2.drawImage(half_heart, x, y, null);
			i++;
			if (i < gp.player.life) {
				g2.drawImage(full_heart, x,y, null);
			}
			i++;
			x += gp.tileSize;
		}
	}

	public void drawPauseScreen() {

		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 80F));
		String text = "PAUSED";

		int x = getXforCenteredText(text);
		int y = gp.screenHeight / 2;

		g2.drawString(text, x, y);

	}

	public void drawDialogueScreen() {
		if (currentDialogue == null) return;
		// WINDOW
		int boxX = gp.tileSize * 2;
		int boxY = (gp.tileSize * 8);
		int boxWidth = gp.screenWidth - (gp.tileSize * 4);
		int boxHeight = gp.tileSize * 3;

		g2.drawImage(InImage, boxX, boxY, boxWidth, boxHeight, null); // adjust x/y to resize wood dialogue

		// Position number is changeable here
		int textX = boxX + 55;
		int textY = boxY + 50;

		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 14F));

		// Typing animation logic
		if (!textCompleted) {
			textTimer++;
			if (textTimer >= textSpeed) {
				if (charIndex < currentDialogue.length()) {
					char currentChar = currentDialogue.charAt(charIndex);
					displayedText += currentChar;
					charIndex++;
					textTimer = 0;

					if (currentChar != ' ' && currentChar != '\n') {
						playBlip(); // Only play sound for visible letters
					}
				} else {
					textCompleted = true;
				}
			}
		}

		// Shadow
		for (String line : displayedText.split("\n")) {
			g2.setColor(Color.black);
			g2.drawString(line, textX + 2, textY + 2);

			// Main text
			g2.setColor(Color.white);
			g2.drawString(line, textX, textY);

			textY += 35;
		}

	}

	public void drawOptionsScreen() {

		g2.setColor(Color.white);
		g2.setFont(g2.getFont().deriveFont(14F));

		// SUB WINDOW
		int frameX = gp.tileSize * 4;
		int frameY = gp.tileSize;
		int frameWidth = gp.tileSize * 8;
		int frameHeight = gp.tileSize * 10;

		// IF WE WANT TO USE WOOD DIALOGUE
		g2.drawImage(dialogueBoxImage, frameX, frameY, frameWidth, frameHeight, null);

		// DEFAULT DIALOGUE
//		drawSubWindow(frameX, frameY, frameWidth, frameHeight);

		switch (subState) {
		case 0:
			options_top(frameX, frameY);
			break;
		case 1:
			options_fullScreenNotification(frameX, frameY);
			break;
		case 2:
			options_control(frameX, frameY);
			break;
		case 3:
			options_endGameConfirmation(frameX, frameY);
			break;
		}

		gp.keyH.enterPressed = false;

	}

	public void options_top(int frameX, int frameY) {

		int textX;
		int textY;

		// TITLE
		String text = "OPTIONS";
		textX = getXforCenteredText(text);
		textY = frameY + gp.tileSize + 75;
		g2.drawString(text, textX, textY);

		g2.setFont(g2.getFont().deriveFont(12F));

		// FULL SCREEN ON/OFF
		textX = frameX + gp.tileSize + 20;
		textY += gp.tileSize;
		g2.drawString("FULL SCREEN", textX, textY);
		if (commandNum == 0) {
			g2.drawString("> ", textX - 25, textY);
			if (gp.keyH.enterPressed == true) {
				if (gp.fullScreenOn == false) {
					gp.fullScreenOn = true;
				} else if (gp.fullScreenOn == true) {
					gp.fullScreenOn = false;
				}
				subState = 1;
			}
		}

		// MUSIC
		textY += gp.tileSize - 20;
		g2.drawString("MUSIC", textX, textY);
		if (commandNum == 1) {
			g2.drawString("> ", textX - 25, textY);
		}

		// SE
		textY += gp.tileSize - 20;
		g2.drawString("SE", textX, textY);
		if (commandNum == 2) {
			g2.drawString("> ", textX - 25, textY);
		}

		// CONTROL
		textY += gp.tileSize - 20;
		g2.drawString("CONTROL", textX, textY);
		if (commandNum == 3) {
			g2.drawString("> ", textX - 25, textY);
			if (gp.keyH.enterPressed == true) {
				subState = 2;
				commandNum = 0;
			}
		}

		// END GAME
		textY += gp.tileSize - 20;
		g2.drawString("END GAME", textX, textY);
		if (commandNum == 4) {
			g2.drawString("> ", textX - 25, textY);
			if (gp.keyH.enterPressed == true) {
				subState = 3;
				commandNum = 0;
			}
		}

		// BACK
		textY += (gp.tileSize * 2) - 20;
		g2.drawString("BACK", textX, textY);
		if (commandNum == 5) {
			g2.drawString("> ", textX - 25, textY);
			if (gp.keyH.enterPressed == true) {
				gp.gameState = gp.playState;
				commandNum = 0;
			}
		}

		// FULL SCREEN CHECK BOX
		textX = (int) (frameX + gp.tileSize * 4.5);
		textY = (frameY + gp.tileSize * 3) + 5;
//		g2.drawRect(textX, textY, 24, 24);
		g2.drawImage(checkBoxImage, textX, textY, 24, 30, null);
		if (gp.fullScreenOn == true) {
			g2.drawImage(checkmarkImage, textX, textY, 24, 24, null);
		}

		// MUSIC VOLUME BAR
		textY += gp.tileSize - 20;
		g2.drawImage(longBoxImage, textX, textY, 110, 30, null); // 110 / 5 = 22

		// Draw volume "points" based on volume scale
		for (int i = 0; i < gp.music.volumeScale; i++) {
			int pointX = textX + (i * 22); // 110px wide total, so 22px per unit
			g2.drawImage(pointImage, pointX, textY, 22, 30, null);
		}

		// SE VOLUME BAR
		textY += gp.tileSize - 20;
		g2.drawImage(longBoxImage, textX, textY, 110, 30, null);

		for (int i = 0; i < gp.se.volumeScale; i++) {
			int pointX = textX + (i * 22);
			g2.drawImage(pointImage, pointX, textY, 22, 30, null);
		}

		gp.config.saveConfig();
	}

	public void options_fullScreenNotification(int frameX, int frameY) {

		int textX = frameX + gp.tileSize + 20;
		int textY = frameY + gp.tileSize * 3;

		g2.setFont(g2.getFont().deriveFont(10F));

		currentDialogue = "The change will take \neffect after restarting \nthe game.";

		for (String line : currentDialogue.split("\n")) {
			g2.drawString(line, textX, textY);
			textY += 40;
		}

		// BACK
		textY += frameY + gp.tileSize;
		g2.drawString("BACK", textX, textY);
		if (commandNum == 0) {
			g2.drawString(">", textX - 25, textY);
			if (gp.keyH.enterPressed == true) {
				subState = 0;
			}
		}

	}

	public void options_control(int frameX, int frameY) {

		int textX;
		int textY;

		g2.setFont(g2.getFont().deriveFont(12F));

		// TITLE
		String text = "CONTROL";
		textX = getXforCenteredText(text);
		textY = frameY + gp.tileSize + 70;
		g2.drawString(text, textX, textY);

		textX = frameX + gp.tileSize;
		textY += gp.tileSize;
		g2.drawString("Move", textX, textY);
		textY += gp.tileSize - 20;
		g2.drawString("Confirm/Attack", textX, textY);
		textY += gp.tileSize - 20;
		g2.drawString("Shoot/Cast", textX, textY);
		textY += gp.tileSize - 20;
		g2.drawString("Pause", textX, textY);
		textY += gp.tileSize - 20;
		g2.drawString("Options", textX, textY);
		textY += gp.tileSize - 20;

		textX = frameX + gp.tileSize * 6;
		textY = frameY + (gp.tileSize * 4) - 25;
		g2.drawString("WASD", textX, textY);
		textY += gp.tileSize - 20;
		g2.drawString("ENTER", textX, textY);
		textY += gp.tileSize - 20;
		g2.drawString("F", textX, textY);
		textY += gp.tileSize - 20;
		g2.drawString("P", textX, textY);
		textY += gp.tileSize - 20;
		g2.drawString("ESC", textX, textY);
		textY += gp.tileSize - 20;

		// BACK
		textX = frameX + gp.tileSize;
		textY = frameY + gp.tileSize * 7;
		g2.drawString("BACK", textX, textY);
		if (commandNum == 0) {
			g2.drawString(">", textX - 25, textY);
			if (gp.keyH.enterPressed == true) {
				subState = 0;
				commandNum = 3;
			}
		}
	}

	public void options_endGameConfirmation(int frameX, int frameY) {

		int textX = frameX + gp.tileSize;
		int textY = frameY + gp.tileSize * 3;

		g2.setFont(g2.getFont().deriveFont(12F));

		currentDialogue = "Quit the game and return \nto the title screen?";

		for (String line : currentDialogue.split("\n")) {
			g2.drawString(line, textX, textY);
			textY += 40;
		}

		// YES
		String text = "Yes";
		textX = getXforCenteredText(text);
		textY += gp.tileSize * 2;
		g2.drawString(text, textX, textY);
		if (commandNum == 0) {
			g2.drawString(">", textX - 25, textY);
			if (gp.keyH.enterPressed == true) {
				subState = 0;
				gp.music.stop();
				gp.gameState = gp.titleState;
			}
		}

		// NO
		text = "No";
		textX = getXforCenteredText(text);
		textY += gp.tileSize;
		g2.drawString(text, textX, textY);
		if (commandNum == 1) {
			g2.drawString(">", textX - 25, textY);
			if (gp.keyH.enterPressed == true) {
				subState = 0;
				commandNum = 4;
			}
		}
	}

	public void drawCharacterScreen() {

	}

	public void drawInventory(Entity entity, boolean cursor) {

		int frameWidth = 0;
		int frameHeight = 0;
		int frameX = 0;
		int frameY = 0;
		int slotCol = 0;
		int slotRow = 0;

		if (entity == gp.player) {
			frameWidth = gp.tileSize * 9; // Width to match visual slots
			frameHeight = gp.tileSize * 4 + 12; // Slight padding for title bar
			frameX = gp.screenWidth / 2 - frameWidth / 2 - gp.tileSize * 2; // Center horizontally
			frameY = gp.screenHeight / 2 + gp.tileSize + 8; // Just below player
			slotCol = playerSlotCol;
			slotRow = playerSlotRow;
		} else {
			frameWidth = gp.tileSize * 9;
			frameHeight = gp.tileSize * 4 + 12;
			frameX = gp.screenWidth / 2 - frameWidth / 2 - gp.tileSize * 2;
			frameY = gp.screenHeight / 2 + gp.tileSize + 8 - (int) (gp.tileSize * 6.5);
			slotCol = npcSlotCol;
			slotRow = npcSlotRow;
		}

		g2.drawImage(InImage, frameX, frameY, frameWidth, frameHeight, null);

		final int slotXStart = frameX + 21;
		final int slotYStart = frameY + 21;
		int slotX = slotXStart;
		int slotY = slotYStart;
		final int slotSize = gp.tileSize + 8;

		for (int i = 0; i < entity.inventory.size(); i++) {

			// Equip cursor
			// In drawInventory method, update the equipment highlighting:
			if (entity.inventory.get(i) == entity.currentWeapon || 
			    entity.inventory.get(i) == entity.currentShield || 
			    entity.inventory.get(i) == entity.currentBall || 
			    entity.inventory.get(i) == entity.currentLight) {
			    
			    // Only highlight if the equipment is actually equipped (not null)
			    if (entity.currentWeapon != null || entity.currentShield != null || 
			        entity.currentBall != null || entity.currentLight != null) {
			        g2.setColor(new Color(75, 46, 25));
			        g2.fillRoundRect(slotX, slotY, gp.tileSize, gp.tileSize, 10, 10);
			    }
			}
			g2.drawImage(entity.inventory.get(i).down1, slotX, slotY, null);

			// display amount
			if (entity == gp.player && entity.inventory.get(i).amount > 1) {
				g2.setFont(g2.getFont().deriveFont(14f));
				FontMetrics fm = g2.getFontMetrics();
				String s = String.valueOf(entity.inventory.get(i).amount);
				int textWidth = fm.stringWidth(s);
				int textHeight = fm.getHeight();

				int amountX = slotX + gp.tileSize - textWidth - 4; // 4px padding from right
				int amountY = slotY + gp.tileSize - 4; // 4px padding from bottom

				// Shadow
				g2.setColor(new Color(60, 60, 60));
				g2.drawString(s, amountX + 1, amountY + 1);
				// NUmber
				g2.setColor(Color.white);
				g2.drawString(s, amountX, amountY);
			}

			slotX += slotSize;

			if (i == 6 || i == 13 || i == 20) {
				slotX = slotXStart;
				slotY += slotSize;
			}
		}

		// Cursor
		if (cursor == true) {

			int cursorX = slotXStart + (slotSize * slotCol);
			int cursorY = slotYStart + (slotSize * slotRow);
			int cursorWidth = gp.tileSize;
			int cursorHeight = gp.tileSize;
			// draw Cursor
			Color goldenBrown = new Color(0xb37731);
			g2.setColor(goldenBrown);
			g2.setStroke(new BasicStroke(5));
			g2.drawRoundRect(cursorX, cursorY, cursorWidth, cursorHeight, 10, 10);

			// Description frame
			int dFrameX = frameX + frameWidth + 16;
			int dFrameY = frameY;
			int dFrameWidth = gp.tileSize * 4;
			int dFrameHeight = frameHeight;
			int textX = dFrameX + 20;
			int textY = dFrameY + gp.tileSize;
			g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 10F));

			int maxLineWidth = dFrameWidth - 40; // total horizontal space (20px padding on both sides)
			FontMetrics fm = g2.getFontMetrics();

			int itemIndex = getItemIndexOnSlot(slotCol, slotRow);
			if (itemIndex < entity.inventory.size()) {
				g2.drawImage(DeImage, dFrameX, dFrameY, dFrameWidth, dFrameHeight, null);
				String[] paragraphLines = entity.inventory.get(itemIndex).description.split("\n");

				for (String paragraph : paragraphLines) {
					String[] words = paragraph.split(" ");
					String currentLine = "";

					for (String word : words) {
						String testLine = currentLine.isEmpty() ? word : currentLine + " " + word;

						if (fm.stringWidth(testLine) <= maxLineWidth) {
							currentLine = testLine;
						} else {
							g2.drawString(currentLine, textX, textY);
							textY += 20;
							currentLine = word;
						}
					}

					// draw any remaining line after the loop
					if (!currentLine.isEmpty()) {
						g2.drawString(currentLine, textX, textY);
						textY += 20;
					}
				}

			}
		}
	}

	public void drawTradeScreen() {

		switch (subState) {
		case 0:
			tradeSelection();
			break;
		case 1:
			tradeBuy();
			break;
		case 2:
			tradeSell();
			break;
		}
//		gp.keyH.enterPressed = false;
	}

	public void tradeSelection() {
		drawDialogueScreen();

		int x = gp.tileSize * 11;
		int y = gp.tileSize * 5;
		int width = gp.tileSize * 3;
		int height = (int) (gp.tileSize * 3.5);
		g2.drawImage(DeImage, x, y, width, height, null);

		int textX = x + gp.tileSize;
		int textY = y + gp.tileSize;
		int lineSpacing = 40; // Adjust this value to control spacing

		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 14F));
		g2.setColor(Color.white);

		g2.drawString("Buy", textX, textY);
		if (commandNum == 0) {
			g2.drawString(">", textX - 24, textY);
			if (gp.keyH.enterPressed == true) {
				subState = 1;
				gp.keyH.enterPressed = false;
			}
		}
		textY += lineSpacing;

		g2.drawString("Sell", textX, textY);
		if (commandNum == 1) {
			g2.drawString(">", textX - 24, textY);
			if (gp.keyH.enterPressed == true) {
				subState = 2;
				gp.keyH.enterPressed = false;
			}
		}
		textY += lineSpacing;

		g2.drawString("Leave", textX, textY);
		if (commandNum == 2) {
			g2.drawString(">", textX - 24, textY);
			if (gp.keyH.enterPressed == true) {
				currentDialogue = "Come Back Later !!!";
				gp.ui.displayedText = "";
				gp.ui.charIndex = 0;
				gp.ui.textCompleted = false;
				commandNum = 0;
				gp.gameState = gp.dialogueState;

			}
		}
	}

	public void tradeBuy() {

		// draw player inventory
		drawInventory(gp.player, false);
		// draw npc inventory
		drawInventory(wander, true);

		// draw hint window
		int width = gp.tileSize * 4;
		int height = gp.tileSize;
		int x = (int) (gp.tileSize * 1.5);
		int y = (gp.screenHeight / 2 + gp.tileSize + 8) - gp.tileSize;
		g2.drawImage(DeImage, x, y - 10, width, height, null);
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 14F));
		g2.setColor(Color.white);
		g2.drawString("[ESC] Back", x + 24, y + 20);

		// draw price widow
		int itemIndex = getItemIndexOnSlot(npcSlotCol, npcSlotRow);
		if (itemIndex < wander.inventory.size()) {

			width = gp.tileSize * 3;
			height = gp.tileSize;
			x = gp.screenWidth / 2 - (gp.tileSize * 9) / 2 - gp.tileSize * 2 + gp.tileSize * 9 + 16 + 30;
			y = (int) ((gp.screenHeight / 2 + gp.tileSize + 8) - gp.tileSize * 3);
			g2.drawImage(DeImage, x - 5, y, width, height, null);
			g2.drawImage(emeral, x + 20, y + 0, 48, 48, null);

			int price = wander.inventory.get(itemIndex).price;
			String text = "" + price;
			g2.drawString(text, x + 100, y + 32);

			// buy Items
			if (gp.keyH.enterPressed == true) {
				gp.keyH.enterPressed = false;

				int emeraldCount = 0;
				for (Entity item : gp.player.inventory) {
					if (item instanceof OBJ_Emerald) {
						emeraldCount += item.amount;
					}
				}

				// logic condidtion to check whether player have enough emeral to buy or not
				if (emeraldCount < price) {
					subState = 0;
					currentDialogue = "You need more Emeral !!!";
					gp.ui.displayedText = "";
					gp.ui.charIndex = 0;
					gp.ui.textCompleted = false;
					gp.gameState = gp.dialogueState;
				} else {
					if (gp.player.canObtainItem(wander.inventory.get(itemIndex)) == true) {
						int emeraldsToRemove = price;
						for (int i = 0; i < gp.player.inventory.size() && emeraldsToRemove > 0; i++) {
							Entity item = gp.player.inventory.get(i);

							if (item instanceof OBJ_Emerald) {
								if (item.amount > emeraldsToRemove) {
									item.amount -= emeraldsToRemove;
									emeraldsToRemove = 0;
								} else {
									emeraldsToRemove -= item.amount;
									gp.player.inventory.remove(i);
									i--; // Adjust index after removal
								}
							}
						}
					} else {
						subState = 0;
						currentDialogue = "You cannot carry more !!!";
						gp.ui.displayedText = "";
						gp.ui.charIndex = 0;
						gp.ui.textCompleted = false;
						gp.gameState = gp.dialogueState;
					}
				}
			}
		}
	}

	public void tradeSell() {

		// draw player inventory
		drawInventory(gp.player, true);

		int x;
		int y;
		int width;
		int height;

		// draw hint window
		width = gp.tileSize * 4;
		height = gp.tileSize;
		x = (int) (gp.tileSize * 1.5);
		y = (gp.screenHeight / 2 + gp.tileSize + 8) - gp.tileSize;
		g2.drawImage(DeImage, x, y - 10, width, height, null);
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 14F));
		g2.setColor(Color.white);
		g2.drawString("[ESC] Back", x + 24, y + 20);

		// draw price widow
		int itemIndex = getItemIndexOnSlot(playerSlotCol, playerSlotRow);
		if (itemIndex < gp.player.inventory.size()) {

			width = gp.tileSize * 3;
			height = gp.tileSize;
			x = gp.screenWidth / 2 - (gp.tileSize * 9) / 2 - gp.tileSize * 2 + gp.tileSize * 9 + 16 + 30;
			y = (int) ((gp.screenHeight / 2 + gp.tileSize + 8) - gp.tileSize * 0.5);
			g2.drawImage(DeImage, x - 5, y, width, height, null);
			g2.drawImage(emeral, x + 20, y + 0, 48, 48, null);

			int price = gp.player.inventory.get(itemIndex).price;
			String text = "" + price;
			g2.drawString(text, x + 100, y + 32);

			// sell Items
			if (gp.keyH.enterPressed == true) {
				gp.keyH.enterPressed = false;

				Entity itemToSell = gp.player.inventory.get(itemIndex);

				if (itemToSell == gp.player.currentWeapon || itemToSell == gp.player.currentShield
						|| itemToSell == gp.player.currentBall || itemToSell == gp.player.currentLight) {

					commandNum = 0;
					subState = 0;
					currentDialogue = "You can't sell equipped item!";
					gp.ui.displayedText = "";
					gp.ui.charIndex = 0;
					gp.ui.textCompleted = false;
					gp.gameState = gp.dialogueState;
				}
				// Check if trying to sell emerald
				else if (itemToSell instanceof OBJ_Emerald) {
					commandNum = 0;
					subState = 0;
					currentDialogue = "You can't sell emeralds!";
					gp.ui.displayedText = "";
					gp.ui.charIndex = 0;
					gp.ui.textCompleted = false;
					gp.gameState = gp.dialogueState;
				} else {
					if (gp.player.inventory.get(itemIndex).amount > 1) {
						gp.player.inventory.get(itemIndex).amount--;
					} else {
						gp.player.inventory.remove(itemIndex);
					}

					// Calculate sale price (half original, rounded up for odd numbers)
					int salePrice = (int) Math.ceil(itemToSell.price / 2.0);

					// Add emeralds to inventory
					boolean stacked = false;
					for (Entity item : gp.player.inventory) {
						if (item instanceof OBJ_Emerald) {
							item.amount += salePrice;
							stacked = true;
							break;
						}
					}

					if (!stacked && gp.player.inventory.size() < gp.player.maxinventorySize) {
						OBJ_Emerald emerald = new OBJ_Emerald(gp);
						emerald.amount = salePrice;
						gp.player.inventory.add(emerald);
					}
				}
			}
		}

	}

	public void drawTransition() {

		counter++;
		g2.setColor(new Color(0, 0, 0, counter * 5));
		g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

		if (counter == 50) { // The transition is done
			counter = 0;
			gp.gameState = gp.playState;
			gp.currentMap = gp.eHandler.tempMap;
			gp.player.worldX = gp.tileSize * gp.eHandler.tempCol;
			gp.player.worldY = gp.tileSize * gp.eHandler.tempRow;
			gp.eHandler.previousEventX = gp.player.worldX;
			gp.eHandler.previousEventY = gp.player.worldY;
			gp.changeArea();

		}
	}

	public void drawSleepScreen() {

		counter++;

		if (counter < 120) { // screen gets darker for the next 2s
			gp.eManager.lighting.filterAlpha += 0.01f;
			if (gp.eManager.lighting.filterAlpha > 1f) {
				gp.eManager.lighting.filterAlpha = 1f;
			}
		}

		if (counter >= 120) {
			gp.eManager.lighting.filterAlpha -= 0.01f;
			if (gp.eManager.lighting.filterAlpha <= 0f) {
				gp.eManager.lighting.filterAlpha = 0f;
				counter = 0;
				gp.eManager.lighting.dayState = gp.eManager.lighting.day;
				gp.eManager.lighting.dayCounter = 0;
				gp.gameState = gp.playState;
				gp.player.getImage();
			}
		}
	}

	public int getItemIndexOnSlot(int slotCol, int slotRow) {
		int itemIndex = slotCol + (slotRow * 7);
		return itemIndex;
	}

	// FOR DEFAULT DIALOGUE
	public void drawSubWindow(int x, int y, int width, int height) {

		Color c = new Color(0, 0, 0, 210);
		g2.setColor(c);
		g2.fillRoundRect(x, y, width, height, 35, 35);

		c = new Color(255, 255, 255);
		g2.setColor(c);
		g2.setStroke(new BasicStroke(5));
		g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);

	}

	public int getXforCenteredText(String text) {

		int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
		int x = gp.screenWidth / 2 - length / 2;

		return x;
	}

	public int getXforAlignToRightText(String text, int tailX) {
		int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
		int x = gp.screenWidth / 2 - length / 2;
		return x;
	}

	public void resetDialogueState() {
		displayedText = "";
		charIndex = 0;
		textTimer = 0;
		textCompleted = false;
	}

	public void drawMonsterLife() {
	    // Monster HP bar
	    for(int i = 0; i < gp.monster[1].length; i++) {
	        Entity monster = gp.monster[gp.currentMap][i];
	        if(monster != null && monster.inCamera()) {
	            
	            // BOSS health bar - check this FIRST
	            if(monster.boss == true) {
	                double oneScale = (double) gp.tileSize * 8 / monster.maxLife;
	                double hpBarValue = oneScale * monster.life;
	                
	                int x = gp.screenWidth/2 - gp.tileSize*4;
	                int y = gp.tileSize*10;

	                g2.setColor(new Color(35, 35, 35));
	                g2.fillRect(x - 1, y - 1, gp.tileSize*8 + 2, 22);

	                g2.setColor(new Color(255, 0, 0));
	                g2.fillRect(x, y, (int) hpBarValue, 20);
	                
	                g2.setFont(g2.getFont().deriveFont(Font.BOLD, 24f));
	                g2.setColor(Color.white);
	                g2.drawString(monster.name, x+4, y-10);
	            }
	            // REGULAR monster health bar - only for non-boss monsters
	            else if (monster.hpBarOn == true) {
	                double oneScale = (double) gp.tileSize / monster.maxLife;
	                double hpBarValue = oneScale * monster.life;

	                g2.setColor(new Color(35, 35, 35));
	                g2.fillRect(monster.getScreenX() - 1, monster.getScreenY() - 16, gp.tileSize, 10);

	                g2.setColor(new Color(255, 0, 0));
	                g2.fillRect(monster.getScreenX(), monster.getScreenY() - 15, (int) hpBarValue, 10);

	                monster.hpBarCounter++;

	                if (monster.hpBarCounter > 600) {
	                    monster.hpBarCounter = 0;
	                    monster.hpBarOn = false;
	                }
	            }
	        }
	    }
	}
}