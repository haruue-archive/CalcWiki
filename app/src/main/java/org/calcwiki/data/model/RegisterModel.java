package org.calcwiki.data.model;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public class RegisterModel {

    public static class NeedToken {

        /**
         * result : NeedToken
         * token : xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
         */

        @JSONField(name = "createaccount")
        public CreateaccountEntity createaccount;

        public static class CreateaccountEntity {
            @JSONField(name = "result")
            public String result;
            @JSONField(name = "token")
            public String token;
        }
    }

    public static class Success {

        /**
         * result : Success
         * token : xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
         * userid : 1234
         * username : GymBeauWhales
         */

        @JSONField(name = "createaccount")
        public CreateaccountEntity createaccount;

        public static class CreateaccountEntity {
            @JSONField(name = "result")
            public String result;
            @JSONField(name = "token")
            public String token;
            @JSONField(name = "userid")
            public int userid;
            @JSONField(name = "username")
            public String username;
        }
    }

    public static class NeedCaptcha {

        /**
         * result : NeedCaptcha
         * captcha : {"type":"simple","mime":"text/plain","id":"323035635","question":"77+5"}
         */

        @JSONField(name = "createaccount")
        public CreateaccountEntity createaccount;

        public static class CreateaccountEntity {
            @JSONField(name = "result")
            public String result;
            /**
             * type : simple
             * mime : text/plain
             * id : 323035635
             * question : 77+5
             */

            @JSONField(name = "captcha")
            public CaptchaEntity captcha;

            public static class CaptchaEntity {
                @JSONField(name = "type")
                public String type;
                @JSONField(name = "mime")
                public String mime;
                @JSONField(name = "id")
                public String id;
                @JSONField(name = "question")
                public String question;
            }
        }
    }


    public static class Error {

        /**
         * code : code
         * info : info
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

}
