$(function() {
	$('#payMoney').click(prepayId)
	var flag = false;
	function pay() {
		var prepayId = $("#prepayId").val();
		if (flag)
			return;
		flag = true;
		$.ajax({
			url : ctxPaths + '/flow/' + prepayId + '/sendPayRequest.do',
			type : 'get',
			dataType : 'json',
			aysnc : false,
			success : function(data) {
				if (data.success) {
					var obj = data.data;
					wx.chooseWXPay({
						timestamp : obj.timeStamp, // 支付签名时间戳，注意微信jssdk中的所有使用timestamp字段均为小写。但最新版的支付后台生成签名使用的timeStamp字段名需大写其中的S字符
						nonceStr : obj.nonceStr, // 支付签名随机串，不长于 32 位
						package : obj.package, // 统一支付接口返回的prepay_id参数值，提交格式如：prepay_id=***）
						signType : obj.signType, // 签名方式，默认为'SHA1'，使用新版支付需传入'MD5'
						paySign : obj.paySign, // 支付签名
						success : function(res) {
							flag = false;
							alert(res);
						}
					});
					wx.error(function(res) {
						flag = false;
						alert(res.err_msg);
					});
				}
			}
		});
	}
});