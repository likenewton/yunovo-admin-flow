package cn.yunovo.iov.fc.ant.lanage.entity;

    import com.baomidou.mybatisplus.annotation.IdType;
    import com.baomidou.mybatisplus.annotation.TableId;
    import java.io.Serializable;
    import io.swagger.annotations.ApiModel;
    import io.swagger.annotations.ApiModelProperty;
    import lombok.Data;
    import lombok.EqualsAndHashCode;
    import lombok.experimental.Accessors;

/**
* <p>
    * 语言表
    * </p>
*
* @author bill
* @since 2019-06-13
*/
    @Data
        @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    @ApiModel(value="Cc_language对象", description="语言表")
    public class Cc_language implements Serializable {

    private static final long serialVersionUID = 1L;

            @TableId(value = "language_id", type = IdType.AUTO)
    private Integer language_id;

    private String name;

    private String code;

    private String locale;

    private String image;

    private String directory;

    private String filename;

    private Integer sort_order;

    private Boolean status;


}
