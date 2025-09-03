package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import javax.swing.JPanel;

import ai.PathFinder;
import entity.Entity;
import entity.Player;
import entity.SnowParticle;
import environment.EnvironmentManager;
import object.OBJ_Portal;
import tile.Map;
import tile.TileManager;
import tile_interactive.InteractiveTile;

public class GamePanel extends JPanel implements Runnable {

	private Random random = new Random();
	// Screen Panel
	final int originalTileSize = 16; // 16x 16
	final int scale = 3;

	public final int tileSize = originalTileSize * scale; // make element 48x48
	public final int maxScreenRow = 12;
	public final int maxScreenCol = 16;
	public final int screenWidth = tileSize * maxScreenCol; // 768 px of screen
	public final int screenHeight = tileSize * maxScreenRow; // 576 px

	// WORLD SETTINGS
	public int maxWorldCol;
	public int maxWorldRow ;

	public final int maxMap = 10;
	public int currentMap = 0;

	public final int worldWidth = tileSize * maxWorldCol; // Can be deleted
	public final int worldHeight = tileSize * maxWorldRow;// Can be deleted

	// Full Screen
	int screenWidth2 = screenWidth;
	int screenHeight2 = screenHeight;
	BufferedImage tempScreen;
	Graphics2D g2;
	// FOR MENU ACTION
	public boolean fullScreenOn = true;

	// FPS
	int FPS = 60;

	// SYSTEM
	public TileManager tileM = new TileManager(this);
	public KeyHandler keyH = new KeyHandler(this);
	public Sound music = new Sound();
	public Sound se = new Sound();
	public EventHandler eHandler = new EventHandler(this);

	public CollisionChecker cChecker = new CollisionChecker(this);
	public AssetSetter aSetter = new AssetSetter(this);
	public UI ui = new UI(this);
	Config config = new Config(this);
	public PathFinder pFinder = new PathFinder(this);
	EnvironmentManager eManager = new EnvironmentManager(this);
	Map map = new Map(this);
	Thread gameThread; // to start and to stop
	public CutscenceManager csManager = new CutscenceManager(this);

	// Entity and Object
	public Player player = new Player(this, keyH);
	public Entity obj[][] = new Entity[maxMap][30];// object array
	public Entity npc[][] = new Entity[maxMap][10];
	public Entity monster[][] = new Entity[maxMap][20];
	public InteractiveTile iTile[][] = new InteractiveTile[maxMap][300];
	public Entity projectile[][] = new Entity[maxMap][20];
	public ArrayList<Entity> projectileList = new ArrayList<>();
	public ArrayList<Entity> particleList = new ArrayList<>();
	// This list store player npc obj
	ArrayList<Entity> entityList = new ArrayList<>();
	
	public ArrayList<SnowParticle> snowParticles = new ArrayList<>();
	private final int MAX_SNOW_PARTICLES = 200;
	private boolean snowEffectActive = false;
	// GAME STATE
	public int gameState;
	public final int titleState = 0;
	public final int playState = 1;
	public final int pauseState = 2;
	public final int characterState = 4;
	// For dialogues
	public final int dialogueState = 3;
	public final int optionsState = 5;
	public final int gameOverState = 6;
	public final int transitionState = 7;
	public final int tradeState = 8;
	public final int sleepState = 9;
	public final int mapState = 10;
	public final int cutscenceState = 11;
	public boolean bossBattleOn = false;
	
	// AREA
	public int currentArea;
	public int nextArea;
	public final int outside = 50;
	public final int indoor = 51;
	public final int dungeon = 52;

