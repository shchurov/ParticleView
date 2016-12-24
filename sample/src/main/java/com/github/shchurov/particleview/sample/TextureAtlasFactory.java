package com.github.shchurov.particleview.sample;


import android.content.res.Resources;

import com.github.shchurov.particleview.SimpleTextureAtlasPacker;
import com.github.shchurov.particleview.TextureAtlas;

import java.util.Arrays;
import java.util.List;

public class TextureAtlasFactory {

    public static final int TEXTURE_COUNT = 16;

    public static TextureAtlas createTextureAtlas(Resources resources) {
        List<Integer> drawables = Arrays.asList(R.drawable.tex0, R.drawable.tex1, R.drawable.tex2, R.drawable.tex3,
                R.drawable.tex4, R.drawable.tex5, R.drawable.tex6, R.drawable.tex7, R.drawable.tex8, R.drawable.tex9,
                R.drawable.tex10, R.drawable.tex11, R.drawable.tex12, R.drawable.tex13, R.drawable.tex14,
                R.drawable.tex15);
        SimpleTextureAtlasPacker packer = new SimpleTextureAtlasPacker();
        return packer.pack(drawables, resources, 300, 300);
    }

}
