package com.github.shchurov.particleview.sample;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.github.shchurov.particleview.TextureAtlas;

public class SampleTextureAtlasFactory {

    public static TextureAtlas createTextureAtlas(Resources resources) {
        final int size = 64;
        int[] drawables = {R.drawable.tex0, R.drawable.tex1, R.drawable.tex2, R.drawable.tex3, R.drawable.tex4,
                R.drawable.tex5, R.drawable.tex6, R.drawable.tex7, R.drawable.tex8, R.drawable.tex9, R.drawable.tex10,
                R.drawable.tex11, R.drawable.tex12, R.drawable.tex13, R.drawable.tex14, R.drawable.tex15};
        TextureAtlas atlas = new TextureAtlas(4 * size, 4 * size);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                Bitmap bitmap = BitmapFactory.decodeResource(resources, drawables[i * 4 + j]);
                atlas.addRegion(j * size, i * size, bitmap);
            }
        }
        return atlas;
    }

}
