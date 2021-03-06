package com.hichlink.funion.portal.common.job.biz;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aspire.webbas.core.pagination.mybatis.pager.Page;
import com.hichlink.funion.common.entity.FlowExchangeLog;
import com.hichlink.funion.common.entity.FlowPayRecord;
import com.hichlink.funion.common.entity.FlowProductInfo;
import com.hichlink.funion.common.flow.FlowDispatch;
import com.hichlink.funion.common.flow.entity.FlowOrderReq;
import com.hichlink.funion.common.flow.entity.FlowOrderResp;
import com.hichlink.funion.common.service.FlowExchangeLogService;
import com.hichlink.funion.common.service.FlowPayRecordService;
import com.hichlink.funion.common.service.FlowProductInfoService;
import com.hichlink.funion.common.util.OrderSeqGen;
import com.hichlink.funion.portal.common.config.SystemConfig;

@Service("flowDispatchBiz")
public class FlowDispatchBiz {
	private static final Logger LOG = LoggerFactory.getLogger(FlowDispatchBiz.class);
	@Autowired
	private FlowPayRecordService flowPayRecordService;
	@Autowired
	private FlowProductInfoService flowProductInfoService;
	@Autowired
	private FlowExchangeLogService flowExchangeLogService;

	public void batchDispatch() {
		Page<FlowPayRecord> page = new Page<FlowPayRecord>();
		page.setRows(50);
		page.getParams().put("payStatus", FlowPayRecord.PAY_STATUS_SUCC);
		page.getParams().put("sendStatus", FlowPayRecord.SEND_STATUS_INIT);
		Page<FlowPayRecord> list = flowPayRecordService.pageQuery(page);
		List<FlowPayRecord> datas = list.getDatas();
		for (FlowPayRecord flowPayRecord : datas) {

			FlowProductInfo flowProductInfo = flowProductInfoService.get(flowPayRecord.getProductId());
			if (null == flowProductInfo) {
				LOG.error("productId={}找不到对应的流量产品", flowPayRecord.getProductId());
				flowPayRecord.setSendStatus(FlowPayRecord.SEND_STATUS_FAIL);
				flowPayRecord.setRemark("找不到对应的流量产品");
				flowPayRecordService.update(flowPayRecord);
				continue;
			}

			try {
				String extOrder = "F" + OrderSeqGen.createApplyId();
				FlowExchangeLog flowExchangeLog = new FlowExchangeLog();
				flowExchangeLog.setCreateTime(new Date());
				flowExchangeLog.setItemCount(1);
				flowExchangeLog.setMobile(flowPayRecord.getMobile());
				flowExchangeLog.setProductId(flowPayRecord.getProductId());
				flowExchangeLog.setSourceType("00");
				flowExchangeLog.setMobileHome("");
				flowExchangeLog.setMobileOperator("");
				flowExchangeLog.setFlowVoucherId(extOrder);
				flowExchangeLog.setExchangeOrderId("");
				flowPayRecord.setSendStatus(FlowPayRecord.SEND_STATUS_SENDING);
				flowPayRecordService.update(flowPayRecord);
				FlowOrderResp resp = dispatchFlow(flowPayRecord.getMobile(), flowProductInfo.getPackageId(),
						extOrder);
				LOG.debug("resp={}",resp.toString());
				if (null != resp && resp.isSucc()) {
					flowPayRecord.setSendStatus(FlowPayRecord.SEND_STATUS_GATE_OK);
					flowExchangeLog.setFlag(FlowPayRecord.SEND_STATUS_GATE_OK);
					flowExchangeLog.setExchangeOrderId(resp.getMsgBody().getContent().getOrderId());
				} else {
					flowPayRecord.setSendStatus(FlowPayRecord.SEND_STATUS_FAIL);
					flowExchangeLog.setFlag(FlowPayRecord.SEND_STATUS_FAIL);
				}
				flowExchangeLog.setRecordId(flowPayRecord.getRecordId());
				flowPayRecordService.update(flowPayRecord);
				flowExchangeLogService.saveAndUpdate(flowExchangeLog);
			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
			}
		}
	}

	public FlowOrderResp dispatchFlow(String mobile, String packageId, String extOrder) throws Exception {
		FlowOrderReq.Content content = new FlowOrderReq.Content();
		content.setUser(mobile);
		content.setPackageId(packageId);
		content.setExtorder(extOrder);
		content.setOrderType("1");
		return FlowDispatch.getInstance().dispatchFlow(content, SystemConfig.getInstance().geFlowAppId(),
				SystemConfig.getInstance().getFlowAppkey(), SystemConfig.getInstance().getDispatchUrl());
	}
}
