package org.calcwiki.data.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.List;

/**
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public class ParseModel {

    public static class Page implements Serializable {

        /**
         * title : 计算机代数系统
         * pageid : 4142
         * revid : 5000
         * redirects : [{"from":"CAS","to":"计算机代数系统"}]
         * text : {"content":"page-html-body"}
         * sections : [{"toclevel":1,"level":"2","line":"代数功能","number":"1","index":"1","fromtitle":"计算机代数系统","byteoffset":271,"anchor":".E4.BB.A3.E6.95.B0.E5.8A.9F.E8.83.BD"},{"toclevel":2,"level":"3","line":"代数式的化简","number":"1.1","index":"2","fromtitle":"计算机代数系统","byteoffset":408,"anchor":".E4.BB.A3.E6.95.B0.E5.BC.8F.E7.9A.84.E5.8C.96.E7.AE.80"},{"toclevel":2,"level":"3","line":"代数式的微积分、极限、三角函数、指数函数","number":"1.2","index":"3","fromtitle":"计算机代数系统","byteoffset":581,"anchor":".E4.BB.A3.E6.95.B0.E5.BC.8F.E7.9A.84.E5.BE.AE.E7.A7.AF.E5.88.86.E3.80.81.E6.9E.81.E9.99.90.E3.80.81.E4.B8.89.E8.A7.92.E5.87.BD.E6.95.B0.E3.80.81.E6.8C.87.E6.95.B0.E5.87.BD.E6.95.B0"},{"toclevel":2,"level":"3","line":"代数式的求和与求积","number":"1.3","index":"4","fromtitle":"计算机代数系统","byteoffset":958,"anchor":".E4.BB.A3.E6.95.B0.E5.BC.8F.E7.9A.84.E6.B1.82.E5.92.8C.E4.B8.8E.E6.B1.82.E7.A7.AF"},{"toclevel":2,"level":"3","line":"多项式的因式分解和展开","number":"1.4","index":"5","fromtitle":"计算机代数系统","byteoffset":1049,"anchor":".E5.A4.9A.E9.A1.B9.E5.BC.8F.E7.9A.84.E5.9B.A0.E5.BC.8F.E5.88.86.E8.A7.A3.E5.92.8C.E5.B1.95.E5.BC.80"},{"toclevel":2,"level":"3","line":"使用特征方程法求解数列通项公式","number":"1.5","index":"6","fromtitle":"计算机代数系统","byteoffset":1258,"anchor":".E4.BD.BF.E7.94.A8.E7.89.B9.E5.BE.81.E6.96.B9.E7.A8.8B.E6.B3.95.E6.B1.82.E8.A7.A3.E6.95.B0.E5.88.97.E9.80.9A.E9.A1.B9.E5.85.AC.E5.BC.8F"},{"toclevel":2,"level":"3","line":"矩阵的超越函数","number":"1.6","index":"7","fromtitle":"计算机代数系统","byteoffset":1455,"anchor":".E7.9F.A9.E9.98.B5.E7.9A.84.E8.B6.85.E8.B6.8A.E5.87.BD.E6.95.B0"},{"toclevel":1,"level":"2","line":"其他功能","number":"2","index":"8","fromtitle":"计算机代数系统","byteoffset":1713,"anchor":".E5.85.B6.E4.BB.96.E5.8A.9F.E8.83.BD"},{"toclevel":2,"level":"3","line":"高精度求值","number":"2.1","index":"9","fromtitle":"计算机代数系统","byteoffset":1773,"anchor":".E9.AB.98.E7.B2.BE.E5.BA.A6.E6.B1.82.E5.80.BC"},{"toclevel":2,"level":"3","line":"精确数值运算","number":"2.2","index":"10","fromtitle":"计算机代数系统","byteoffset":1964,"anchor":".E7.B2.BE.E7.A1.AE.E6.95.B0.E5.80.BC.E8.BF.90.E7.AE.97"},{"toclevel":2,"level":"3","line":"大数求和求积","number":"2.3","index":"11","fromtitle":"计算机代数系统","byteoffset":3062,"anchor":".E5.A4.A7.E6.95.B0.E6.B1.82.E5.92.8C.E6.B1.82.E7.A7.AF"},{"toclevel":1,"level":"2","line":"拥有计算机代数系统的计算器","number":"3","index":"12","fromtitle":"计算机代数系统","byteoffset":3346,"anchor":".E6.8B.A5.E6.9C.89.E8.AE.A1.E7.AE.97.E6.9C.BA.E4.BB.A3.E6.95.B0.E7.B3.BB.E7.BB.9F.E7.9A.84.E8.AE.A1.E7.AE.97.E5.99.A8"},{"toclevel":2,"level":"3","line":"卡西欧","number":"3.1","index":"13","fromtitle":"计算机代数系统","byteoffset":3641,"anchor":".E5.8D.A1.E8.A5.BF.E6.AC.A7"},{"toclevel":2,"level":"3","line":"德州仪器","number":"3.2","index":"14","fromtitle":"计算机代数系统","byteoffset":3826,"anchor":".E5.BE.B7.E5.B7.9E.E4.BB.AA.E5.99.A8"},{"toclevel":2,"level":"3","line":"惠普","number":"3.3","index":"15","fromtitle":"计算机代数系统","byteoffset":4028,"anchor":".E6.83.A0.E6.99.AE"},{"toclevel":1,"level":"2","line":"支持计算机代数系统的计算软件","number":"4","index":"16","fromtitle":"计算机代数系统","byteoffset":4145,"anchor":".E6.94.AF.E6.8C.81.E8.AE.A1.E7.AE.97.E6.9C.BA.E4.BB.A3.E6.95.B0.E7.B3.BB.E7.BB.9F.E7.9A.84.E8.AE.A1.E7.AE.97.E8.BD.AF.E4.BB.B6"},{"toclevel":2,"level":"3","line":"开源软件","number":"4.1","index":"17","fromtitle":"计算机代数系统","byteoffset":4167,"anchor":".E5.BC.80.E6.BA.90.E8.BD.AF.E4.BB.B6"},{"toclevel":2,"level":"3","line":"商业软件","number":"4.2","index":"18","fromtitle":"计算机代数系统","byteoffset":4217,"anchor":".E5.95.86.E4.B8.9A.E8.BD.AF.E4.BB.B6"},{"toclevel":1,"level":"2","line":"参考","number":"5","index":"19","fromtitle":"计算机代数系统","byteoffset":4282,"anchor":".E5.8F.82.E8.80.83"}]
         * displaytitle : 计算机代数系统
         * headhtml : {"content":"page-html-head"}
         */

        @JSONField(name = "parse")
        public ParseEntity parse;

        public static class ParseEntity implements Serializable {
            @JSONField(name = "title")
            public String title;
            @JSONField(name = "pageid")
            public int pageid;
            @JSONField(name = "revid")
            public int revid;
            /**
             * content : page-html-body
             */

            @JSONField(name = "text")
            public TextEntity text;
            @JSONField(name = "displaytitle")
            public String displaytitle;
            @JSONField(name = "headhtml")
            public TextEntity headhtml;
            /**
             * from : CAS
             * to : 计算机代数系统
             */

            @JSONField(name = "redirects")
            public List<RedirectsEntity> redirects;
            /**
             * toclevel : 1
             * level : 2
             * line : 代数功能
             * number : 1
             * index : 1
             * fromtitle : 计算机代数系统
             * byteoffset : 271
             * anchor : .E4.BB.A3.E6.95.B0.E5.8A.9F.E8.83.BD
             */

            @JSONField(name = "sections")
            public List<SectionsEntity> sections;

            public static class TextEntity implements Serializable {
                @JSONField(name = "*")
                public String content;
            }

            public static class RedirectsEntity implements Serializable {
                @JSONField(name = "from")
                public String from;
                @JSONField(name = "to")
                public String to;
            }

            public static class SectionsEntity implements Serializable {
                @JSONField(name = "toclevel")
                public int toclevel;
                @JSONField(name = "level")
                public String level;
                @JSONField(name = "line")
                public String line;
                @JSONField(name = "number")
                public String number;
                @JSONField(name = "index")
                public String index;
                @JSONField(name = "fromtitle")
                public String fromtitle;
                @JSONField(name = "byteoffset")
                public int byteoffset;
                @JSONField(name = "anchor")
                public String anchor;
            }
        }
    }

    public static class Error {

        /**
         * code : missingtitle
         * info : The page you specified doesn't exist
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
