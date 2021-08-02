package xin.allonsy.utils.mybatis;

import lombok.Data;


@Data
public class SqlLogVO {

    private String prepareSqlStr;

    private String parameterStr;

    private String completeSqlStr;

}
