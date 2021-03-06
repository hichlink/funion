package com.hichlink.funion.common.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.hichlink.funion.common.util.DateUtil;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

public class JsonDateValueProcessor implements JsonValueProcessor {
    /**
     * yyyy-MM-dd HH:mm:ss
     */
    private String datePattern = DateUtil.YYYY_MM_DD_HH_MM_SS_EN;

    public JsonDateValueProcessor() {
        super();
    }

    public JsonDateValueProcessor(String format) {
        super();
        this.datePattern = format;
    }

    public Object processArrayValue(Object value, JsonConfig jsonConfig) {
        return process(value);
    }

    public Object processObjectValue(String key, Object value, JsonConfig jsonConfig) {
        return process(value);
    }

    private Object process(Object value) {
        try {
            if (value instanceof Date) {
                SimpleDateFormat sdf = new SimpleDateFormat(datePattern, Locale.UK);
                return sdf.format((Date)value);
            }
            return value == null ? "" : value.toString();
        } catch (Exception e) {
            return "";
        }
    }

    public String getDatePattern() {
        return datePattern;
    }

    public void setDatePattern(String pDatePattern) {
        datePattern = pDatePattern;
    }
}
