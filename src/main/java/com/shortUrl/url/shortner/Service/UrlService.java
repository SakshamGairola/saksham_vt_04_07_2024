package com.shortUrl.url.shortner.Service;

import com.shortUrl.url.shortner.Model.UrlModel;

import java.util.List;
import java.util.Map;

public interface UrlService {

    Map<String, Object> createShortUrl(String url);

    String fetchFullUrl(String url);

    Boolean updateShortUrl(String shortUrl, String destinationUrl);

    Boolean updateExpireDate(String shortUrl, Long daysToExtend);

    List<UrlModel> fetchAll();
}
