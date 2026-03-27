package cn.edu.seig.vibemusic.mapper;

import cn.edu.seig.vibemusic.model.entity.Album;
import cn.edu.seig.vibemusic.model.entity.Song;
import cn.edu.seig.vibemusic.model.vo.AlbumDetailVO;
import cn.edu.seig.vibemusic.model.vo.SongAdminVO;
import cn.edu.seig.vibemusic.model.vo.SongDetailVO;
import cn.edu.seig.vibemusic.model.vo.SongVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author sunpingli
 * @since 2025-01-09
 */
@Mapper
public interface SongMapper extends BaseMapper<Song> {

//    // 获取歌曲列表
//    @Select("""
//                SELECT
//                    s.id AS songId,
//                    s.name AS songName,
//                    s.album,
//                    s.duration,
//                    s.cover_url AS coverUrl,
//                    s.audio_url AS audioUrl,
//                    s.release_time AS releaseTime,
//                    a.name AS artistName
//                FROM tb_song s
//                LEFT JOIN tb_artist a ON s.artist_id = a.id
//                WHERE
//                    (#{songName} IS NULL OR s.name LIKE CONCAT('%', #{songName}, '%'))
//                    AND (#{artistName} IS NULL OR a.name LIKE CONCAT('%', #{artistName}, '%'))
//                    AND (#{album} IS NULL OR s.album LIKE CONCAT('%', #{album}, '%'))
//            """)

    @Select("SELECT s.id AS songId, s.artist_id AS artistId, s.name AS songName, s.album, s.duration, " +
            "s.cover_url AS coverUrl, s.audio_url AS audioUrl, s.release_time AS releaseTime, a.name AS artistName " +
            "FROM tb_song s " +
            "LEFT JOIN tb_artist a ON s.artist_id = a.id " +
            "WHERE " +
            "((#{songName} IS NULL OR #{songName} = '') AND (#{artistName} IS NULL OR #{artistName} = '') AND (#{album} IS NULL OR #{album} = '')) " +
            "OR (#{songName} IS NOT NULL AND #{songName} != '' AND s.name LIKE CONCAT('%', #{songName}, '%')) " +
            "OR (#{artistName} IS NOT NULL AND #{artistName} != '' AND a.name LIKE CONCAT('%', #{artistName}, '%')) " +
            "OR (#{album} IS NOT NULL AND #{album} != '' AND s.album LIKE CONCAT('%', #{album}, '%'))")
    IPage<SongVO> getSongsWithArtist(Page<SongVO> page,
                                     @Param("songName") String songName,
                                     @Param("artistName") String artistName,
                                     @Param("album") String album);
//@Select("SELECT s.id AS songId, s.artist_id AS artistId, s.name AS songName, s.album, s.duration, s.cover_url AS coverUrl, s.audio_url AS audioUrl, s.release_time AS releaseTime, a.name AS artistName " +
//        "FROM tb_song s " +
//        "LEFT JOIN tb_artist a ON s.artist_id = a.id " +
//        "WHERE (#{songName} IS NULL OR s.name LIKE CONCAT('%', #{songName}, '%')) " +
//        "AND (#{artistName} IS NULL OR a.name LIKE CONCAT('%', #{artistName}, '%')) " +
//        "AND (#{album} IS NULL OR s.album LIKE CONCAT('%', #{album}, '%'))")
//    IPage<SongVO> getSongsWithArtist(Page<SongVO> page,
//                                     @Param("songName") String songName,
//                                     @Param("artistName") String artistName,
//                                     @Param("album") String album);





//    // 获取歌曲列表
//    @Select("""
//                SELECT
//                    s.id AS songId,
//                    s.name AS songName,
//                    s.artist_id AS artistId,
//                    s.album,
//                    s.lyric,
//                    s.duration,
//                    s.style,
//                    s.cover_url AS coverUrl,
//                    s.audio_url AS audioUrl,
//                    s.release_time AS releaseTime,
//                    a.name AS artistName
//                FROM tb_song s
//                LEFT JOIN tb_artist a ON s.artist_id = a.id
//                WHERE
//                    (#{artistId} IS NULL OR s.artist_id = #{artistId})
//                    AND(#{songName} IS NULL OR s.name LIKE CONCAT('%', #{songName}, '%'))
//                    AND (#{album} IS NULL OR s.album LIKE CONCAT('%', #{album}, '%'))
//                ORDER BY s.release_time DESC
//            """)
//    IPage<SongAdminVO> getSongsWithArtistName(Page<SongAdminVO> page,
//                                              @Param("artistId") Long artistId,
//                                              @Param("songName") String songName,
//                                              @Param("album") String album);
//
//    // 获取随机歌曲列表
//    @Select("""
//                SELECT
//                    s.id AS songId,
//                    s.name AS songName,
//                    s.album,
//                    s.duration,
//                    s.cover_url AS coverUrl,
//                    s.audio_url AS audioUrl,
//                    s.release_time AS releaseTime,
//                    a.name AS artistName
//                FROM tb_song s
//                LEFT JOIN tb_artist a ON s.artist_id = a.id
//                ORDER BY RAND() LIMIT 20
//            """)
//    List<SongVO> getRandomSongsWithArtist();
@Select("SELECT s.id AS songId, s.artist_id AS artistId, s.name AS songName,  " +
        "s.album, s.lyric, s.duration, s.style, s.cover_url AS coverUrl, " +
        "s.audio_url AS audioUrl, s.release_time AS releaseTime, a.name AS artistName " +
        "FROM tb_song s " +
        "LEFT JOIN tb_artist a ON s.artist_id = a.id " +
        "WHERE (#{artistId} IS NULL OR s.artist_id = #{artistId}) " +
        "AND (#{songName} IS NULL OR s.name LIKE CONCAT('%', #{songName}, '%')) " +
        "AND (#{album} IS NULL OR s.album LIKE CONCAT('%', #{album}, '%')) " +
        "ORDER BY s.release_time DESC")
IPage<SongAdminVO> getSongsWithArtistName(Page<SongAdminVO> page,
                                          @Param("artistId") Long artistId,
                                          @Param("songName") String songName,
                                          @Param("album") String album);

