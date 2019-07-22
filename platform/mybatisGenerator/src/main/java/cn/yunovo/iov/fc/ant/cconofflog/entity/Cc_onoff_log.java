package cn.yunovo.iov.fc.ant.cconofflog.entity;

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
    * 停卡日志
    * </p>
*
* @author bill
* @since 2019-06-01
*/
    @Data
        @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    @ApiModel(value="Cc_onoff_log对象", description="停卡日志")
    public class Cc_onoff_log implements Serializable {

    private static final long serialVersionUID = 1L;

            @ApiModelProperty(value = "开关编号")
            @TableId(value = "onoff_id", type = IdType.AUTO)
    private Integer onoff_id;

            @ApiModelProperty(value = "流量卡编号")
    private Integer card_id;

            @ApiModelProperty(value = "开关类型：0开1关")
    private Boolean onoff_type;

            @ApiModelProperty(value = "执行结果状态：0失败1成功")
    private Boolean exec_status;

            @ApiModelProperty(value = "剩余流量(MB)")
    private Double balance_value;

            @ApiModelProperty(value = "操作者编号")
    private Integer user_id;

            @ApiModelProperty(value = "操作者姓名")
    private String user_name;

            @ApiModelProperty(value = "操作时间")
    private LocalDateTime time_added;


}
