package a2.old;

/*
import a2.*;
import sage.scene.SceneNode;

import java.util.ArrayList;
import java.util.Iterator;

public class mainCollisionStuff
{
    ArrayList<SceneNode> toRemove = new ArrayList<>();

    for (SceneNode s : getGameWorld())
    {

        if (s instanceof Dog || s instanceof MySquare || s instanceof MySphere)
        {
            //s.getWorldBound().intersects(avatarOne.getWorldBound())
            if (s.getWorldBound().contains(
                    cameraOne.getLocation()))// s.getWorldBound().intersects(avatarOne.getWorldBound()))
            {
                if (s instanceof Dog)
                {
                    if (((Dog) s).getRemoved())
                    {
                        continue;
                    } else
                    {
                        ((Dog) s).setRemoved(true);
                    }
                }
                if (s instanceof MySquare)
                {
                    if (((MySquare) s).getRemoved())
                    {
                        continue;
                    } else
                    {
                        ((MySquare) s).setRemoved(true);
                    }
                }
                if (s instanceof MySphere)
                {
                    if (((MySphere) s).getRemoved())
                    {
                        continue;
                    } else
                    {
                        ((MySphere) s).setRemoved(true);
                    }
                }
                // always false
                if (s instanceof MyPyramid)
                {
                    if (((MyPyramid) s).getRemoved())
                    {
                        continue;
                    } else
                    {
                        ((MyPyramid) s).setRemoved(true);
                    }
                }

                numCrashes++;
                scorePOne++;
                System.out.println("Player 1: " + scorePOne);
                player1ID.setText("Player 1: " + scorePOne);
                toRemove.add(s);
                //crash
                MoveToDoghouseEvent newMTDhE = new MoveToDoghouseEvent(s);
                eventManager.triggerEvent(newMTDhE);
            }

            if (s.getWorldBound().contains(
                    cameraTwo.getLocation()))// s.getWorldBound().intersects(avatarTwo.getWorldBound()))
            {
                System.out.println("Camera two intersection");
                if (s instanceof Dog)
                {
                    if (((Dog) s).getRemoved())
                    {
                        continue;
                    } else
                    {
                        ((Dog) s).setRemoved(true);
                    }
                }
                if (s instanceof MySquare)
                {
                    if (((MySquare) s).getRemoved())
                    {
                        continue;
                    } else
                    {
                        ((MySquare) s).setRemoved(true);
                    }
                }
                if (s instanceof MySphere)
                {
                    if (((MySphere) s).getRemoved())
                    {
                        continue;
                    } else
                    {
                        ((MySphere) s).setRemoved(true);
                    }
                }
                // always false
                if (s instanceof MyPyramid)
                {
                    if (((MyPyramid) s).getRemoved())
                    {
                        continue;
                    } else
                    {
                        ((MyPyramid) s).setRemoved(true);
                    }
                }

                numCrashes++;
                scorePTwo++;
                System.out.println("Player 2: " + scorePTwo);
                player2ID.setText("Player 2: " + scorePTwo);
                toRemove.add(s);
                //crash
                MoveToDoghouseEvent newMTDhE = new MoveToDoghouseEvent(s);
                eventManager.triggerEvent(newMTDhE);
            }
        }

        if (s instanceof Doghouse)
        {
            ((Doghouse) s).updateGameWorldObject(elapsedTimeMS);
        }

        if (s instanceof PyramidGroup)
        {
            Iterator k = ((PyramidGroup) s).getChildren();
            while (k.hasNext())
            {
                SceneNode n = (SceneNode) k.next();

                if (n.getWorldBound().contains(cameraOne.getLocation()) ||
                        n.getWorldBound().contains(cameraTwo.getLocation()))
                {
                    if (((MyPyramid) n).getRemoved() == true)
                    {
                        continue;
                    } else
                    {
                        ((MyPyramid) n).setRemoved(true);
                    }

                    numCrashes++;
                    //scorePOne++;
                    //System.out.println(scorePOne);

                    if (n.getWorldBound().contains(cameraOne.getLocation()))
                    {
                        scorePOne++;
                        System.out.println("Player 1: " + scorePOne);
                        player1ID.setText("Player 1: " + scorePOne);
                    } else if (n.getWorldBound().contains(cameraTwo.getLocation()))
                    {
                        scorePTwo++;
                        System.out.println("Player 2: " + scorePTwo);
                        player2ID.setText("Player 2: " + scorePTwo);
                    }

                    toRemove.add(n);

                    //crash
                    MoveToDoghouseEvent newMTDhE = new MoveToDoghouseEvent(n);
                    eventManager.triggerEvent(newMTDhE);
                }
            }
        }
    }

    for (int x = 0; x < toRemove.size(); x++)
    {
        // removeGameWorldObject(toRemove.get(x));
    }

    toRemove.clear();
}
*/