	public GamePanel() {
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);
		this.addKeyListener(keyH);
		this.setFocusable(true);
		currentArea = outside;
		nextArea = outside;
	}

	public void setupGame() {
		aSetter.setObject();
		aSetter.setNpc();
		aSetter.setMonster();
		aSetter.setInteractiveTile();
		eManager.setup();

		gameState = titleState;
		currentArea = outside;

		tempScreen = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
		g2 = (Graphics2D) tempScreen.getGraphics();

		if (fullScreenOn == true) {
			setFullScreen(); // command this if want to turn off fullscreen mode
		}
	}

	public void retry() {
		bossBattleOn = false;
		stopMusic();
		currentMap = 0;
		player.setDefaultPositions();
		player.restoreLife();
		aSetter.setNpc();
		aSetter.setMonster();
		
		removeBossDoor();
		currentArea = outside;
		nextArea = outside;
		playMusic(0);

	}

	public void restart() {

		removeTempEntity();
		removeBossDoor();
		bossBattleOn = false;
		currentMap =0;
		player.setDefaultValues();
		player.setDefaultPositions();
		player.restoreLife();
		player.setItems();
		aSetter.setNpc();
		aSetter.setMonster();
		aSetter.setInteractiveTile();
		
		Progress.GolemDefeated = false;
		stopMusic();
		playMusic(0);
	}

	public void setFullScreen() {
		// Get screen size from local device
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();

		if (fullScreenOn) {
			gd.setFullScreenWindow(Main.window);
		} else {
			// Otherwise, stay windowed
			gd.setFullScreenWindow(null);
			Main.window.setSize(screenWidth, screenHeight); // Restore to windowed size
			Main.window.setLocationRelativeTo(null);
		}

		// Update actual width/height based on result
		screenWidth2 = Main.window.getWidth();
		screenHeight2 = Main.window.getHeight();

	}

	public void startGameThread() {
		gameThread = new Thread(this);
		gameThread.start();
	}

	// delta method
	public void run() {

		double drawInterval = 1000000000 / FPS;
		double delta = 0;
		long lastTime = System.nanoTime();
		long currentTime;
		long timer = 0;
		int drawCount = 0;

		while (gameThread != null) {

			currentTime = System.nanoTime();

			delta += (currentTime - lastTime) / drawInterval;
			timer += (currentTime - lastTime);
			lastTime = currentTime;

			if (delta >= 1) {
				update();
//				repaint();
				drawToTempScreen();
				drawToScreen();
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
				}
				delta--;
				drawCount++;
			}

			if (timer >= 1000000000) {				drawCount = 0;
				timer = 0;
			}
		}
	}

	public void update() {
	    if (gameState == playState) {
	        // Player
	        player.update();
	        
	     // OBJECT - Update only portal objects (to avoid blinking other objects)
	        for (int i = 0; i < obj[1].length; i++) {
	            if (obj[currentMap][i] != null && obj[currentMap][i] instanceof OBJ_Portal) {
	                obj[currentMap][i].update();  // Only portals get updated
	            }
	        }
	        
	        // Npc
	        for (int i = 0; i < npc[1].length; i++) {
	            if (npc[currentMap][i] != null) {
	                npc[currentMap][i].update();
	            }
	        }

	        if (monster[currentMap] != null) {
	            for (int i = 0; i < monster[currentMap].length; i++) {
	                if (monster[currentMap][i] != null) {
	                    if (monster[currentMap][i].alive == true && monster[currentMap][i].die == false) {
	                        monster[currentMap][i].update();
	                    }
	                    if (monster[currentMap][i].alive == false) {
	                        // Clear pathfinding before removing
	                        if (monster[currentMap][i].onPath) {
	                            pFinder.clearPath();
	                        }
	                        monster[currentMap][i].checkDrop();
	                        monster[currentMap][i] = null;
	                    }
	                }
	            }
	        }

	        // projectile
	        for (int i = 0; i < projectile[1].length; i++) {
	            if (projectile[currentMap][i] != null) {
	                if (projectile[currentMap][i].alive == true) {
	                    projectile[currentMap][i].update();
	                }
	                if (projectile[currentMap][i].alive == false) {
	                    projectile[currentMap][i] = null;
	                }
	            }
	        }
	        
			// Particle Improvement
			for (int i = particleList.size() - 1; i >= 0; i--) {
				Entity particle = particleList.get(i);
				if (particle != null) {
					if (particle.alive) {
						particle.update();
					} else {
						particleList.remove(i);
					}
				}
			}

	        for (int i = 0; i < iTile[1].length; i++) {
	            if (iTile[currentMap][i] != null) {
	                iTile[currentMap][i].update();
	            }
	        }
	        
			// SNOW PARTICLES Improvement
			if (snowEffectActive && currentMap == 2) { // Map 2 is the frozen map
				for (int i = snowParticles.size() - 1; i >= 0; i--) {
					SnowParticle sp = snowParticles.get(i);
					if (sp != null) {
						sp.update();
					}
				}
	            
	            // Add new particles occasionally
	            if (snowParticles.size() < MAX_SNOW_PARTICLES && random.nextInt(100) < 10) {
	                snowParticles.add(new SnowParticle(this));
	            }
	        }
	        
	        eManager.update();
	    }

	    if (gameState == pauseState) {
	        // Pause state logic
	    }
	    if(gameState == cutscenceState) {
//	    	csManager.u
	    }
	}

	public void drawToTempScreen() {
	    // TITLE SCREEN
	    if (gameState == titleState) {
	        ui.draw(g2);
	    }
	    
	    // MAP SCREEN
	    else if(gameState == mapState) {
	    	map.drawFullMapScreen(g2);
	    }
	    
	    // OTHERS
	    else {
	        tileM.draw(g2); // draw the world before player !!

	        // Draw INTERACTIVE TILE
	        for (int i = 0; i < iTile[1].length; i++) {
	            if (iTile[currentMap][i] != null) {
	                iTile[currentMap][i].draw(g2);
	            }
	        }
	        
	        // Draw OBJECTS - Draw FIRST (always behind entities)
	        for (int i = 0; i < obj[1].length; i++) {
	            if (obj[currentMap][i] != null) {
	                obj[currentMap][i].draw(g2); // Draw objects directly here
	            }
	        }

	        // Now add ONLY entities (player, NPCs, monsters) to the list for sorting
			entityList.clear(); // Clear the list before adding entities
	        entityList.add(player);

	        // NPC
	        for (int i = 0; i < npc[1].length; i++) {
	            if (npc[currentMap][i] != null) {
	                entityList.add(npc[currentMap][i]);
	            }
	        }

	        // MONSTER
	        for (int i = 0; i < monster[1].length; i++) {
	            if (monster[currentMap][i] != null) {
	                entityList.add(monster[currentMap][i]);
	            }
	        }

			// PROJECTILES
	        for (int i = 0; i < projectile[1].length; i++) {
	            if (projectile[currentMap][i] != null) {
	                entityList.add(projectile[currentMap][i]);
	            }
	        }

			// PARTICLES
	        for (int i = 0; i < particleList.size(); i++) {
	            if (particleList.get(i) != null) {
	                entityList.add(particleList.get(i));
	            }
	        }

	        // Sort the entity to check their coordinate
	        Collections.sort(entityList, new Comparator<Entity>() {
	            @Override
	            public int compare(Entity e1, Entity e2) {
	                int result = Integer.compare(e1.worldY, e2.worldY);
	                return result;
	            }
	        });
	        
			for(Entity entity : entityList) {
				entity.draw(g2);
			}
			// SNOW PARTICLES Improvement
			if (snowEffectActive && currentMap == 2) {
				for (int i = snowParticles.size() - 1; i >= 0; i--) {
					SnowParticle sp = snowParticles.get(i);
					if (sp != null) {
						sp.draw(g2);
					}
				}
			}
	        
	        // ENVIRONMENT
	        eManager.draw(g2);
	        
	        // MINI MAP
	        map.drawMiniMap(g2);
	        
	        // cut scence
	        csManager.draw(g2);

	        // UI
	        ui.draw(g2);
	    }
	}

	public void drawToScreen() {

		Graphics g = getGraphics();
		g.drawImage(tempScreen, 0, 0, screenWidth2, screenHeight2, null);
		g.dispose();
	}

	// for background music
	public void playMusic(int i) {

		music.setFile(i); // get sound from file
		music.play(); // play sound
		music.loop(); // loop sound through game
	}

	public void stopMusic() {

		music.stop();
	}

	// for item sound effect
	public void playSE(int i) {

		se.setFile(i);
		se.play();
	}
	
	public void startSnowEffect() {
	    snowEffectActive = true;
	    snowParticles.clear();
	    // Pre-populate with some particles
	    for (int i = 0; i < MAX_SNOW_PARTICLES / 2; i++) {
	        snowParticles.add(new SnowParticle(this));
	    }
	}

	public void stopSnowEffect() {
	    snowEffectActive = false;
	    snowParticles.clear();
	}
	
	public void changeArea() {
	    if(nextArea != currentArea) {
	        stopMusic();
	        
	        // FIX: Only play music if NOT in boss battle
	        if(!bossBattleOn) {
	            if(nextArea == outside) {
	                playMusic(0); // Overworld music
	            }
	            else if(nextArea == indoor) {
	                // Indoor music
	                // playMusic(x);
	            }
	            else if(nextArea == dungeon) {
	                playMusic(27); // Dungeon music
	            }
	        }
	        // If in boss battle, the boss fight music should continue playing
	    }
	    currentArea = nextArea;
	    aSetter.setMonster();
	}
	
	public void removeTempEntity() {
		
		for(int mapNum = 0; mapNum < maxMap;mapNum++) {
			for(int i =0; i<obj[1].length;i++) {
				if(obj[mapNum][i] != null && obj[mapNum][i].temp == true) {
					obj[mapNum][i] = null;
				}
			}
		}
	}
	
	public void removeBossDoor() {
	    for (int mapNum = 0; mapNum < maxMap; mapNum++) {
	        for (int i = 0; i < obj[1].length; i++) {
	            if (obj[mapNum][i] != null && obj[mapNum][i].temp == true) {
	                obj[mapNum][i] = null;
	            }
	        }
	    }
	}
}