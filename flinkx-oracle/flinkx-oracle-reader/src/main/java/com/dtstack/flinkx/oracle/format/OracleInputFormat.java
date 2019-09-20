package com.dtstack.flinkx.oracle.format;

import com.dtstack.flinkx.rdb.inputformat.JdbcInputFormat;
import org.apache.flink.types.Row;

import java.io.IOException;

import static com.dtstack.flinkx.rdb.util.DBUtil.clobToString;

/**
 * Date: 2019/09/19
 * Company: www.dtstack.com
 *
 * @author tudou
 */
public class OracleInputFormat extends JdbcInputFormat {

    @Override
    public Row nextRecordInternal(Row row) throws IOException {
        if (!hasNext) {
            return null;
        }
        row = new Row(columnCount);

        try {
            for (int pos = 0; pos < row.getArity(); pos++) {
                Object obj = resultSet.getObject(pos + 1);
                if(obj != null) {
                    if((obj instanceof java.util.Date || obj.getClass().getSimpleName().toUpperCase().contains("TIMESTAMP")) ) {
                        obj = resultSet.getTimestamp(pos + 1);
                    }
                    obj = clobToString(obj);
                }

                row.setField(pos, obj);
            }
            return super.nextRecordInternal(row);
        }catch (Exception e) {
            throw new IOException("Couldn't read data - " + e.getMessage(), e);
        }
    }
}
