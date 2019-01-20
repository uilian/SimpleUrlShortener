package br.com.uilian.urlshortener.redis;

public interface MessagePublisher {

    void publish(final String message);
}
