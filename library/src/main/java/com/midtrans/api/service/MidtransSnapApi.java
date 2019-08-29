package com.midtrans.api.service;

import java.util.Map;

public interface MidtransSnapApi {
    String generateSnapToken(Map<String, Object> objectMap);
    String snapRedirect(Map<String, Object> objectMap);
}
