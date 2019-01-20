package br.com.uilian.urlshortener.shortener;

public class UrlShortenerVO {

    private String url;
    private String id;

    UrlShortenerVO(){

    }

    UrlShortenerVO(String url){
        this.url = url;
    }

    UrlShortenerVO(String url, String id){
        this.id = id;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String urlOriginal) {
        this.url = urlOriginal;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "UrlShortenerVO{" + ", id='" + id+ '\'' + ", url=" + url + "}";
    }
}
