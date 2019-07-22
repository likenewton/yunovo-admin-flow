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
    * 流量分配表
    * </p>
*
* @author bill
* @since 2019-06-14
*/
    @Data
        @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    @ApiModel(value="Cc_gprs_allot对象", description="流量分配表")
    public class Cc_gprs_allot implements Serializable {

    private static final long serialVersionUID = 1L;

            @ApiModelProperty(value = "分配增号")
            @TableId(value = "allot_id", type = IdType.AUTO)
    private Integer allot_id;

            @ApiModelProperty(value = "流量卡编号")
    private Integer card_id;

            @ApiModelProperty(value = "套餐总量(MB)")
    private Double gprs_amount;

            @ApiModelProperty(value = "分配几个月")
    private Integer allot_month;

            @ApiModelProperty(value = "分配流量值(MB)")
    private Double allot_value;

            @ApiModelProperty(value = "分配流量是否清零:0不清 1清零")
    private Boolean allot_reset;

            @ApiModelProperty(value = "已分配几个月")
    private Integer assigned_month;

            @ApiModelProperty(value = "添加时间")
    private LocalDateTime time_added;

            @ApiModelProperty(value = "过期时间")
    private LocalDateTime time_expire;


}
