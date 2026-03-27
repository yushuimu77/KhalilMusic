<template>
  <div class="dynamic-bg-container">
    <div v-if="theme.backgroundEffect === 'aurora'" class="aurora-bg">
      <div class="aurora-wave wave-1"></div>
      <div class="aurora-wave wave-2"></div>
      <div class="aurora-wave wave-3"></div>
      <div class="aurora-wave wave-4"></div>
    </div>
    <div v-else class="color-bends-bg">
      <div class="bend bend-1"></div>
      <div class="bend bend-2"></div>
      <div class="bend bend-3"></div>
      <div class="bend bend-4"></div>
    </div>
    <div class="noise-layer"></div>
  </div>
</template>

<script setup lang="ts">
import { themeStore } from '@/stores/modules/theme'

const theme = themeStore()
</script>

<style scoped>
.dynamic-bg-container {
  position: absolute;
  inset: 0;
  z-index: 0;
  overflow: hidden;
  pointer-events: none;
  background-color: var(--theme-bg-color, hsl(var(--background)));
  transition: background-color 0.5s ease;
}

.aurora-bg {
  position: absolute;
  inset: -15%;
  filter: blur(48px) saturate(145%);
}

.aurora-wave {
  position: absolute;
  inset: 0;
  mix-blend-mode: screen;
  opacity: 0.7;
}

.dark .aurora-wave {
  mix-blend-mode: lighten;
  opacity: 0.58;
}

.wave-1 {
  background: radial-gradient(circle at 20% 25%, rgba(255, 176, 248, 0.52), transparent 58%);
  animation: aurora-flow-a 20s ease-in-out infinite alternate;
}

.wave-2 {
  background: radial-gradient(circle at 72% 36%, rgba(169, 196, 255, 0.48), transparent 62%);
  animation: aurora-flow-b 24s ease-in-out infinite alternate;
}

.wave-3 {
  background: radial-gradient(circle at 44% 72%, rgba(199, 147, 255, 0.42), transparent 65%);
  animation: aurora-flow-c 28s ease-in-out infinite alternate;
}

.wave-4 {
  background: radial-gradient(circle at 84% 84%, rgba(112, 210, 255, 0.34), transparent 62%);
  animation: aurora-flow-d 22s ease-in-out infinite alternate;
}

.color-bends-bg {
  position: absolute;
  inset: -10%;
  filter: blur(80px);
}

.bend {
  position: absolute;
  border-radius: 50%;
  opacity: 0.5;
}

.bend-1 {
  width: 80vmax;
  height: 80vmax;
  background: radial-gradient(circle, rgba(255, 177, 245, 0.46) 0%, transparent 70%);
  top: -20%;
  left: -20%;
  animation: float-1 40s infinite linear;
}

.bend-2 {
  width: 70vmax;
  height: 70vmax;
  background: radial-gradient(circle, rgba(153, 192, 255, 0.44) 0%, transparent 70%);
  bottom: -10%;
  right: -10%;
  animation: float-2 45s infinite linear;
}

.bend-3 {
  width: 60vmax;
  height: 60vmax;
  background: radial-gradient(circle, rgba(201, 139, 255, 0.4) 0%, transparent 70%);
  top: 40%;
  left: 30%;
  animation: float-3 50s infinite linear;
}

.bend-4 {
  width: 50vmax;
  height: 50vmax;
  background: radial-gradient(circle, rgba(140, 231, 255, 0.36) 0%, transparent 70%);
  top: 10%;
  right: 20%;
  animation: float-1 35s infinite linear reverse;
}

.noise-layer {
  position: absolute;
  inset: 0;
  background-image: url("data:image/svg+xml,%3Csvg viewBox='0 0 200 200' xmlns='http://www.w3.org/2000/svg'%3E%3Cfilter id='noiseFilter'%3E%3CfeTurbulence type='fractalNoise' baseFrequency='0.65' numOctaves='3' stitchTiles='stitch'/%3E%3C/filter%3E%3Crect width='100%25' height='100%25' filter='url(%23noiseFilter)'/%3E%3C/svg%3E");
  opacity: 0.04;
  mix-blend-mode: overlay;
}

@keyframes float-1 {
  from { transform: rotate(0deg) translate(5%, 5%) rotate(0deg); }
  to { transform: rotate(360deg) translate(5%, 5%) rotate(-360deg); }
}

@keyframes float-2 {
  from { transform: rotate(0deg) translate(-5%, -5%) rotate(0deg); }
  to { transform: rotate(-360deg) translate(-5%, -5%) rotate(360deg); }
}

@keyframes float-3 {
  from { transform: rotate(0deg) translate(10%, -10%) rotate(0deg); }
  to { transform: rotate(360deg) translate(10%, -10%) rotate(-360deg); }
}

@keyframes aurora-flow-a {
  from { transform: translate3d(-8%, -4%, 0) scale(1.02); }
  to { transform: translate3d(10%, 6%, 0) scale(1.2); }
}

@keyframes aurora-flow-b {
  from { transform: translate3d(8%, -6%, 0) scale(1.08); }
  to { transform: translate3d(-6%, 8%, 0) scale(0.96); }
}

@keyframes aurora-flow-c {
  from { transform: translate3d(-4%, 10%, 0) rotate(-4deg); }
  to { transform: translate3d(6%, -8%, 0) rotate(6deg); }
}

@keyframes aurora-flow-d {
  from { transform: translate3d(4%, -2%, 0) scale(0.95); }
  to { transform: translate3d(-8%, 8%, 0) scale(1.16); }
}
</style>
