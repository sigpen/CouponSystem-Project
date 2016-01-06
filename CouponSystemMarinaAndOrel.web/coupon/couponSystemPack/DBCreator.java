package couponSystemPack;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import company.pkg.ConnectionPool;

public class DBCreator {

	public void createDB() {

		String sql1 = "CREATE TABLE CompaniesL (id bigint NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), CompanyName VARCHAR(255), CompanyPassword  VARCHAR(255), CompanyEmail  VARCHAR(255), primary key(id))";

		String sql2 = "CREATE TABLE CouponsL (id bigint NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),Title VARCHAR(255), Message VARCHAR(255) , Image VARCHAR(255), Amount int, startDate date, endDate date, Price DOUBLE PRECISION, Type VARCHAR(255), primary key(id))";

		String sql3 = "CREATE TABLE CustomersL (id bigint NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),CUSTOMERNAME VARCHAR(255), CUSTOMERPASSWORD VARCHAR(255), primary key(id))";

		String sql4 = "CREATE TABLE COMPANY_COUPON (CompaniesL bigint, CouponsL bigint)";

		String sql5 = "CREATE TABLE CUSTOMER_COUPON (CustomersL bigint, CouponsL bigint)";

		Connection conn = ConnectionPool.getInstance().getConnection();

		try {
			
			PreparedStatement prpst1 = conn.prepareStatement(sql1);
			PreparedStatement prpst2 = conn.prepareStatement(sql2);
			PreparedStatement prpst3 = conn.prepareStatement(sql3);
			PreparedStatement prpst4 = conn.prepareStatement(sql4);
			PreparedStatement prpst5 = conn.prepareStatement(sql5);
			prpst1.executeUpdate();
			prpst2.executeUpdate();
			prpst3.executeUpdate();
			prpst4.executeUpdate();
			prpst5.executeUpdate();

		} catch (SQLException e) {

			e.printStackTrace();
		}

	}

}
