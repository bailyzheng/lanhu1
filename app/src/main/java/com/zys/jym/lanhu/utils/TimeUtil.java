package com.zys.jym.lanhu.utils;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtil {
    static String TAG = "TAG--TimeUtil";
    public static String[] hours = new String[]{
             "00", "01", "02", "03", "04", "05", "06", "07", "08", "09",
             "10", "11", "12", "13", "14", "15", "16", "17", "18", "19",
             "20", "21", "22", "23"
    };
    public static String[] minutes = new String[]{
            "00", "01", "02", "03", "04", "05", "06", "07", "08", "09",
            "10", "11", "12", "13", "14", "15", "16", "17", "18", "19",
            "20", "21", "22", "23", "24", "25", "26", "27", "28", "29",
            "30", "31", "32", "33", "34", "35", "36", "37", "38", "39",
            "40", "41", "42", "43", "44", "45", "46", "47", "48", "49",
            "50", "51", "52", "53", "54", "55", "56", "57", "58", "59",
    };
    public static String[] age = new String[]{
            "18", "19",
            "20", "21", "22", "23", "24", "25", "26", "27", "28", "29",
            "30", "31", "32", "33", "34", "35", "36", "37", "38", "39",
            "40", "41", "42", "43", "44", "45", "46", "47", "48", "49",
            "50", "51", "52", "53", "54", "55", "56", "57", "58", "59",
            "60", "61", "62", "63", "64", "65", "66", "67", "68", "69",
            "70", "71", "72", "73", "74", "75", "76", "77", "78", "79",
            "80", "81", "82", "83", "84", "85", "86", "87", "88", "89",
    };
    public static String[] mylifeData = new String[]{"160", "159", "158",
            "157", "156", "155", "154", "153", "152", "151", "150", "149",
            "148", "147", "146", "145", "144", "143", "142", "141", "140",
            "139", "138", "137", "136", "135", "134", "133", "132", "131",
            "130", "129", "128", "127", "126", "125", "124", "123", "122",
            "121", "120", "119", "118", "117", "116", "115", "114", "113",
            "112", "111", "110", "109", "108", "107", "106", "105", "104",
            "103", "102", "101", "100", "99", "98", "97", "96", "95", "94",
            "93", "92", "91", "90", "89", "88", "87", "86", "85", "84", "83",
            "82", "81", "80", "79", "78", "77", "76", "75", "74", "73", "72",
            "71", "70", "69", "68", "67", "66", "65", "64", "63", "62", "61",
            "60", "59", "58", "57", "56", "55", "54", "53", "52", "51", "50",
            "49", "48", "47", "46", "45", "44", "43", "42", "41", "40", "39",
            "38", "37", "36", "35", "34", "33", "32", "31", "30", "29", "28",
            "27", "26", "25", "24", "23", "22", "21", "10", "9", "8", "7", "6",
            "5", "4", "3", "2", "1", "0"};
    public static String[] yearData = new String[]{"1901", "1902", "1903",
            "1904", "1905", "1906", "1907", "1908", "1909", "1910", "1911",
            "1912", "1913", "1914", "1915", "1916", "1917", "1918", "1919",
            "1920", "1921", "1922", "1923", "1924", "1925", "1926", "1927",
            "1928", "1929", "1930", "1931", "1932", "1933", "1934", "1935",
            "1936", "1937", "1938", "1939", "1940", "1941", "1942", "1943",
            "1944", "1945", "1946", "1947", "1948", "1949", "1950", "1951",
            "1952", "1953", "1954", "1955", "1956", "1957", "1958", "1959",
            "1960", "1961", "1962", "1963", "1964", "1965", "1966", "1967",
            "1968", "1969", "1970", "1971", "1972", "1973", "1974", "1975",
            "1976", "1977", "1978", "1979", "1980", "1981", "1982", "1983",
            "1984", "1985", "1986", "1987", "1988", "1989", "1980", "1991",
            "1992", "1993", "1994", "1995", "1996", "1997", "1998", "1999",
            "2000", "2001", "2002", "2003", "2004", "2005", "2006", "2007",
            "2008", "2009", "2010", "2011", "2012", "2013", "2014", "2015",
            "2016", "2017", "2018", "2019", "2020", "2021", "2022", "2023",
            "2024", "2025", "2026", "2027", "2028", "2029", "2030", "2031",
            "2032", "2033", "2034", "2035", "2036", "2037", "2038", "2039",
            "2040", "2041", "2042", "2043", "2044", "2045", "2046", "2047",
            "2048", "2049", "2050"};
    public static String[] monthData = new String[]{"1", "2", "3", "4", "5",
            "6", "7", "8", "9", "10", "11", "12"};
    public static String[] dateData = new String[]{"1", "2", "3", "4", "5",
            "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17",
            "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28"};
    public static String[] dateData1 = new String[]{"1", "2", "3", "4", "5",
            "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17",
            "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28",
            "29"};
    public static String[] dateData2 = new String[]{"1", "2", "3", "4", "5",
            "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17",
            "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28",
            "29", "30"};
    public static String[] dateData3 = new String[]{"1", "2", "3", "4", "5",
            "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17",
            "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28",
            "29", "30", "31"};
    public static String[] hourData = new String[]{"1", "2", "3", "4", "5",
            "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17",
            "18", "19", "20", "21", "22", "23", "24"};


    @SuppressLint("SimpleDateFormat")
    public static String getNowTime() {
        String sc = "";
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy/MM/dd HH:mm:ss");// 可以方便地修改日期格式

        Calendar c = Calendar.getInstance();// 可以对每个时间域单独修改

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int date = c.get(Calendar.DATE);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        int second = c.get(Calendar.SECOND);

        MyUtils.Loge("TAG", year + "/" + month + "/" + date + " " + hour + ":"
                + minute + ":" + second);

        sc = Hour2Time(hour);

        return sc;
    }


    public static String Hour2Time(int hours) {
        String sc = "";
        String hour = hours + "";
        if (hour.equals("23") || hour.equals("00") || hour.equals("0")
                || hour.equals("24")) {
            return sc = "子";
        }
        if (hour.equals("01") || hour.equals("02") || hour.equals("1")
                || hour.equals("2")) {
            return sc = "丑";
        }
        if (hour.equals("03") || hour.equals("04") || hour.equals("3")
                || hour.equals("4")) {
            return sc = "寅";
        }
        if (hour.equals("05") || hour.equals("06") || hour.equals("5")
                || hour.equals("6")) {
            return sc = "卯";
        }
        if (hour.equals("07") || hour.equals("08") || hour.equals("7")
                || hour.equals("8")) {
            return sc = "辰";
        }
        if (hour.equals("09") || hour.equals("10") || hour.equals("9")) {
            return sc = "巳";
        }
        if (hour.equals("11") || hour.equals("12")) {
            return sc = "午";
        }
        if (hour.equals("13") || hour.equals("14")) {
            return sc = "未";
        }
        if (hour.equals("15") || hour.equals("16")) {
            return sc = "申";
        }
        if (hour.equals("17") || hour.equals("18")) {
            return sc = "酉";
        }
        if (hour.equals("19") || hour.equals("20")) {
            return sc = "戌";
        }
        if (hour.equals("21") || hour.equals("22")) {
            return sc = "亥";
        }
        return sc;
    }


    public static int getCurrentYear() {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy/MM/dd HH:mm:ss");// 可以方便地修改日期格式

        Calendar c = Calendar.getInstance();// 可以对每个时间域单独修改

        int year = c.get(Calendar.YEAR);
        return year;
    }

    public static int getCurrentMonth() {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy/MM/dd HH:mm:ss");// 可以方便地修改日期格式
        Calendar c = Calendar.getInstance();// 可以对每个时间域单独修改
        int month = c.get(Calendar.MONTH);
        return month + 1;
    }

    public static int getCurrentDay() {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy/MM/dd HH:mm:ss");// 可以方便地修改日期格式
        Calendar c = Calendar.getInstance();// 可以对每个时间域单独修改
        int date = c.get(Calendar.DATE);
        return date;
    }

    /*
     * 闰年的条件是： ① 能被4整除，但不能被100整除； ② 能被100整除，又能被400整除。
     */
    public static boolean isLeapYear(int year) {
        if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
            return true;
        }
        return false;
    }

    public static String getNowTimeYMDHMS() {
        Calendar c = Calendar.getInstance();// 可以对每个时间域单独修改

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int date = c.get(Calendar.DATE);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        int second = c.get(Calendar.SECOND);
        String time = year + "-" + month + "-" + date + " " + hour + ":"
                + minute + ":" + second;
        return time;
    }

    public static String getNowTimeYMD() {
        Calendar c = Calendar.getInstance();// 可以对每个时间域单独修改

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int date = c.get(Calendar.DATE);
        String time = year + "-" + month + "-" + date;
        return time;
    }

    /**
     * 得到距离某时间点有多少秒    传入时间Str（如：2088-12-12 01:01:01）
     */
    public static int getSurplusTime(String str) {
        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dstr = str;
        try {
            date = sdf.parse(dstr);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        long s1 = date.getTime();// 将时间转为毫秒
        long s2 = System.currentTimeMillis();// 得到当前的毫秒
//        int day = (int) Math.abs((s1 - s2) / 1000 / 60 / 60 / 24);//这是绝对值
        int miao= (int) ((s1 - s2) / 1000 );// 得到还有多少秒 //没有取绝对值
        MyUtils.Loge("TAG--TimeUtil", "距离str有" + miao + "秒");
        return miao;
    }

    /**
     * 得到距离某时间点有多少天   传入时间Str（如：2088-12-12 01:01:01）
     */
    public static int getSurplusDay(String str) {
        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dstr = str;
        try {
            date = sdf.parse(dstr);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        long s1 = date.getTime();// 将时间转为毫秒
        long s2 = System.currentTimeMillis();// 得到当前的毫秒
//        int day = (int) Math.abs((s1 - s2) / 1000 / 60 / 60 / 24);//这是绝对值
        int day1=(int)((s1 - s2) / 1000 / 60 / 60 / 24);
//        int miao= (int) ((s1 - s2) / 1000 );// 得到还有多少秒 //没有取绝对值
        MyUtils.Loge("TAG--TimeUtil", "距离"+dstr+"有" + day1 + "天");
        return day1;
    }
    /**
     * 得到多少分钟后的时间字符串
     * @return
     */
    public static String getLastTime(int lasttime){
        long curren = System.currentTimeMillis();
        curren += lasttime * 60 * 1000;
        Date da = new Date(curren);
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(da);
    }
    /**
     * 今天剩余时间(时，分，秒)
     */
    public static int[] surplusSecond() {
        final Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        final double diff = cal.getTimeInMillis() - System.currentTimeMillis();
        int hour = (int) (diff / 3600000);
        int minute = (int) (diff / 60000);
        int second = (int) (diff / 1000);// 秒 这里四舍五入了
//		Log.i(TAG, "今天还剩下 " + diff / 86400000 + "天");
//		Log.i(TAG, "今天还剩下 " + diff / 3600000 + "小时");
//		Log.i(TAG, "今天还剩下 " + diff / 60000 + "分钟");
//		Log.i(TAG, "今天还剩下 " + second + "秒");
//		Log.i(TAG, "今天还剩下 " + diff + "毫秒");
        int[] times = new int[]{hour, minute, second};

        return times;
    }



        /**
         * 时间戳转换成日期格式字符串
         * @param seconds 精确到秒的字符串
         * @return
         */
        public static String timeStamp2Date(String seconds,String format) {
            if(seconds == null || seconds.isEmpty() || seconds.equals("null")){
                return "";
            }
            if(format == null || format.isEmpty()){
                format = "yyyy-MM-dd HH:mm:ss";
            }
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.format(new Date(Long.valueOf(seconds+"000")));
        }
        /**
         * 日期格式字符串转换成时间戳
         * @param //date 字符串日期
         * @param format 如：yyyy-MM-dd HH:mm:ss
         * @return
         */
        public static String date2TimeStamp(String date_str,String format){
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(format);
                return String.valueOf(sdf.parse(date_str).getTime()/1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }

        /**
         * 取得当前时间戳（精确到秒）
         * @return
         */
        public static String timeStamp(){
            long time = System.currentTimeMillis();
            String t = String.valueOf(time/1000);
            return t;
        }
    /**
     * 取得当前时间戳（精确到毫秒）
     * @return
     */
    public static String getTimeStamp(){
        long time = System.currentTimeMillis();
        String t = String.valueOf(time);
        return t;
    }
    /**
     * 毫秒转时间
     */
    public static String formatDuring(long mss) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");//初始化Formatter的转换格式。
        String hms = formatter.format(mss);
        return  hms;
    }

}
