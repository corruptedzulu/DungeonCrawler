package a2.assets;

import sage.model.loader.OBJLoader;
import sage.model.loader.ogreXML.OgreXMLParser;
import sage.scene.*;
import sage.texture.Texture;
import sage.texture.TextureManager;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;

public class AssetBuilder
{
    private static AssetBuilder builder;
    private static OBJLoader loader;
    private static OgreXMLParser parser;
    private static HashMap<String, Texture> textures;

    private AssetBuilder()
    {
        builder = this;
        loader = new OBJLoader();
        parser = new OgreXMLParser();
        textures = new HashMap<>();
    }

    public static TriMesh BuildMesh(String fileName, String name)
    {
        if (builder == null)
            builder = new AssetBuilder();

        if (!new File(fileName).exists())
            throw new RuntimeException("Cannot find: " + fileName);
        try
        {
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
                tm.setTexture(getTexture(textureName));
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

    public static Group BuildMeshWithAni(String fileName, String name, String textureName) throws Exception
    {
        Group group = null;
        if (fileName.endsWith(".mesh.xml"))
        {
            System.out.println("trying ogre: " + fileName);
            String mesh = fileName;
            String material = fileName.substring(0, fileName.length() - 9) + ".material";
            String skeleton = fileName.substring(0, fileName.length() - 9) + ".skeleton.xml";
            String path = fileName.substring(0, fileName.lastIndexOf("\\")+1);
            group = parser.loadModel(mesh, material, skeleton,path);
            Iterator<SceneNode> itr = group.getChildren();
            while (itr.hasNext())
            {
                Model3DTriMesh submesh = (Model3DTriMesh) itr.next();
                if (textureName != "")
                {
                    submesh.setTexture(getTexture(textureName));
                }
            }
        }
        return group;
    }

    public static SkyBox BuildSkyBox(String filesPath, String name, float xExtent, float yExtent, float zExtent, boolean zBufferEnabled)
    {
        if (builder == null)
            builder = new AssetBuilder();

        SkyBox skyBox = new SkyBox(name, xExtent, yExtent, zExtent);
        skyBox.setTexture(SkyBox.Face.Up, getTexture(filesPath + "up.jpg"));
        skyBox.setTexture(SkyBox.Face.Down, getTexture(filesPath + "down.jpg"));
        skyBox.setTexture(SkyBox.Face.North, getTexture(filesPath + "north.jpg"));
        skyBox.setTexture(SkyBox.Face.South, getTexture(filesPath + "south.jpg"));
        skyBox.setTexture(SkyBox.Face.East, getTexture(filesPath + "east.jpg"));
        skyBox.setTexture(SkyBox.Face.West, getTexture(filesPath + "west.jpg"));
        skyBox.setZBufferStateEnabled(zBufferEnabled);
        return skyBox;
    }

    private static Texture getTexture(String name)
    {
        if (!textures.containsKey(name))
            textures.put(name, TextureManager.loadTexture2D(name));
        return textures.get(name);
    }
}