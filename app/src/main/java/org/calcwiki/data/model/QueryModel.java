package org.calcwiki.data.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public class QueryModel {

    /**
     * https://calcwiki.org/api.php?action=query&meta=userinfo&uiprop=blockinfo|hasmsg|groups|editcount|email|realname|unreadcount
     */
    public static class UserInfo {

        /**
         * batchcomplete :
         * query : {"userinfo":{"id":9,"name":"春上冰月","groups":["bureaucrat","checkuser","sysop","*","user","autoconfirmed"],"editcount":225,"realname":"春上冰月","email":"caoyue@caoyue.com.cn","emailauthenticated":"2016-02-15T15:18:49Z","unreadcount":4}}
         */

        @JSONField(name = "batchcomplete")
        public String batchcomplete;
        /**
         * userinfo : {"id":9,"name":"春上冰月","groups":["bureaucrat","checkuser","sysop","*","user","autoconfirmed"],"editcount":225,"realname":"春上冰月","email":"caoyue@caoyue.com.cn","emailauthenticated":"2016-02-15T15:18:49Z","unreadcount":4}
         */

        @JSONField(name = "query")
        public QueryEntity query;

        public static class QueryEntity {
            /**
             * id : 9
             * name : 春上冰月
             * groups : ["bureaucrat","checkuser","sysop","*","user","autoconfirmed"]
             * editcount : 225
             * realname : 春上冰月
             * email : caoyue@caoyue.com.cn
             * emailauthenticated : 2016-02-15T15:18:49Z
             * unreadcount : 4
             */

            @JSONField(name = "userinfo")
            public UserinfoEntity userinfo;

            public static class UserinfoEntity {
                @JSONField(name = "id")
                public int id;
                @JSONField(name = "name")
                public String name;
                @JSONField(name = "editcount")
                public int editcount;
                @JSONField(name = "realname")
                public String realname;
                @JSONField(name = "email")
                public String email;
                @JSONField(name = "emailauthenticated")
                public String emailauthenticated;
                @JSONField(name = "unreadcount")
                public int unreadcount;
                @JSONField(name = "groups")
                public List<String> groups;
            }
        }
    }
}
