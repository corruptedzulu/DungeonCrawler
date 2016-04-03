
package a2;

import sage.event.AbstractGameEvent;
import sage.scene.SceneNode;

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
	
	
}
