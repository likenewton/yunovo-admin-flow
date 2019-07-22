package cn.yunovo.iov.fc.ant.GprsStats.entity;

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
    * GPRS流量卡信息表
    * </p>
*
* @author bill
* @since 2019-05-30
*/
    @Data
        @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    @ApiModel(value="Cc_gprs_card对象", description="GPRS流量卡信息表")
    public class Cc_gprs_card implements Serializable {

    private static final long serialVersionUID = 1L;

            @ApiModelProperty(value = "流量卡编号")
            @TableId(value = "card_id", type = IdType.AUTO)
    private Integer card_id;

            @ApiModelProperty(value = "机构编号")
    private Integer org_id;

            @ApiModelProperty(value = "批次增号")
    private Integer batch_id;

            @ApiModelProperty(value = "卡号码")
    private String card_sn;

            @ApiModelProperty(value = "卡ICCID")
    private String card_iccid;

            @ApiModelProperty(value = "卡IMSI")
    private String card_imsi;

            @ApiModelProperty(value = "绑定的IMEI")
    private String card_imei;

            @ApiModelProperty(value = "卡的类型:0未知卡,1智网吉林,2智网JASPER卡,3吉林长春,4智网定向卡")
    private Boolean card_type;

            @ApiModelProperty(value = "卡微信名(openid)")
    private String card_openid;

            @ApiModelProperty(value = "卡主是否已绑定设备:0否1是")
    private Boolean owner_bind;

            @ApiModelProperty(value = "卡主是否已实名制:0否1是")
    private Boolean owner_real;

            @ApiModelProperty(value = "卡主是否已完善资料:0否1是")
    private Boolean owner_wszl;

            @ApiModelProperty(value = "累记使用流量(MB)")
    private Double used_total;

            @ApiModelProperty(value = "当月使用流量(MB)")
    private Double used_month;

            @ApiModelProperty(value = "累记充值流量(MB)")
    private Double pay_total;

            @ApiModelProperty(value = "最大可使用流量(MB)")
    private Double max_unused;

            @ApiModelProperty(value = "重置前当月联通用量差异值")
    private Double reset_diff;

            @ApiModelProperty(value = "联通累记使用流量(MB)")
    private Double unicom_total;

            @ApiModelProperty(value = "联通当月使用流量(MB)")
    private Double unicom_month;

            @ApiModelProperty(value = "联通最大可使用流量(MB)")
    private Double unicom_unused;

            @ApiModelProperty(value = "上日差异流量(MB)")
    private Double unicom_diff;

            @ApiModelProperty(value = "联通是否已停卡:0否1是")
    private Boolean unicom_stop;

            @ApiModelProperty(value = "联通建卡时间")
    private LocalDateTime unicom_ctime;

            @ApiModelProperty(value = "激活时间")
    private LocalDateTime time_active;

            @ApiModelProperty(value = "导卡时间")
    private LocalDateTime time_added;

            @ApiModelProperty(value = "最后时间")
    private LocalDateTime time_last;

            @ApiModelProperty(value = "上次充值时间")
    private LocalDateTime time_paid;

            @ApiModelProperty(value = "上次停号时间")
    private LocalDateTime time_stop;

            @ApiModelProperty(value = "过期时间")
    private LocalDateTime time_expire;


}
