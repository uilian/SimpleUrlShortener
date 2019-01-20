package br.com.uilian.urlshortener.shortener;

import com.fasterxml.jackson.databind.util.JSONPObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.util.UUID;

@Service
public class UrlShortenerService {

    private static final Logger logger = LoggerFactory.getLogger(UrlShortenerService.class);

    @Autowired
    private UrlShortenerRepository repository;

    /**
     * Returns the url corresponding to the id.
     *
     * @param id
     * @return
     */
    public UrlShortener getUrl(String id) {
        UrlShortener us = repository.findById(id).orElse(new UrlShortener());
        return  us;
    }

    /**
     * Adds a new shortened url to the database.
     * The id can be specified, but need to follow the same rules:
     * - only numbers or letters.
     * - at least 2 characteres.
     *
     * @param url       Url to be shortened.
     * @param shortId   Optional param, specified short code.
     */
    public UrlShortener addUrl(String url, String shortId) {
        if (shortId == null) {
            // Generates an unique long id
            String uuid = UrlShortenerService.md5Hash(url);

            if (uuid == null){
                return null;
            } else {
                Optional<UrlShortener> testUrl = repository.findById(uuid);
            }

            boolean shortCodeOk = false;
            int shortLength = 2;
            /*
                I wanna to generate short codes with 2 or more char, so the
                need to verify if the short id already exists, and add more
                characteres until it is unique.
                If a url was found, then returns it.
            */
            String minId = uuid.substring(0, shortLength);
            while (!shortCodeOk && shortLength <= uuid.length()) {
                minId = uuid.substring(0, shortLength);
                Optional<UrlShortener> testUrl = repository.findById(minId);
                // The code is already in use by another URL
                if (testUrl.isPresent() && !testUrl.get().getUrlOriginal().equalsIgnoreCase(url)) {
                    shortLength += 1;
                } else {
                    shortCodeOk = true;
                }
            }
            UrlShortener us = new UrlShortener(url.toLowerCase(), minId);
            repository.save(us);
            return us;
        } else if (shortId.length() >= 2 && shortId.matches("^[a-zA-Z0-9]*$")) {
            Optional<UrlShortener> ous = repository.findById(shortId);
            if (ous.isPresent()){
                return ous.get();
            } else {
                UrlShortener us = new UrlShortener(url.toLowerCase(), shortId);
                repository.save(us);
                return us;
            }
        } else {
            return null;
        }
    }

    public Long countRecords() {
        Long numberOfRecords = repository.count();
        return numberOfRecords;
    }


    /**
     * Hash generation using MD5, same output given the same input.
     *
     * @param url
     * @return
     */
    private static String md5Hash(String url) {
        String result = "";

        if (null == url)
            return null;

        url= url + "URLSHORTENER";//adding a salt to the string before it gets hashed.
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(url.getBytes(), 0, url.length());
            result= new BigInteger(1, digest.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Simple random hash generation using UUID.
     *
     * @param url
     * @return
     */
    private static String uuidHash(String url) {
        String result = UUID.randomUUID().toString().replace("-", "");
        return result;
    }

}