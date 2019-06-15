package io.vinson.notebook.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;

/**
 * @author: jiangweixin
 * @date: 2019/6/10
 */
public class JSONUtil {
    private static Logger logger = org.slf4j.LoggerFactory.getLogger(JSONUtil.class);

    private static ObjectMapper mapper = new ObjectMapper();
    static {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
    public static String toJson(Object obj) {
        String str = null;
        try {
            str =  mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
        }
        return str;
    }

}
