package org.example.base62.service;

import org.example.base62.dao.UrlDao;
import org.example.base62.dto.shortenRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class UrlServiceImpl implements UrlService {
    private static final String BASE_URL = "http://localhost:8080/";
    private static final Logger log = LoggerFactory.getLogger(UrlServiceImpl.class);

    @Autowired
    UrlDao urlDao;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${kgs.url}")
    private String kgsUrl;

    //長網址轉短網址主要邏輯
    @Override
    public String shortenUrl(shortenRequest shortenRequest){

        String existingShortUrl = urlDao.findLongUrl(shortenRequest.getLongUrl());
        if (existingShortUrl != null) {
            log.info("Short URL for this long URL already exists in the database.");
            return existingShortUrl;
        }

        String shortKey = getShortKeyFromCache();
        if (shortKey == null) {
            throw new RuntimeException("No available keys in cache");
        }

        String shortUrl = BASE_URL+ shortKey;

        //確認資料庫是否已有該長網址轉換之結果
        String findLongUrlResult= urlDao.findLongUrl(shortenRequest.getLongUrl());
        if( findLongUrlResult != null ){
            return findLongUrlResult;
        }


        //若無儲存網址轉換結果
        if(urlDao.saveUrlInfo(shortenRequest.getLongUrl(),shortUrl,shortenRequest.getTtl()) != null){
            return shortUrl;
        }
        return null;
    }

    @Override
    public String getLongUrl(String shortKey) {
        // 兩種結果 1.longUrl(成功取得 url) 2.null(沒有該url)
        log.info(BASE_URL+shortKey);

        return urlDao.findLongUrlByShortUrl(BASE_URL + shortKey);
    }

    private String getShortKeyFromCache() {
        Set<String> keys = redisTemplate.keys("*");
        if (keys != null && !keys.isEmpty()) {
            String shortKey = keys.iterator().next();
            redisTemplate.delete(shortKey);
            return shortKey;
        }
        return null;
    }

}