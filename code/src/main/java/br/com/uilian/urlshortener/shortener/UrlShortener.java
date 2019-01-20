package br.com.uilian.urlshortener.shortener;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@RedisHash("UrlShortener")
public class UrlShortener implements Serializable {

    @JsonProperty("id")
    private String id;
    @JsonProperty("url")
    private String urlOriginal;

    UrlShortener(){

    }

    UrlShortener(String url){
        this.urlOriginal = url;
    }

    UrlShortener(String url, String id){
        this.id = id;
        this.urlOriginal = url;
    }

    public String getUrlOriginal() {
        return urlOriginal;
    }

    public void setUrlOriginal(String urlOriginal) {
        this.urlOriginal = urlOriginal;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "UrlShortener{" + "id='" + id + '\'' + ", urlOriginal=" + urlOriginal + "}";
    }
}

