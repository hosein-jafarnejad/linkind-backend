package com.hosmos.linkind.services;

import com.hosmos.linkind.dao.LinkMapper;
import com.hosmos.linkind.models.Link;
import com.hosmos.linkind.utils.LinkShortener;
import org.apache.ibatis.session.SqlSessionException;
import org.postgresql.util.PSQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Component
public class LinkServiceImpl implements LinkService {
    private final Logger logger = LoggerFactory.getLogger(getClass());


    private LinkMapper linkMapper;

    @Autowired
    public void setLinkMapper(LinkMapper linkMapper) {
        this.linkMapper = linkMapper;
    }

    @Override
    @Transactional
    public void save(String address) {
        logger.trace("START...");

        Link link = new Link();
        // TODO replace with spring security context user id
        link.setOwner(1);
        link.setUrl(address);
        link.setShort_url(LinkShortener.makeShort());
        link.setCreation_date(new Date());
        try {
            linkMapper.save(link);
        } catch (DuplicateKeyException e) {
            if (e.getCause().getMessage().contains("u_short_url")) {
                logger.trace(String.format("DuplicateKeyException: u_short_url. [short_url: %s]", link.getShort_url()));
                this.save(address);
            }
        } catch (DataIntegrityViolationException e2) {
            if (e2.getCause().getMessage().contains("f_owner")) {
                logger.error(String.format("DataIntegrityViolationException: f_owner. [owner_id: %s]", link.getOwner()));
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
        } finally {
            logger.trace("END...");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Link get(int id) {
        logger.trace(String.format("START... [ID: %d]", id));
        Link link = linkMapper.getWithId(id);

        if (link == null) {
            logger.trace("Link does not exist.");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        logger.trace("Link fetched. END...");
        System.out.println("------------------------------------ LinkServiceImpl " + link.getUrl());

        return link;
    }

    @Override
    @Transactional(readOnly = true)
    public String get(String shortUrl) {
        logger.trace(String.format("START... [SHORT_URL: %s]", shortUrl));
        String address = linkMapper.getWithShortUrl(shortUrl);

        if (address == null) {
            logger.trace("Link does not exist.");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        logger.trace("Address fetched. END...");
        return address;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Link> getLinks(int page, int rowsPerPage) {
        int offset;

        if (page == 1) {
            offset = 0;
        } else {
            offset = page * rowsPerPage;
        }

        return linkMapper.getLinks(rowsPerPage, offset);
    }

    @Override
    public void delete(int id) {
        if (linkMapper.delete(id) == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
