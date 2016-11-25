package com.github.shchurov.particleview;


import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_BLEND;
import static android.opengl.GLES20.GL_CLAMP_TO_EDGE;
import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_BUFFER_BIT;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_LINEAR;
import static android.opengl.GLES20.GL_ONE_MINUS_SRC_ALPHA;
import static android.opengl.GLES20.GL_SRC_ALPHA;
import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_TEXTURE_MAG_FILTER;
import static android.opengl.GLES20.GL_TEXTURE_MIN_FILTER;
import static android.opengl.GLES20.GL_TEXTURE_WRAP_S;
import static android.opengl.GLES20.GL_TEXTURE_WRAP_T;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_UNSIGNED_SHORT;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glAttachShader;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glBlendFunc;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glCreateProgram;
import static android.opengl.GLES20.glDisableVertexAttribArray;
import static android.opengl.GLES20.glDrawElements;
import static android.opengl.GLES20.glEnable;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGenTextures;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glLinkProgram;
import static android.opengl.GLES20.glTexParameteri;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.GLES20.glViewport;

class ParticleRenderer implements GLSurfaceView.Renderer {

    private volatile ParticleSystem particleSystem;
    private volatile boolean particleSystemNeedsSetup;
    private volatile TextureAtlas textureAtlas;
    private volatile boolean textureAtlasNeedsSetup;
    private int surfaceHeight;
    private int programRef;
    private float[] projectionViewM = new float[16];
    private ShortBuffer drawListBuffer;
    private float[] vertexArray;
    private FloatBuffer vertexBuffer;
    private float[] alphaArray;
    private FloatBuffer alphaBuffer;
    private float[] textureCoordsCacheArray;
    private float[] textureCoordsArray;
    private FloatBuffer textureCoordsBuffer;

