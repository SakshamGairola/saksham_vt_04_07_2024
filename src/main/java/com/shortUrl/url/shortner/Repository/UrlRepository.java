package com.shortUrl.url.shortner.Repository;


import com.shortUrl.url.shortner.Model.UrlModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
public interface UrlRepository extends JpaRepository<UrlModel, UUID> {

    @Transactional
    @Modifying (clearAutomatically = true, flushAutomatically = true)
    @Query(value = "update url set destination_url = ?1 where shortened_url = ?2 ", nativeQuery = true)
    int updateDestinationUrlForShortUrl(String destinationUrl, String shortUrl);

}
