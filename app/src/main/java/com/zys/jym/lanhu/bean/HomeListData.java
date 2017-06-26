package com.zys.jym.lanhu.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/2/28.
 */

public class HomeListData implements Serializable {

    /**
     * errcode : 1
     * errmsg : 查询通信录列表成功
     * data : {"overtime":"0","isswitch":"0","p":"1","pageSize":"30","count":"1805","contactList":[{"id":"14","phone":"18637752014","nickname":"蓝狐327D","regtime":"1482397102","headurl":null,"describe":null,"isswitch":"1","province":"浙江省","city":"杭州市","viprest":"-4936099","toprest":"-1489670238"},{"id":"17","phone":"18353678628","nickname":"齐心箱包","regtime":"1483184373","headurl":"uploads/headurl/2017-03-16/58ca88d87faee.png","describe":"好好学习，好好工作","isswitch":"1","province":"江苏省","city":"南京市","viprest":"22553107","toprest":"-455"},{"id":"20","phone":"18656358910","nickname":"蓝狐487J","regtime":"1483448539","headurl":"uploads/headurl/2017-03-04/58baadba326d3.png","describe":null,"isswitch":"1","province":"安徽省","city":"合肥市","viprest":"-5022323","toprest":"-1489670238"},{"id":"1754","phone":"17183457036","nickname":"蓝狐4000BP","regtime":"1488890043","headurl":"uploads/headurl/def/t10.jpg","describe":null,"isswitch":"1","province":"江苏省","city":"南京市","viprest":"-1489670238","toprest":"-779960"},{"id":"1808","phone":"15516860803","nickname":"蓝狐6240D7","regtime":"1489664091","headurl":"uploads/headurl/def/t6.jpg","describe":null,"isswitch":"0","province":"","city":"","viprest":"-1489670238","toprest":"-1489670238"},{"id":"1807","phone":"15805051190","nickname":"蓝狐3310D6","regtime":"1489662921","headurl":"uploads/headurl/2017-03-16/58ca74101bb89.png","describe":null,"isswitch":"0","province":"","city":"","viprest":"-1489670238","toprest":"-1489670238"},{"id":"1806","phone":"18993590675","nickname":"蓝狐4300D5","regtime":"1489654891","headurl":"uploads/headurl/def/t14.jpg","describe":null,"isswitch":"0","province":"","city":"","viprest":"-1489670238","toprest":"-1489670238"},{"id":"1805","phone":"15182363084","nickname":"蓝狐3850D4","regtime":"1489635348","headurl":"uploads/headurl/def/t18.jpg","describe":null,"isswitch":"0","province":"","city":"","viprest":"-1489670238","toprest":"-1489670238"},{"id":"1804","phone":"15028006717","nickname":"蓝狐940D3","regtime":"1489620554","headurl":"uploads/headurl/def/t14.jpg","describe":null,"isswitch":"0","province":"","city":"","viprest":"-1489670238","toprest":"-1489670238"},{"id":"1803","phone":"18376751553","nickname":"蓝狐3240D2","regtime":"1489585245","headurl":"uploads/headurl/def/t13.jpg","describe":null,"isswitch":"0","province":"","city":"","viprest":"-1489670238","toprest":"-1489670238"},{"id":"1802","phone":"13009151500","nickname":"蓝狐6860D1","regtime":"1489579676","headurl":"uploads/headurl/def/t14.jpg","describe":null,"isswitch":"0","province":"","city":"","viprest":"-1489670238","toprest":"-1489670238"},{"id":"1801","phone":"13163758284","nickname":"8水晶心","regtime":"1489557156","headurl":"uploads/headurl/2017-03-15/58c8d6f129d6f.png","describe":"喜欢交友","isswitch":"0","province":"","city":"","viprest":"-1489670238","toprest":"-1489670238"},{"id":"1800","phone":"13367536293","nickname":"蓝狐550CZ","regtime":"1489505236","headurl":"uploads/headurl/def/t15.jpg","describe":null,"isswitch":"0","province":"","city":"","viprest":"-1489670238","toprest":"-1489670238"},{"id":"1799","phone":"13552799747","nickname":"蓝狐3540CY","regtime":"1489491574","headurl":"uploads/headurl/def/t12.jpg","describe":null,"isswitch":"0","province":"","city":"","viprest":"-1489670238","toprest":"-1489670238"},{"id":"1798","phone":"15010094322","nickname":"蓝狐5710CX","regtime":"1489491029","headurl":"uploads/headurl/def/t15.jpg","describe":null,"isswitch":"0","province":"","city":"","viprest":"-1489670238","toprest":"-1489670238"}]}
     */

