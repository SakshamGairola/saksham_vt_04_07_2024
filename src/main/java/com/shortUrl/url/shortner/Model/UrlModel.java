package com.shortUrl.url.shortner.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "url")
public class UrlModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private UUID id;

    @Column(name="destinationUrl")
    private String destinationUrl;

    @Column(name="shortenedUrl")
    private String shortenedUrl;

    @Column(name="expireDate")
    private LocalDateTime expireDate;
}