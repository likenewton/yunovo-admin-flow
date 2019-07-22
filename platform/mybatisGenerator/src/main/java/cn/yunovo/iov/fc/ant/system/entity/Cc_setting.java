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
    * 设置表
    * </p>
*
* @author bill
* @since 2019-06-11
*/
    @Data
        @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    @ApiModel(value="Cc_setting对象", description="设置表")
    public class Cc_setting implements Serializable {

    private static final long serialVersionUID = 1L;

            @TableId(value = "setting_id", type = IdType.AUTO)
    private Integer setting_id;

    private Integer store_id;

    private String group;

    private String key;

    private String value;

    private Boolean serialized;


}
