package a2.newdc.assets;


import sage.model.loader.ogreXML.OgreXMLParser;
import sage.scene.TriMesh;

import java.io.File;

public class AssetBuilder
{
    private static AssetBuilder builder;
    private static OgreXMLParser parser;

    private AssetBuilder()
    {
        builder = this;
        parser = new OgreXMLParser();
    }

    public static TriMesh BuildMesh(String fileName, String name)
    {
        if (builder == null)
            builder = new AssetBuilder();

        if (!new File(fileName).exists())
            throw new RuntimeException("Cannot find: " + fileName);
        try
        {
            // just a TriMesh for now
            TriMesh tm = (TriMesh)parser.loadModel(fileName, fileName, fileName).getChild("NoName");
            Mesh mesh = new Mesh(name, tm);
            if (mesh != null)
                return mesh;
            else
                throw new RuntimeException("Failed to load a child named NoName");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    private static class Mesh extends TriMesh
    {
        public Mesh(String name, TriMesh tm)
        {
            setName(name);

            setVertexBuffer(tm.getVertexBuffer());
            setColorBuffer(tm.getColorBuffer());
            setIndexBuffer(tm.getIndexBuffer());
        }
    }

}