    private int errcode;
    private String errmsg;
    private DataBean data;

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * overtime : 0
         * isswitch : 0
         * p : 1
         * pageSize : 30
         * step : 新手必看url
         * count : 1805
         * contactList : [{"id":"14","phone":"18637752014","nickname":"蓝狐327D","regtime":"1482397102","headurl":null,"describe":null,"isswitch":"1","province":"浙江省","city":"杭州市","viprest":"-4936099","toprest":"-1489670238"},{"id":"17","phone":"18353678628","nickname":"齐心箱包","regtime":"1483184373","headurl":"uploads/headurl/2017-03-16/58ca88d87faee.png","describe":"好好学习，好好工作","isswitch":"1","province":"江苏省","city":"南京市","viprest":"22553107","toprest":"-455"},{"id":"20","phone":"18656358910","nickname":"蓝狐487J","regtime":"1483448539","headurl":"uploads/headurl/2017-03-04/58baadba326d3.png","describe":null,"isswitch":"1","province":"安徽省","city":"合肥市","viprest":"-5022323","toprest":"-1489670238"},{"id":"1754","phone":"17183457036","nickname":"蓝狐4000BP","regtime":"1488890043","headurl":"uploads/headurl/def/t10.jpg","describe":null,"isswitch":"1","province":"江苏省","city":"南京市","viprest":"-1489670238","toprest":"-779960"},{"id":"1808","phone":"15516860803","nickname":"蓝狐6240D7","regtime":"1489664091","headurl":"uploads/headurl/def/t6.jpg","describe":null,"isswitch":"0","province":"","city":"","viprest":"-1489670238","toprest":"-1489670238"},{"id":"1807","phone":"15805051190","nickname":"蓝狐3310D6","regtime":"1489662921","headurl":"uploads/headurl/2017-03-16/58ca74101bb89.png","describe":null,"isswitch":"0","province":"","city":"","viprest":"-1489670238","toprest":"-1489670238"},{"id":"1806","phone":"18993590675","nickname":"蓝狐4300D5","regtime":"1489654891","headurl":"uploads/headurl/def/t14.jpg","describe":null,"isswitch":"0","province":"","city":"","viprest":"-1489670238","toprest":"-1489670238"},{"id":"1805","phone":"15182363084","nickname":"蓝狐3850D4","regtime":"1489635348","headurl":"uploads/headurl/def/t18.jpg","describe":null,"isswitch":"0","province":"","city":"","viprest":"-1489670238","toprest":"-1489670238"},{"id":"1804","phone":"15028006717","nickname":"蓝狐940D3","regtime":"1489620554","headurl":"uploads/headurl/def/t14.jpg","describe":null,"isswitch":"0","province":"","city":"","viprest":"-1489670238","toprest":"-1489670238"},{"id":"1803","phone":"18376751553","nickname":"蓝狐3240D2","regtime":"1489585245","headurl":"uploads/headurl/def/t13.jpg","describe":null,"isswitch":"0","province":"","city":"","viprest":"-1489670238","toprest":"-1489670238"},{"id":"1802","phone":"13009151500","nickname":"蓝狐6860D1","regtime":"1489579676","headurl":"uploads/headurl/def/t14.jpg","describe":null,"isswitch":"0","province":"","city":"","viprest":"-1489670238","toprest":"-1489670238"},{"id":"1801","phone":"13163758284","nickname":"8水晶心","regtime":"1489557156","headurl":"uploads/headurl/2017-03-15/58c8d6f129d6f.png","describe":"喜欢交友","isswitch":"0","province":"","city":"","viprest":"-1489670238","toprest":"-1489670238"},{"id":"1800","phone":"13367536293","nickname":"蓝狐550CZ","regtime":"1489505236","headurl":"uploads/headurl/def/t15.jpg","describe":null,"isswitch":"0","province":"","city":"","viprest":"-1489670238","toprest":"-1489670238"},{"id":"1799","phone":"13552799747","nickname":"蓝狐3540CY","regtime":"1489491574","headurl":"uploads/headurl/def/t12.jpg","describe":null,"isswitch":"0","province":"","city":"","viprest":"-1489670238","toprest":"-1489670238"},{"id":"1798","phone":"15010094322","nickname":"蓝狐5710CX","regtime":"1489491029","headurl":"uploads/headurl/def/t15.jpg","describe":null,"isswitch":"0","province":"","city":"","viprest":"-1489670238","toprest":"-1489670238"}]
         */

        private String overtime;
        private String isswitch;
        private String p;
        private String pageSize;
        private String count;
        private List<ContactListBean> contactList;
        private String step;

        public String getStep() {
            return step;
        }

        public void setStep(String step) {
            this.step = step;
        }

        public String getOvertime() {
            return overtime;
        }

        public void setOvertime(String overtime) {
            this.overtime = overtime;
        }

        public String getIsswitch() {
            return isswitch;
        }

        public void setIsswitch(String isswitch) {
            this.isswitch = isswitch;
        }

        public String getP() {
            return p;
        }

        public void setP(String p) {
            this.p = p;
        }

        public String getPageSize() {
            return pageSize;
        }

        public void setPageSize(String pageSize) {
            this.pageSize = pageSize;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public List<ContactListBean> getContactList() {
            return contactList;
        }

        public void setContactList(List<ContactListBean> contactList) {
            this.contactList = contactList;
        }

        public static class ContactListBean {
            /**
             * id : 14
             * phone : 18637752014
             * nickname : 蓝狐327D
             * regtime : 1482397102
             * headurl : null
             * describe : null
             * isswitch : 1
             * province : 浙江省
             * city : 杭州市
             * viprest : -4936099
             * toprest : -1489670238
             */

            private String id;
            private String phone;
            private String nickname;
            private String regtime;
            private Object headurl;
            private Object describe;
            private String isswitch;
            private String province;
            private String city;
            private String viprest;
            private String toprest;
            private int type = 1;

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getRegtime() {
                return regtime;
            }

            public void setRegtime(String regtime) {
                this.regtime = regtime;
            }

            public Object getHeadurl() {
                return headurl;
            }

            public void setHeadurl(Object headurl) {
                this.headurl = headurl;
            }

            public Object getDescribe() {
                return describe;
            }

            public void setDescribe(Object describe) {
                this.describe = describe;
            }

            public String getIsswitch() {
                return isswitch;
            }

            public void setIsswitch(String isswitch) {
                this.isswitch = isswitch;
            }

            public String getProvince() {
                return province;
            }

            public void setProvince(String province) {
                this.province = province;
            }

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public String getViprest() {
                return viprest;
            }

            public void setViprest(String viprest) {
                this.viprest = viprest;
            }

            public String getToprest() {
                return toprest;
            }

            public void setToprest(String toprest) {
                this.toprest = toprest;
            }
        }
    }
}
