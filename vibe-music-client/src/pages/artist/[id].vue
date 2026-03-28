<script setup lang="ts">
import { getArtistDetail, followArtist, unfollowArtist } from '@/api/system'
import Table from '@/components/Table.vue'
import { useArtistStore } from '@/stores/modules/artist'
import { ElMessage } from 'element-plus'
import { useRoute, useRouter } from 'vue-router'
import { Icon } from '@iconify/vue'
import { AudioStore } from '@/stores/modules/audio'
import { useAudioPlayer } from '@/hooks/useAudioPlayer'
import { UserStore } from '@/stores/modules/user'
import default_album from '@/assets/default_album.jpg'

const route = useRoute()
const router = useRouter()
const artistStore = useArtistStore()
const audioStore = AudioStore()
const userStore = UserStore()
const { loadTrack, play } = useAudioPlayer()

// 歌手数据
const artistInfo = computed(() => artistStore.artistInfo)
const activeTab = ref('songs')

const fetchArtistDetail = async () => {
    const id = route.params.id
    const numericId = parseInt(id.toString())

    try {
        artistStore.setArtistInfo(null as any) // 清空之前的数据
        const res = await getArtistDetail(numericId)

        if (res.code === 0 && res.data) {
            artistStore.setArtistInfo(res.data as any)
        } else {
            ElMessage.error(res.message || '获取歌手信息失败')
        }
    } catch (error) {
        console.error('获取歌手详情失败:', error)
        ElMessage.error('获取歌手信息失败，请稍后重试')
    }
}

watch(
    () => route.params.id,
    () => {
        fetchArtistDetail()
    },
    { immediate: true }
)

// 转换歌曲实体
const convertToTrackModel = (song: any) => {
    if (!song.songId || !song.songName || !song.audioUrl) {
        return null
    }
    return {
        id: song.songId.toString(),
        title: song.songName,
        artist: song.artistName,
        album: song.album,
        cover: song.coverUrl || default_album,
        url: song.audioUrl,
        duration: Number(song.duration) || 0,
        likeStatus: song.likeStatus || 0,
    }
}

// 播放热门歌曲
const handlePlayHot = async () => {
    if (!artistInfo.value?.songs?.length) return

    const allTracks = artistInfo.value.songs
        .map(song => convertToTrackModel(song))
        .filter(track => track !== null)

    audioStore.setAudioStore('trackList', allTracks)
    audioStore.setAudioStore('currentSongIndex', 0)

    await loadTrack()
    play()
}

// 随机播放
const handleRandomPlay = async () => {
    if (!artistInfo.value?.songs?.length) return

    const allTracks = artistInfo.value.songs
        .map(song => convertToTrackModel(song))
        .filter(track => track !== null)

    // 随机洗牌
    for (let i = allTracks.length - 1; i > 0; i--) {
        const j = Math.floor(Math.random() * (i + 1));
        [allTracks[i], allTracks[j]] = [allTracks[j], allTracks[i]];
    }

    audioStore.setAudioStore('trackList', allTracks)
    audioStore.setAudioStore('currentSongIndex', 0)

    await loadTrack()
    play()
}

// 关注/取消关注
const handleFollow = async () => {
    if (!userStore.isLoggedIn) {
        ElMessage.warning('请先登录')
        return
    }

    if (!artistInfo.value) return

    try {
        if (artistInfo.value.isFollowed) {
            const res = await unfollowArtist(artistInfo.value.artistId)
            if (res.code === 0) {
                artistInfo.value.isFollowed = false
                ElMessage.success('已取消关注')
            }
        } else {
            const res = await followArtist(artistInfo.value.artistId)
            if (res.code === 0) {
                artistInfo.value.isFollowed = true
                ElMessage.success('关注成功')
            }
        }
    } catch (error) {
        ElMessage.error('操作失败')
    }
}

// 跳转到专辑详情
const goAlbumDetail = (album: any) => {
    router.push({
        path: '/album',
        query: {
            albumName: album.albumName,
            artistId: album.artistId
        }
    })
}
</script>

