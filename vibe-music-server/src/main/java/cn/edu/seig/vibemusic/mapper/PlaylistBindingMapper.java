package cn.edu.seig.vibemusic.mapper;

import cn.edu.seig.vibemusic.model.entity.PlaylistBinding;
import cn.edu.seig.vibemusic.model.vo.SongAdminVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author sunpingli
 * @since 2025-01-09
 */
@Mapper
public interface PlaylistBindingMapper extends BaseMapper<PlaylistBinding> {

    @Delete("delete from tb_playlist_binding where playlist_id = #{playlistId}")
    void deleteByPlaylistId(@Param("playlistId") Long playlistId);

    @Insert("<script>" +
            "insert into tb_playlist_binding (playlist_id, song_id) values " +
            "<foreach collection='songIds' item='songId' separator=','>" +
            "(#{playlistId}, #{songId})" +
            "</foreach>" +
            "</script>")
    void insertBatch(@Param("playlistId") Long playlistId, @Param("songIds") List<Long> songIds);

    @Select("select song_id from tb_playlist_binding where playlist_id = #{playlistId}")
    List<Long> selectSongIdsByPlaylistId(@Param("playlistId") Long playlistId);

    @Select("SELECT s.id AS songId, s.artist_id AS artistId, a.name AS artistName, s.name AS songName, s.album AS album, " +
            "s.lyric AS lyric, s.duration AS duration, s.style AS style, s.cover_url AS coverUrl, s.audio_url AS audioUrl, " +
            "s.release_time AS releaseTime " +
            "FROM tb_playlist_binding pb " +
            "JOIN tb_song s ON pb.song_id = s.id " +
            "LEFT JOIN tb_artist a ON s.artist_id = a.id " +
            "WHERE pb.playlist_id = #{playlistId}")
    List<SongAdminVO> getPlaylistSongs(@Param("playlistId") Long playlistId);
}
