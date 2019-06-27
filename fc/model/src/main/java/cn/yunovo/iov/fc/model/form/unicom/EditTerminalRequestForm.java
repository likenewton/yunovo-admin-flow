package cn.yunovo.iov.fc.model.form.unicom;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@ApiModel
public class EditTerminalRequestForm extends JasperForm implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty("The enum change type for terminal changes. Each int value indicate a change type.\r\n" + 
			"                Each type allows a different set of target values pending business logic validation.\r\n" + 
			"                Commas (,) and asterisks (*) are not allowed in literal string target values:\r\n" + 
			"\r\n" + 
			"                1 - Device ID (also known as \"Terminal ID\"): literal string, max 50 characters\r\n" + 
			"                2 - Modem ID: literal string, max 40 characters\r\n" + 
			"                3 - SIM Status:\r\n" + 
			"                    \"TEST_READY_NAME\": Test Ready,\r\n" + 
			"                    \"INVENTORY_NAME\": Inventory,\r\n" + 
			"                    \"TRIAL_NAME\": Trial,\r\n" + 
			"                    \"ACTIVATION_READY_NAME\": Activation Ready,\r\n" + 
			"                    \"ACTIVATED_NAME\": Activated,\r\n" + 
			"                    \"DEACTIVATED_NAME\": Deactivated,\r\n" + 
			"                    \"RETIRED_NAME\": Retired,\r\n" + 
			"                    \"PURGED_NAME\": Purged\r\n" + 
			"                4 - Rate Plan: literal string, name of an existing Rate Plan\r\n" + 
			"                6 - Customer: literal string, name of an existing Customer\r\n" + 
			"                7 - Overage Limit Override:\r\n" + 
			"                    \"DEFAULT\": None\r\n" + 
			"                    \"TEMPORARY_OVERRIDE\": Current cycle\r\n" + 
			"                    \"PERMANENT_OVERRIDE\": Ongoing\r\n" + 
			"                17 - Custom 1\r\n" + 
			"                18 - Custom 2\r\n" + 
			"                19 - Custom 3\r\n" + 
			"                21 - Secure SIM Username copy rule:\r\n" + 
			"                    \"F\" :  Unknown\r\n" + 
			"                    \"N\" :  Not enabled\r\n" + 
			"                    \"O\" :  Once\r\n" + 
			"                    \"A\" \"  Always\r\n" + 
			"                    \"L\" :  Locked\r\n" + 
			"                22 - Secure SIM Password copy rule:\r\n" + 
			"                    \"F\" :  Unknown\r\n" + 
			"                    \"N\" :  Not enabled\r\n" + 
			"                    \"O\" :  Once\r\n" + 
			"                    \"A\" \"  Always\r\n" + 
			"                    \"L\" :  Locked\r\n" + 
			"                42 - Operator Custom 1\r\n" + 
			"                43 - Operator Custom 2\r\n" + 
			"                44 - Operator Custom 3\r\n" + 
			"                45 - Operator Custom 4\r\n" + 
			"                46 - Operator Custom 5\r\n" + 
			"                47 - Customer Custom 1\r\n" + 
			"                48 - Customer Custom 2\r\n" + 
			"                49 - Customer Custom 3\r\n" + 
			"                50 - Customer Custom 4\r\n" + 
			"                51 - Customer Custom 5\r\n" + 
			"                68 - Sale Date: date string in the format yyyy-MM-dd\r\n" + 
			"                73 - Custom 4\r\n" + 
			"                74 - Custom 5\r\n" + 
			"                75 - Custom 6\r\n" + 
			"                76 - Custom 7\r\n" + 
			"                77 - Custom 8\r\n" + 
			"                78 - Custom 9\r\n" + 
			"                79 - Custom 10")
	@NotEmpty(message="changeType missing")
	private Integer changeType;
	
	/**
	 * 
	 */
	@ApiModelProperty("目标值")
	@NotEmpty(message="targetValue missing")
	private String targetValue;
	
	/**
	 * 
	 */
	@ApiModelProperty("需要被变更的流量卡iccid")
	@NotEmpty(message="iccid missing")
	private String iccid;
	
	
	
}
