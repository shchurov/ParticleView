package com.github.shchurov.particleview;

class Shaders {

    static final String V_SHADER = "" +
            "uniform mat4 uMvpMatrix;" +
            "attribute vec4 aPosition;" +
            "attribute float aAlpha;" +
            "varying float vAlpha;" +
            "attribute vec2 aTextureCoords;" +
            "varying vec2 vTextureCoords;" +
            "void main() {" +
            "  gl_Position = uMvpMatrix * aPosition;" +
            "  vAlpha = aAlpha;" +
            "  vTextureCoords = aTextureCoords;" +
            "}";

    static final String F_SHADER = "" +
            "varying float vAlpha;" +
            "uniform sampler2D uTexture;" +
            "varying vec2 vTextureCoords;" +
            "void main() {" +
            "  gl_FragColor = texture2D(uTexture, vTextureCoords) * vAlpha;" +
            "}";

}
