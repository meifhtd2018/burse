package com.hnair.consumer.wallet.utils;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

import com.hnair.consumer.utils.Base64Utils;
import com.hnair.consumer.utils.HMACSHA256Util;
import com.hnair.consumer.utils.MD5Utils;
import com.hnair.consumer.utils.MapUtils;
import com.hnair.consumer.utils.Sha1Util;
import com.hnair.consumer.utils.bean.BeanUtils;

public class WalletUtils {

	/**
	 * 生成secret
	 * 
	 * @param id
	 * @return
	 * @author 陶嘉骏
	 * @date 2018年3月2日
	 */
	public static String buildSecret(String walletId) {
		// 当前时间
		String currentTime = String.valueOf(System.currentTimeMillis());

		return Sha1Util.getSha1(walletId + currentTime);
	}

	/**
	 * 生成唯一token
	 * 
	 * @return
	 * @author 陶嘉骏
	 * @date 2018年3月2日
	 */
	public static String buildToken() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString().replaceAll("-", "");
	}

	/**
	 * 签名
	 * @param req
	 * @return
	 * @throws Exception
	 * @author 陶嘉骏
	 * @date 2018年3月20日
	 */
	public static String toSign(Serializable req,String secret) {

		Map<String, Object> map = BeanUtils.convertBeanToMap(req);
		// 首字母升序
		List<Entry<String, Object>> maps = MapUtils.sort(map);
		// 组合待签名字符串
		StringBuffer sb = new StringBuffer();
		for (Map.Entry<String, Object> mapping : maps) {
			if (mapping.getKey().equals("sign")) {
				continue;
			}
			System.out.println(mapping.getKey());
			sb.append(mapping.getKey()).append("=").append(mapping.getValue()).append("&");
		}
		sb.append("key="+secret);
		// 签名
		String sign = MD5Utils.MD5Encode(sb.toString());
		System.out.println("sign="+sign);
		return sign;
	}
}
