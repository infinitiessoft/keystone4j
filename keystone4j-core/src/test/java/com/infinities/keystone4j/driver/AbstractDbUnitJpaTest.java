/*******************************************************************************
 * # Copyright 2015 InfinitiesSoft Solutions Inc.
 * #
 * # Licensed under the Apache License, Version 2.0 (the "License"); you may
 * # not use this file except in compliance with the License. You may obtain
 * # a copy of the License at
 * #
 * #      http://www.apache.org/licenses/LICENSE-2.0
 * #
 * # Unless required by applicable law or agreed to in writing, software
 * # distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * # WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * # License for the specific language governing permissions and limitations
 * # under the License.
 *******************************************************************************/
package com.infinities.keystone4j.driver;

import java.io.InputStream;
import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.hsqldb.HsqldbDataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.hibernate.HibernateException;
import org.hibernate.internal.SessionImpl;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import com.infinities.keystone4j.jpa.EntityManagerHelper;
import com.infinities.keystone4j.jpa.EntityManagerListener;

public class AbstractDbUnitJpaTest {

	private static EntityManagerFactory entityManagerFactory;
	private static IDatabaseConnection connection;
	private static IDataSet dataset;
	protected static EntityManager entityManager;


	@BeforeClass
	public static void initEntityManager() throws HibernateException, DatabaseUnitException {
		entityManagerFactory = Persistence.createEntityManagerFactory("com.infinities.keystone4j.jpa.test");
		entityManager = entityManagerFactory.createEntityManager();
		connection = new DatabaseConnection(((SessionImpl) (entityManager.getDelegate())).connection());
		connection.getConfig().setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new HsqldbDataTypeFactory());

		FlatXmlDataSetBuilder flatXmlDataSetBuilder = new FlatXmlDataSetBuilder();
		flatXmlDataSetBuilder.setColumnSensing(true);
		InputStream dataSet = Thread.currentThread().getContextClassLoader().getResourceAsStream("test-data.xml");
		dataset = flatXmlDataSetBuilder.build(dataSet);
		EntityManagerListener.setEntityManagerFactory(entityManagerFactory);
	}

	@AfterClass
	public static void closeEntityManager() {
		entityManager.close();
		entityManagerFactory.close();
	}

	/**
	 * Will clean the dataBase before each test
	 * 
	 * @throws SQLException
	 * @throws DatabaseUnitException
	 */
	@Before
	public void cleanDB() throws DatabaseUnitException, SQLException {
		// DatabaseOperation.TRUNCATE_TABLE.execute(connection, dataset);
		DatabaseOperation.CLEAN_INSERT.execute(connection, dataset);
		try {
			EntityManagerHelper.beginTransaction();
		} catch (RuntimeException e) {
			e.printStackTrace();
			if (EntityManagerHelper.getEntityManager() != null && EntityManagerHelper.getEntityManager().isOpen()) {
				EntityManagerHelper.rollback();
			}
			EntityManagerHelper.closeEntityManager();
			throw e;
		}
	}

	@After
	public void closeEm() throws DatabaseUnitException, SQLException {
		try {
			EntityManagerHelper.commit();
		} catch (RuntimeException e) {
			e.printStackTrace();
			if (EntityManagerHelper.getEntityManager() != null && EntityManagerHelper.getEntityManager().isOpen()) {
				EntityManagerHelper.rollback();
			}
			throw e;
		} finally {
			EntityManagerHelper.closeEntityManager();
		}
		// DatabaseOperation.TRUNCATE_TABLE.execute(connection, dataset);
		// DatabaseOperation.CLEAN_INSERT.execute(connection, dataset);
	}
}
