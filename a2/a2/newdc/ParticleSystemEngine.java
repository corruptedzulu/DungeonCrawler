package a2.newdc;

import a2.newdc.assets.AssetInfo;
import a2.newdc.assets.ObjectNonInteractableAsset;
import graphicslib3D.Matrix3D;
import graphicslib3D.Point3D;
import graphicslib3D.Quaternion;
import graphicslib3D.Vector3D;
import sage.physics.IPhysicsEngine;
import sage.physics.IPhysicsObject;
import sage.scene.SceneNode;

import java.util.ArrayList;
import java.util.Random;

public class ParticleSystemEngine
{
    private AssetInfo assetInfo;
    private IPhysicsEngine physicsEngine;
    private ArrayList<SceneNode> gameworld;
    private ArrayList<IPhysicsObject> physicsObjects;
    private Random random;
    private final float[] gravity = new float[]{0,-10f,0f};

    public ParticleSystemEngine(AssetInfo info, IPhysicsEngine engine, ArrayList<SceneNode> world, Random rand)
    {
        assetInfo = info;
        physicsEngine = engine;
        gameworld = world;
        physicsObjects = new ArrayList<>();
        random = rand;
        physicsEngine.setGravity(gravity);

        //IPhysicsObject groundPlaneP =
                physicsEngine.addStaticPlaneObject(physicsEngine.nextUID()
                        ,new Matrix3D().getValues(), new float[]{0,1,0}, 0.0f);
        //groundPlaneP.setBounciness(1.0f);

    }

    public ParticleSystem getSystem(String name, int maxChildren, int maxGenerations, float TTL)
    {
        return new ParticleSystem(name, maxChildren, maxGenerations, TTL);
    }

    public class ParticleSystem
    {
        private final String name;
        private final int maxChildren;
        private final int maxGenerations;
        private float ttl;
        private final float startLinearVelocityScale = .000001f;
        private ArrayList<Particle> particles;
        private ArrayList<ParticleSystem> childparticles;
        public final float TTL_DEFAULT = 10000;

        ParticleSystem(String name, int maxChildren, int maxGenerations, float TTL)
        {
            this.name = name;
            this.maxChildren = maxChildren;
            this.maxGenerations = maxGenerations;
            ttl = TTL;

            particles = new ArrayList<>();
            childparticles = new ArrayList<>();
        }


        public void start(Point3D startPos)
        {
            int pCount = random.nextInt(maxChildren);
            int gCount = random.nextInt(maxGenerations);
            for (int i = 0; i < pCount+1 ; i++)
            {
                ObjectNonInteractableAsset barrel = assetInfo.objectNonInteractables.get("barrel");
                SceneNode node = barrel.make(startPos, new Point3D(.4, .4, .4), new Quaternion(1, new double[]{0, 0, 0}));
                Particle p = new Particle(node,1f,.1f,1f);
                particles.add(p);
            }
        }


        public void update(float elaspedTime)
        {
            ttl = ttl - elaspedTime;
            physicsEngine.update(20f);
            Matrix3D mat;
            Vector3D trans;
            if (ttl < 0)
            {
                //System.out.println("particle system died");
                ttl = TTL_DEFAULT;
                for (Particle p : particles)
                {
                    physicsEngine.removeObject(p.pObject.getUID());
                    gameworld.remove(p.gNode);
                }
            }
            else
                for (Particle p : particles)
                {
                    mat = new Matrix3D(p.pObject.getTransform());
                    trans = mat.getCol(3);
                    p.gNode.getLocalTranslation().setCol(3,trans);
                }
        }

        public void stop()
        {
            particles = null;
        }

        private class Particle
        {
            public SceneNode gNode;
            public IPhysicsObject pObject;

            public Particle(SceneNode gn, float mass, float size, float bounciness)
            {
                gNode = gn;
                pObject = physicsEngine.addSphereObject(physicsEngine.nextUID(),mass,gn.getWorldTransform().getValues(),size);
                //pObject.setBounciness(bounciness);

                pObject.setLinearVelocity(getRandomLinearVelocity());

                pObject.setFriction(2f);
                pObject.setDamping(.5f,1.2f);
                pObject.setTransform(gNode.getWorldTransform().getValues());
                gNode.setPhysicsObject(pObject);

                gameworld.add(gn);
                physicsObjects.add(pObject);
            }
            private float[] getRandomLinearVelocity()
            {
                float tx = (.5f - random.nextFloat())*startLinearVelocityScale;
                float ty = (.5f - random.nextFloat())*startLinearVelocityScale;
                float tz = (.5f - random.nextFloat())*startLinearVelocityScale;
                //System.out.println("tx: " + tx + " ty: " + ty + " tz: " + tz);
                System.out.println("tx: " + tx + " ty: " + ty + " tz: " + tz);
                return new float[]{tx,ty,tz};
            }
        }
    }
}
