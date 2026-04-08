package com.techpulse.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

private static SessionFactory sessionFactory;
static{
    try {
        sessionFactory=new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
     } catch (Exception e) {
        System.err.println("SessionFactory creation failed:" + e.getMessage());
        throw new ExceptionInInitializerError(e);  
      }
}
 public static SessionFactory getSessionFactory()
 {
    return sessionFactory;
 }
  //calls this when application shuts down
  public static void shutdown(){
    getSessionFactory().close();
  }
    
}
