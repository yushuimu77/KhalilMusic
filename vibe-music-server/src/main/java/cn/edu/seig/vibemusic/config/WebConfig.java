package cn.edu.seig.vibemusic.config;

import cn.edu.seig.vibemusic.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Value("${storage.local.songCoversDir:../vibe-music-data/songCovers}")
    private String songCoversDir;

    @Value("${storage.local.artistsDir:../vibe-music-data/artists}")
    private String artistsDir;

    @Value("${storage.local.playlistsDir:../vibe-music-data/playlists}")
    private String playlistsDir;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 登录接口和注册接口不拦截
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**") // 拦截所有请求
                .excludePathPatterns(
                        "/admin/login", "/admin/logout", "/admin/register",
                        "/user/login", "/user/logout", "/user/register",
                        "/user/sendVerificationCode", "/user/resetUserPassword",
                        "/banner/getBannerList",
                        "/playlist/getAllPlaylists", "/playlist/getRecommendedPlaylists", "/playlist/getPlaylistDetail/**",
                        "/artist/getAllArtists", "/artist/getArtistDetail/**",
                        "/song/getAllSongs", "/song/getRecommendedSongs", "/song/getSongDetail/**",
                        "/songCovers/**",
                        "/artists/**",
                        "/playlists/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path dir = Paths.get(songCoversDir).toAbsolutePath().normalize();
        registry.addResourceHandler("/songCovers/**")
                .addResourceLocations(dir.toUri().toString());

        Path artists = Paths.get(artistsDir).toAbsolutePath().normalize();
        registry.addResourceHandler("/artists/**")
                .addResourceLocations(artists.toUri().toString());

        Path playlists = Paths.get(playlistsDir).toAbsolutePath().normalize();
        registry.addResourceHandler("/playlists/**")
                .addResourceLocations(playlists.toUri().toString());
    }
}
