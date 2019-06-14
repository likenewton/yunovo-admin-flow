package cn.yunovo.iov.fc.model.entity;

    import com.baomidou.mybatisplus.annotation.TableName;
    import java.time.LocalDateTime;
    import java.io.Serializable;
    import io.swagger.annotations.ApiModel;
    import io.swagger.annotations.ApiModelProperty;
    import lombok.Data;
    import lombok.EqualsAndHashCode;
    import lombok.experimental.Accessors;

/**
* <p>
    * 定向流量卡实名制表
    * </p>
*
* @author bill
* @since 2019-06-14
*/
    @Data
        @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    @TableName("cc_realname_dxk")
    @ApiModel(value="Cc_realname_dxk对象", description="定向流量卡实名制表")
    public class CcRealnameDxk implements Serializable {

    private static final long serialVersionUID = 1L;

            @ApiModelProperty(value = "卡ICCID")
    private String card_iccid;

            @ApiModelProperty(value = "卡主是否已实名制:0否1是")
    private Boolean owner_real;

            @ApiModelProperty(value = "修改时间")
    private LocalDateTime time_modify;


}
