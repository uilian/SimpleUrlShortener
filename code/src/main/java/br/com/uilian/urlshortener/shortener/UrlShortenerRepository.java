package br.com.uilian.urlshortener.shortener;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UrlShortenerRepository extends CrudRepository<UrlShortener, String> {}
