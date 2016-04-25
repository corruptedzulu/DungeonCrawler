package a2.newdc;

import a2.newdc.assets.AssetInfo;
import graphicslib3D.Point3D;
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

    public ParticleSystemEngine(AssetInfo info, IPhysicsEngine engine, ArrayList<SceneNode> world, Random rand)
    {
        assetInfo = info;
        physicsEngine = engine;
        gameworld = world;
        physicsObjects = new ArrayList<>();
        random = rand;
    }

    public class ParticleSystem
    {
        private final String name;
        private final int maxChildren;
        private final int maxGenerations;
        private final float ttl;
        private ArrayList<Particle> particles;
        private ArrayList<ParticleSystem> childparticles;

        ParticleSystem(String name, int maxChildren, int maxGenerations, float TTL)
        {
            this.name = name;
            this.maxChildren = maxChildren;
            this.maxGenerations = maxGenerations;
            ttl = TTL;

            particles = new ArrayList<>();
            //childparticles = new ArrayList<>();1
        }


        public void start(Point3D startPos)
        {
            int pCount = random.nextInt(maxChildren);
            int gCount= random.nextInt(maxGenerations);
            for (int i = 0; i < pCount ; i++)
            {
                //SceneNode node = assetInfo.g
                //Particle particle = new Particle()
            }

        }

        public void update(float elaspedTime)
        {
            for (Particle p : particles)
            {

            }
        }

        public void stop()
        {
            for (Particle p : particles)
            {

            }
        }

        private class Particle
        {
            SceneNode gNode;
            IPhysicsObject pObject;

            public Particle(SceneNode gn, float mass, float size, float bounciness)
            {
                gNode = gn;
                pObject = physicsEngine.addSphereObject(physicsEngine.nextUID(),mass,gn.getWorldTransform().getValues(),size);
                pObject.setBounciness(bounciness);
                gNode.setPhysicsObject(pObject);
                gameworld.add(gn);
                physicsObjects.add(pObject);
            }

            public void update(float elaspedTime)
            {

            }

        }
    }
}
