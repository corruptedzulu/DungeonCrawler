package a2.newdc.assets;

public class RoomAsset extends Asset
{
    public int DimX;
    public int DimZ;

    public RoomAsset(String[] s)
    {
        super.setFileName(String.join("_",s));
        super.setName(s[1]);
        super.setVersion(Integer.parseInt(s[4]));
        DimX = Integer.parseInt(s[2]);
        DimZ = Integer.parseInt(s[3]);
    }

    public String toString()
    {
        return "Name: " + getName() + "Dim: " + DimX + "x" +DimZ + " Version: " + getVersion();
    }
}
