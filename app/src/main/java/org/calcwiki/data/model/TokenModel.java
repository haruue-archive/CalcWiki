package org.calcwiki.data.model;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public class TokenModel {


    /**
     * batchcomplete :
     * query : {"tokens":{"csrftoken":"cfd92a947f3a75ed84fe191a2584dc9b56f692d6+\\","patroltoken":"05dbacd29f71540da38e2134618efdf256f692d6+\\","rollbacktoken":"6e6370d61daa17e194c9b7da9212aa9056f692d6+\\","userrightstoken":"b46ccb2333adf6cf952db42ccb8681d856f692d6+\\","watchtoken":"0715e7b1f0598bc10cc3b4a9995c2c2956f692d6+\\"}}
     */

    @JSONField(name = "batchcomplete")
    public String batchcomplete;
    /**
     * tokens : {"csrftoken":"cfd92a947f3a75ed84fe191a2584dc9b56f692d6+\\","patroltoken":"05dbacd29f71540da38e2134618efdf256f692d6+\\","rollbacktoken":"6e6370d61daa17e194c9b7da9212aa9056f692d6+\\","userrightstoken":"b46ccb2333adf6cf952db42ccb8681d856f692d6+\\","watchtoken":"0715e7b1f0598bc10cc3b4a9995c2c2956f692d6+\\"}
     */

    @JSONField(name = "query")
    public QueryEntity query;

    public static class QueryEntity {
        /**
         * csrftoken : cfd92a947f3a75ed84fe191a2584dc9b56f692d6+\
         * patroltoken : 05dbacd29f71540da38e2134618efdf256f692d6+\
         * rollbacktoken : 6e6370d61daa17e194c9b7da9212aa9056f692d6+\
         * userrightstoken : b46ccb2333adf6cf952db42ccb8681d856f692d6+\
         * watchtoken : 0715e7b1f0598bc10cc3b4a9995c2c2956f692d6+\
         */

        @JSONField(name = "tokens")
        public TokensEntity tokens;

        public static class TokensEntity {
            @JSONField(name = "csrftoken")
            public String csrftoken;
            @JSONField(name = "patroltoken")
            public String patroltoken;
            @JSONField(name = "rollbacktoken")
            public String rollbacktoken;
            @JSONField(name = "userrightstoken")
            public String userrightstoken;
            @JSONField(name = "watchtoken")
            public String watchtoken;
        }
    }
}
