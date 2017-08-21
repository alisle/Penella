package org.penella;

/**
 * Created by alisle on 8/21/17.
 */
public enum Errors {
    STORE_ADD_EXECUTION_ERROR(-1001, "Execution Problem while adding hash"),
    STORE_ADD_IO_ERROR(-1002, "")
    ;

    private final int errorno;
    private final String description;

    // TODO: We should load the strings using a json file which can have different languages
    Errors(int errorno, String description) {
        this.errorno = errorno;
        this.description = description;
    }

    public int getErrorno() {
        return errorno;
    }

    public String getDescription() {
        return description;
    }
}
