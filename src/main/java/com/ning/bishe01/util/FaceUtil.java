package com.ning.bishe01.util;

import com.baidu.aip.face.AipFace;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * 百度AI人脸识别工具类
 */
public class FaceUtil {
    private static final String AppID = "32461153";
    private static final String APIKey = "YOYZHWXwRLN05qeWhwhRyF2e";
    private static final String SecretKey = "USvmo8nSK9UaOWMdQfAzVgYKlk6mbs0l";
//1.创建Java代码和百度云交互的client对象
    static AipFace client = null;
    static {
        client = new AipFace(AppID, APIKey, SecretKey);
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);
    }

    /**
     * 人脸注册
     * @param image 注册的人脸图片
     * @param userId 用户编号
     * @param groupId 用户组编号
     * @return
     */
    public static String faceRegister(String image,String userId,String groupId) {
        //2.参数设置
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("quality_control", "NORMAL");//图片质量
        options.put("liveness_control", "LOW");//活体检测
        options.put("action_type", "REPLACE");

        String imageType = "BASE64";
        //3.调用api方法完成人脸识别
//参数1：图片的url或图片的Base64字符串
// 参数2：图片形式
// 参数3：组ID(固定值字符串)
// 参数4：用户ID
// 参数5：hashMap中的基本参数配置
        JSONObject res = client.addUser(image, imageType, groupId, userId, options);
        //格式化返回结果
        return res.toString(2);
    }

    /**
     * 人脸登录
     * @param image 登录的人脸图片
     * @param groupIdList 用户组编号，多个之间用逗号隔开
     * @return
     */
    public static String faceLogin(String image,String groupIdList){
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("match_threshold", "90");
        options.put("quality_control", "NORMAL");
        options.put("liveness_control", "LOW");

        String imageType = "BASE64";
        JSONObject res = client.search(image, imageType, groupIdList, options);
        return res.toString(2);
    }
}
