package org.calcwiki.data.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.List;

/**
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public class MobileViewModel {

    public static class Error implements Serializable {

        /**
         * code : missingtitle
         * info : The page you requested doesn't exist
         * * : See https://calcwiki.org/api.php for API usage
         */

        @JSONField(name = "error")
        public ErrorEntity error;

        public static class ErrorEntity {
            @JSONField(name = "code")
            public String code;
            @JSONField(name = "info")
            public String info;
        }
    }

    public static class CheckPageExist implements Serializable {

        /**
         * redirected : 计算机代数系统
         * sections : []
         */

        @JSONField(name = "mobileview")
        public MobileviewEntity mobileview;

        public static class MobileviewEntity implements Serializable {
            @JSONField(name = "redirected")
            public String redirected;
            @JSONField(name = "sections")
            public List<?> sections;
        }
    }

}
