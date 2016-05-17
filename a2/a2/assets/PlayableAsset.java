package a2.assets;

public class PlayableAsset extends Asset
{
    public PlayableAsset(String[] s)
    {
        super.setFileName(String.join("_",s));
        super.setName(s[1]);
        super.setVersion(Integer.parseInt(s[2]));
    }

    public String toString()
    {
        return "Name: " + getName() + " Version: " + getVersion();
    }
}
