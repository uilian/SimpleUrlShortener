package br.com.uilian.urlshortener.shortener;

import com.sun.deploy.net.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.MalformedURLException;
import java.net.URL;

@RestController
public class UrlShortenerRestController {

    private static final Logger logger = LoggerFactory.getLogger(UrlShortenerRestController.class);

    @Autowired
    UrlShortenerService uss;

    /**
     * Registers a new URL.
     *
     * @param newUrl
     * @param res
     * @return
     */
    @RequestMapping(value = "/", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UrlShortener> addUrl(@RequestBody UrlShortenerVO newUrl, HttpServletRequest req, HttpServletResponse res) {
        UrlShortener result;

        if (newUrl == null || newUrl.getUrl() == null) {
            return new ResponseEntity("{\"message\": \"Couldn't process request.\"}", HttpStatus.BAD_REQUEST);
        }

        if (!isValidURL(newUrl.getUrl())) {
            return new ResponseEntity("{\"message\": \"Invalid URL.\"}", HttpStatus.BAD_REQUEST);
        }

        result = uss.addUrl(newUrl.getUrl(), newUrl.getId());

        if (result != null && result.getId() != null) {
            if (result.getUrlOriginal().equalsIgnoreCase(newUrl.getUrl())) {
                String requestUrl = req.getRequestURL().toString();
                String prefix = requestUrl.substring(0, requestUrl.indexOf(req.getRequestURI(),"http://".length()));
                result.setUrlOriginal(prefix+"/"+result.getId());

                return new ResponseEntity(result, HttpStatus.OK);
            } else {
                return new ResponseEntity("{\"message\": \"Invalid short id\"}", HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity("{\"message\": \"Couldn't process request.\"}",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Redirect to the original URL
     * @param uid
     * @param res
     */
    @RequestMapping(value = "/{uid}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public void redirectUrl(@PathVariable("uid") String uid, HttpServletResponse res) {
        UrlShortener result = uss.getUrl(uid);
        if (result != null && result.getUrlOriginal() != null) {
            res.setHeader("Location", result.getUrlOriginal());
            res.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
        } else {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }


    /**
     * This is the most simple and direct statistic I can provide now,
     * just counts how many urls have been shortened so far.
     *
     * TODO: Include the following statistics:
     *  - the most accessed URLS;
     *  - the most shortened URLS;
     *  - global number of accesses.
     */
    @RequestMapping(value = "/statistics", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity statistics() {
        Long numberOfRecords = uss.countRecords();
        return new ResponseEntity(
                "{\"totalUrlsShortned\": \"" + numberOfRecords +"\"}",
                HttpStatus.OK);
    }

    /**
     * Tests if the string represents a valid URL.
     *
     * @param url
     * @return
     */
    private boolean isValidURL(String url) {
        boolean result = true;

        try {
            new URL(url);
        } catch (MalformedURLException e) {
             result = false;
        }
        return result;
    }
}