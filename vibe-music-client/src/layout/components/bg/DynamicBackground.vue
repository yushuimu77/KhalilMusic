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
  background: radial-gradient(circle at 20% 25%, rgba(138, 43, 226, 0.7), transparent 58%);
  animation: aurora-flow-a 12s ease-in-out infinite alternate;
}

.wave-2 {
  background: radial-gradient(circle at 72% 36%, rgba(0, 100, 255, 0.65), transparent 62%);
  animation: aurora-flow-b 14s ease-in-out infinite alternate;
}

.wave-3 {
  background: radial-gradient(circle at 44% 72%, rgba(0, 206, 209, 0.6), transparent 65%);
  animation: aurora-flow-c 16s ease-in-out infinite alternate;
}

.wave-4 {
  background: radial-gradient(circle at 84% 84%, rgba(75, 0, 130, 0.75), transparent 62%);
  animation: aurora-flow-d 13s ease-in-out infinite alternate;
}

.color-bends-bg {
  position: absolute;
  inset: -10%;
  filter: blur(80px);
}

.bend {
  position: absolute;
  border-radius: 50%;
  opacity: 0.6;
}

.bend-1 {
  width: 80vmax;
  height: 80vmax;
  background: radial-gradient(circle, rgba(255, 99, 132, 0.5) 0%, transparent 70%);
  top: -20%;
  left: -20%;
  animation: float-1 25s infinite linear;
}

.bend-2 {
  width: 70vmax;
  height: 70vmax;
  background: radial-gradient(circle, rgba(54, 162, 235, 0.5) 0%, transparent 70%);
  bottom: -10%;
  right: -10%;
  animation: float-2 28s infinite linear;
}

.bend-3 {
  width: 60vmax;
  height: 60vmax;
  background: radial-gradient(circle, rgba(255, 206, 86, 0.4) 0%, transparent 70%);
  top: 40%;
  left: 30%;
  animation: float-3 30s infinite linear;
}

.bend-4 {
  width: 50vmax;
  height: 50vmax;
  background: radial-gradient(circle, rgba(75, 192, 192, 0.5) 0%, transparent 70%);
  top: 10%;
  right: 20%;
  animation: float-1 22s infinite linear reverse;
}

.noise-layer {
  position: absolute;
  inset: 0;
  background-image: url("data:image/svg+xml,%3Csvg viewBox='0 0 200 200' xmlns='http://www.w3.org/2000/svg'%3E%3Cfilter id='noiseFilter'%3E%3CfeTurbulence type='fractalNoise' baseFrequency='0.65' numOctaves='3' stitchTiles='stitch'/%3E%3C/filter%3E%3Crect width='100%25' height='100%25' filter='url(%23noiseFilter)'/%3E%3C/svg%3E");
  opacity: 0.04;
  mix-blend-mode: overlay;
}

@keyframes float-1 {
  0% { transform: rotate(0deg) translate(5%, 5%) rotate(0deg) scale(1); }
  50% { transform: rotate(180deg) translate(15%, 10%) rotate(-180deg) scale(1.1); }
  100% { transform: rotate(360deg) translate(5%, 5%) rotate(-360deg) scale(1); }
}

@keyframes float-2 {
  0% { transform: rotate(0deg) translate(-5%, -5%) rotate(0deg) scale(1); }
  50% { transform: rotate(-180deg) translate(-15%, -15%) rotate(180deg) scale(1.2); }
  100% { transform: rotate(-360deg) translate(-5%, -5%) rotate(360deg) scale(1); }
}

@keyframes float-3 {
  0% { transform: rotate(0deg) translate(10%, -10%) rotate(0deg) scale(1); }
  50% { transform: rotate(180deg) translate(20%, -20%) rotate(-180deg) scale(1.15); }
  100% { transform: rotate(360deg) translate(10%, -10%) rotate(-360deg) scale(1); }
}

@keyframes aurora-flow-a {
  0% { transform: translate3d(-8%, -4%, 0) scale(1.02) rotate(0deg); }
  50% { transform: translate3d(15%, 10%, 0) scale(1.25) rotate(5deg); }
  100% { transform: translate3d(-8%, -4%, 0) scale(1.02) rotate(0deg); }
}

@keyframes aurora-flow-b {
  0% { transform: translate3d(8%, -6%, 0) scale(1.08) rotate(0deg); }
  50% { transform: translate3d(-12%, 15%, 0) scale(0.9) rotate(-5deg); }
  100% { transform: translate3d(8%, -6%, 0) scale(1.08) rotate(0deg); }
}

@keyframes aurora-flow-c {
  0% { transform: translate3d(-4%, 10%, 0) rotate(-4deg) scale(1); }
  50% { transform: translate3d(12%, -15%, 0) rotate(8deg) scale(1.15); }
  100% { transform: translate3d(-4%, 10%, 0) rotate(-4deg) scale(1); }
}

@keyframes aurora-flow-d {
  0% { transform: translate3d(4%, -2%, 0) scale(0.95) rotate(0deg); }
  50% { transform: translate3d(-15%, 12%, 0) scale(1.2) rotate(-8deg); }
  100% { transform: translate3d(4%, -2%, 0) scale(0.95) rotate(0deg); }
}
</style>
