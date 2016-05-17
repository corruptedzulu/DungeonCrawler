package a2.assets;

public class TileAsset extends Asset
{
    public boolean CanWalkOn;

    public TileAsset(String[] s)
    {
        super.setFileName(String.join("_",s));
        super.setName(s[1]);
        super.setVersion(Integer.parseInt(s[3]));
        CanWalkOn = (s[2] == "W");
    }

    public String toString()
    {
        return "Name: " + getName() + " CanWalkOn: " + CanWalkOn + " Version: " + getVersion();
    }
}
