<script setup lang="ts">
import Left from './components/left.vue'
import Center from './components/center.vue'
import Right from './components/right.vue'
</script>

<template>
  <footer class="px-8 flex items-center justify-between gap-8 bg-[linear-gradient(90deg,rgba(164,238,255,0.72),rgba(150,220,255,0.62))] border border-white/65">
    <!-- Left: Song info -->
    <div class="flex-1 min-w-0">
      <Left />
    </div>
    
    <!-- Center: Playback controls -->
    <div class="flex-[2] max-w-2xl">
      <Center />
    </div>
    
    <!-- Right: Volume/Queue -->
    <div class="flex-1 flex justify-end">
      <Right />
    </div>
  </footer>
</template>

<style scoped>
/* Any footer specific styles can go here */
</style>
