package org.calcwiki.data.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;

import java.io.Serializable;
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

    /**
     * https://calcwiki.org/api.php?action=query&prop=info&inprop=protection|talkid|watched|watchers|notificationtimestamp|subjectid|url|readable|preload|displaytitle&titles=B:Server
     */
    public static class PageInfo implements Serializable {

        /**
         * batchcomplete :
         * query : {"pages":{"4377":{"pageid":4377,"ns":3002,"title":"B:Server","contentmodel":"wikitext","pagelanguage":"zh","touched":"2016-04-13T03:08:02Z","lastrevid":5616,"length":38,"protection":[{"type":"edit","level":"sysop","expiry":"infinity"},{"type":"move","level":"sysop","expiry":"infinity"}],"restrictiontypes":["edit","move"],"watched":"","watchers":2,"notificationtimestamp":"","fullurl":"https://calcwiki.org/B:Server","editurl":"https://calcwiki.org/index.php?title=B:Server&action=edit","canonicalurl":"https://calcwiki.org/B:Server","readable":"","preload":"","displaytitle":"B:Server"}}}
         */

        @JSONField(name = "batchcomplete")
        public String batchcomplete;
        /**
         * pages : {"4377":{"pageid":4377,"ns":3002,"title":"B:Server","contentmodel":"wikitext","pagelanguage":"zh","touched":"2016-04-13T03:08:02Z","lastrevid":5616,"length":38,"protection":[{"type":"edit","level":"sysop","expiry":"infinity"},{"type":"move","level":"sysop","expiry":"infinity"}],"restrictiontypes":["edit","move"],"watched":"","watchers":2,"notificationtimestamp":"","fullurl":"https://calcwiki.org/B:Server","editurl":"https://calcwiki.org/index.php?title=B:Server&action=edit","canonicalurl":"https://calcwiki.org/B:Server","readable":"","preload":"","displaytitle":"B:Server"}}
         */

        @JSONField(name = "query")
        public QueryEntity query;

        public static class QueryEntity implements Serializable {
            /**
             * 4377 : {"pageid":4377,"ns":3002,"title":"B:Server","contentmodel":"wikitext","pagelanguage":"zh","touched":"2016-04-13T03:08:02Z","lastrevid":5616,"length":38,"protection":[{"type":"edit","level":"sysop","expiry":"infinity"},{"type":"move","level":"sysop","expiry":"infinity"}],"restrictiontypes":["edit","move"],"watched":"","watchers":2,"notificationtimestamp":"","fullurl":"https://calcwiki.org/B:Server","editurl":"https://calcwiki.org/index.php?title=B:Server&action=edit","canonicalurl":"https://calcwiki.org/B:Server","readable":"","preload":"","displaytitle":"B:Server"}
             */

            @JSONField(name = "pages")
            public PagesEntity pages;

            public static class PagesEntity implements Serializable {
                /**
                 * pageid : 4377
                 * ns : 3002
                 * title : B:Server
                 * contentmodel : wikitext
                 * pagelanguage : zh
                 * touched : 2016-04-13T03:08:02Z
                 * lastrevid : 5616
                 * length : 38
                 * protection : [{"type":"edit","level":"sysop","expiry":"infinity"},{"type":"move","level":"sysop","expiry":"infinity"}]
                 * restrictiontypes : ["edit","move"]
                 * watched :
                 * watchers : 2
                 * notificationtimestamp :
                 * fullurl : https://calcwiki.org/B:Server
                 * editurl : https://calcwiki.org/index.php?title=B:Server&action=edit
                 * canonicalurl : https://calcwiki.org/B:Server
                 * readable :
                 * preload :
                 * displaytitle : B:Server
                 */

                @JSONField(name = "content")
                public NumEntity content;

                public static class NumEntity implements Serializable {
                    @JSONField(name = "pageid")
                    public int pageid;
                    @JSONField(name = "ns")
                    public int ns;
                    @JSONField(name = "title")
                    public String title;
                    @JSONField(name = "contentmodel")
                    public String contentmodel;
                    @JSONField(name = "pagelanguage")
                    public String pagelanguage;
                    @JSONField(name = "touched")
                    public String touched;
                    @JSONField(name = "lastrevid")
                    public int lastrevid;
                    @JSONField(name = "length")
                    public int length;
                    @JSONField(name = "watched")
                    public String watched;
                    @JSONField(name = "watchers")
                    public int watchers;
                    @JSONField(name = "notificationtimestamp")
                    public String notificationtimestamp;
                    @JSONField(name = "fullurl")
                    public String fullurl;
                    @JSONField(name = "editurl")
                    public String editurl;
                    @JSONField(name = "canonicalurl")
                    public String canonicalurl;
                    @JSONField(name = "readable")
                    public String readable;
                    @JSONField(name = "preload")
                    public String preload;
                    @JSONField(name = "displaytitle")
                    public String displaytitle;
                    /**
                     * type : edit
                     * level : sysop
                     * expiry : infinity
                     */

                    @JSONField(name = "protection")
                    public List<ProtectionEntity> protection;
                    @JSONField(name = "restrictiontypes")
                    public List<String> restrictiontypes;

                    public static class ProtectionEntity implements Serializable {
                        @JSONField(name = "type")
                        public String type;
                        @JSONField(name = "level")
                        public String level;
                        @JSONField(name = "expiry")
                        public String expiry;
                    }
                }
            }
        }
    }
}
