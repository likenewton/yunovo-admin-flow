package cn.yunovo.iov.fc.ant.resetlog.entity;

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
    * 重置流量卡日志
    * </p>
*
* @author bill
* @since 2019-06-13
*/
    @Data
        @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    @ApiModel(value="Cc_reset_log对象", description="重置流量卡日志")
    public class Cc_reset_log implements Serializable {

    private static final long serialVersionUID = 1L;

            @ApiModelProperty(value = "重置增号")
            @TableId(value = "reset_id", type = IdType.AUTO)
    private Integer reset_id;

            @ApiModelProperty(value = "机构编号")
    private Integer org_id;

            @ApiModelProperty(value = "流量卡编号")
    private Integer card_id;

            @ApiModelProperty(value = "卡ICCID")
    private String card_iccid;

            @ApiModelProperty(value = "已用流量(MB)")
    private Double card_used;

            @ApiModelProperty(value = "剩余流量(MB)")
    private Double card_unused;

            @ApiModelProperty(value = "卡状态：0未激活 1正常 2停卡")
    private Boolean card_status;

            @ApiModelProperty(value = "出货批次编号")
    private Integer batch_id;

            @ApiModelProperty(value = "batch time")
    private LocalDateTime batch_time;

            @ApiModelProperty(value = "重置时卡主是否已绑定设备:0否1是")
    private Boolean owner_bind;

            @ApiModelProperty(value = "重置时卡主是否已实名制:0否1是")
    private Boolean owner_real;

            @ApiModelProperty(value = "重置时卡主是否已完善资料:0否1是")
    private Boolean owner_wszl;

            @ApiModelProperty(value = "操作者编号")
    private Integer user_id;

            @ApiModelProperty(value = "操作者姓名")
    private String user_name;

            @ApiModelProperty(value = "操作时间")
    private LocalDateTime time_added;


}
