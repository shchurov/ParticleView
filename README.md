# ParticleView 0.9.9
Custom Android view that helps you displaying large number of sprites at the same time. Implemented using OpenGL ES 2.0, resulting in significantly better performance than regular Canvas drawing.

![Sample 1](https://raw.githubusercontent.com/shchurov/ParticleView/master/github_assets/demo1.gif)

Installation
-------
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

`compile 'com.github.shchurov:particleview:0.9.9'`

Key components
-------
Component |  |
--- | ---
`ParticleView` | The view itself
`Particle` | Keeps all the information about certain sprite (e.g., texture, position, size) 
`ParticleSystem` | Keeps and updates particles over time
`TextureAtlas` | Contains all the textures
`TextureAtlasFactory` | Fires missiles on the other side of the Moon

Getting started
-------
[Check GettingStarted sample](https://github.com/shchurov/ParticleView/blob/master/sample/src/main/java/com/github/shchurov/particleview/sample/getting_started/GettingStartedActivity.java)

- Add `ParticleView` to your layout
- Implement `TextureAtlasFactory`, `ParticleSystem` and bind them to the view
- Call `ParticleView.startRendering()`

Updating particles
-------
After you've called `startRendering()` `ParticleView` will start calling `ParticleSystem.update(double timeDelta)` before rendering every frame. There you're supposed to update your particles' state. `timeDelta` shows how much time has passed since the previous frame. Note that `ParticleSystem.update(double timeDelta)` is called from the rendering thread. 

`ParticleSystem` has also `getParticles()` method where you return a reference to your particles and `getMaxCount()` where you return the maximum number of particles that you expect to be on the screen simultaneously. 

![Sample 2](https://raw.githubusercontent.com/shchurov/ParticleView/master/github_assets/demo2.gif)

License
-------
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
