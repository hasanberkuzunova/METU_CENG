import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
public class Tatec {
    private static final int CORRECT_TOTAL_TOKEN_PER_STUDENT = 100;
    private static final String OUT_TATEC_UNHAPPY = "unhappyOutTATEC.txt";
    private static final String OUT_TATEC_ADMISSION = "admissionOutTATEC.txt";
    private static final String OUT_RAND_UNHAPPY = "unhappyOutRANDOM.txt";
    private static final String OUT_RAND_ADMISSION = "admissionOutRANDOM.txt";

    public static void main(String args[]) throws IOException {
        if (args.length < 4) {
            System.err.println("Not enough arguments!");
            return;
        }

        // File Paths
        String courseFilePath = args[0];
        String studentIdFilePath = args[1];
        String tokenFilePath = args[2];
        double h;

        try {
            h = Double.parseDouble(args[3]);
        } catch (NumberFormatException ex) {
            System.err.println("4th argument is not a double!");
            return;
        }


        List<Course> courses = Files.lines(Paths.get(courseFilePath))
                .map(line -> line.split(","))
                .map(parts -> new Course(parts[0].trim(), Integer.parseInt(parts[1].trim())))
                .collect(Collectors.toList());


        List<String> studentIds = Files.readAllLines(Paths.get(studentIdFilePath))
                .stream()
                .map(String::trim)
                .collect(Collectors.toList());

        List<Student> students = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(tokenFilePath));
            IntStream.range(0, lines.size())
                    .forEach(i -> {
                        String[] parts = lines.get(i).split(",");
                        String id = studentIds.get(i);
                        List<Token> tokens = IntStream.range(0, parts.length)
                                .mapToObj(j -> {
                                    int value = Integer.parseInt(parts[j].trim());
                                    if (value > 0) {
                                        return new Token(courses.get(j).code, value);
                                    } else {
                                        return null;
                                    }
                                })
                                .filter(Objects::nonNull)
                                .collect(Collectors.toList());
                        students.add(new Student(id, tokens));
                    });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        students.stream()
                .filter(s -> s.tokens.stream().mapToInt(t -> t.value).sum() != CORRECT_TOTAL_TOKEN_PER_STUDENT)
                .findAny()
                .ifPresentOrElse(
                        s -> System.err.println("Error: student " + s.id + " has a total of " + s.tokens.stream().mapToInt(t -> t.value).sum() + " tokens, expected " + CORRECT_TOTAL_TOKEN_PER_STUDENT),
                        () -> {
                        }
                );
        tatecAlgorithm(courses, students, h, studentIds);
        randomAlgorithm(courses, students, h, studentIds);

    }

    public static void randomAlgorithm(List<Course> courses, List<Student> students, double h, List<String> studentIds) throws IOException {
        students.forEach(s -> s.tokens.forEach(t -> t.setSuccessful(false)));
        Map<String, List<String>> courseToStudentsMap = courses.stream()
                .collect(Collectors.toMap(
                        c -> c.code,
                        c -> {
                            List<Student> studentsWithTokenForCourse = students.stream()
                                    .filter(s -> s.tokens.stream().anyMatch(t -> t.code.equals(c.code)))
                                    .collect(Collectors.toList());
                            Collections.shuffle(studentsWithTokenForCourse);
                            return studentsWithTokenForCourse.stream()
                                    .peek(s -> s.tokens.stream()
                                            .filter(t -> t.code.equals(c.code))
                                            .forEach(t -> t.setSuccessful(true)))
                                    .limit(c.capacity)
                                    .map(s -> s.id)
                                    .collect(Collectors.toList());
                        }
                ));


        BufferedWriter bw = new BufferedWriter(new FileWriter(OUT_RAND_ADMISSION));


        courses.stream()
                .forEach(c -> {
                    String course = c.code;
                    List<String> studentsInCourse = courseToStudentsMap.get(course);
                    if (studentsInCourse.isEmpty()) {
                        try {
                            bw.write(course);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        try {
                            bw.newLine();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        try {
                            bw.write(course + ", " + String.join(", ", studentsInCourse));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        try {
                            bw.newLine();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });


        bw.close();

        try (PrintWriter writer = new PrintWriter(OUT_RAND_UNHAPPY)) {
            double totalUnhappiness = students.stream()
                    .mapToDouble(student -> {
                        double unhappiness = student.tokens.stream()
                            .filter(Token::isSuccessful)
                            .mapToDouble(token -> Math.min((-100 / h) * Math.log(1 - (double) token.value / 100), 100))
                            .sum();
                        if (student.tokens.stream().noneMatch(Token::isNotSuccessful)) {
                        unhappiness = unhappiness * unhappiness;
                        }
                        return unhappiness;
                    })
                    .sum();
            double averageUnhappiness = totalUnhappiness / students.size();
            writer.println(averageUnhappiness);

            studentIds.forEach(id -> {
                Student student = students.stream().filter(s -> s.id.equals(id)).findFirst().orElse(null);
                if (student != null) {
                    double unhappiness = student.tokens.stream()
                            .filter(Token::isSuccessful)
                            .mapToDouble(token -> Math.min((-100 / h) * Math.log(1 - (double) token.value / 100), 100))
                            .sum();
                    if (student.tokens.stream().noneMatch(Token::isNotSuccessful)) {
                        unhappiness = unhappiness * unhappiness;
                    }
                    writer.println(unhappiness);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void tatecAlgorithm(List<Course> courses, List<Student> students, double h, List<String> studentIds) throws IOException {
        Map<String, List<String>> courseToStudentsMap = courses.stream()
                .collect(Collectors.toMap(
                        c -> c.code,
                        c -> students.stream()
                                .sorted((s1, s2) -> {
                                    int t1 = s1.tokens.stream().filter(t -> t.code.equals(c.code)).mapToInt(t -> t.value).sum();
                                    int t2 = s2.tokens.stream().filter(t -> t.code.equals(c.code)).mapToInt(t -> t.value).sum();
                                    return t2 - t1;
                                })
                                .filter(s -> s.tokens.stream().anyMatch(t -> t.code.equals(c.code)))
                                .peek(s -> s.tokens.stream()
                                        .filter(t -> t.code.equals(c.code))
                                        .forEach(t -> t.setSuccessful(true)))
                                .limit(c.capacity)
                                .sorted((s1, s2) -> s1.id.compareTo(s2.id))
                                .map(s -> s.id)
                                .collect(Collectors.toList())
                ));

        courses.stream()
                .forEach(cr -> {
                    String courseCode = cr.code;
                    List<String> studentsInCourse = courseToStudentsMap.get(courseCode);
                    if (studentsInCourse != null && !studentsInCourse.isEmpty()) {
                        studentsInCourse.sort((s1, s2) -> {
                            int t1 = students.stream()
                                    .filter(s -> s.id.equals(s1))
                                    .flatMap(s -> s.tokens.stream())
                                    .filter(t -> t.code.equals(courseCode))
                                    .mapToInt(t -> t.value)
                                    .sum();
                            int t2 = students.stream()
                                    .filter(s -> s.id.equals(s2))
                                    .flatMap(s -> s.tokens.stream())
                                    .filter(t -> t.code.equals(courseCode))
                                    .mapToInt(t -> t.value)
                                    .sum();
                            return t2 - t1;
                        });
                        String lastStudentId = studentsInCourse.get(studentsInCourse.size() - 1);
                        int minTokensToAssign = students.stream()
                                .filter(s -> s.id.equals(lastStudentId))
                                .flatMap(s -> s.tokens.stream())
                                .filter(t -> t.code.equals(courseCode))
                                .mapToInt(t -> t.value)
                                .sum();
                        cr.setMinToken(minTokensToAssign);

                    }
                });


        courses.stream()
                .forEach(c -> {
                    String courseCode = c.code;
                    List<String> studentsInCourse = courseToStudentsMap.get(courseCode);
                    students.stream()
                            .filter(s -> s.tokens.stream().filter(t -> t.code.equals(courseCode)).mapToInt(t -> t.value).sum() == c.minToken)
                            .filter(s -> !studentsInCourse.contains(s.id))
                            .forEach(s -> {
                                studentsInCourse.add(s.id);
                                s.tokens.stream()
                                        .filter(t -> t.code.equals(courseCode))
                                        .forEach(t -> t.setSuccessful(true));
                            });
                });


        BufferedWriter bw = new BufferedWriter(new FileWriter(OUT_TATEC_ADMISSION));

        courses.stream()
                .forEach(c -> {
                    String course = c.code;
                    List<String> studentsInCourse = courseToStudentsMap.get(course);
                    if (studentsInCourse.isEmpty()) {
                        try {
                            bw.write(course);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        try {
                            bw.newLine();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        try {
                            bw.write(course + ", " + String.join(", ", studentsInCourse));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        try {
                            bw.newLine();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });


        bw.close();


        try (PrintWriter writer = new PrintWriter(OUT_TATEC_UNHAPPY)) {
           double totalUnhappiness = students.stream()
                    .mapToDouble(student -> {
                        double unhappiness = student.tokens.stream()
                            .filter(Token::isSuccessful)
                            .mapToDouble(token -> Math.min((-100 / h) * Math.log(1 - (double) token.value / 100), 100))
                            .sum();
                        if (student.tokens.stream().noneMatch(Token::isNotSuccessful)) {
                        unhappiness = unhappiness * unhappiness;
                        }
                        return unhappiness;
                    })
                    .sum();
            double averageUnhappiness = totalUnhappiness / students.size();
            writer.println(averageUnhappiness);
            studentIds.forEach(id -> {
                Student student = students.stream().filter(s -> s.id.equals(id)).findFirst().orElse(null);
                if (student != null) {
                    double unhappiness = student.tokens.stream()
                            .filter(Token::isSuccessful)
                            .mapToDouble(token -> Math.min((-100 / h) * Math.log(1 - (double) token.value / 100), 100))
                            .sum();
                    if (student.tokens.stream().noneMatch(Token::isNotSuccessful)) { //check
                        unhappiness = unhappiness * unhappiness;
                    }
                    writer.println(unhappiness);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}



