package cn.yunovo.iov.fc.ant.system.entity;

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
    * 延伸表
    * </p>
*
* @author bill
* @since 2019-06-11
*/
    @Data
        @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    @ApiModel(value="Cc_extension对象", description="延伸表")
    public class Cc_extension implements Serializable {

    private static final long serialVersionUID = 1L;

            @TableId(value = "extension_id", type = IdType.AUTO)
    private Integer extension_id;

    private String type;

    private String code;


}
