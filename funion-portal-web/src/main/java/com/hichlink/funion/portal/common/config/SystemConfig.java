package com.hichlink.funion.portal.common.config;

import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aspire.webbas.configuration.config.ConfigurationHelper;

public class SystemConfig {

	/**
	 * logger.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(SystemConfig.class);
	/**
	 * 配置文件接口.
	 */
	private static Configuration configuration = null;

	/**
	 * 门户配置对象.
	 */
	private static SystemConfig config;

	/**
	 * 缺省配置文件名称.
	 */
	private static final String DEFAULT_CONFIGURATION_FILENAME = "system-config.xml";

	/**
	 * 配置文件名.
	 */
	private static String configurationFileName = DEFAULT_CONFIGURATION_FILENAME;

	/**
	 * 私有构造方法.
	 * 
	 * @param configurationFileName
	 *            配置文件相对路径
	 */
	private SystemConfig() {
		if (configuration == null) {
			refresh();
		}
	}

	/*
	 * * 获取AJAX服务端配置对象.
	 * 
	 * @return AJAX服务端配置对象
	 */
	public static SystemConfig getInstance() {
		if (config == null) {
			config = new SystemConfig();
		}
		return config;
	}

	/**
	 * 刷新配置文件.
	 */
	private static void refresh() {
		configuration = ConfigurationHelper.getConfiguration(configurationFileName, 50000);
		if (configuration == null) {
			LOGGER.error("读portal配置文件失败, 配置文件：" + configurationFileName);
		}
	}

	public String getString(String arg) {
		if (configuration == null) {
			return null;
		}
		return configuration.getString(arg);
	}

	public int getInt(String arg) {
		if (configuration == null) {
			return 0;
		}
		return configuration.getInt(arg);
	}

	public String getAppId() {
		return getString("wx.appId");
	}

	public String getDomain() {
		return getString("domain");
	}

	public String geFlowAppId() {
		return getString("flow.flowAppId");
	}

	public String getFlowAppkey() {
		return getString("flow.flowAppkey");
	}

	public String getDispatchUrl() {
		return getString("flow.dispatchUrl");
	}

	public int getCashMin() {
		return getInt("cash-min");
	}

	public int getCashMax() {
		return getInt("cash-max");
	}
	public String getCertPath(){
		return getString("cert-path");
	}
	public int getCommisionRatio(){
		return getInt("commision-ratio");
	}
}