    // 第二个方法（已修正，同样用单引号拼接）
    @Select("SELECT s.id AS songId, s.artist_id AS artistId, s.name AS songName, s.album, s.duration, " +
            "s.cover_url AS coverUrl, s.audio_url AS audioUrl, s.release_time AS releaseTime, " +
            "a.name AS artistName " +
            "FROM tb_song s " +
            "LEFT JOIN tb_artist a ON s.artist_id = a.id " +
            "ORDER BY RAND() LIMIT 20")
    List<SongVO> getRandomSongsWithArtist();
    // 根据id获取歌曲详情
    SongDetailVO getSongDetailById(Long songId);

    // 根据用户收藏的歌曲id列表获取歌曲列表
    IPage<SongVO> getSongsByIds(Page<SongVO> page,
                                @Param("songIds") List<Long> songIds,
                                @Param("songName") String songName,
                                @Param("artistName") String artistName,
                                @Param("album") String album);

    // 根据用户收藏的歌曲id列表获取歌曲列表
    List<Long> getFavoriteSongStyles(@Param("favoriteSongIds") List<Long> favoriteSongIds);

    // 根据用户收藏的歌曲id列表获取歌曲列表
    List<SongVO> getRecommendedSongsByStyles(@Param("sortedStyleIds") List<Long> sortedStyleIds,
                                             @Param("favoriteSongIds") List<Long> favoriteSongIds,
                                             @Param("limit") int limit);

    // 根据专辑名称和歌手ID获取专辑详情
    AlbumDetailVO getAlbumDetailByNameAndArtist(@Param("albumName") String albumName,
                                                 @Param("artistId") Long artistId);

    // 根据专辑名称和歌手ID获取专辑歌曲列表
    List<SongVO> getAlbumSongs(@Param("albumName") String albumName,
                               @Param("artistId") Long artistId);
//    @Select("SELECT\n" +
//            "  s.album AS name, -- 对应实体的name（专辑名）\n" +
//            "  s.artist_id AS artistId, -- 对应实体的artistId\n" +
//            "  a.name AS artistName, -- 对应实体的artistName（String类型）\n" +
//            "  MAX(s.cover_url) AS albumCoverUrl, -- 对应实体的albumCoverUrl\n" +
//            "  MAX(s.release_time) AS releaseTime -- 对应实体的releaseTime（LocalDateTime）\n" +
//            "FROM tb_song s\n" +
//            "LEFT JOIN tb_artist a ON s.artist_id = a.id\n" +
//            "WHERE s.album IS NOT NULL -- 过滤空专辑名\n" +
//            "GROUP BY s.album, s.artist_id, a.name -- 分组字段包含SELECT中非聚合字段\n" +
//            "ORDER BY releaseTime DESC;")
//    List<Album> selectByAlbum();
//    @Insert("INSERT INTO tb_album (name, artist_id, artist_name, cover_url, release_time) " +
//            "VALUES (#{name}, #{artistId}, #{artistName}, #{albumCoverUrl}, #{releaseTime})")
//    void insertAlbumDetail(Album album);
}
