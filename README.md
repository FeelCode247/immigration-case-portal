Immigration Case Portal

A secure Java-based web application built with JSP, Servlets, JDBC, and MySQL to manage immigration applications, case documents, and user roles.
Designed using MVC architecture for clean separation of concerns.

🚀 Features

User Roles: Applicant, officer, and Admin dashboards.

Application Management: Submit, view, and update case applications.

Document Uploads: Applicants can upload case-related documents.

Authentication & Authorization: Secure login, role-based access.

Database Persistence: Application and document data stored in MySQL.

Filters: Logging, Authentication, and Role-based security.

🛠️ Tech Stack

Backend: Java, Servlets, JSP

Frontend: JSP, HTML, CSS, Bootstrap

Database: MySQL

Server: Apache Tomcat (v11 recommended)

Tools: Eclipse IDE, GitHub

⚙️ Setup Instructions
1. Clone the repository
git clone https://github.com/FeelCode247/immigration-case-portal.git
cd immigration-case-portal

2. Import into Eclipse

Open Eclipse IDE → File → Import → Existing Projects into Workspace.

Select the project folder.

3. Configure Database

Create a MySQL database (e.g., case_portal_db).

Import the SQL schema file.

Update your DB credentials in DBConnection.java (or config file):

private static final String URL = "jdbc:mysql://localhost:3306/case_portal_db";
private static final String USERNAME = "your_db_username";
private static final String PASSWORD = "your_db_password";

4. Deploy on Tomcat

Add project to Apache Tomcat Server in Eclipse.

Run the server and open:

http://localhost:8080/immigration-case-portal

🔑 Dummy Credentials

Use the following test logins to explore:

Applicant

Username: applicant@caseportal.local

Password: Applicant@123

Lawyer

Username: officer@caseportal.local

Password: Officer@123

Admin

Username: admin@caseportal.local

Password: Admin@123



