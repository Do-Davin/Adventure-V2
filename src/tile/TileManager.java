package tile;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.UtilityTool;

public class TileManager {

	GamePanel gp;
	public Tile[] tile;
	public int mapTileNum[][][];
	boolean drawPath = true;
	ArrayList<String> fileNames = new ArrayList<>();
	ArrayList<String> collisionStatus = new ArrayList<>();
	
	public TileManager(GamePanel gp) {
		
		this.gp = gp;
		
		// read tiledata file
		InputStream is = getClass().getResourceAsStream("/maps/tiledata.txt");
		BufferedReader br =  new BufferedReader(new InputStreamReader(is));
		
		// get tile name and its collision
		String line;
		try {
			while((line = br.readLine()) != null) {
				fileNames.add(line);
				collisionStatus.add(br.readLine());
			}
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		
		// initialize the tile array base on fileNames size
		tile = new Tile[fileNames.size()];
		getTileImage();
		
		try {
	        is = getClass().getResourceAsStream("/maps/world02.txt");
	        br = new BufferedReader(new InputStreamReader(is));
	        
	        // Read first line to get column count
	        String firstLine = br.readLine();
	        if (firstLine != null) {
	            String maxTile[] = firstLine.split(" ");
	            gp.maxWorldCol = maxTile.length;
	            
	            // Count rows
	            int rowCount = 1;
	            while (br.readLine() != null) {
	                rowCount++;
	            }
	            gp.maxWorldRow = rowCount;
	            
	            br.close();
	        }
	        
	        // Initialize map array with proper dimensions
	        mapTileNum = new int[gp.maxMap][gp.maxWorldCol][gp.maxWorldRow];
	        
	    } catch(IOException e) {
	        System.out.println("Exception reading map dimensions!");
	        e.printStackTrace();
	        // Set safe default dimensions
	        gp.maxWorldCol = 50;
	        gp.maxWorldRow = 50;
	        mapTileNum = new int[gp.maxMap][gp.maxWorldCol][gp.maxWorldRow];
	    }
		
		loadMap("/maps/world02.txt", 0);
		loadMap("/maps/wander_house.txt", 1);
		loadMap("/maps/frozen.txt", 2);
		loadMap("/maps/fire.txt", 3);
		loadMap("/maps/final.txt", 4);
	}
	
	public void getTileImage() {
		
		for(int i =0 ; i<fileNames.size();i++) {
			
			String fileName;
			boolean collision;
			
			// get a file name
			fileName = fileNames.get(i);
			
			// collision status
			if(collisionStatus.get(i).equals("true")) {
				collision = true;
			}
			else {
				collision = false;
			}
			setup(i,fileName,collision);
		}
	}
	
	public void setup(int index, String image, boolean collision) {
		
		UtilityTool uTool = new UtilityTool();
		try {
			
			tile[index] =new Tile();
			tile[index].image = ImageIO.read(getClass().getResourceAsStream("/tiles/"+image));
			tile[index].image = uTool.scaleImage(tile[index].image,gp.tileSize,gp.tileSize);
			tile[index].collision = collision;
			
		}catch(IOException e) {
			
		}
	}
	
	public void loadMap(String filePath, int map) {
	    try {
	        InputStream is = getClass().getResourceAsStream(filePath);
	        BufferedReader br = new BufferedReader(new InputStreamReader(is));

	        int row = 0;

	        while (row < gp.maxWorldRow) {
	            String line = br.readLine(); // read one line from map file
	            if (line == null) break; // safety: stop if file is shorter than expected

	            String numbers[] = line.split(" ");

	            for (int col = 0; col < gp.maxWorldCol; col++) {
	                if (col < numbers.length) {
	                    int num = Integer.parseInt(numbers[col]);
	                    mapTileNum[map][col][row] = num;
	                } else {
	                    // Optional: fill with default tile if missing
	                    mapTileNum[map][col][row] = 0; // or any safe default
	                }
	            }

	            row++;
	        }

	        br.close(); // close the reader after reading
	    } catch (Exception e) {
	        e.printStackTrace(); // print error if file not found or bad format
	    }
	}

	
	public void draw(Graphics2D g2) { // Draw world map, camera setting
	
		
		int worldCol = 0;
		int worldRow = 0;
		
		while(worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) {
			
			int tileNum = mapTileNum[gp.currentMap][worldCol][worldRow];
			
			int worldX = worldCol * gp.tileSize; // For example worldX = worldCol++ x 48
			int worldY = worldRow * gp.tileSize; // For example worldY = worldRow++ x 48
			/*
			 	+ gp.player.screenX, because we want the player still origin at the center 
			 	in your screen while you are walking
			 */
			int screenX = worldX - gp.player.worldX + gp.player.screenX; 
			int screenY = worldY - gp.player.worldY + gp.player.screenY;
			
			/*
			 	Avoiding draw world that far away from screen
			 		- Improve performance with larger map ex. 10000 x 10000, 
			 		  it not draw 10000 x 10000 at the same time 
			 */
			
			if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
			   worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
		       worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
			   worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
				
				g2.drawImage(tile[tileNum].image, screenX, screenY, null);
				
			}
		
			worldCol++;
			
			if(worldCol == gp.maxWorldCol) {
				worldCol = 0;
				worldRow ++;
			}
		}
	}
}