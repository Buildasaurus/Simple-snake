package com.snake.Model;

import java.util.ArrayList;

import com.snake.Utils.LevelGenerator;

public class Region
{
    public ArrayList<Vector> coords;
    public ArrayList<Region> connectedRegions;
    public ArrayList<Vector> edgeTiles;
    int regionSize;
    int regionID = -1;
    boolean[][] map;
    public static int minAvailableRegionID = 0;

    public Region()
    {

    }

    public Region(ArrayList<Vector> coords, boolean[][] map)
    {
        this.coords = coords;
        regionSize = coords.size();
        connectedRegions = new ArrayList<Region>();

        edgeTiles = new ArrayList<Vector>();
        int height = map.length;
        int width = map[0].length;
        for (Vector tile : coords)
        {
            for (int dx = -1; dx <= 1; dx++)
            {
                for (int dy = -1; dy <= 1; dy++)
                {
                    if (!(dy == 0 && dx == 0) && (dy == 0 || dx == 0))
                    {
                        int modX = LevelGenerator.mod(tile.x + dx, width);
                        int modY = LevelGenerator.mod(tile.y + dy, height);
                        if (map[modY][modX])
                        {
                            edgeTiles.add(tile); //some tiles are added multiple times.
                        }
                    }
                }
            }
        }
    }

    public void setRegionID(int id)
    {
        if(id < regionID || regionID == -1)
        {
            regionID = id;
            for (Region region : connectedRegions) {
                region.setRegionID(id);
            }
        }
    }

    public boolean IsConnected(Region otherRoom)
    {
        return regionID == otherRoom.regionID && regionID != -1;
    }

    public static void ConnectRooms(Region roomA, Region roomB)
    {
        int regionID = Math.min(roomA.regionID, roomB.regionID);
        if (regionID == -1)
        {
            regionID = minAvailableRegionID;
            minAvailableRegionID++;
        }
        roomA.connectedRegions.add(roomB);
        roomA.setRegionID(regionID);
        roomB.connectedRegions.add(roomA);
        roomB.setRegionID(regionID);
    }
}
