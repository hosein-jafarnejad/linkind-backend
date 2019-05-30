package com.hosmos.linkind.dao;

import com.hosmos.linkind.models.Link;
import com.hosmos.linkind.models.Visit;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.dao.DuplicateKeyException;

import java.util.List;

public interface LinkMapper {
    @Insert("INSERT INTO " +
            "LINKS(ID, URL, OWNER, SHORT_URL) " +
            "VALUES(nextval('SLINKS'), #{link.url}, #{link.owner}, #{link.short_url})")
    void save(@Param("link") Link link) throws DuplicateKeyException;

    @Select("SELECT * FROM LINKS WHERE ID = #{id}")
    Link getWithId(@Param("id") int id);

    @Select("SELECT ID,URL FROM LINKS WHERE SHORT_URL = #{short_url}")
    Link getWithShortUrl(@Param("short_url") String shortUrl);

    @Delete("DELETE FROM LINKS WHERE ID = #{id}")
    int delete(@Param("id") int id);

    @Select("SELECT" +
            " ID," +
            " URL," +
            " SHORT_URL," +
            " CREATION_DATE"+
            " FROM LINKS ORDER BY CREATION_DATE LIMIT #{limit} OFFSET #{offset}")
    List<Link> getLinks(@Param("limit") int limit, @Param("offset") int offset);

    @Insert("INSERT INTO VISITS(IP, LINK_ID, BROWSER_NAME, BROWSER_VERSION, OS) VALUES(#{visit.ip}, #{visit.link_id}, #{visit.browser_name}, #{visit.browser_version}, #{visit.os})")
    void saveVisit (@Param("visit") Visit visit);
}
