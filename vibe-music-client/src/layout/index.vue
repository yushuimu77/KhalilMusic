<script setup lang="ts">
import Header from './components/header/index.vue'
import Aside from './components/aside/index.vue'
import Main from './components/main/index.vue'
import Footer from './components/footer/index.vue'
import DynamicBackground from './components/bg/DynamicBackground.vue'
import { AudioPlayer } from '@/hooks/useAudioPlayer'

provide('audioPlayer', AudioPlayer())
</script>

<template>
  <div class="relative w-full h-full overflow-hidden p-3 flex flex-col">
    <DynamicBackground />
    <div class="glass-effect rounded-[1.4rem] p-3 w-full h-full z-10 flex flex-col gap-3">
      <Header class="glass-effect rounded-2xl h-16 shrink-0 z-20 glass-effect-hover" />
      <div class="flex flex-1 gap-3 overflow-hidden">
        <Aside class="glass-effect rounded-2xl w-64 shrink-0 hidden md:block glass-effect-hover" />
        <div class="flex-1 glass-effect rounded-2xl overflow-hidden">
          <Main />
        </div>
      </div>
      <Footer class="glass-effect rounded-2xl h-24 shrink-0 z-20" />
    </div>
  </div>
</template>

<style scoped>
/* Ensure the layout takes full viewport */
:global(html, body, #app) {
  margin: 0;
  padding: 0;
  width: 100%;
  height: 100%;
  overflow: hidden;
}
</style>
