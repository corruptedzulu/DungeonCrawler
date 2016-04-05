package a2.newdc.assets;

import graphicslib3D.Matrix3D;
import graphicslib3D.Point3D;
import graphicslib3D.Quaternion;
import sage.scene.SceneNode;

public abstract class Asset
{
    private String fileName;
    private String name;
    private int version;

    public enum FamilyType
    {
        Tile,
        Room,
        Wall,
        Playable,
        NPCFriendly,
        NPCEnemy,
        ObjectInteractable,
        ObjectNonInteractable
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

    public SceneNode make(Matrix3D position, Matrix3D rotation)
    {
        SceneNode newNode = AssetBuilder.BuildMesh(getFileName(), getName());
        newNode.setLocalTranslation(position);
        newNode.setLocalRotation(rotation);
        return newNode;
    }

    public SceneNode make(Point3D position, Quaternion rotation)
    {
        SceneNode newNode = AssetBuilder.BuildMesh(getFileName(), getName());
        Matrix3D trans = new Matrix3D();
        trans.translate(position.getX(),position.getY(),position.getZ());
        Matrix3D rot = new Matrix3D();
        rot.rotate(rotation.getAngleAxis()[1],rotation.getAngleAxis()[2],rotation.getAngleAxis()[3]);
        newNode.setLocalTranslation(trans);
        newNode.setLocalRotation(rot);
        return newNode;
    }

    public SceneNode make(Point3D position, Point3D scale, Quaternion rotation)
    {
        SceneNode newNode = AssetBuilder.BuildMesh(getFileName(), getName());
        Matrix3D trans = new Matrix3D();
        trans.translate(position.getX(),position.getY(),position.getZ());
        Matrix3D rot = new Matrix3D();
        rot.rotate(rotation.getAngleAxis()[1],rotation.getAngleAxis()[2],rotation.getAngleAxis()[3]);
        Matrix3D sc = new Matrix3D();
        sc.scale(scale.getX(),scale.getY(),scale.getZ());
        newNode.setLocalTranslation(trans);
        newNode.setLocalRotation(rot);
        newNode.setLocalScale(sc);
        return newNode;
    }
}
