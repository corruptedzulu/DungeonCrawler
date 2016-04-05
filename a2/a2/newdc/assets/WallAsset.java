package a2.newdc.assets;

public class WallAsset extends Asset
{
    public WallAsset(String[] s)
    {
        super.setFileName(String.join("_",s));
        super.setName(s[1]);
        super.setVersion(Integer.parseInt(s[3]));
    }
    public String toString()
    {
        return "Name: " + getName() + " Version: " + getVersion();
    }
}
