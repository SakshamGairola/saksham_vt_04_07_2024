package com.shortUrl.url.shortner.controller;

import com.shortUrl.url.shortner.Model.UrlModel;
import com.shortUrl.url.shortner.Service.UrlService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
public class UrlController {

    @Autowired
    UrlService urlService;



    @GetMapping("/shortUrl/{shortenString}")
    public void redirectToFullUrl(HttpServletResponse response, @PathVariable String shortenString) {
        try {
            // get the full url from csv and use the below lines for redirection
            String fullUrl = urlService.fetchFullUrl(shortenString);
            response.sendRedirect(fullUrl);
        } catch (NoSuchElementException e) {

            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Url not found", e);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not redirect to the full url", e);
        }
    }

    @PostMapping("/")
    public Map<String, Object> createShortUrl(@RequestBody Map<String, Object> requestBody){
        return urlService.createShortUrl(requestBody.get("destinationUrl").toString());
    }

    @PostMapping("/getDestinationUrl")
    public String getDestinationUrl(@RequestBody Map<String, Object> requestBody){
        String shortUrl = requestBody.get("shortUrl").toString().replace("http://localhost:8080/shortUrl/", "");
        return urlService.fetchFullUrl(shortUrl);
    }

    @PatchMapping ("/update")
    Boolean updateShortUrl(@RequestBody Map<String, Object> requestBody){

        String shortUrl = requestBody.get("shortUrl").toString().replace("http://localhost:8080/shortUrl/", "");
        String destinationUrl = requestBody.get("destinationUrl").toString().replace("http://localhost:8080/", "");

        return urlService.updateShortUrl(shortUrl, destinationUrl);
    }
    @PatchMapping ("/extend")
    Boolean updateExpiry (@RequestBody Map<String, Object> requestBody){

        String shortUrl = requestBody.get("shortUrl").toString().replace("http://localhost:8080/shortUrl/", "");
        Long daysToExtend = Long.parseLong(requestBody.get("daysToExtend").toString());

        return urlService.updateExpireDate(shortUrl, daysToExtend);
    }

    @GetMapping("/all")
    public List<UrlModel> all(){
        return urlService.fetchAll();
    }

    @GetMapping("**")
    public String Hello(HttpServletRequest request){
        String requestUrl = request.getRequestURL().toString();
        return "Handling request for URL: <b>" + requestUrl + "</b>";
    }


}
