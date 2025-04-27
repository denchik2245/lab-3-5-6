package com.example.servlet;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public final class HibernateUtil {
    private static final SessionFactory sessionFactory;

    static {
        try {
            Configuration cfg = new Configuration().configure();
            cfg.addAnnotatedClass(User.class);

            ServiceRegistry registry = new StandardServiceRegistryBuilder()
                    .applySettings(cfg.getProperties())
                    .build();
            sessionFactory = cfg.buildSessionFactory(registry);
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError("Initial SessionFactory creation failed: " + ex);
        }
    }

    private HibernateUtil() {}

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}