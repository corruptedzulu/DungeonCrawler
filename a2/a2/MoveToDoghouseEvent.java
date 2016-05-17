
package a2;

import graphicslib3D.Point3D;
import sage.event.AbstractGameEvent;
import sage.scene.SceneNode;
import sage.scene.TriMesh;

import java.util.UUID;

public class MoveToDoghouseEvent extends AbstractGameEvent 
{
	
	private SceneNode affectedSceneNode;
	
	public MoveToDoghouseEvent(SceneNode affected)
	{
		super();
		this.addAffectedSceneNode(affected);
	}

	public void addAffectedSceneNode(SceneNode affected)
	{
		affectedSceneNode = affected;
	}
	
	public SceneNode getAffectedNode()
	{
		return affectedSceneNode;
	}


	public static class GhostAvatar extends TriMesh
    {
        public GhostAvatar(UUID ghostID, Point3D ghostPosition)
        {
            // TODO
        }

        public void scale(float v, float v1, float v2)
        {
        }

        public void move(Point3D ghostPosition)
        {

        }
    }
}
