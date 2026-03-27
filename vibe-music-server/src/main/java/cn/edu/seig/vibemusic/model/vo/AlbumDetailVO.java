package cn.edu.seig.vibemusic.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * 专辑详情VO
 */
@Data
public class AlbumDetailVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 专辑名称
     */
    private String albumName;

    /**
     * 专辑封面（取第一首歌曲的封面）
     */
    private String coverUrl;

    /**
     * 歌手id
     */
    private Long artistId;

    /**
     * 歌手名称
     */
    private String artistName;

    /**
     * 歌手头像
     */
    private String artistAvatar;

    /**
     * 歌手简介
     */
    private String artistIntroduction;

    /**
     * 发行时间（取专辑中最早的发行时间）
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseTime;

    /**
     * 专辑歌曲数量
     */
    private Integer songCount;

    /**
     * 专辑总时长（格式：mm:ss）
     */
//    private String totalDuration;

    /**
     * 专辑歌曲列表
     */
    private List<SongVO> songs;

}
