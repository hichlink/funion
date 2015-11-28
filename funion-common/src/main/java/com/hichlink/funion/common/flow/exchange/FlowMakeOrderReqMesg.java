package com.hichlink.funion.common.flow.exchange;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class FlowMakeOrderReqMesg {
	@JsonProperty("SIGN")
	private String sign;// 签名
	@JsonProperty("USER")
	private String user; // 用户标识
	@JsonProperty("PACKAGEID")
	private String packageId; // 流量包ID。
	@JsonProperty("ORDERTYPE")
	private String orderType; // 订单类型

	@JsonProperty("EXTORDER")
	private String extorder; // 外部订单号
	@JsonProperty("NOTE")
	private String note; // 备用
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPackageId() {
		return packageId;
	}
	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public String getExtorder() {
		return extorder;
	}
	public void setExtorder(String extorder) {
		this.extorder = extorder;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}

	
}
