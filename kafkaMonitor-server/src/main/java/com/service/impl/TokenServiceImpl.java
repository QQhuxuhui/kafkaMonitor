package com.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import com.bean.user.UserInfo;
import com.service.TokenService;

import java.util.Date;
import java.util.Map;

/**
 * @Auther: huxuhui
 * @Date: 2019/4/19 11:07
 * @Description: 给予JWT的token生成
 */
@Service
public class TokenServiceImpl implements TokenService {

    //默认的加密key
    private final static String secret = "xiot";
    // 过期时间12小时
    private final static long EXPIRE_TIME = 12 * 60 * 60 * 1000;
//    private final static long EXPIRE_TIME = 0;

    @Override
    public String sign(UserInfo userInfo) {
        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        Algorithm algorithm;
//        if (StringUtils.isEmpty(userInfo.getSalt())) {
//            algorithm = Algorithm.HMAC256(secret);
//        } else {
//            algorithm = Algorithm.HMAC256(userInfo.getSalt());
//        }
        //当前版本采用固定秘钥
        algorithm = Algorithm.HMAC256(secret);
        // 附带username信息
        return JWT.create()
                .withClaim("username", userInfo.getUsername())
                .withExpiresAt(date)
                .sign(algorithm);
    }

    /**
     * 验证
     *
     * @param token
     * @return
     */
    @Override
    public Map<String, Claim> verify(String token) {
        if(StringUtils.isEmpty(token)){
            return  null;
        }
        Algorithm algorithm = Algorithm.HMAC256(secret);
        try {
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim("username", getUserName(token))
                    .build();
            DecodedJWT jwt;
            jwt = verifier.verify(token);
            return jwt.getClaims();
        } catch (JWTVerificationException e) {
        }
        return null;
    }

    /**
     * 获得token中的信息无需secret解密也能获得
     *
     * @return token中包含的用户名
     */
    @Override
    public String getUserName(String token) {
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getClaim("username").asString();
    }

    @Override
    public String expire(String token) {
        //时间设置为当前，即为过期
        Date date = new Date(System.currentTimeMillis());
        Algorithm algorithm;
        //当前版本采用固定秘钥
        algorithm = Algorithm.HMAC256(secret);
        // 附带username信息
        return JWT.create()
                .withClaim("username", getUserName(token))
                .withExpiresAt(date)
                .sign(algorithm);
    }
}
