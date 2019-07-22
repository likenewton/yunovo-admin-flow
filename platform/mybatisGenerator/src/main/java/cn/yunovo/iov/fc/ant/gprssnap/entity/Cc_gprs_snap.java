package cn.yunovo.iov.fc.ant.gprssnap.entity;

    import java.math.BigDecimal;
    import java.time.LocalDateTime;
    import java.io.Serializable;
    import io.swagger.annotations.ApiModel;
    import io.swagger.annotations.ApiModelProperty;
    import lombok.Data;
    import lombok.EqualsAndHashCode;
    import lombok.experimental.Accessors;

/**
* <p>
    * GPRS流量快照表
    * </p>
*
* @author bill
* @since 2019-07-07
*/
    @Data
        @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    @ApiModel(value="Cc_gprs_snap对象", description="GPRS流量快照表")
    public class Cc_gprs_snap implements Serializable {

    private static final long serialVersionUID = 1L;

            @ApiModelProperty(value = "快照分配编号")
    private Integer allot_id;

            @ApiModelProperty(value = "流量卡编号")
    private Integer card_id;

            @ApiModelProperty(value = "套餐总量(MB)")
    private Double gprs_amount;

            @ApiModelProperty(value = "充值套餐总量(MB)")
    private Double gprs_amount1;

            @ApiModelProperty(value = "分配几个月")
    private Integer allot_month;

            @ApiModelProperty(value = "分配流量值(MB)")
    private Double allot_value;

            @ApiModelProperty(value = "分配流量是否清零:0不清 1清零")
    private Boolean allot_reset;

            @ApiModelProperty(value = "已分配几个月")
    private Integer assigned_month;

            @ApiModelProperty(value = "快照时间")
    private LocalDateTime time_snap;

            @ApiModelProperty(value = "套餐激活时间")
    private LocalDateTime time_added;

            @ApiModelProperty(value = "套餐过期时间")
    private LocalDateTime time_expire;

            @ApiModelProperty(value = "设备更新时间")
    private LocalDateTime time_device;

            @ApiModelProperty(value = "套餐已用总流量(MB)")
    private Double total_used;

            @ApiModelProperty(value = "套餐剩余总流量(MB)")
    private Double total_balance;

            @ApiModelProperty(value = "支付订单来源")
    private String pay_from;

            @ApiModelProperty(value = "套餐类型备注")
    private String pay_memo;

            @ApiModelProperty(value = "充值套餐类型")
    private String pay_memo1;

            @ApiModelProperty(value = "付款方式：0赠送,1微信,2支付宝,3充值卡,4转账")
    private Boolean pay_method;

            @ApiModelProperty(value = "套餐价格(元)")
    private BigDecimal gprs_price;

            @ApiModelProperty(value = "充值套餐价格(元)")
    private BigDecimal gprs_price1;

            @ApiModelProperty(value = "月流量(MB)")
    private Double gprs_value;

            @ApiModelProperty(value = "月余量(MB)")
    private Double balance_dval;


}
