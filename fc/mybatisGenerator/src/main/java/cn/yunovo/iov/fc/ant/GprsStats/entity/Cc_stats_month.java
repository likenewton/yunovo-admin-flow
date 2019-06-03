package cn.yunovo.iov.fc.ant.GprsStats.entity;

    import java.time.LocalDateTime;
    import java.io.Serializable;
    import io.swagger.annotations.ApiModel;
    import io.swagger.annotations.ApiModelProperty;
    import lombok.Data;
    import lombok.EqualsAndHashCode;
    import lombok.experimental.Accessors;

/**
* <p>
    * 流量卡月流量使用情况统计表
    * </p>
*
* @author bill
* @since 2019-05-30
*/
    @Data
        @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    @ApiModel(value="Cc_stats_month对象", description="流量卡月流量使用情况统计表")
    public class Cc_stats_month implements Serializable {

    private static final long serialVersionUID = 1L;

            @ApiModelProperty(value = "月份:201601")
    private Integer how_month;

            @ApiModelProperty(value = "流量卡编号")
    private Integer card_id;

            @ApiModelProperty(value = "月使用流量(MB)")
    private Double month_used;

            @ApiModelProperty(value = "月剩余流量(MB)")
    private Double month_unused;

            @ApiModelProperty(value = "月超标流量(MB)")
    private Double month_over;

            @ApiModelProperty(value = "白名单日用流量(MB)")
    private Double month_wlist;

            @ApiModelProperty(value = "修改时间")
    private LocalDateTime time_modify;


}
