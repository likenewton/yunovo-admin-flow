package cn.yunovo.iov.fc.ant.card.entity;

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
    * 流量值表
    * </p>
*
* @author bill
* @since 2019-06-14
*/
    @Data
        @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    @ApiModel(value="Cc_gprs_value对象", description="流量值表")
    public class Cc_gprs_value implements Serializable {

    private static final long serialVersionUID = 1L;

            @ApiModelProperty(value = "自增号")
            @TableId(value = "gprs_vid", type = IdType.AUTO)
    private Integer gprs_vid;

            @ApiModelProperty(value = "流量卡编号")
    private Integer card_id;

            @ApiModelProperty(value = "分配编号")
    private Integer allot_id;

            @ApiModelProperty(value = "当前月份")
    private Integer how_month;

            @ApiModelProperty(value = "当月流量(MB)")
    private Double gprs_value;

            @ApiModelProperty(value = "设备计算余量(MB)")
    private Double balance_dval;

            @ApiModelProperty(value = "剩余流量(MB)")
    private Double balance_value;

            @ApiModelProperty(value = "添加时间")
    private LocalDateTime time_added;

            @ApiModelProperty(value = "修改时间")
    private LocalDateTime time_modify;

            @ApiModelProperty(value = "过期时间")
    private LocalDateTime time_expire;


}
