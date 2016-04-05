package a2.newdc.assets;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class AssetInfo
{
    public String assetsPath;
    public HashMap<String, TileAsset> tiles;
    public HashMap<String, RoomAsset> rooms;
    public HashMap<String, WallAsset> walls;
    public HashMap<String, PlayableAsset> playables;
    public HashMap<String, NPCFriendlyAsset> npcFriendlies;
    public HashMap<String, NPCEnemyAsset> npcEnemies;
    public HashMap<String, ObjectInteractableAsset> objectInteractables;
    public HashMap<String, ObjectNonInteractableAsset> objectNonInteractables;

    public AssetInfo(String path)
    {
        //assetsPath = new File("").getAbsolutePath() + "\\assets\\";
        assetsPath = path;
        String reg = "_";

        tiles = new HashMap<>();
        for (String asset : GetNamesOfAssets(Asset.FamilyType.Tile))
        {
            TileAsset tile = new TileAsset(asset.split(reg));
            if (!tiles.containsKey(tile.getName()))
                tiles.put(tile.getName(), tile);
        }

        rooms = new HashMap<>();
        for (String asset : GetNamesOfAssets(Asset.FamilyType.Room))
        {
            RoomAsset room = new RoomAsset(asset.split(reg));
            if (!rooms.containsKey(room.getName()))
                rooms.put(room.getName(), room);
        }

        walls = new HashMap<>();
        for (String asset : GetNamesOfAssets(Asset.FamilyType.Wall))
        {
            WallAsset wall = new WallAsset(asset.split(reg));
            if (!walls.containsKey(wall.getName()))
                walls.put(wall.getName(), wall);
        }

        playables = new HashMap<>();
        for (String asset : GetNamesOfAssets(Asset.FamilyType.Playable))
        {
            PlayableAsset playable = new PlayableAsset(asset.split(reg));
            if (!playables.containsKey(playable.getName()))
                playables.put(playable.getName(), playable);
        }

        npcFriendlies = new HashMap<>();
        for (String asset : GetNamesOfAssets(Asset.FamilyType.NPCFriendly))
        {
            NPCFriendlyAsset friendly = new NPCFriendlyAsset(asset.split(reg));
            if (!npcFriendlies.containsKey(friendly.getName()))
                npcFriendlies.put(friendly.getName(), friendly);
        }

        npcEnemies = new HashMap<>();
        for (String asset : GetNamesOfAssets(Asset.FamilyType.NPCEnemy))
        {
            NPCEnemyAsset enemy = new NPCEnemyAsset(asset.split(reg));
            if (!npcEnemies.containsKey(enemy.getName()))
                npcEnemies.put(enemy.getName(), enemy);
        }

        objectInteractables = new HashMap<>();
        for (String asset : GetNamesOfAssets(Asset.FamilyType.ObjectInteractable))
        {
            ObjectInteractableAsset interactable = new ObjectInteractableAsset(asset.split(reg));
            if (!objectInteractables.containsKey(interactable.getName()))
                objectInteractables.put(interactable.getName(), interactable);
        }

        objectNonInteractables = new HashMap<>();
        for (String asset : GetNamesOfAssets(Asset.FamilyType.ObjectNonInteractable))
        {
            ObjectNonInteractableAsset nonInteractable = new ObjectNonInteractableAsset(asset.split(reg));
            if (!objectNonInteractables.containsKey(nonInteractable.getName()))
                objectNonInteractables.put(nonInteractable.getName(), nonInteractable);
        }
    }

    private ArrayList<String> GetNamesOfAssets(Asset.FamilyType familyType)
    {
        ArrayList<String> namesList = new ArrayList<>();
        System.out.println(assetsPath + familyType + "s\\");

        File dir = new File(assetsPath + familyType + "s\\");
        if (!dir.isDirectory())
            dir.mkdir();
        for (File file : dir.listFiles())
        {
            if (file.getName().endsWith(".mesh.xml"))
            {
                //System.out.println(file.getPath());
                namesList.add(file.getPath());
            }
        }
        return namesList;
    }
}