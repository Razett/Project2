package com.y2021s2.project2;

public final class DB {

    /**
     * It was implemented through the inner class for ease of DB table management.
     */
    public static final class ClassDB {
        public static final String TABLE_NAME = "Class";
        public static final String COLUMN_TITLE_KEY = "Key";
        public static final String COLUMN_TITLE_NAME = "Name";
        public static final String COLUMN_TITLE_AGE = "Age";
        public static final String COLUMN_TITLE_DEPT = "Dept";
        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + ClassDB.TABLE_NAME + " (" +
                ClassDB.COLUMN_TITLE_KEY + " INTEGER PRIMARY KEY," +
                ClassDB.COLUMN_TITLE_NAME + " TEXT," +
                ClassDB.COLUMN_TITLE_AGE + " INTEGER," +
                ClassDB.COLUMN_TITLE_DEPT + " TEXT)";
        public static final String SQL_DELETE_TABLE =
                "DROP TABLE IF EXISTS " + ClassDB.TABLE_NAME;
    }
}
