package com.github.shchurov.particleview;

class Shaders {

    static final String V_SHADER = "" +
            "uniform mat4 uMvpMatrix;" +
            "attribute vec4 aPosition;" +
            "attribute lowp float aAlpha;" +
            "varying lowp float vAlpha;" +
            "attribute mediump vec2 aTextureCoords;" +
            "varying mediump vec2 vTextureCoords;" +
            "void main() {" +
            "  gl_Position = uMvpMatrix * aPosition;" +
            "  vAlpha = aAlpha;" +
            "  vTextureCoords = aTextureCoords;" +
            "}";

    static final String F_SHADER = "" +
            "varying lowp float vAlpha;" +
            "uniform mediump sampler2D uTexture;" +
            "varying mediump vec2 vTextureCoords;" +
            "void main() {" +
            "  gl_FragColor = texture2D(uTexture, vTextureCoords) * vAlpha;" +
            "}";

}
