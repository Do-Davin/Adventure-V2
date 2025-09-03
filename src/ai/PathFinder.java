package ai;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Comparator;

import entity.Entity;
import main.GamePanel;

public class PathFinder {
    GamePanel gp;
    Node[][] node;
    ArrayList<Node> openList = new ArrayList<>();
    public ArrayList<Node> pathList = new ArrayList<>();
    Node startNode;
    public Node goalNode;
    Node currentNode;
    boolean goalReached = false;
    int step = 0;
    
    // Add priority queue for better performance
    private PriorityQueue<Node> openQueue;
    
    public PathFinder(GamePanel gp) {
        this.gp = gp;
        instantiateNodes();
        
        // Initialize priority queue with comparator
        openQueue = new PriorityQueue<>(new Comparator<Node>() {
            @Override
            public int compare(Node n1, Node n2) {
                return Integer.compare(n1.fCost, n2.fCost);
            }
        });
    }
    
    public void instantiateNodes() {
        node = new Node[gp.maxWorldCol][gp.maxWorldRow];
        
        for(int col = 0; col < gp.maxWorldCol; col++) {
            for(int row = 0; row < gp.maxWorldRow; row++) {
                node[col][row] = new Node(col, row);
            }
        }
    }
    
    public void resetNodes() {
        // Clear lists efficiently
        openList.clear();
        openQueue.clear();
        pathList.clear();
        
        // Only reset necessary node states
        for(int col = 0; col < gp.maxWorldCol; col++) {
            for(int row = 0; row < gp.maxWorldRow; row++) {
                Node n = node[col][row];
                n.open = false;
                n.checked = false;
                // Don't reset solid here - it's set in setNodes
            }
        }
        
        goalReached = false;
        step = 0;
    }
    
    public void setNodes(int startCol, int startRow, int goalCol, int goalRow, Entity entity) {
    	// Check if coordinates are valid
        if (startCol < 0 || startCol >= gp.maxWorldCol || startRow < 0 || startRow >= gp.maxWorldRow ||
            goalCol < 0 || goalCol >= gp.maxWorldCol || goalRow < 0 || goalRow >= gp.maxWorldRow) {
            return; // Invalid coordinates, don't proceed
        }
        resetNodes();
        
        startNode = node[startCol][startRow];
        currentNode = startNode;
        goalNode = node[goalCol][goalRow];
        
        // Set solid nodes more efficiently
        for(int col = 0; col < gp.maxWorldCol; col++) {
            for(int row = 0; row < gp.maxWorldRow; row++) {
                Node currentNode = node[col][row];
                
                // Check tile collision
                int tileNum = gp.tileM.mapTileNum[gp.currentMap][col][row];
                currentNode.solid = gp.tileM.tile[tileNum].collision;
                
                // Check interactive tiles - only if not already solid
                if(!currentNode.solid) {
                    for(int i = 0; i < gp.iTile[1].length; i++) {
                        if(gp.iTile[gp.currentMap][i] != null && 
                           gp.iTile[gp.currentMap][i].destructible == true &&
                           gp.iTile[gp.currentMap][i].worldX / gp.tileSize == col &&
                           gp.iTile[gp.currentMap][i].worldY / gp.tileSize == row) {
                            currentNode.solid = true;
                            break;
                        }
                    }
                }
                
                getCost(currentNode);
            }
        }
        
        openList.add(currentNode);
        openQueue.add(currentNode);
        currentNode.open = true;
    }
    
    public void getCost(Node node) {
        // Manhattan distance is fine for grid-based movement
        node.gCost = Math.abs(node.col - startNode.col) + Math.abs(node.row - startNode.row);
        node.hCost = Math.abs(node.col - goalNode.col) + Math.abs(node.row - goalNode.row);
        node.fCost = node.gCost + node.hCost;
    }
    
    public boolean search() {
        int maxSteps = 200; // Reduced from 500 for performance
        
        while(!goalReached && step < maxSteps && !openQueue.isEmpty()) {
            // Get node with lowest fCost from priority queue
            currentNode = openQueue.poll();
            
            if(currentNode == goalNode) {
                goalReached = true;
                trackThePath();
                return true;
            }
            
            currentNode.checked = true;
            openList.remove(currentNode);
            
            // Check adjacent nodes
            checkNode(currentNode.col, currentNode.row - 1); // Up
            checkNode(currentNode.col, currentNode.row + 1); // Down
            checkNode(currentNode.col - 1, currentNode.row); // Left
            checkNode(currentNode.col + 1, currentNode.row); // Right
            
            step++;
        }
        
        return goalReached;
    }
    
    private void checkNode(int col, int row) {
        if(col < 0 || col >= gp.maxWorldCol || row < 0 || row >= gp.maxWorldRow) {
            return;
        }
        
        Node nodeToCheck = node[col][row];
        
        if(!nodeToCheck.open && !nodeToCheck.checked && !nodeToCheck.solid) {
            nodeToCheck.open = true;
            nodeToCheck.parent = currentNode;
            openList.add(nodeToCheck);
            openQueue.add(nodeToCheck);
            getCost(nodeToCheck);
        }
    }
    
    public void trackThePath() {
        Node current = goalNode;
        
        while(current != startNode) {
            pathList.add(0, current);
            current = current.parent;
        }
    }
    
    public void clearPath() {
        if (pathList != null) pathList.clear();
        if (openList != null) openList.clear();
        if (openQueue != null) openQueue.clear();
        goalReached = false;
    }
}