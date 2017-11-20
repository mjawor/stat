package pl.jwr.stat.services.dto;

import java.util.List;

/**
 * Created by mjawor on 2017-04-27.
 */
public class DataTable {
    private List<String> keyColumn;
    private List<String> valueColumn;

    public List<String> getKeyColumn() {
        return keyColumn;
    }

    public List<String> getValueColumn() {
        return valueColumn;
    }

    public void setKeyColumn(List<String> keyColumn) {
        this.keyColumn = keyColumn;
    }

    public void setValueColumn(List<String> valueColumn) {
        this.valueColumn = valueColumn;
    }
}
