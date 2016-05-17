package myGameEngine;

import sage.display.IDisplaySystem;
import sage.renderer.IRenderer;
import sage.renderer.RendererFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.Window;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MyDisplaySystem implements IDisplaySystem
{
    private static IDisplaySystem displaySystem = null;
    private static final String RENDERER_NAME = "sage.renderer.jogl.JOGLRenderer";
    private static final boolean FSEM = false;
    private GraphicsDevice device;
    private JFrame jFrame;
    private IRenderer renderer;
    private Canvas rendererCanvas;
    private int width;
    private int height;
    private int bitDepth;
    private int refreshRate;
    private boolean isCreated;
    private boolean isFullScreen;

    public MyDisplaySystem(int w, int h, int depth, int rate, boolean isFS, String rendererName)
    {
        width = w;
        height = h;
        bitDepth = depth;
        refreshRate = rate;
        isFullScreen = isFS;
        renderer = RendererFactory.createRenderer(rendererName);
        if (rendererName == null)
            throw new RuntimeException("Unable to find renderer '" + rendererName + "'");
        rendererCanvas = renderer.getCanvas();
        jFrame = new JFrame("Default Title");
        jFrame.add(rendererCanvas);

        DisplayMode displayMode = new DisplayMode(width, height, bitDepth, refreshRate);
        initScreen(displayMode, isFullScreen);

        displaySystem = this;
        jFrame.setVisible(true);
        isCreated = true;
    }


    private void initScreen(DisplayMode displayMode, boolean isFS)
    {
        GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        device = environment.getDefaultScreenDevice();

        if (device.isFullScreenSupported() && isFS)
        {
            jFrame.setUndecorated(true);
            jFrame.setResizable(false);
            jFrame.setIgnoreRepaint(true);

            device.setFullScreenWindow(jFrame);

            if (displayMode != null && device.isDisplayChangeSupported())
            {
                try
                {
                    device.setDisplayMode(displayMode);
                    jFrame.setSize(displayMode.getWidth(), displayMode.getHeight());
                }
                catch (Exception ex)
                {
                    System.err.println("Setting display mode failed: " + ex);
                }
            } else
                System.err.println("Cannot set display mode");
        } else
        {
            jFrame.setSize(displayMode.getWidth(), displayMode.getHeight());
            jFrame.setLocationRelativeTo(null);
        }
    }

    // if isn't created returns a FS MyDisplaySystem with the DisplayMode that has the largest width.
    public static IDisplaySystem getDisplaySystem()
    {
        if (displaySystem != null)
            return displaySystem;

        GraphicsDevice d = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        DisplayMode m = null;
        for (DisplayMode mode : d.getDisplayModes())
        {
            if (m == null)
                m = mode;
            if (mode.getWidth() > m.getWidth())
                m = mode;
        }
        if (m == null)
            throw new RuntimeException("No display modes");

        displaySystem = new MyDisplaySystem(
                m.getWidth(), m.getHeight(), m.getBitDepth(), m.getRefreshRate(), FSEM, RENDERER_NAME);
        int count = 0;
        while (!displaySystem.isCreated())
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
            if (count > 2000)
                throw new RuntimeException("Unable to create display");
        }
        return displaySystem;
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    public int getBitDepth()
    {
        return bitDepth;
    }

    public int getRefreshRate()
    {
        return refreshRate;
    }

    public void setWidth(int w)
    {
        throw new RuntimeException("Not implemented myGameEngine.MyDisplaySystem.setWidth()");
    }

    public void setHeight(int h)
    {
        throw new RuntimeException("Not implemented myGameEngine.MyDisplaySystem.setHeight()");
    }

    public void setBitDepth(int depth)
    {
        throw new RuntimeException("Not implemented myGameEngine.MyDisplaySystem.setBitDepth()");
    }

    public void setRefreshRate(int rate)
    {
        throw new RuntimeException("Not implemented myGameEngine.MyDisplaySystem.setRefreshRate()");
    }

    public void setTitle(String title)
    {
        jFrame.setTitle(title);
    }

    public IRenderer getRenderer()
    {
        return renderer;
    }

    public void close()
    {
        if (device != null)
        {
            Window window = device.getFullScreenWindow();
            if (window != null)
                window.dispose();
            device.setFullScreenWindow(null);
        }
    }

    public boolean isCreated()
    {
        return isCreated;
    }

    public boolean isFullScreen()
    {
        return isFullScreen;
    }

    public void addKeyListener(KeyListener keyListener)
    {
        rendererCanvas.addKeyListener(keyListener);
        rendererCanvas.requestFocus();
    }

    public void addMouseListener(MouseListener mouseListener)
    {
        rendererCanvas.addMouseListener(mouseListener);
    }

    public void addMouseMotionListener(MouseMotionListener mouseMotionListener)
    {
        rendererCanvas.addMouseMotionListener(mouseMotionListener);
    }

    public boolean isShowing()
    {
        return jFrame.isShowing();
    }

    public void convertPointToScreen(Point point)
    {
        SwingUtilities.convertPointToScreen(point, jFrame);
    }

    public void setPredefinedCursor(int cursor)
    {
        jFrame.setCursor(Cursor.getPredefinedCursor(cursor));
    }

    public void setCustomCursor(String s)
    {
        throw new RuntimeException("Not implemented myGameEngine.MyDisplaySystem.setCustomCursor()"); // todo
    }
}
