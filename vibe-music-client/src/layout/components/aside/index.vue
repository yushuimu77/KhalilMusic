<script setup lang="ts">
import { MenuData } from './data'
import { Icon } from '@iconify/vue'
import { useRoute, useRouter } from 'vue-router'
import { ref, watch } from 'vue'
import { UserStore } from '@/stores/modules/user'
import { ElMessage } from 'element-plus'
import AuthTabs from '@/components/Auth/AuthTabs.vue'
import { useFavoriteStore } from '@/stores/modules/favorite'

const route = useRoute()
const router = useRouter()
const user = UserStore()
const favoriteStore = useFavoriteStore()
const authVisible = ref(false)

const handleProtectedRoute = (path: string) => {
  if (!user.isLoggedIn && (path === '/like' || path === '/user')) {
    ElMessage.warning('请先登录')
    authVisible.value = true
    return false
  }
  return true
}

watch(
  () => user.isLoggedIn,
  (newVal) => {
    if (newVal) {
      favoriteStore.getFavoritePlaylists()
    } else {
      favoriteStore.clearFavoritePlaylists()
    }
  },
  { immediate: true }
)
</script>

<template>
  <aside class="flex flex-col h-full overflow-hidden bg-transparent border-none">
    <nav class="flex-1 p-4 space-y-8 overflow-y-auto">
      <div v-for="(group, index) in MenuData" :key="index" class="space-y-2">
        <h3 class="px-4 text-[11px] font-bold text-foreground/30 uppercase tracking-[2px]">
          {{ group.title }}
        </h3>
        
        <div class="space-y-1">
          <div v-for="(item, idx) in group.children" :key="idx"
            class="group px-4 py-2.5 rounded-xl flex items-center gap-3 cursor-pointer transition-all duration-300"
            :class="{
              'bg-white/70 text-primary shadow-[0_10px_24px_rgba(108,123,255,0.28)] scale-[1.02] border border-white/80': route.path === item.router,
              'text-foreground/65 hover:text-foreground hover:bg-white/35': route.path !== item.router,
            }"
            @click="handleProtectedRoute(item.router) && router.push(item.router)"
          >
            <Icon :icon="item.icon" class="text-xl group-hover:scale-110 transition-transform" />
            <span class="font-medium text-[15px]">{{ item.title }}</span>
          </div>
        </div>
      </div>

      <!-- Favorites Section -->
      <div v-if="user.isLoggedIn && favoriteStore.favoritePlaylists.length > 0" class="space-y-2">
        <h3 class="px-4 text-[11px] font-bold text-foreground/30 uppercase tracking-[2px]">
          我的歌单
        </h3>
        <div class="space-y-1">
          <div v-for="item in favoriteStore.favoritePlaylists" :key="item.id"
            class="px-4 py-2 rounded-xl flex items-center gap-3 cursor-pointer hover:bg-white/35 transition-all text-foreground/60 hover:text-foreground"
            @click="router.push(`/playlist/${item.id}`)"
          >
            <el-image 
              lazy 
              :src="item.coverImgUrl + '?param=40y40'" 
              class="w-9 h-9 rounded-lg shrink-0 border border-white/10 shadow-sm"
            />
            <span class="truncate text-[14px]">{{ item.name }}</span>
          </div>
        </div>
      </div>
    </nav>

    <!-- Auth Dialog -->
    <AuthTabs v-model="authVisible" />
  </aside>
</template>

<style scoped>
/* Custom scrollbar for Aside */
nav::-webkit-scrollbar {
  width: 4px;
}
nav::-webkit-scrollbar-thumb {
  background: rgba(255, 255, 255, 0.05);
  border-radius: 10px;
}
nav:hover::-webkit-scrollbar-thumb {
  background: rgba(255, 255, 255, 0.1);
}
</style>
