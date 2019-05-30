package com.hosmos.linkind.services;

import com.hosmos.linkind.models.Link;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("linkService")
public interface LinkService {
    void save(String address);

    Link get(int id);

    String get(String shortUrl);

    List<Link> getLinks(int page, int rowsPerPage);

    void delete(int id);

    String visit(String url, String remoteAddr, String userAgent);
}
