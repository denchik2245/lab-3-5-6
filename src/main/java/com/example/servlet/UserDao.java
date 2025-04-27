package com.example.servlet;

import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.Optional;

public class UserDao {
    public Optional<User> findByUsername(String username) {
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            User user = session.createQuery(
                            "FROM User u WHERE u.username = :uname", User.class)
                    .setParameter("uname", username)
                    .uniqueResult();
            tx.commit();
            return Optional.ofNullable(user);
        } catch (Exception e) {
            if (tx != null && tx.getStatus().canRollback()) {
                tx.rollback();
            }
            throw e;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public void save(User user) {
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            session.persist(user);
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.getStatus().canRollback()) {
                tx.rollback();
            }
            throw e;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}