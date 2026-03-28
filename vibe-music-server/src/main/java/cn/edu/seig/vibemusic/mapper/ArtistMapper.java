package cn.edu.seig.vibemusic.mapper;

import cn.edu.seig.vibemusic.model.entity.Artist;
import cn.edu.seig.vibemusic.model.vo.AlbumVO;
import cn.edu.seig.vibemusic.model.vo.ArtistDetailVO;
import cn.edu.seig.vibemusic.model.vo.ArtistVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
public interface ArtistMapper extends BaseMapper<Artist> {

    // 根据id查询歌手详情
    ArtistDetailVO getArtistDetailById(Long artistId);

    // 根据歌手id查询专辑列表
    @Select("SELECT album_id AS albumId, name AS albumName, artist_id AS artistId, artist_name AS artistName, " +
            "cover_url AS albumCoverUrl, release_time AS releaseTime, introduction AS albumIntroduction " +
            "FROM tb_album WHERE artist_id = #{artistId}")
    List<AlbumVO> getAlbumsByArtistId(Long artistId);

    // 查询是否关注了该歌手
    @Select("SELECT count(*) FROM tb_follow WHERE user_id = #{userId} AND artist_id = #{artistId}")
    Integer isFollowed(Long userId, Long artistId);

    @Select("<script>" +
            "SELECT id AS artistId, name AS artistName, avatar FROM tb_artist WHERE id IN " +
            "<foreach item='item' index='index' collection='ids' open='(' separator=',' close=')'>" +
            "#{item}" +
            "</foreach>" +
            "</script>")
    IPage<ArtistVO> getArtistsByIds(Page<ArtistVO> page, @Param("ids") List<Long> ids);
}
