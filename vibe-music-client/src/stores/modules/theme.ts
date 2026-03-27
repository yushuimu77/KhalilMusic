import { defineStore } from 'pinia'
import piniaPersistConfig from '@/stores/helper/persist'
import { ThemeState } from '@/stores/interface'

const applyDarkMode = (isDark: boolean) => {
  const root = document.documentElement
  root.classList.remove('light', 'dark')
  root.classList.add(isDark ? 'dark' : 'light')
}

const applyPrimary = (primary: string) => {
  document.documentElement.style.setProperty('--primary', primary)
}

const applyBackgroundEffect = (effect: ThemeState['backgroundEffect']) => {
  document.documentElement.setAttribute('data-bg-effect', effect)
}

export const themeStore = defineStore({
  id: 'themeStore',
  state: (): ThemeState => ({
    isDark: false,
    primary: '#7E22CE',
    backgroundEffect: 'aurora',
  }),
  getters: {
    themeIcon: state => (state.isDark ? 'mdi:weather-night' : 'material-symbols:wb-sunny-outline-rounded'),
  },
  actions: {
    setDark(isDark: string | number | boolean) {
      this.isDark = Boolean(isDark)
      applyDarkMode(this.isDark)
    },
    toggleDark() {
      this.setDark(!this.isDark)
    },
    setPrimary(primary: string) {
      this.primary = primary
      applyPrimary(primary)
    },
    setBackgroundEffect(effect: ThemeState['backgroundEffect']) {
      this.backgroundEffect = effect
      applyBackgroundEffect(effect)
    },
    initializeTheme() {
      applyDarkMode(this.isDark)
      applyPrimary(this.primary)
      applyBackgroundEffect(this.backgroundEffect)
    }
  },
  persist: piniaPersistConfig('ThemeStore'),
})
