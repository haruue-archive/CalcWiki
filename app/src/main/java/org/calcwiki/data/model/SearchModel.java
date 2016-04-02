package org.calcwiki.data.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.List;

/**
 * 搜索相关 API Model
 * https://calcwiki.org/api.php?action=query&list=search
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public class SearchModel {

    public static class Result {

        /**
         * batchcomplete :
         * continue : {"sroffset":5,"continue":"-||"}
         * query : {"searchinfo":{"totalhits":6},"search":[{"ns":0,"title":"TI图形计算器对比","snippet":"| [[<span class='searchmatch'>TI<\/span>-73(Explorer)]]\n| [[<span class='searchmatch'>TI<\/span>-BASIC]]，汇编（需Mallard），Flash Apps\n","size":3852,"wordcount":303,"timestamp":"2016-02-15T15:51:27Z"},{"ns":0,"title":"TI-Nspire","snippet":"...[https://en.wikipedia.org/wiki/zh:美国德州仪器 美国德州仪器（<span class='searchmatch'>TI<\/span>）公司]生产的当今最为强大的计算器之一，同时也是德州\n...于之前型号的产品更为接近家用电脑的界面。硬件方面，<span class='searchmatch'>TI<\/span>-Nspire改用了ARM架构的CPU,灰度屏机型速度可达75MHz（后期的\n","size":27610,"wordcount":844,"timestamp":"2016-02-25T09:03:18Z"},{"ns":0,"title":"TI-84系列","snippet":"<span class='searchmatch'>TI<\/span>-84系列是德州仪器推出的<span class='searchmatch'>TI<\/span>-83计算器系列的升级换代产品。\n有关<span class='searchmatch'>TI<\/span>-84系列计算器的硬件性能，请参见：[[<span class='searchmatch'>TI<\/span>图形计算器对比]]\n","size":6566,"wordcount":298,"timestamp":"2016-03-08T09:08:29Z"},{"ns":0,"title":"TI-BASIC","snippet":"\n\n","size":8989,"wordcount":147,"timestamp":"2016-03-27T05:54:02Z"},{"ns":0,"title":"TI-92系列","snippet":"...基础，[[<span class='searchmatch'>TI<\/span>-Nspire]]系列计算器的CAS系统在很多地方沿袭了<span class='searchmatch'>TI<\/span>-92的CAS系统的设计。\n...及和电脑键盘类似的按键布局，加之其硕大的体积，使得<span class='searchmatch'>TI<\/span>-92系列计算器看起来更像是小型的电脑而非计算器。\n","size":5391,"wordcount":150,"timestamp":"2016-03-19T10:52:58Z"}]}
         */

        @JSONField(name = "batchcomplete")
        public String batchcomplete;
        /**
         * sroffset : 5
         * continue : -||
         */

        @JSONField(name = "continue")
        public ContinueEntity continueX;
        /**
         * searchinfo : {"totalhits":6}
         * search : [{"ns":0,"title":"TI图形计算器对比","snippet":"| [[<span class='searchmatch'>TI<\/span>-73(Explorer)]]\n| [[<span class='searchmatch'>TI<\/span>-BASIC]]，汇编（需Mallard），Flash Apps\n","size":3852,"wordcount":303,"timestamp":"2016-02-15T15:51:27Z"},{"ns":0,"title":"TI-Nspire","snippet":"...[https://en.wikipedia.org/wiki/zh:美国德州仪器 美国德州仪器（<span class='searchmatch'>TI<\/span>）公司]生产的当今最为强大的计算器之一，同时也是德州\n...于之前型号的产品更为接近家用电脑的界面。硬件方面，<span class='searchmatch'>TI<\/span>-Nspire改用了ARM架构的CPU,灰度屏机型速度可达75MHz（后期的\n","size":27610,"wordcount":844,"timestamp":"2016-02-25T09:03:18Z"},{"ns":0,"title":"TI-84系列","snippet":"<span class='searchmatch'>TI<\/span>-84系列是德州仪器推出的<span class='searchmatch'>TI<\/span>-83计算器系列的升级换代产品。\n有关<span class='searchmatch'>TI<\/span>-84系列计算器的硬件性能，请参见：[[<span class='searchmatch'>TI<\/span>图形计算器对比]]\n","size":6566,"wordcount":298,"timestamp":"2016-03-08T09:08:29Z"},{"ns":0,"title":"TI-BASIC","snippet":"\n\n","size":8989,"wordcount":147,"timestamp":"2016-03-27T05:54:02Z"},{"ns":0,"title":"TI-92系列","snippet":"...基础，[[<span class='searchmatch'>TI<\/span>-Nspire]]系列计算器的CAS系统在很多地方沿袭了<span class='searchmatch'>TI<\/span>-92的CAS系统的设计。\n...及和电脑键盘类似的按键布局，加之其硕大的体积，使得<span class='searchmatch'>TI<\/span>-92系列计算器看起来更像是小型的电脑而非计算器。\n","size":5391,"wordcount":150,"timestamp":"2016-03-19T10:52:58Z"}]
         */

        @JSONField(name = "query")
        public QueryEntity query;

        public static class ContinueEntity {
            @JSONField(name = "sroffset")
            public int sroffset;
            @JSONField(name = "continue")
            public String continueX;
        }

        public static class QueryEntity implements Serializable {
            /**
             * totalhits : 6
             */

            @JSONField(name = "searchinfo")
            public SearchinfoEntity searchinfo;
            /**
             * ns : 0
             * title : TI图形计算器对比
             * snippet : | [[<span class='searchmatch'>TI</span>-73(Explorer)]]
             | [[<span class='searchmatch'>TI</span>-BASIC]]，汇编（需Mallard），Flash Apps

             * size : 3852
             * wordcount : 303
             * timestamp : 2016-02-15T15:51:27Z
             */

            @JSONField(name = "search")
            public List<SearchEntity> search;

            public static class SearchinfoEntity implements Serializable {
                @JSONField(name = "totalhits")
                public int totalhits;
            }

            public static class SearchEntity implements Serializable {
                @JSONField(name = "ns")
                public int ns;
                @JSONField(name = "title")
                public String title;
                @JSONField(name = "snippet")
                public String snippet;
                @JSONField(name = "size")
                public int size;
                @JSONField(name = "wordcount")
                public int wordcount;
                @JSONField(name = "timestamp")
                public String timestamp;
            }
        }
    }

}
