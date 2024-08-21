package com.example.kgs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class KeyGenerationService {

    private static final int MIN_KEY_THRESHOLD = 100;
    private static final int KEYS_TO_GENERATE = 50;

    @Autowired
    private KeyGenerationRepository keyGenerationRepository;

    @Autowired
    private CacheService cacheService;

    public String generateKey() {
        long id = keyGenerationRepository.generateUniqueId();
        String generatedKey = Base62EncoderDecoder.encode(id);

        keyGenerationRepository.saveGeneratedKey(id, generatedKey);
        cacheService.saveKeyToCache(generatedKey);

        checkAndGenerateKeysIfNeeded();

        return generatedKey;
    }

    private void checkAndGenerateKeysIfNeeded() {
        long keyCount = cacheService.getKeyCount();
        if (keyCount < MIN_KEY_THRESHOLD) {
            generateAndCacheKeys(KEYS_TO_GENERATE);
        }
    }

    private void generateAndCacheKeys(int keysToGenerate) {
        for (int i = 0; i < keysToGenerate; i++) {
            long id = keyGenerationRepository.generateUniqueId();
            String generatedKey = Base62EncoderDecoder.encode(id);

            keyGenerationRepository.saveGeneratedKey(id, generatedKey);
            cacheService.saveKeyToCache(generatedKey);
        }
    }

    @Scheduled(fixedRate = 10000)
    public void scheduledKeyCheck() {
        checkAndGenerateKeysIfNeeded();
    }
}

