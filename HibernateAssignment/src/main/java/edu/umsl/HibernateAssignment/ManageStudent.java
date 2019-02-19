package edu.umsl.HibernateAssignment;

import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class ManageStudent {
	private static SessionFactory factory;

	public static void main(String[] args) {
		try {
			factory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
		} catch (Throwable ex) {
			System.err.println("Failed to create sessionFactory object." + ex);
			throw new ExceptionInInitializerError(ex);
		}
		ManageStudent MS = new ManageStudent();

		/* Add few student records in database */
		Integer stuID1 = MS.addStudent("Jennifer ", "Speer ", 125474569);
		Integer stuID2 = MS.addStudent("Willy ", "Wonka ", 345678901);
		Integer stuID3 = MS.addStudent("Sue ", "Connor ", 234567890);

		/* List down all the students */
		MS.listStudents();

		/* Update student's records */
		MS.updateStudent(stuID1, "Purcell");

		/* Delete an student from the database */
		MS.deleteStudent(stuID2);

		/* List down new list of the students */
		MS.listStudents();
	}

	/* Method to CREATE a student in the database */
	public Integer addStudent(String fname, String lname, int ssn) {
		Session session = factory.openSession();
		Transaction tx = null;
		Integer studentID = null;
		try {
			tx = session.beginTransaction();
			Student student = new Student(fname, lname, ssn);
			studentID = (Integer) session.save(student);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return studentID;
	}

	/* Method to READ all the students */
	public void listStudents() {
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			@SuppressWarnings("unchecked")

			List<Student> students = session.createQuery("FROM Student").list();

			for (Iterator<Student> iterator = students.iterator(); iterator.hasNext();) {
				Student student = iterator.next();
				System.out.print("First Name: " + student.getFirstName());
				System.out.print(" Last Name: " + student.getLastName());
				System.out.println(" SSN: " + student.getSsn());
			}
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	/* Method to UPDATE lastName for a student */
	public void updateStudent(Integer StudentID, String lastName) {
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Student student = (Student) session.get(Student.class, StudentID);
			student.setLastName(lastName);
			session.update(student);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	/* Method to DELETE a student from the records */
	public void deleteStudent(Integer StudentID) {
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Student student = (Student) session.get(Student.class, StudentID);
			session.delete(student);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}
}
