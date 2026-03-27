<script setup lang="ts">
import { Icon } from '@iconify/vue'
import Avatar from './components/avatar.vue'
import { themeStore } from '@/stores/modules/theme'

const route = useRoute()
const router = useRouter()
const theme = themeStore()

const searchText = ref('')

const toggleMode = () => {
  theme.toggleDark()
}

const backgroundEffect = computed({
  get: () => theme.backgroundEffect,
  set: (val: 'aurora' | 'colorBends') => theme.setBackgroundEffect(val)
})

watch(
  () => route.query,
  (newValue) => {
    if (newValue.query) {
      searchText.value = newValue.query as string
    }
  },
  { immediate: true }
)
</script>

<template>
  <header class="px-5 flex items-center justify-between">
    <!-- Logo/Title -->
    <div class="flex items-center gap-4 cursor-pointer" @click="router.push('/')">
      <img src="/logo.svg" alt="logo" class="w-10 h-10" />
      <span class="text-xl font-bold tracking-tight">Vibe Music</span>
    </div>

    <!-- Search & Controls -->
    <div class="flex items-center gap-4">
      <div class="relative group">
        <Icon
          icon="mdi:magnify"
          class="absolute left-3 top-1/2 -translate-y-1/2 text-foreground/40 text-lg group-focus-within:text-primary transition-colors"
        />
        <input
          v-model="searchText"
          type="text"
          class="w-60 bg-white/38 border border-white/55 rounded-xl pl-10 pr-4 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-primary/45 focus:w-80 transition-all duration-300 placeholder:text-foreground/35 shadow-[inset_0_1px_0_rgba(255,255,255,0.7)]"
          placeholder="搜索音乐、艺术家..."
          @keyup.enter="router.push('/library?query=' + searchText)"
        />
      </div>

      <!-- Background Selector -->
      <el-select v-model="backgroundEffect" size="small" class="w-32 effect-select">
        <el-option label="Aurora 极光" value="aurora" />
        <el-option label="ColorBends 渐变" value="colorBends" />
      </el-select>

      <!-- Theme Toggle -->
      <button 
        class="p-2.5 rounded-xl bg-white/34 border border-white/55 hover:bg-white/55 transition-all active:scale-95 shadow-[inset_0_1px_0_rgba(255,255,255,0.7)]"
        @click="toggleMode"
      >
        <Icon class="text-xl" :icon="theme.themeIcon" />
      </button>

      <!-- User Avatar -->
      <div class="flex items-center gap-3 border-l border-white/30 pl-5 ml-1">
        <Avatar />
      </div>
    </div>
  </header>
</template>

<style scoped>
:deep(.effect-select .el-input__wrapper) {
  background: rgba(255, 255, 255, 0.34) !important;
  box-shadow: none !important;
  border: 1px solid rgba(255, 255, 255, 0.55) !important;
  border-radius: 0.75rem !important;
}

:deep(.effect-select .el-input__inner) {
  color: currentColor !important;
}
</style>