<template>
    <div class="container mx-auto py-10 px-5 h-full flex-1 flex flex-col">
        <!-- 歌手详情 Header -->
        <div class="relative rounded-3xl bg-[#2d2841] p-10 shadow-xl mb-8">
            <div class="flex flex-col lg:flex-row items-center gap-12 relative z-10">
                <!-- 歌手头像 -->
                <div class="w-64 h-64 rounded-full overflow-hidden border-4 border-white/20 shadow-2xl shrink-0 bg-gray-800">
                    <img :src="artistInfo?.avatar" :alt="artistInfo?.artistName" class="w-full h-full object-contain" />
                </div>

                <!-- 歌手信息 -->
                <div class="text-center lg:text-left flex-1 text-white">
                    <h1 class="text-6xl font-bold mb-4 tracking-tight">
                        {{ artistInfo?.artistName }}
                    </h1>
                    <p class="text-xl text-gray-400 mb-8" v-if="artistInfo?.area">
                        {{ artistInfo?.area }}
                    </p>

                    <!-- 统计数据 -->
                    <div class="flex flex-wrap justify-center lg:justify-start gap-4 mb-10">
                        <div class="flex items-center gap-2 px-5 py-2.5 bg-white/10 backdrop-blur-md rounded-full text-pink-400 border border-white/5">
                            <Icon icon="ph:music-notes-fill" class="text-lg" />
                            <span class="font-bold text-base">{{ artistInfo?.songCount || 0 }} 歌曲</span>
                        </div>
                        <div class="flex items-center gap-2 px-5 py-2.5 bg-white/10 backdrop-blur-md rounded-full text-purple-400 border border-white/5">
                            <Icon icon="ph:disc-fill" class="text-lg" />
                            <span class="font-bold text-base">{{ artistInfo?.albumCount || 0 }} 专辑</span>
                        </div>
                        <div class="flex items-center gap-2 px-5 py-2.5 bg-white/10 backdrop-blur-md rounded-full text-blue-400 border border-white/5">
                            <Icon icon="ph:video-fill" class="text-lg" />
                            <span class="font-bold text-base">{{ artistInfo?.mvCount || 0 }} MV</span>
                        </div>
                    </div>

                    <!-- 操作按钮 -->
                    <div class="flex flex-wrap justify-center lg:justify-start gap-5">
                        <button @click="handlePlayHot"
                            class="flex items-center gap-3 px-10 py-4 bg-primary text-primary-foreground rounded-full font-black hover:scale-105 active:scale-95 transition-all shadow-lg shadow-primary/20">
                            <Icon icon="ph:play-fill" class="text-2xl" />
                            播放热门
                        </button>
                        <button @click="handleRandomPlay"
                            class="flex items-center gap-3 px-10 py-4 bg-white/10 text-white rounded-full font-black hover:bg-white/20 hover:scale-105 active:scale-95 transition-all border border-white/10 backdrop-blur-md shadow-lg">
                            <Icon icon="ph:shuffle-fill" class="text-2xl" />
                            随机播放
                        </button>
                        <button @click="handleFollow"
                            class="flex items-center gap-3 px-10 py-4 rounded-full font-black transition-all border border-white/10 shadow-lg hover:scale-105 active:scale-95"
                            :class="artistInfo?.isFollowed ? 'bg-white/20 text-white' : 'bg-white text-gray-900 hover:bg-gray-100'">
                            <Icon :icon="artistInfo?.isFollowed ? 'ph:heart-fill' : 'ph:heart'"
                                :class="artistInfo?.isFollowed ? 'text-red-500 text-2xl' : 'text-2xl'" />
                            {{ artistInfo?.isFollowed ? '已关注' : '关注' }}
                        </button>
                    </div>
                </div>
            </div>
        </div>

        <!-- 简介部分 -->
        <div class="mb-12 p-8 bg-white dark:bg-gray-900 rounded-3xl border border-border shadow-sm">
            <h3 class="flex items-center gap-3 text-xl font-black mb-5">
                <Icon icon="ph:info-bold" class="text-primary text-2xl" />
                艺人简介
            </h3>
            <p class="text-muted-foreground leading-loose text-lg font-medium italic">
                {{ artistInfo?.introduction || '暂无详细介绍' }}
            </p>
        </div>

        <!-- 内容 Tab -->
        <div class="flex flex-col flex-1">
            <!-- Tab 切换 -->
            <div class="flex items-center gap-12 border-b border-border mb-10 px-4">
                <button @click="activeTab = 'songs'" class="relative py-5 text-2xl font-black transition-all group"
                    :class="activeTab === 'songs' ? 'text-primary' : 'text-muted-foreground hover:text-foreground'">
                    <div class="flex items-center gap-3">
                        <Icon icon="ph:music-notes-bold" />
                        热门歌曲
                        <span class="text-sm font-bold bg-gray-100 dark:bg-gray-800 px-2 py-0.5 rounded-md opacity-70">{{ artistInfo?.songCount || 0 }}</span>
                    </div>
                    <div v-if="activeTab === 'songs'" class="absolute bottom-0 left-0 right-0 h-1.5 bg-primary rounded-full shadow-[0_-2px_10px_rgba(var(--primary-rgb),0.5)]">
                    </div>
                </button>
                <button @click="activeTab = 'albums'" class="relative py-5 text-2xl font-black transition-all group"
                    :class="activeTab === 'albums' ? 'text-primary' : 'text-muted-foreground hover:text-foreground'">
                    <div class="flex items-center gap-3">
                        <Icon icon="ph:disc-bold" />
                        所有专辑
                        <span class="text-sm font-bold bg-gray-100 dark:bg-gray-800 px-2 py-0.5 rounded-md opacity-70">{{ artistInfo?.albumCount || 0 }}</span>
                    </div>
                    <div v-if="activeTab === 'albums'" class="absolute bottom-0 left-0 right-0 h-1.5 bg-primary rounded-full shadow-[0_-2px_10px_rgba(var(--primary-rgb),0.5)]">
                    </div>
                </button>
            </div>

            <!-- 内容展示区 -->
            <div class="w-full flex-1">
                <!-- 歌曲列表 -->
                <div v-if="activeTab === 'songs'" class="h-full animate-in fade-in slide-in-from-bottom-4 duration-500">
                    <Table :data="artistInfo?.songs" :artistId="artistInfo?.artistId" />
                </div>

                <!-- 专辑列表 -->
                <div v-else-if="activeTab === 'albums'" class="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-6 gap-8 animate-in fade-in slide-in-from-bottom-4 duration-500">
                    <div v-for="album in artistInfo?.albums" :key="album.albumId"
                        @click="goAlbumDetail(album)"
                        class="group cursor-pointer">
                        <div class="aspect-square rounded-3xl overflow-hidden mb-4 shadow-lg group-hover:shadow-2xl group-hover:-translate-y-2 transition-all duration-500 relative">
                            <img :src="album.albumCoverUrl || default_album" :alt="album.albumName"
                                class="w-full h-full object-cover group-hover:scale-110 transition-transform duration-700" />
                            <div class="absolute inset-0 bg-black/40 opacity-0 group-hover:opacity-100 transition-all duration-500 flex items-center justify-center backdrop-blur-[2px]">
                                <div class="w-16 h-16 bg-primary text-primary-foreground rounded-full flex items-center justify-center shadow-2xl transform scale-50 group-hover:scale-100 transition-all duration-500">
                                    <Icon icon="ph:play-fill" class="text-3xl" />
                                </div>
                            </div>
                        </div>
                        <h4 class="font-black text-lg line-clamp-1 group-hover:text-primary transition-colors mb-1">{{ album.albumName }}</h4>
                        <p class="text-sm font-bold text-muted-foreground flex items-center gap-1">
                            <Icon icon="ph:calendar-blank" />
                            {{ album.releaseTime }}
                        </p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>
