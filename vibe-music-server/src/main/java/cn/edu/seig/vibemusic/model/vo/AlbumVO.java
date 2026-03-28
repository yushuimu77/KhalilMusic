package cn.edu.seig.vibemusic.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

@Data
public class AlbumVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 专辑 id
     */
    private Long albumId;

    /**
     * 专辑名称
     */
    private String albumName;

    /**
     * 歌手 id
     */
    private Long artistId;

    /**
     * 歌手名称
     */
    private String artistName;

    /**
     * 专辑封面 url
     */
    private String albumCoverUrl;

    /**
     * 专辑发行时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseTime;

    /**
     * 专辑简介
     */
    private String albumIntroduction;
}
