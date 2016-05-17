package a2.newdc.assets;

import graphicslib3D.Matrix3D;
import graphicslib3D.Point3D;
import graphicslib3D.Quaternion;
import sage.scene.Group;
import sage.scene.SkyBox;
import sage.scene.TriMesh;
import java.io.File;
import java.util.HashMap;
import java.util.Random;

public abstract class Asset
{
    private String fileName = "fileName";
    private String textureName = "textureName";
    private String name = "name";

    private int version = 0;
    private HashMap<String, String> textures;
    private Random random = new Random();
    private boolean isRandomTexture = false;

    public enum FamilyType
    {
        Tile,
        Room,
        Wall,
        Playable,
        NPCFriendly,
        NPCEnemy,
        ObjectInteractable,
        ObjectNonInteractable,
        SkyBox
    }

    public String getTextureName()
    {
        return textureName;
    }

    public void setTextureName(String textureName)
    {
        this.textureName = textureName;
    }

    public String getFileName()
    {
        return fileName;
    }

    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getVersion()
    {
        return version;
    }

    public void setVersion(int version)
    {
        this.version = version;
    }

    public boolean isRandomTexture()
    {
        return isRandomTexture;
    }

    public void setRandomTexture(boolean isRandomTexture)
    {
        this.isRandomTexture = isRandomTexture;
    }

    public TriMesh make(Matrix3D position, Matrix3D rotation, String name)
    {
        TriMesh newNode = AssetBuilder.BuildMesh(getFileName(), getName(), getTexture(name));
        newNode.setLocalTranslation(position);
        newNode.setLocalRotation(rotation);
        return newNode;
    }

    public TriMesh make(Point3D position, String name)
    {
        TriMesh newNode = AssetBuilder.BuildMesh(getFileName(), getName(), getTexture(name));
        Matrix3D trans = new Matrix3D();
        trans.translate(position.getX(),position.getY(),position.getZ());
        newNode.setLocalTranslation(trans);
        return newNode;
    }

    public TriMesh make(Point3D position, Quaternion rotation, String name)
    {
        TriMesh newNode = make(position, name);
        Matrix3D rot = new Matrix3D();
        rot.rotate(rotation.getAngleAxis()[1],rotation.getAngleAxis()[2],rotation.getAngleAxis()[3]);
        newNode.setLocalRotation(rot);
        return newNode;
    }

    public TriMesh make(Point3D position, Point3D scale, Quaternion rotation, String name)
    {
        TriMesh newNode = make(position, rotation, name);
        Matrix3D sc = new Matrix3D();
        sc.scale(scale.getX(),scale.getY(),scale.getZ());
        newNode.setLocalScale(sc);
        return newNode;
    }

    public Group makeAni(Point3D position, Point3D scale, Quaternion rotation) throws Exception
    {
        Group newNode = AssetBuilder.BuildMeshWithAni(getFileName(), getName(), getTexture());
        Matrix3D trans = new Matrix3D();
        trans.translate(position.getX(),position.getY(),position.getZ());
        newNode.setLocalTranslation(trans);
        Matrix3D sc = new Matrix3D();
        sc.scale(scale.getX(),scale.getY(),scale.getZ());
        newNode.setLocalScale(sc);
        Matrix3D rot = new Matrix3D();
        rot.rotate(rotation.getAngleAxis()[1],rotation.getAngleAxis()[2],rotation.getAngleAxis()[3]);
        newNode.setLocalRotation(rot);
        return newNode;
    }

    public TriMesh make(Matrix3D position, Matrix3D rotation)
    {
        TriMesh newNode = AssetBuilder.BuildMesh(getFileName(), getName(), getTexture());
        newNode.setLocalTranslation(position);
        newNode.setLocalRotation(rotation);
        return newNode;
    }

    public TriMesh make(Point3D position)
    {
        TriMesh newNode = AssetBuilder.BuildMesh(getFileName(), getName(), getTexture());
        Matrix3D trans = new Matrix3D();
        trans.translate(position.getX(),position.getY(),position.getZ());
        newNode.setLocalTranslation(trans);
        return newNode;
    }

    public TriMesh make(Point3D position, Quaternion rotation)
    {
        TriMesh newNode = make(position);
        Matrix3D rot = new Matrix3D();
        rot.rotate(rotation.getAngleAxis()[1],rotation.getAngleAxis()[2],rotation.getAngleAxis()[3]);
        newNode.setLocalRotation(rot);
        return newNode;
    }

    public TriMesh make(Point3D position, Point3D scale, Quaternion rotation)
    {
        TriMesh newNode = make(position, rotation);
        Matrix3D sc = new Matrix3D();
        sc.scale(scale.getX(),scale.getY(),scale.getZ());
        newNode.setLocalScale(sc);
        return newNode;
    }

    public SkyBox make(float xExtent, float yExtent, float zExtent, boolean zBufferEnabled)
    {
        return AssetBuilder.BuildSkyBox(getFileName(), getName(), xExtent, yExtent, zExtent,zBufferEnabled);
    }

    private String getTexture()
    {
        return (isRandomTexture) ? getRandomTexture() : getFirstTexture();
    }

    private String getFirstTexture()
    {
        if (textures == null)
            setTextures();
        if (textures.size() >= 1)
            return textures.entrySet().iterator().next().getValue();

        System.out.println("Found no texture for asset '"+getName()+"'");
        return "";
    }

    private String getRandomTexture()
    {
        if (textures == null)
            setTextures();
        if (textures.size() == 0)
        {
            System.out.println("Found no texture for asset '" + getName() + "'");
            return "";
        }
        if (random == null)
            random = new Random();

        Object[] array = textures.values().toArray();
        return (String)array[random.nextInt(array.length)];
    }

    private String getTexture(String shortName)
    {
        if (textures == null)
            setTextures();

        if (textures.containsKey(shortName))
            return textures.get(shortName);

        return "";
    }

    private void setTextures()
    {
        textures = new HashMap<>();
        File dir = new File(fileName.substring(0,fileName.indexOf('_')));
        if (getName().contains("_t_"))
        {
            String[] split = getName().split("_");
            for (int i = 0; i < split.length; i++)
            {
                if (split[i] == "t")
                {
                    String path = dir.getParent() + "\\Tiles\\_tile_stone_" +split[i+1]+"_.png";
                    textures.put(getName(),path);
                }
            }
        }
        else if (dir.isDirectory())
        {
            for (File file : dir.listFiles())
            {
                String f = file.getName(); // _sameName_xyz.png of _sameName_xyz.jpg
                if (f.startsWith("_" + getName()+"_") && (f.endsWith(".png") || f.endsWith(".jpg")) && !textures.containsKey(f))
                {
                    textures.put(f, file.getAbsolutePath());
                }
            }
        }
    }
}
