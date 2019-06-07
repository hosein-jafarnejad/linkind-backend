package com.hosmos.linkind.dao.dynamic;

import java.util.Map;

public class TagDynamicSql {

    public String selectExistingTags (Map<String, Object> params) {
        return String.format("SELECT * FROM TAGS WHERE OWNER = %s AND NAME IN (%s)", params.get("owner"), params.get("tags"));
    }

    public String deleteTags (Map<String, Object> params) {
        return String.format("DELETE FROM TAGS WHERE OWNER = %s AND NAME IN (%s)", params.get("owner"), params.get("tags"));
    }

}
