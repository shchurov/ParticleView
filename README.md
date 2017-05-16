# ParticleView 0.9.10
Custom Android view that helps you displaying large number of sprites at the same time. Implemented using OpenGL ES 2.0, resulting in significantly better performance than regular Canvas drawing.

![Sample 1](https://raw.githubusercontent.com/shchurov/ParticleView/master/github_assets/demo1.gif)

## Installation
Add jitpack.io repository to your **root** build.gradle:
```groovy
allprojects {
 repositories {
    jcenter()
    maven { url "https://jitpack.io" }
 }
}
```
Add dependency to your **module** build.gradle:

`compile 'com.github.shchurov:particleview:0.9.10'`

## Key components
Component |  |
--- | ---
`ParticleView` | The view itself
`Particle` | Keeps all the information about certain sprite (e.g., texture, position, size) 
`ParticleSystem` | Keeps and updates particles over time
`TextureAtlas` | Contains all the textures
`TextureAtlasFactory` | Fires missiles on the other side of the Moon

## Getting started
[Check GettingStarted sample](https://github.com/shchurov/ParticleView/blob/master/sample/src/main/java/com/github/shchurov/particleview/sample/getting_started/GettingStartedActivity.java)

- Add `ParticleView` to your layout
- Implement `TextureAtlasFactory`, `ParticleSystem` and bind them to the view
- Call `ParticleView.startRendering()`

### Updating particles
While active `ParticleView` will call `ParticleSystem.update(double timeDelta)` before every frame. Use this method to update your particles' state. Note that it's called from **rendering** thread. You will also need to implement `ParticleSystem.getMaxCount()` that represents the maximum number of particles which will be displayed at the same time. It's required for allocating buffers of sufficient size.

### Textures
In order to optimize rendering performance all textures should be packed inside `TextureAtlas`. To do this, you need to create `TextureAtlas` and put bitmaps there using `TextureAtlas.addRegion(int x, int y, Bitmap bitmap)` so they do not overlap. The library contains helper class `SimpleTextureAtlasPacker` that takes a list of bitmaps and generates `TextureAtlas` for you. Although its implementation is quite "naive", it should perform well in most cases. At certain stages `ParticleView` needs to re-create `TextureAtlas`, for this purpose `TextureAtlasFactory` is used.

### Some tips
- Remember to call `ParticleView.stopRendering()` when its idle or invisible.
- To achieve better performance consider reusing particle objects instead of creating new instances every time. [Check sample.](https://github.com/shchurov/ParticleView/blob/master/sample/src/main/java/com/github/shchurov/particleview/sample/burst/BurstParticleSystem.java)

### ToDo list
- add support for multiple `TextureAtlas`es
- prevent invalid method calls by throwing exceptions
- improve built-in `SimpleTextureAtlasPacker`

![Sample 2](https://raw.githubusercontent.com/shchurov/ParticleView/master/github_assets/demo2.gif)

## License
    Copyright 2016 Mykhailo Shchurov

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
