import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

public class TeacherChatbotGUI {

    static ArrayList<Teacher> teacherList = new ArrayList<>();
    static ArrayList<Student> studentList = new ArrayList<>();

    // ===== Function to Capitalize First Letter =====
    public static String capitalize(String text) {
        if (text == null || text.length() == 0)
            return text;
        return text.substring(0,1).toUpperCase() + text.substring(1);
    }

    // ================= TEACHER CLASS =================
    static class Teacher {
        String name, subject, qualification, experience, email;

        Teacher(String n, String s, String q, String e, String m) {
            name = n.toLowerCase();
            subject = s;
            qualification = q;
            experience = e;
            email = m;
        }
    }

    // ================= STUDENT CLASS =================
    static class Student {
        String name, branch, year, rollno, email;

        Student(String n, String b, String y, String r, String m) {
            name = n.toLowerCase();
            branch = b;
            year = y;
            rollno = r;
            email = m;
        }
    }

    // ================= LOAD TEACHERS =================
    public static void loadTeachersFromCSV() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("teachers.csv"));
            br.readLine();
            String line;

            while ((line = br.readLine()) != null) {
                String[] d = line.split(",");
                if (d.length == 5)
                    teacherList.add(new Teacher(d[0], d[1], d[2], d[3], d[4]));
            }

            br.close();
        } catch (Exception e) {
            System.out.println("Teacher file error " + e);
        }
    }

    // ================= LOAD STUDENTS =================
    public static void loadStudentsFromCSV() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("students.csv"));
            br.readLine();
            String line;

            while ((line = br.readLine()) != null) {
                String[] d = line.split(",");
                if (d.length == 5)
                    studentList.add(new Student(d[0], d[1], d[2], d[3], d[4]));
            }

            br.close();
        } catch (Exception e) {
            System.out.println("Student file error " + e);
        }
    }

    // ================= WELCOME SCREEN =================
    public static void showWelcomeScreen() {

        JFrame frame = new JFrame("Teacher and Student Information Chatbot");
        frame.setSize(500, 450);
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel title = new JLabel("Teacher and Student Information Chatbot", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));

        ImageIcon logo = new ImageIcon("logo2.png");
        JLabel logoLabel = new JLabel(logo);
        logoLabel.setHorizontalAlignment(JLabel.CENTER);

        JButton startBtn = new JButton("START");
        startBtn.setPreferredSize(new Dimension(120, 40));

        JPanel bottom = new JPanel();
        bottom.add(startBtn);

        frame.add(title, BorderLayout.NORTH);
        frame.add(logoLabel, BorderLayout.CENTER);
        frame.add(bottom, BorderLayout.SOUTH);

        startBtn.addActionListener(e -> {
            frame.dispose();
            showChatbot();
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // ================= CHATBOT =================
    public static void showChatbot() {

        JFrame frame = new JFrame("Teacher and Student Information Chatbot");
        frame.setSize(1000, 600);
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel header = new JLabel("Teacher and Student Information Chatbot", JLabel.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 20));
        header.setOpaque(true);
        header.setBackground(new Color(30, 144, 255));
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 50));

        frame.add(header, BorderLayout.NORTH);

        // LEFT PANEL
        JPanel left = new JPanel();
        left.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 40));
        left.setPreferredSize(new Dimension(200, 0));
        left.setBackground(new Color(220, 235, 250));

        JButton teacherBtn = new JButton("Teacher Info");
        JButton studentBtn = new JButton("Student Info");
        JButton clearBtn = new JButton("Clear Chat");

        teacherBtn.setPreferredSize(new Dimension(150, 30));
        studentBtn.setPreferredSize(new Dimension(150, 30));
        clearBtn.setPreferredSize(new Dimension(150, 30));

        left.add(teacherBtn);
        left.add(studentBtn);
        left.add(clearBtn);

        frame.add(left, BorderLayout.WEST);

        // RIGHT PANEL
        JPanel right = new JPanel(new BorderLayout());

        JTextArea chatArea = new JTextArea();
        chatArea.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);

        JScrollPane scroll = new JScrollPane(chatArea);

        JPanel inputPanel = new JPanel(new BorderLayout());
        JTextField input = new JTextField();
        JButton send = new JButton("Send");

        input.setFont(new Font("Segoe UI", Font.PLAIN, 15));

        inputPanel.add(input, BorderLayout.CENTER);
        inputPanel.add(send, BorderLayout.EAST);

        right.add(scroll, BorderLayout.CENTER);
        right.add(inputPanel, BorderLayout.SOUTH);

        frame.add(right, BorderLayout.CENTER);

        final String[] mode = {"none"};

        // BUTTON ACTIONS
        teacherBtn.addActionListener(e -> {
            mode[0] = "teacher";
            chatArea.setText("Teacher Information Mode.\nAsk about Subject, Qualification, Experience or Email.\n\n");
        });

        studentBtn.addActionListener(e -> {
            mode[0] = "student";
            chatArea.setText("Student Information Mode.\nAsk about Branch, Year, Roll Number or Email.\n\n");
        });

        clearBtn.addActionListener(e -> chatArea.setText(""));

        // ================= CHAT LOGIC =================
        ActionListener sendAction = e -> {

            String q = input.getText().trim().toLowerCase();
            if (q.isEmpty()) return;

            chatArea.append("You: " + q + "\n");

            boolean found = false;

            // TEACHER MODE
            if (mode[0].equals("teacher")) {

                for (Teacher t : teacherList) {

                    if (q.contains(t.name)) {
                        found = true;

                        chatArea.append("Name: " + capitalize(t.name) + "\n");

                        if (q.contains("subject"))
                            chatArea.append("Subject: " + capitalize(t.subject) + "\n\n");
                        else if (q.contains("qualification"))
                            chatArea.append("Qualification: " + capitalize(t.qualification) + "\n\n");
                        else if (q.contains("experience"))
                            chatArea.append("Experience: " + t.experience + "\n\n");
                        else if (q.contains("email"))
                            chatArea.append("Email: " + t.email + "\n\n");
                        else
                            chatArea.append("Please ask about Subject, Qualification, Experience or Email.\n\n");
                    }
                }
            }

            // STUDENT MODE
            else if (mode[0].equals("student")) {

                for (Student s : studentList) {

                    if (q.contains(s.name)) {
                        found = true;

                        chatArea.append("Name: " + capitalize(s.name) + "\n");

                        if (q.contains("branch"))
                            chatArea.append("Branch: " + capitalize(s.branch) + "\n\n");
                        else if (q.contains("year"))
                            chatArea.append("Year: " + s.year + "\n\n");
                        else if (q.contains("roll"))
                            chatArea.append("Roll Number: " + s.rollno + "\n\n");
                        else if (q.contains("email"))
                            chatArea.append("Email: " + s.email + "\n\n");
                        else
                            chatArea.append("Please ask about Branch, Year, Roll Number or Email.\n\n");
                    }
                }
            }

            if (!found)
                chatArea.append("Please enter a valid name or correct question.\n\n");

            input.setText("");
            chatArea.setCaretPosition(chatArea.getDocument().getLength());
        };

        send.addActionListener(sendAction);
        input.addActionListener(sendAction);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // ================= MAIN =================
    public static void main(String[] args) {

        loadTeachersFromCSV();
        loadStudentsFromCSV();

        showWelcomeScreen();
    }
}