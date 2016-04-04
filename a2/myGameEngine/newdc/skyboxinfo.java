/*
import sage.scene.SkyBox;
import sage.terrain.AbstractHeightMap;
import sage.terrain.HillHeightMap;
import sage.terrain.TerrainBlock;

private SkyBox sky;
private TerrainBlock theTerrain;
private void initGameObjects() {
		sky = new SkyBox();
		Texture spaceTexture = TextureManager.loadTexture2D(texFolder + File.separator +"space.jpg");
		sky.setTexture(SkyBox.Face.Up, spaceTexture);
		sky.setTexture(SkyBox.Face.Down, spaceTexture);
		sky.setTexture(SkyBox.Face.North, spaceTexture);
		sky.setTexture(SkyBox.Face.South, spaceTexture);
		sky.setTexture(SkyBox.Face.East, spaceTexture);
		sky.setTexture(SkyBox.Face.West, spaceTexture);
		addGameWorldObject(sky);
      
      theTerrain = initTerrain();
		theTerrain.scale(2, 1, 2);
		background = new Group();
		background.addChild(theTerrain);
}

private static TerrainBlock createTerBlock(AbstractHeightMap heightMap)
{ 
		float heightScale = 0.05f;
		Vector3D terrainScale = new Vector3D(1, heightScale, 1);
		
		// use the size of the height map as the size of the terrain
		int terrainSize = heightMap.getSize();
		
		// specify terrain origin so heightmap (0,0) is at world origin
		float cornerHeight =
		heightMap.getTrueHeightAtPoint(0, 0) * heightScale;
		Point3D terrainOrigin = new Point3D(-64.5, -cornerHeight, -64.5);
		
		// create a terrain block using the height map
		String name = "Terrain:" + heightMap.getClass().getSimpleName();
		TerrainBlock tb = new TerrainBlock(name, terrainSize, terrainScale, heightMap.getHeightData(), terrainOrigin);
		
		return tb;
 }
 private TerrainBlock initTerrain()
{ // create height map and terrain block
		HillHeightMap myHillHeightMap =
		new HillHeightMap(129, 2000, 5.0f, 20.0f,(byte)2, 12345);
		myHillHeightMap.setHeightScale(0.1f);
		TerrainBlock hillTerrain = createTerBlock(myHillHeightMap);
		
		// create texture and texture state to color the terrain
		TextureState groundState;
		Texture groundTexture = TextureManager.loadTexture2D(texFolder + File.separator +"redrock.jpg");
		
		groundTexture.setApplyMode(sage.texture.Texture.ApplyMode.Replace);
		groundState = (TextureState) display.getRenderer().createRenderState(RenderStateType.Texture);
		groundState.setTexture(groundTexture,0);
		groundState.setEnabled(true);
		
		// apply the texture to the terrain
		hillTerrain.setRenderState(groundState);
		return hillTerrain;
 }
 
 private IDisplaySystem createDisplaySystem(){
 //instead of display 
      display = new MyDisplaySystem(1024, 720, 24, 60, false,"sage.renderer.jogl.JOGLRenderer");
		System.out.print("\nWaiting for display creation... ");
		int count = 0;
		// wait until display creation completes or a timeout occurs
		while (!display.isCreated())
		{
			try
			{ 
				Thread.sleep(10); 
			}
			catch (InterruptedException e)
			{ 
				throw new RuntimeException("Display creation interrupted"); 
			}
			count++;
			System.out.print("+");
			if (count % 80 == 0) { System.out.println(); }
			if (count > 2000) // 20 seconds (approx.)
			{ 
				throw new RuntimeException("Unable to create display");
			}
		}
		System.out.println();
		return display ;
 
 }
*/