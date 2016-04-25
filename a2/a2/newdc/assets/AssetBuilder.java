package a2.newdc.assets;


import sage.model.loader.OBJLoader;
import sage.model.loader.ogreXML.OgreXMLParser;
import sage.scene.Group;
import sage.scene.SkyBox;
import sage.scene.TriMesh;
import sage.texture.TextureManager;

import java.io.File;

public class AssetBuilder
{
    private static AssetBuilder builder;
    private static OBJLoader loader;
    //private static OgreXMLParser parser;

    private AssetBuilder()
    {
        builder = this;
        loader = new OBJLoader();
        //parser = new OgreXMLParser();
    }

    public static TriMesh BuildMesh(String fileName, String name)
    {
        if (builder == null)
            builder = new AssetBuilder();

        if (!new File(fileName).exists())
            throw new RuntimeException("Cannot find: " + fileName);
        try
        {
            //System.out.println(fileName);
            TriMesh tm = loader.loadModel(fileName);
            tm.setName(name);
            return tm;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static TriMesh BuildMesh(String fileName, String name, String textureName)
    {
        if (builder == null)
            builder = new AssetBuilder();

        if (!new File(fileName).exists())
            throw new RuntimeException("Cannot find: " + fileName);
        try
        {
            TriMesh tm = loader.loadModel(fileName);
            tm.setName(name);
            if (textureName != "")
            {
                //System.out.println("trying to set texture with name:\n--" + textureName);
                tm.setTexture(TextureManager.loadTexture2D(textureName));
            }
            tm.updateRenderStates();
            return tm;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static SkyBox BuildSkyBox(String filesPath, String name, float xExtent, float yExtent, float zExtent, boolean zBufferEnabled)
    {
        if (builder == null)
            builder = new AssetBuilder();

        SkyBox skyBox = new SkyBox(name, xExtent, yExtent, zExtent);
        skyBox.setTexture(SkyBox.Face.Up,  TextureManager.loadTexture2D(filesPath + "up.jpg"));
        skyBox.setTexture(SkyBox.Face.Down, TextureManager.loadTexture2D(filesPath + "down.jpg"));
        skyBox.setTexture(SkyBox.Face.North, TextureManager.loadTexture2D(filesPath + "north.jpg"));
        skyBox.setTexture(SkyBox.Face.South, TextureManager.loadTexture2D(filesPath + "south.jpg"));
        skyBox.setTexture(SkyBox.Face.East, TextureManager.loadTexture2D(filesPath + "east.jpg"));
        skyBox.setTexture(SkyBox.Face.West, TextureManager.loadTexture2D(filesPath + "west.jpg"));
        skyBox.setZBufferStateEnabled(zBufferEnabled);
        return skyBox;
    }
}
