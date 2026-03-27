package cn.edu.seig.vibemusic.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_album")
public class Album implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "album_id", type = IdType.AUTO)
    private Long albumId;

    @TableField("name")
    private String name;

    @TableField("artist_id")
    private Long artistId;

    @TableField("artist_name")
    private String artistName;

    @TableField("cover_url")
    private String albumCoverUrl;

    @TableField("release_time")
    private LocalDate releaseTime;

    @TableField("introduction")
    private String albumIntroduction;



}
