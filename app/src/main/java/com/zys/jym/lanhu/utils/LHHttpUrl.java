package com.zys.jym.lanhu.utils;

/**
 * Created by Administrator on 2016/6/2.
 */
public class LHHttpUrl {
    /**
     * 接口全局地址
     */
    public static final String URL = "http://139.224.194.108/lh/Api/";
    /**
     * 图片特殊地址
     */
    public static final String IMG_URL = "http://139.224.194.108/lh/";
    /**
     * 登录
     */
    public static final String LOGIN_URL = URL + "Public/login";
    /**
     * 退出登录
     */
    public static final String LOGOUT_URL = URL + "Public/logout";
    /**
     * 发送验证码  type  1注册2找回密码
     */
    public static final String SMSCODE_URL = URL + "Public/smsCode";
    /**
     * 注册
     */
    public static final String REG_URL = URL + "Public/reg";
    /**
     * 找回密码
     */
    public static final String RESETPASSWORD_URL = URL + "Public/resetPassword";
    /**
     * 修改昵称或描述  type  修改类型1昵称2描述
     */
    public static final String MODIFYINFO_URL = URL + "UserCenter/modifyInfo";
    /**
     * 上传头像
     */
    public static final String MODIFYHEADURL_URL = URL + "UserCenter/modifyHeadurl";
    /**
     * 上传群二维码
     */
    public static final String UPLOADQRCODE_URL = URL + "Topic/uploadQrcode";
    /**
     * 上传个人二维码
     */
    public static final String UPLOAD_PERSONAL_QRCODE_URL = URL + "Topic/uploadPersonalQrcode";
    /**
     *
     * 获取省列表
     */
    public static final String GETPROVICELIST_URL = URL + "Public/getProviceList";
    /**
     * 获取市列表
     */
    public static final String GETCITYBYPROID_URL = URL + "Public/getCityByProId";
    /**
     * 发布名片
     */
    public static final String ADDTOPIC_URL = URL + "Topic/addTopic";
    /**
     * 投诉
     */
    public static final String ADDCOMPLAIN_URL = URL + "Complain/addComplain";
    /**
     * 我发布的群聊
     */
    public static final String MYTOPICLIST_URL = URL + "Topic/myTopicList";
//    /**群聊*/
//    public static final String TOPICLIST_URL=URL+"Topic/topicList";
    /**
     * 群聊
     */
    public static final String TOPICLIST_URL = URL + "Index/topicList";
    /**
     * 置顶
     */
    public static final String TOPTOPIC_URL = URL + "Topic/topTopic";
    /**
     * 自动刷新
     */
    public static final String AUTOREFRESH_URL = URL + "Topic/autoRefresh";
    /**
     * 手动刷新
     */
    public static final String HANDREFRESH_URL = URL + "Topic/handRefresh";
    /**
     * 删除群聊
     */
    public static final String DELTOPIC_URL = URL + "Topic/delTopic";
    /**
     * 获取用户账户信息
     */
    public static final String GETPURSEDATA_URL = URL + "UserCenter/getPurseData";
    /**
     * 群聊详情
     */
    public static final String DETAILTOPIC_URL = URL + "Index/detailTopic";
    /**
     * 调起支付
     */
    public static final String PREPAY_URL = URL + "Pay/prepay";
    /**
     * 狐币流水
     */
    public static final String GETPURSELOG_URL = URL + "UserCenter/getPurseLog";
    /**
     * 置顶流水
     */
    public static final String GETTOPLOG_URL = URL + "UserCenter/getTopLog";
    /**
     * 消息列表
     */
    public static final String INFORMLIST_URL = URL + "Index/informList";
    /**
     * 刷新缓存
     */
    public static final String FLUSH_URL = URL + "Public/flush";
    /**
     * 兑换码兑换
     */
    public static final String EXCHANGECODE_URL = URL + "Code/exchangeCode";
    /**
     * 检查更新
     */
    public static final String GETVERSION_URL = URL + "Index/getVersion";
    /**
     * 首页列表数据
     */
    public static final String HOME_LIST_URL = URL + "Contact/contactList";//http:/139.224.194.108/lh/Api/Contact/contactList
    /**
     * 是否允许被加粉
     */
    public static final String HOME_ADD_URL = URL + "Contact/switchContact";
    /**
     * 更新用户地区
     */
    public static final String UPDATA_LOCATION_URL=URL+"Public/updateCity";
    /**
     * 置顶通讯录
     */
    public static final String HOME_UP_PHONE_URL=URL+"Contact/topContact";
    /**
     * 保存通讯录
     */
    public static final String KEEP_PHONE_URL=URL+"Contact/expAnzhuoContact";//Contact/expAnzhuoContact  //Contact/expContact
    /**
     * 获取分享配置
     */
    public static final String GET_SHARE_URL=URL+"Public/getShareConfig";
    /**
     * 获取分享状态
     */
    public static final String GET_SHARE_STATUS_URL=URL+"Index/share";
}
