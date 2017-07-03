package com.jojoldu.case1;

/**
 * Created by jojoldu@gmail.com on 2017. 6. 24.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

public enum TableStatus {

    Y("1", true),
    N("0", false);

    private String table1Value;
    private boolean table2Value;

    TableStatus(String table1Value, boolean table2Value) {
        this.table1Value = table1Value;
        this.table2Value = table2Value;
    }

    public String getTable1Value() {
        return table1Value;
    }

    public boolean isTable2Value() {
        return table2Value;
    }

}
