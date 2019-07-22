package cn.yunovo.iov.fc.ant.gprsmove.entity;

    import com.baomidou.mybatisplus.annotation.IdType;
    import com.baomidou.mybatisplus.annotation.TableId;
    import java.time.LocalDateTime;
    import java.io.Serializable;
    import io.swagger.annotations.ApiModel;
    import io.swagger.annotations.ApiModelProperty;
    import lombok.Data;
    import lombok.EqualsAndHashCode;
    import lombok.experimental.Accessors;

/**
* <p>
    * GPRS流量迁移表
    * </p>
*
* @author bill
* @since 2019-06-14
*/
    @Data
        @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    @ApiModel(value="Cc_gprs_move对象", description="GPRS流量迁移表")
    public class Cc_gprs_move implements Serializable {

    private static final long serialVersionUID = 1L;

            @ApiModelProperty(value = "迁移编号")
            @TableId(value = "move_id", type = IdType.AUTO)
    private Integer move_id;

            @ApiModelProperty(value = "操作者编号")
    private Integer user_id;

            @ApiModelProperty(value = "旧卡编号")
    private Integer old_cardid;

            @ApiModelProperty(value = "新卡编号")
    private Integer new_cardid;

            @ApiModelProperty(value = "旧卡ICCID")
    private String old_iccid;

            @ApiModelProperty(value = "新卡ICCID")
    private String new_iccid;

            @ApiModelProperty(value = "旧卡机构编号")
    private Integer old_orgid;

            @ApiModelProperty(value = "新卡机构编号")
    private Integer new_orgid;

            @ApiModelProperty(value = "迁移备注")
    private String move_memo;

            @ApiModelProperty(value = "添加时间")
    private LocalDateTime time_added;


}
