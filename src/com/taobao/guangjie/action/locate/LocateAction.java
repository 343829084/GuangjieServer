package com.taobao.guangjie.action.locate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.taobao.guangjie.action.BaseAction;
import com.taobao.guangjie.dataobject.BaseResult;
import com.taobao.guangjie.dataobject.Constants;
import com.taobao.guangjie.datatypes.WifiRecord;
import com.taobao.guangjie.locate.LocateLoose;

/**
 * 室内定位调用类
 * 
 * 接受客户端的bssid以及信号强度列表，调用室内定位 算法并返回定位到的商铺shopPos
 * 
 */
public class LocateAction extends BaseAction {

	private static final long serialVersionUID = -8953965130932719066L;
	public static final String api = "com.taobao.guangjie.locateapi.locate";
	private String wifiList;
	private LocateLoose locate;

	@Override
	public String execute() throws IOException {
		Logger logger = LoggerFactory.getLogger(LocateAction.class);

		if (wifiList == null || wifiList.length() == 0) {
			BaseResult<String> result = new BaseResult<String>(api, "1.0",
					Constants.API_ERROR_WRONG_PARAM);
			sendJson(result);
			return null;
		}

		locate = new LocateLoose();

		List<WifiRecord> records = new ArrayList<WifiRecord>();

		String[] split = wifiList.split("\\|");
		WifiRecord wifiRecord = null;
		for (int i = 0; i < split.length; i++) {
			if (i % 2 == 0) {
				wifiRecord = new WifiRecord();
				wifiRecord.bssid = split[i];
				logger.info(wifiRecord.bssid);
			} else {
				wifiRecord.level = Integer.parseInt(split[i]);
				records.add(wifiRecord);
				logger.info(wifiRecord.level + "");
			}
		}

		String location = locate.locateOne(records);
		logger.info(location);
		BaseResult<String> result = new BaseResult<String>(api);
		result.setData(location);
		sendJson(result);

		return null;
	}

	public String getWifiList() {
		return wifiList;
	}

	public void setWifiList(String wifiList) {
		this.wifiList = wifiList;
	}

}
