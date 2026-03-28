<script setup lang="ts">
import { getFollowedArtists } from '@/api/system'
import { ref, onActivated, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { Icon } from '@iconify/vue'
import { ElMessage } from 'element-plus'

const route = useRoute()
const router = useRouter()
const followedArtists = ref<any[]>([])
const loading = ref(false)
const pageNum = ref(1)
const pageSize = ref(20)
const total = ref(0)

const fetchFollowedArtists = async () => {
  loading.value = true
  try {
    const res = await getFollowedArtists({
      pageNum: pageNum.value,
      pageSize: pageSize.value
    })
    if (res.code === 0 && res.data) {
      followedArtists.value = res.data.items || []
      total.value = res.data.total || 0
    }
  } catch (error) {
    ElMessage.error('获取关注歌手失败')
  } finally {
    loading.value = false
  }
}

const handlePageChange = (page: number) => {
  pageNum.value = page
  fetchFollowedArtists()
}

onMounted(() => {
  fetchFollowedArtists()
})

onActivated(() => {
  if (route.path === '/favorite-artist') {
    fetchFollowedArtists()
  }
})
</script>

<template>
  <div class="container mx-auto py-10 px-5 h-full flex flex-col">
    <div class="flex items-center gap-4 mb-10">
      <div class="w-12 h-12 rounded-2xl bg-primary/10 flex items-center justify-center text-primary shadow-lg shadow-primary/5">
        <Icon icon="ri:user-heart-line" class="text-2xl" />
      </div>
      <div>
        <h1 class="text-3xl font-black text-foreground tracking-tight">我喜欢的歌手</h1>
        <p class="text-sm font-medium text-muted-foreground mt-1 opacity-70">
          共关注了 {{ total }} 位艺人
        </p>
      </div>
    </div>

    <div v-if="loading" class="flex-1 flex items-center justify-center">
      <el-skeleton :rows="5" animated />
    </div>

    <div v-else-if="followedArtists.length > 0" class="flex-1">
      <div class="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-5 xl:grid-cols-6 gap-8">
        <div 
          v-for="artist in followedArtists" 
          :key="artist.artistId"
          class="group cursor-pointer flex flex-col items-center"
          @click="router.push(`/artist/${artist.artistId}`)"
        >
          <div class="relative w-full aspect-square mb-4">
            <div class="absolute inset-0 rounded-full bg-primary/5 group-hover:bg-primary/10 transition-colors duration-500 scale-110 blur-xl opacity-0 group-hover:opacity-100"></div>
            <div class="w-full h-full rounded-full overflow-hidden border-4 border-white dark:border-gray-800 shadow-lg group-hover:shadow-2xl group-hover:-translate-y-2 transition-all duration-500 relative z-10 bg-gray-100 dark:bg-gray-800">
              <img 
                :src="artist.avatar || ''" 
                :alt="artist.artistName"
                class="w-full h-full object-cover group-hover:scale-110 transition-transform duration-700"
              />
              <div class="absolute inset-0 bg-black/20 opacity-0 group-hover:opacity-100 transition-opacity duration-500 flex items-center justify-center">
                <Icon icon="ri:arrow-right-line" class="text-white text-3xl transform -translate-x-4 group-hover:translate-x-0 transition-transform duration-500" />
              </div>
            </div>
          </div>
          <h3 class="font-black text-lg text-center line-clamp-1 group-hover:text-primary transition-colors duration-300">
            {{ artist.artistName }}
          </h3>
        </div>
      </div>

      <div class="mt-12 flex justify-center" v-if="total > pageSize">
        <el-pagination
          v-model:current-page="pageNum"
          :page-size="pageSize"
          layout="prev, pager, next"
          :total="total"
          @current-change="handlePageChange"
          background
        />
      </div>
    </div>

    <div v-else class="flex-1 flex flex-col items-center justify-center text-muted-foreground gap-4">
      <div class="w-24 h-24 rounded-full bg-secondary/30 flex items-center justify-center opacity-40">
        <Icon icon="ri:user-search-line" class="text-5xl" />
      </div>
      <p class="text-lg font-bold opacity-60">暂无关注的歌手</p>
      <button 
        @click="router.push('/artist')"
        class="px-6 py-2.5 bg-primary text-primary-foreground rounded-full font-bold hover:scale-105 transition-transform shadow-lg shadow-primary/20"
      >
        去发现歌手
      </button>
    </div>
  </div>
</template>

<style scoped>
.animate-in {
  animation: fadeIn 0.5s ease-out forwards;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(10px); }
  to { opacity: 1; transform: translateY(0); }
}
</style>
