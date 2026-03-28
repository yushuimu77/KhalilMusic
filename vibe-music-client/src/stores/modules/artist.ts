import { defineStore } from 'pinia'
import type { Song } from '@/api/interface'

interface ArtistInfo {
  artistId: number
  artistName: string
  avatar: string
  birth: string
  area: string
  introduction: string
  songs: Song[]
  albums: Album[]
  songCount: number
  albumCount: number
  mvCount: number
  isFollowed: boolean
}

interface Album {
  albumId: number
  albumName: string
  artistId: number
  artistName: string
  albumCoverUrl: string
  releaseTime: string
  albumIntroduction: string
}

export const useArtistStore = defineStore('ArtistStore', {
  state: () => ({
    artistInfo: null as ArtistInfo | null,
  }),
  actions: {
    setArtistInfo(info: ArtistInfo) {
      this.artistInfo = info
    },
  },
}) 