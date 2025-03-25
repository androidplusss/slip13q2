package com.example.slip13q2stud;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
public class MainActivity extends AppCompatActivity {
    private EditText teacherNameEditText;
    private Button showStudentsButton;
    private TextView tvStudentDetails;
    private DatabaseHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        teacherNameEditText = findViewById(R.id.teacherNameEditText);
        showStudentsButton = findViewById(R.id.showStudentsButton);
        tvStudentDetails = findViewById(R.id.tvStudentDetails);
        dbHelper = new DatabaseHelper(this);
        insertSampleData();
        showStudentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStudents();
            }
        });
    }
    private void insertSampleData() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM Teacher", null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        if (count > 0) {
            return;
        }
        dbHelper.insertTeacher("Mr. Sharma", "M.Sc. Mathematics", 10);
        dbHelper.insertTeacher("Ms. Verma", "M.A. English", 8);
        dbHelper.insertStudent("Rahul", "10th", "Delhi");
        dbHelper.insertStudent("Priya", "12th", "Mumbai");
        dbHelper.insertStudent("Karan", "10th", "Pune");
        dbHelper.insertStudentTeacher(1, 1, "Mathematics");
        dbHelper.insertStudentTeacher(1, 3, "Mathematics");
        dbHelper.insertStudentTeacher(2, 2, "English");
    }
    private void showStudents() {
        String teacherName = teacherNameEditText.getText().toString().trim();
        if (teacherName.isEmpty()) {
            Toast.makeText(this, "Please enter a teacher name", Toast.LENGTH_SHORT).show();
            return;
        }
        Cursor cursor = dbHelper.getStudentsByTeacherName(teacherName);
        StringBuilder studentDetails = new StringBuilder();
        if (cursor.moveToFirst()) {
            do {
                String studentName = cursor.getString(0);
                String subject = cursor.getString(1);
                studentDetails.append("Student: ").append(studentName)
                        .append(" | Subject: ").append(subject).append("\n");
            } while (cursor.moveToNext());
        } else {
            studentDetails.append("No students found for ").append(teacherName);
        }
        cursor.close();
        tvStudentDetails.setText(studentDetails.toString());
    }
    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}