    private volatile boolean fpsLogEnabled;
    private long fps;
    private long fpsMark;

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        initGl();
        initGlProgram();
        particleSystemNeedsSetup = true;
        textureAtlasNeedsSetup = true;
    }

    private void initGl() {
        glClearColor(0f, 0f, 0f, 0f);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

    private void initGlProgram() {
        int vShaderRef = compileShader(GL_VERTEX_SHADER, Shaders.V_SHADER);
        int fShaderRef = compileShader(GL_FRAGMENT_SHADER, Shaders.F_SHADER);
        programRef = glCreateProgram();
        glAttachShader(programRef, vShaderRef);
        glAttachShader(programRef, fShaderRef);
        glLinkProgram(programRef);
        glUseProgram(programRef);
    }

    private int compileShader(int type, String shaderCode) {
        int ref = GLES20.glCreateShader(type);
        GLES20.glShaderSource(ref, shaderCode);
        GLES20.glCompileShader(ref);
        return ref;
    }

    void setTextureAtlas(TextureAtlas atlas) {
        this.textureAtlas = atlas;
        atlas.setEditable(false);
        textureAtlasNeedsSetup = true;
    }

    void setParticleSystem(ParticleSystem system) {
        this.particleSystem = system;
        particleSystemNeedsSetup = true;
    }

    void setFpsLogEnabled(boolean enabled) {
        fpsLogEnabled = enabled;
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        glViewport(0, 0, width, height);
        surfaceHeight = height;
        initProjectionViewMatrix(width, height);
    }

    private void initProjectionViewMatrix(int width, int height) {
        float[] projectionM = new float[16];
        float[] viewM = new float[16];
        Matrix.orthoM(projectionM, 0, 0f, width, 0f, height, 0, 1f);
        Matrix.setLookAtM(viewM, 0, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f, 0f);
        Matrix.multiplyMM(projectionViewM, 0, projectionM, 0, viewM, 0);
    }

    @Override
    public void onDrawFrame(GL10 unused) {
        if (particleSystem == null) {
            return;
        }
        if (particleSystemNeedsSetup) {
            setupBuffers();
            particleSystemNeedsSetup = false;
        }
        if (textureAtlasNeedsSetup) {
            setupTextures();
            textureAtlasNeedsSetup = false;
        }
        if (fpsLogEnabled) {
            logFps();
        }
        particleSystem.update();
        updateBuffers(particleSystem.getParticles());
        render(particleSystem.getParticles().size());
    }

    private void logFps() {
        fps++;
        if (System.nanoTime() - fpsMark >= 1000000000) {
            Log.d("ParticleView", "fps: " + fps);
            fps = 0;
            fpsMark = System.nanoTime();
        }
    }

    private void setupTextures() {
        int[] names = new int[1];
        glGenTextures(1, names, 0);
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, names[0]);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        GLUtils.texImage2D(GL_TEXTURE_2D, 0, Bitmap.createBitmap(textureAtlas.getWidth(), textureAtlas.getHeight(),
                Bitmap.Config.ARGB_8888), 0);

        List<TextureAtlas.Region> regions = textureAtlas.getRegions();
        textureCoordsCacheArray = new float[regions.size() * 8];
        final int k = 8;
        float atlasWidth = textureAtlas.getWidth();
        float atlasHeight = textureAtlas.getHeight();
        for (int i = 0; i < regions.size(); i++) {
            TextureAtlas.Region r = regions.get(i);
            GLUtils.texSubImage2D(GL_TEXTURE_2D, 0, r.x, r.y, r.bitmap);
            float x0 = r.x / atlasWidth;
            float y0 = r.y / atlasHeight;
            float x1 = x0 + r.bitmap.getWidth() / atlasWidth;
            float y1 = y0 + r.bitmap.getHeight() / atlasHeight;
            textureCoordsCacheArray[i * k] = x0;
            textureCoordsCacheArray[i * k + 1] = y0;
            textureCoordsCacheArray[i * k + 2] = x0;
            textureCoordsCacheArray[i * k + 3] = y1;
            textureCoordsCacheArray[i * k + 4] = x1;
            textureCoordsCacheArray[i * k + 5] = y1;
            textureCoordsCacheArray[i * k + 6] = x1;
            textureCoordsCacheArray[i * k + 7] = y0;
        }
    }

    private void setupBuffers() {
        int maxCount = particleSystem.getMaxCount();

        ByteBuffer b = ByteBuffer.allocateDirect(maxCount * 8 * 4);
        b.order(ByteOrder.nativeOrder());
        vertexBuffer = b.asFloatBuffer();
        vertexArray = new float[maxCount * 8];

        b = ByteBuffer.allocateDirect(maxCount * 8 * 4);
        b.order(ByteOrder.nativeOrder());
        textureCoordsBuffer = b.asFloatBuffer();
        textureCoordsArray = new float[maxCount * 8];

        b = ByteBuffer.allocateDirect(maxCount * 4 * 4);
        b.order(ByteOrder.nativeOrder());
        alphaBuffer = b.asFloatBuffer();
        alphaArray = new float[maxCount * 4];

        b = ByteBuffer.allocateDirect(maxCount * 6 * 2);
        b.order(ByteOrder.nativeOrder());
        drawListBuffer = b.asShortBuffer();
        fillDrawListBuffer(maxCount);
        drawListBuffer.position(0);
    }

    private void fillDrawListBuffer(int count) {
        int k = 4;
        for (int i = 0; i < count; i++) {
            drawListBuffer.put((short) (i * k));
            drawListBuffer.put((short) (i * k + 1));
            drawListBuffer.put((short) (i * k + 2));
            drawListBuffer.put((short) (i * k));
            drawListBuffer.put((short) (i * k + 2));
            drawListBuffer.put((short) (i * k + 3));
        }
    }

    private void updateBuffers(List<? extends Particle> particles) {
        final int k1 = 8;
        final int k2 = 4;
        for (int i = 0, count = particles.size(); i < count; i++) {
            Particle p = particles.get(i);
            vertexArray[i * k1] = p.getX() - p.getDx2();
            vertexArray[i * k1 + 1] = surfaceHeight - p.getY() + p.getDy2();
            vertexArray[i * k1 + 2] = p.getX() - p.getDx1();
            vertexArray[i * k1 + 3] = surfaceHeight - p.getY() - p.getDy1();
            vertexArray[i * k1 + 4] = p.getX() + p.getDx2();
            vertexArray[i * k1 + 5] = surfaceHeight - p.getY() - p.getDy2();
            vertexArray[i * k1 + 6] = p.getX() + p.getDx1();
            vertexArray[i * k1 + 7] = surfaceHeight - p.getY() + p.getDy1();

            System.arraycopy(textureCoordsCacheArray, p.getTexture() * k1, textureCoordsArray, i * k1, k1);

            alphaArray[i * k2] = p.getAlpha();
            alphaArray[i * k2 + 1] = p.getAlpha();
            alphaArray[i * k2 + 2] = p.getAlpha();
            alphaArray[i * k2 + 3] = p.getAlpha();
        }
        vertexBuffer.put(vertexArray);
        textureCoordsBuffer.put(textureCoordsArray);
        alphaBuffer.put(alphaArray);
        vertexBuffer.position(0);
        textureCoordsBuffer.position(0);
        alphaBuffer.position(0);
    }

    private void render(int count) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        int matrixHandle = glGetUniformLocation(programRef, "uMvpMatrix");
        glUniformMatrix4fv(matrixHandle, 1, false, projectionViewM, 0);

        int positionHandle = glGetAttribLocation(programRef, "aPosition");
        glEnableVertexAttribArray(positionHandle);
        glVertexAttribPointer(positionHandle, 2, GL_FLOAT, false, 0, vertexBuffer);

        int textureHandle = glGetUniformLocation(programRef, "uTexture");
        glUniform1i(textureHandle, 0);

        int textureCoordsHandle = glGetAttribLocation(programRef, "aTextureCoords");
        glEnableVertexAttribArray(textureCoordsHandle);
        glVertexAttribPointer(textureCoordsHandle, 2, GL_FLOAT, false, 0, textureCoordsBuffer);

        int alphaHandle = glGetAttribLocation(programRef, "aAlpha");
        glEnableVertexAttribArray(alphaHandle);
        glVertexAttribPointer(alphaHandle, 1, GL_FLOAT, false, 0, alphaBuffer);

        glDrawElements(GL_TRIANGLES, count * 6, GL_UNSIGNED_SHORT, drawListBuffer);

        glDisableVertexAttribArray(positionHandle);
        glDisableVertexAttribArray(alphaHandle);
        glDisableVertexAttribArray(textureCoordsHandle);
    }

}
