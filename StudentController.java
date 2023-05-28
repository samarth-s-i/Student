import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StudentController {
    private static final String STUDENT_DETAILS_CSV = "student_details.csv";

    @GetMapping("/students")
    public List<Student> getStudents(
            @RequestParam("page") int page,
            @RequestParam("pageSize") int pageSize
    ) throws IOException {
        // Read the student details from the CSV file
        FileReader fileReader = new FileReader(STUDENT_DETAILS_CSV);
        CSVParser csvParser = CSVFormat.DEFAULT.withHeader().parse(fileReader);
        List<Student> studentDetails = new ArrayList<>();

        int startIndex = (page - 1) * pageSize;
        int endIndex = startIndex + pageSize;

        int recordIndex = 0;
        for (CSVRecord record : csvParser) {
            if (recordIndex >= startIndex && recordIndex < endIndex) {
                // Extract the student details from the CSV record and create a Student object
                int id = Integer.parseInt(record.get("id"));
                String name = record.get("name");
                int totalMarks = Integer.parseInt(record.get("total_marks"));

                Student student = new Student(id, name, totalMarks);
                studentDetails.add(student);
            }
            recordIndex++;
        }

        csvParser.close();
        fileReader.close();

        return studentDetails;
    }
}
