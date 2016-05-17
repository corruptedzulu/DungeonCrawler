package a2.old;
/*
import a2.old.MyPyramid;
import a2.old.MySphere;
import a2.old.PyramidGroup;
import a2.old.RotationController;
import graphicslib3D.Matrix3D;

import java.awt.*;

public class mainInitShapes
{
    initStandardShapes();

    private void initStandardShapes()
    {
        for (int x = 0; x < numStandardShapes; x++)
        {
            MySphere sq1 = new MySphere();
            Matrix3D sq1M = sq1.getLocalTranslation();

            int tempX, tempY, tempZ;

            //put an 8 spread away from the center. keeps them from overlapping the doghouse
            if (random.nextBoolean())
            {
                tempX = random.nextInt(maxXDistanceAbsolute + 15);
            } else
            {
                tempX = -(random.nextInt(maxXDistanceAbsolute + 15));
            }

            //tempY = 0;

            //if(random.nextBoolean())
            //{
            tempY = random.nextInt(2);
            //}
            //else
            //{
            //tempY = -(random.nextInt(maxXDistanceAbsolute + 15));
            //}

            if (random.nextBoolean())
            {
                tempZ = random.nextInt(maxXDistanceAbsolute + 15);
            } else
            {
                tempZ = -(random.nextInt(maxXDistanceAbsolute + 15));
            }

            sq1M.translate(tempX, tempY, tempZ);
            sq1.scale(random.nextFloat() + 1, random.nextFloat() + 1, random.nextFloat() + 1);
            sq1.setLocalTranslation(sq1M);
            addGameWorldObject(sq1);
            sq1.setColor(Color.WHITE);
            sq1.updateLocalBound();
            sq1.updateWorldBound();

            //eventManager.addListener(sq1, CrashEvent.class);
        }


        pyramids = new PyramidGroup();
        addGameWorldObject(pyramids);
        rotational = new RotationController();
        rotational.addControlledNode(pyramids);
        pyramids.addController(rotational);

        for (int x = 0; x < numStandardShapes; x++)
        {
            MyPyramid sq1 = new MyPyramid();
            Matrix3D sq1M = sq1.getLocalTranslation();

            int tempX, tempY, tempZ;


            //put an 8 spread away from the center. keeps them from overlapping the doghouse
            if (random.nextBoolean())
            {
                tempX = random.nextInt(maxXDistanceAbsolute + 8);
            } else
            {
                tempX = -(random.nextInt(maxXDistanceAbsolute + 8));
            }

            tempY = random.nextInt(2);
            //			if(random.nextBoolean())
            //			{
            //				tempY = random.nextInt(maxXDistanceAbsolute + 8);
            //			}
            //			else
            //			{
            //				tempY = -(random.nextInt(maxXDistanceAbsolute + 8));
            //			}

            if (random.nextBoolean())
            {
                tempZ = random.nextInt(maxXDistanceAbsolute + 8);
            } else
            {
                tempZ = -(random.nextInt(maxXDistanceAbsolute + 8));
            }


            sq1M.translate(tempX, tempY, tempZ);
            sq1.scale(random.nextFloat() + 1, random.nextFloat() + 1, random.nextFloat() + 1);
            sq1.setLocalTranslation(sq1M);
            pyramids.addChild(sq1);
            //addGameWorldObject(sq1);
            //float[] buff = new float[] {0,0,0,1, 0,0,0,1, 0,0,0,1, 0,0,0,1, 0,0,0,1, 0,0,0,1, 0,0,0,1,
            //							0,0,0,1, 0,0,0,1, 0,0,0,1, 0,0,0,1, 0,0,0,1, 0,0,0,1, 0,0,0,1,
            //						0,0,0,1, 0,0,0,1, 0,0,0,1, 0,0,0,1};
            //FloatBuffer clr = com.jogamp.common.nio.Buffers.newDirectFloatBuffer(buff);
            //sq1.setColorBuffer(clr);
            //sq1.setColor(Color.WHITE);
            sq1.updateLocalBound();
            sq1.updateWorldBound();

            //eventManager.addListener(sq1, CrashEvent.class);
        }

    }
}
*/