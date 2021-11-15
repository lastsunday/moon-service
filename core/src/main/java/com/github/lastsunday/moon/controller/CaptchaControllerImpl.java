package com.github.lastsunday.moon.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.lastsunday.moon.constant.Constants;
import com.github.lastsunday.moon.controller.dto.CaptchaResultDTO;
import com.github.lastsunday.moon.data.component.CacheComponent;
import com.github.lastsunday.moon.util.IdGenerator;
import com.github.lastsunday.moon.util.VerifyCodeUtils;

@RestController
@RequestMapping("/api/captcha")
public class CaptchaControllerImpl implements CaptchaController {

	private static final Logger log = LoggerFactory.getLogger(CaptchaController.class);

	@Autowired
	private CacheComponent cacheComponent;

	@Override
	@GetMapping
	public CaptchaResultDTO getCode() throws IOException {
		// 生成随机字串
		String verifyCode = VerifyCodeUtils.generateVerifyCode(4);
		// 唯一标识
		String uuid = IdGenerator.genUniqueStringId();
		String verifyKey = Constants.CAPTCHA_CODE_KEY + uuid;
		cacheComponent.putRaw(verifyKey, verifyCode, Constants.CAPTCHA_EXPIRATION.intValue() * 60);
		// 生成图片
		int w = 111, h = 36;
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		try {
			VerifyCodeUtils.outputImage(w, h, stream, verifyCode);
			CaptchaResultDTO result = new CaptchaResultDTO();
			result.setUuid(uuid);
			result.setImg(Base64.getEncoder().encodeToString((stream.toByteArray())));
			return result;
		} finally {
			try {
				stream.close();
			} catch (IOException e) {
				log.error(e.getMessage(), e);
			}
		}
	}

}
