<script setup lang="ts">
import { getAlbumDetail } from '@/api/system'
import type { AlbumDetail, Song } from '@/api/interface'
import Table from '@/components/Table.vue'
import { ElMessage } from 'element-plus'

const route = useRoute()
const album = ref<AlbumDetail | null>(null)
const loading = ref(false)

const fetchAlbum = async () => {
  if (route.path !== '/album') {
    return
  }
  const albumNameQuery = route.query.albumName
  const artistIdQuery = route.query.artistId
  const albumName = Array.isArray(albumNameQuery) ? albumNameQuery[0] : albumNameQuery
  const artistIdRaw = Array.isArray(artistIdQuery) ? artistIdQuery[0] : artistIdQuery
  const artistId = Number(artistIdRaw)
  if (!albumName || !Number.isFinite(artistId) || artistId <= 0) {
    album.value = null
    loading.value = false
    return
  }
  loading.value = true
  try {
    const res = await getAlbumDetail(albumName, artistId)
    if (res.code === 0 && res.data) {
      const data = res.data as AlbumDetail
      album.value = data
    } else {
      ElMessage.error(res.message || '获取专辑详情失败')
    }
  } catch (e: any) {
    ElMessage.error(e.message || '获取专辑详情失败')
  } finally {
    loading.value = false
  }
}

watch(() => [route.path, route.query.albumName, route.query.artistId], () => fetchAlbum(), { immediate: true })

const songs = computed<Song[]>(() => album.value?.songs || [])
</script>

<template>
  <div class="flex flex-col h-full bg-background flex-1 md:overflow-hidden">
    <div class="p-6" v-if="album">
      <div class="flex flex-col md:flex-row gap-6">
        <div class="flex-shrink-0 w-60 h-60">
          <img :alt="album.albumName" class="w-full h-full object-cover rounded-lg shadow-lg" :src="(album.coverUrl || '') + '?param=500y500'" />
        </div>
        <div class="flex flex-col justify-between">
          <div>
            <h1 class="text-3xl font-bold mb-2">{{ album.albumName }}</h1>
            <div class="flex items-center gap-2 text-sm text-muted-foreground mb-2">
              <span class="relative flex shrink-0 overflow-hidden rounded-full w-6 h-6">
                <img class="aspect-square h-full w-full" :alt="album.artistName" :src="album.artistAvatar || ''" />
              </span>
              <span>{{ album.artistName }}</span>
              <span>•</span>
              <span>{{ album.songCount }} 首歌曲</span>
            </div>
            <p class="text-muted-foreground mb-4 line-clamp-3" v-if="album.artistIntroduction">{{ album.artistIntroduction }}</p>
            <div class="text-sm text-muted-foreground">
              <span>发行时间：{{ album.releaseTime }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="flex-1 overflow-y-auto min-h-0">
      <div class="p-6" v-if="songs.length">
        <Table :data="songs" :artistId="album?.artistId" />
      </div>
      <div v-else class="p-6" :class="{ 'opacity-50': loading }">
        <el-empty description="暂无专辑数据" />
      </div>
    </div>
  </div>
</template>
