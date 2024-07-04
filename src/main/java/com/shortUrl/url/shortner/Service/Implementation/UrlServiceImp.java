package com.shortUrl.url.shortner.Service.Implementation;

import com.shortUrl.url.shortner.Model.UrlModel;
import com.shortUrl.url.shortner.Repository.UrlRepository;
import com.shortUrl.url.shortner.Service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class UrlServiceImp implements UrlService {

    @Autowired
    private UrlRepository urlRepository;


    @Override
    public Map<String, Object> createShortUrl(String url) {
        String shortUrl = "http://localhost:8080/";

        Map<String, Object> shortUrlMap = new HashMap<>();

        UrlModel modelProbe = UrlModel.builder().destinationUrl(url.replace(shortUrl,"")).build();
        Example<UrlModel> example = Example.of(modelProbe);
        Optional<UrlModel> checkUrlModel = urlRepository.findOne(example);

        if (checkUrlModel.isPresent()) {
            UrlModel urlModel = checkUrlModel.get();
            shortUrl+="shortUrl/"+urlModel.getShortenedUrl();

            shortUrlMap.put("id",urlModel.getId());
            shortUrlMap.put("shortUrl", shortUrl);

            return shortUrlMap;
        }

        String hashingUrl = url.replace(shortUrl, "");
        String hashedUrl = null;
        try {
            hashedUrl = generateUrlHash(hashingUrl);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        LocalDateTime expireDate = LocalDateTime.now().plusMonths(10);
        UrlModel shortenedUrl = UrlModel.builder()
                .destinationUrl(hashingUrl)
                .shortenedUrl(hashedUrl)
                .expireDate(expireDate)
                .build();

        UrlModel urlModel = urlRepository.save(shortenedUrl);

        shortUrl += "shortUrl/" + hashedUrl;

        shortUrlMap.put("id",urlModel.getId());
        shortUrlMap.put("shortUrl", shortUrl);

        return shortUrlMap;
    }

    @Override
    public String fetchFullUrl(String url) {

        UrlModel modelProbe = UrlModel.builder().shortenedUrl(url).build();
        Example<UrlModel> example = Example.of(modelProbe);
        Optional<UrlModel> checkUrlModel = urlRepository.findOne(example);

        if (checkUrlModel.isPresent()) {
            UrlModel urlModel = checkUrlModel.get();
            String fullUrl = "http://localhost:8080/";
            fullUrl += urlModel.getDestinationUrl();
            return fullUrl;
        }
        return "No such mapping found";
    }

    @Override
    public Boolean updateShortUrl(String shortUrl, String destinationUrl) {

        UrlModel modelProbe = UrlModel.builder().shortenedUrl(shortUrl.replace("http://localhost:8080/","")).build();
        Example<UrlModel> example = Example.of(modelProbe);
        Optional<UrlModel> checkUrlModel = urlRepository.findOne(example);

        if (checkUrlModel.isPresent()) {
            UrlModel urlModel = checkUrlModel.get();

            if(LocalDateTime.now().isBefore(urlModel.getExpireDate())){
                return false;
            }
            urlModel.setDestinationUrl(destinationUrl);
            urlRepository.save(urlModel);

            return true;
        }

        return false;
    }

    @Override
    public Boolean updateExpireDate(String url, Long daysToExtend) {

        String shortUrl = "http://localhost:8080/";

        UrlModel modelProbe = UrlModel.builder().shortenedUrl(url.replace(shortUrl,"")).build();
        Example<UrlModel> example = Example.of(modelProbe);
        Optional<UrlModel> checkUrlModel = urlRepository.findOne(example);

        if (checkUrlModel.isPresent()) {
            UrlModel urlModel = checkUrlModel.get();
            urlModel.setExpireDate(urlModel.getExpireDate().plusDays(daysToExtend));

            urlRepository.save(urlModel);

            return true;
        }
        return false;
    }

    @Override
    public List<UrlModel> fetchAll() {
        return urlRepository.findAll();
    }

    private String generateUrlHash(String url) throws NoSuchAlgorithmException {

        url += Instant.now().toString();

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(url.getBytes(StandardCharsets.UTF_8));

        BigInteger number = new BigInteger(1, hash);
        StringBuilder hexString = new StringBuilder(number.toString(16));
        while (hexString.length() < 64) {
            hexString.insert(0, '0');
        }
        return hexString.substring(0, 30);
    }
}
