package org.calcwiki.data.model;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public class TokenModel {


    /**
     * batchcomplete :
     * query : {"tokens":{"csrftoken":"xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx+\\","patroltoken":"xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx+\\","rollbacktoken":"xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx+\\","userrightstoken":"xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx+\\","watchtoken":"xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx+\\"}}
     */

    @JSONField(name = "batchcomplete")
    public String batchcomplete;
    /**
     * tokens : {"csrftoken":"xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx+\\","patroltoken":"xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx+\\","rollbacktoken":"xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx+\\","userrightstoken":"xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx+\\","watchtoken":"xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx+\\"}
     */

    @JSONField(name = "query")
    public QueryEntity query;

    public static class QueryEntity {
        /**
         * csrftoken : xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx+\
         * patroltoken : xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx+\
         * rollbacktoken : xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx+\
         * userrightstoken : xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx+\
         * watchtoken : xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx+\
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
