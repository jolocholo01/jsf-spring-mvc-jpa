// sistema escolar- autor Agostinho jolocholo
package com.mz.sistema.gestao.escolar.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAUtil {

	public static EntityManager getEntityManager() {
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("persistenceUnit");
		return factory.createEntityManager();
	}


}
