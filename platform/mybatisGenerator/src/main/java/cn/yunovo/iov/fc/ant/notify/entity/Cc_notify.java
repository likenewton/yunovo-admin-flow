package cn.yunovo.iov.fc.ant.notify.entity;

    import java.math.BigDecimal;
    import java.time.LocalDate;
    import java.io.Serializable;
    import io.swagger.annotations.ApiModel;
    import io.swagger.annotations.ApiModelProperty;
    import lombok.Data;
    import lombok.EqualsAndHashCode;
    import lombok.experimental.Accessors;

/**
* <p>
    * 通知或来源统计分析表
    * </p>
*
* @author bill
* @since 2019-06-04
*/
    @Data
        @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    @ApiModel(value="Cc_notify对象", description="通知或来源统计分析表")
    public class Cc_notify implements Serializable {

    private static final long serialVersionUID = 1L;

            @ApiModelProperty(value = "通知日期")
    private LocalDate ntf_date;

            @ApiModelProperty(value = "通知或来源")
    private String ntf_type;

            @ApiModelProperty(value = "机构编号")
    private Integer org_id;

            @ApiModelProperty(value = "通知成功数")
    private Integer ntf_succeed;

            @ApiModelProperty(value = "通知失败数")
    private Integer ntf_failed;

            @ApiModelProperty(value = "通知时长(秒)")
    private Integer ntf_duration;

            @ApiModelProperty(value = "下单成功数")
    private Integer ord_succeed;

            @ApiModelProperty(value = "下单失败数")
    private Integer ord_failed;

            @ApiModelProperty(value = "下单成功比率")
    private Float ord_srate;

            @ApiModelProperty(value = "下单失败比率")
    private Float ord_frate;

            @ApiModelProperty(value = "已付金额(元)")
    private BigDecimal money_succeed;

            @ApiModelProperty(value = "未付金额(元)")
    private BigDecimal money_failed;


}
