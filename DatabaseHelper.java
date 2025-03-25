package com.example.slip13q2stud;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "SchoolDB.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_STUDENT = "Student";
    public static final String COLUMN_SNO = "sno";
    public static final String COLUMN_SNAME = "s_name";
    public static final String COLUMN_SCLASS = "s_class";
    public static final String COLUMN_SADDR = "s_addr";
    public static final String TABLE_TEACHER = "Teacher";
    public static final String COLUMN_TNO = "tno";
    public static final String COLUMN_TNAME = "t_name";
    public static final String COLUMN_QUALIFICATION = "qualification";
    public static final String COLUMN_EXPERIENCE = "experience";
    public static final String TABLE_STUDENT_TEACHER = "StudentTeacher";
    public static final String COLUMN_SUBJECT = "subject";
    private static final String CREATE_STUDENT_TABLE = "CREATE TABLE " + TABLE_STUDENT + " (" +
            COLUMN_SNO + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_SNAME + " TEXT, " +
            COLUMN_SCLASS + " TEXT, " +
            COLUMN_SADDR + " TEXT)";
    private static final String CREATE_TEACHER_TABLE = "CREATE TABLE " + TABLE_TEACHER + " (" +
            COLUMN_TNO + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_TNAME + " TEXT, " +
            COLUMN_QUALIFICATION + " TEXT, " +
            COLUMN_EXPERIENCE + " INTEGER)";
    private static final String CREATE_STUDENT_TEACHER_TABLE = "CREATE TABLE " + TABLE_STUDENT_TEACHER + " (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_TNO + " INTEGER, " +
            COLUMN_SNO + " INTEGER, " +
            COLUMN_SUBJECT + " TEXT, " +
            "FOREIGN KEY(" + COLUMN_TNO + ") REFERENCES " + TABLE_TEACHER + "(" + COLUMN_TNO + "), " +
            "FOREIGN KEY(" + COLUMN_SNO + ") REFERENCES " + TABLE_STUDENT + "(" + COLUMN_SNO + "))";
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void insertTeacher(String name, String qualification, int experience) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TNAME, name);
        values.put(COLUMN_QUALIFICATION, qualification);
        values.put(COLUMN_EXPERIENCE, experience);
        db.insert(TABLE_TEACHER, null, values);
        db.close();
    }
    public void insertStudent(String name, String sClass, String address) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SNAME, name);
        values.put(COLUMN_SCLASS, sClass);
        values.put(COLUMN_SADDR, address);
        db.insert(TABLE_STUDENT, null, values);
        db.close();
    }
    public void insertStudentTeacher(int tno, int sno, String subject) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TNO, tno);
        values.put(COLUMN_SNO, sno);
        values.put(COLUMN_SUBJECT, subject);
        db.insert(TABLE_STUDENT_TEACHER, null, values);
        db.close();
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_STUDENT_TABLE);
        db.execSQL(CREATE_TEACHER_TABLE);
        db.execSQL(CREATE_STUDENT_TEACHER_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENT_TEACHER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEACHER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENT);
        onCreate(db);
    }
    public Cursor getStudentsByTeacherName(String teacherName) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT S." + COLUMN_SNAME + ", ST." + COLUMN_SUBJECT +
                " FROM " + TABLE_STUDENT_TEACHER + " ST " +
                " JOIN " + TABLE_STUDENT + " S ON ST." + COLUMN_SNO + " = S." + COLUMN_SNO +
                " JOIN " + TABLE_TEACHER + " T ON ST." + COLUMN_TNO + " = T." + COLUMN_TNO +
                " WHERE T." + COLUMN_TNAME + " = ?", new String[]{teacherName});
    }
}
