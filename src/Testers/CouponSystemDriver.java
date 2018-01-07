package Testers;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import Core.Bean.Company;
import Core.Bean.Coupon;
import Core.Bean.CouponType;
import Core.Bean.Customer;
import Core.DataBase.DBStartUp;
import CouponSystem.CouponSystem;
import DBDAO.CouponDBDAO;
import Facades.AdminFacade;
import Facades.CompanyFacade;
import Facades.CouponClientFacade;
import Facades.CustomerFacade;
import SystemExceptionHandling.CantUpdateException;
import SystemExceptionHandling.CouldntGetFromDataBase;
import SystemExceptionHandling.CreateBeanException;
import SystemExceptionHandling.CreateTableException;
import SystemExceptionHandling.DataBaseIsCloseException;
import SystemExceptionHandling.DatabaseAtMaxCapacityException;
import SystemExceptionHandling.DriverCantLoadException;
import SystemExceptionHandling.GeneralCouponSystemException;
import SystemExceptionHandling.ServerCantLoadException;
import SystemExceptionHandling.WorrgPasswordException;

public class CouponSystemDriver {

	public static void main(String[] args) {

		try {
			DBStartUp.createDatebase();
		} catch (DriverCantLoadException | CreateTableException e1) {
			System.out.println(e1.getMessage());
		}

		Scanner scan = new Scanner(System.in);

		CouponSystem inSystem = CouponSystem.getInstance();

		System.out.println("enter username: ");
		String name = scan.next();

		System.out.println("enter password: ");
		String password = scan.next();

		System.out.println("enter user type: ");
		String type = scan.next();

		try {

			CouponClientFacade facade = inSystem.login(name, password, type);

			if (facade != null && type.equals("admin")) {

				AdminFacade facade2 = (AdminFacade) facade;
				Company comp = new Company();
				Customer cust = new Customer();

				System.out.println("You Are in the System Choose with Action You Want To Execute - \n"
						+ "(Create Company = 1\n" + " Remove Company = 2\n" + " Update Company = 3\n"
						+ " Create Customer = 4\n" + " Remove Customer = 5\n" + " Update Customer = 6\n"
						+ " Get Company = 7\n" + " Get Customer = 8\n" + " Get all companies = 9\n"
						+ " Get all customers = 0\n" + " ******* Quit = Quit******");

				String str = scan.next();

				while (!str.toUpperCase().equals("QUIT")) {

					if (str.equals("1")) {
						try {
							System.out.println("enter CompName to Create: ");
							String compNameCreate = scan.next();
							comp.setCompName(compNameCreate);

							System.out.println("enter CompID: ");
							long compID = scan.nextLong();
							comp.setId(compID);

							System.out.println("enter CompEmail: ");
							String email = scan.next();
							comp.setEmail(email);

							System.out.println("enter compPassword: ");
							String password2 = scan.next();
							comp.setPassword(password2);

							facade2.createCompany(comp);
						} catch (RuntimeException e) {
							System.out.println("Couldnt Execute Query, Possible Input Mismatch.");
						}

					} else if (str.equals("2")) {

						try {
							System.out.println("enter CompName to remove: ");
							String compNameRemove = scan.next();
							comp.setCompName(compNameRemove);
							System.out.println("enter CompID: ");
							long compIDRemove = scan.nextLong();
							comp.setId(compIDRemove);
							facade2.removeCompany(comp);
						} catch (RuntimeException e) {
							System.out.println("Couldnt Execute Query, Possible Input Mismatch.");
						}

					} else if (str.equals("3")) {
						try {
							System.out.println("Enter company id: ");
							long ID = scan.nextLong();
							comp = facade2.getCompany(ID);
							System.out.println("Enter new password: ");
							String password2 = scan.next();
							System.out.println("Enter new Email: ");
							String email = scan.next();
							comp.setEmail(email);

							comp.setPassword(password2);

							facade2.updateCompany(comp);
						} catch (RuntimeException e) {
							System.out.println("Couldnt Execute Query, Possible Input Mismatch.");
						}

					} else if (str.equals("4")) {

						try {
							System.out.println("enter Customer name: ");
							String custNameCreate = scan.next();
							cust.setCusName(custNameCreate);

							System.out.println("enter Customer ID: ");
							long custID = scan.nextLong();
							cust.setId(custID);

							System.out.println("enter Password: ");
							String password3 = scan.next();
							cust.setPassword(password3);

							facade2.createCustomer(cust);
						} catch (RuntimeException e) {
							System.out.println("Couldnt Execute Query, Possible Input Mismatch.");
						}

					} else if (str.equals("5")) {
						try {
							System.out.println("enter Customer Name to remove: ");
							String custNameRemove = scan.next();
							cust.setCusName(custNameRemove);

							System.out.println("enter Customer ID: ");
							long custIDRemove = scan.nextLong();
							cust.setId(custIDRemove);
							;

							facade2.removeCustomer(cust);
						} catch (RuntimeException e) {
							System.out.println("Couldnt Execute Query, Possible Input Mismatch.");
						}

					} else if (str.equals("6")) {
						try {
							System.out.println("Enter customer id: ");
							long ID = scan.nextLong();
							cust = facade2.getCustomer(ID);

							System.out.println("Enter new password: ");
							String password2 = scan.next();
							cust.setPassword(password2);

							facade2.updateCustomer(cust);
						} catch (RuntimeException e) {
							System.out.println("Couldnt Execute Query, Possible Input Mismatch.");
						}

					} else if (str.equals("7")) {
						System.out.println("enter company id");
						long id = scan.nextLong();

						System.out.println(facade2.getCompany(id));

					} else if (str.equals("8")) {
						try {
							System.out.println("enter customer id");
							long id = scan.nextLong();
							System.out.println(facade2.getCustomer(id));
						} catch (RuntimeException e) {
							System.out.println("Couldnt Execute Query, Possible Input Mismatch.");
						}

					} else if (str.equals("9")) {
						System.out.println(facade2.getAllCompanies());

					} else if (str.equals("0")) {
						System.out.println(facade2.getAllCustomer());
					}

					System.out.println("If U want to Quit type Quit (else type one from the options above):");
					str = scan.next();

				}

			} else if (facade != null && type.equals("customer")) {

				CustomerFacade facade2 = (CustomerFacade) facade;

				System.out.println(
						"You Are in the System Choose with Action You Want To Execute - \n" + "(Purchase coupon = 1\n"
								+ " show all purchased coupons = 2\n" + " show all purchased coupons by type = 3\n"
								+ " show all purchased coupons by price = 4\n" + " ******* Quit = Quit******");

				String str = scan.next();

				while (!str.toUpperCase().equals("QUIT")) {
					if (str.equals("1")) {
						try {
							CouponDBDAO DAO = new CouponDBDAO();
							System.out.println(DAO.getAllCoupons());
							System.out.println();
							System.out.println("enter coupon id u want to purchase:");
							long id = scan.nextLong();
							Coupon coup = new Coupon();
							coup.setId(id);
							facade2.purchaseCoupon(coup);
						} catch (RuntimeException e) {
							System.out.println("Couldnt Execute Query, Possible Input Mismatch.");
						}
					} else if (str.equals("2")) {
						System.out.println(facade2.getAllPurchasedCoupons());
					} else if (str.equals("3")) {
						System.out.println("RESTURANTS, ELECTRICITY, FOOD, HEALTH, SPORTS, CAMPING, TRAVELLING");
						System.out.println("enter type :");
						str = scan.next();
						try {
							System.out.println(
									facade2.getAllPurchasedCouponsByType(CouponType.valueOf(str.toUpperCase())));
						} catch (IllegalArgumentException e) {
							System.out.println("No Tyoe like The One you Entered!");
						}
					} else if (str.equals("4")) {
						try {
							System.out.println("syso enter limited price:");
							double limit = scan.nextDouble();
							System.out.println(facade2.getALLPurchasedCouponsByPrice(limit));
						} catch (RuntimeException e) {
							System.out.println("Couldnt Execute Query, Possible Input Mismatch.");
						}
					}

					System.out.println("If U want to Quit type Quit (else type one from the options above):");
					str = scan.next();
				}
			} else if (facade != null && type.equals("company")) {

				CompanyFacade facade2 = (CompanyFacade) facade;

				System.out.println("You Are in the System Choose with Action You Want To Execute - \n"
						+ "(Create coupon = 1\n" + " Remove Coupon = 2\n" + " Update coupon= 3\n" + " Show Coupon = 4\n"
						+ " show all company coupons = 5\n" + " show coupons by type = 6\n"
						+ " ******* Quit = Quit******");

				String str = scan.next();

				while (!str.toUpperCase().equals("QUIT")) {
					if (str.equals("1")) {

						try {
							Coupon coup = new Coupon();

							System.out.println("enter id:");
							long id = scan.nextLong();
							coup.setId(id);

							System.out.println("enter title:");
							String title = scan.next();
							coup.setTitle(title);

							System.out.println("enter start date (example: 31/12/2017):");
							String startDate = scan.next();
							SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
							try {
								java.util.Date d = f.parse(startDate);
								long milliseconds = d.getTime();
								java.sql.Date dateSql = new Date(milliseconds);
								coup.setStartDate(dateSql);
							} catch (ParseException e) {
								System.out.println("not a valid date.");
							}

							System.out.println("enter end date (example: 31/12/2017):");
							String endDate = scan.next();
							try {
								java.util.Date d = f.parse(endDate);
								long milliseconds = d.getTime();
								java.sql.Date dateSql = new Date(milliseconds);
								coup.setEndDate(dateSql);
							} catch (ParseException e) {
								System.out.println("not a valid date.");
							}

							System.out.println("enter amount:");
							int amount = scan.nextInt();
							coup.setAmount(amount);

							System.out.println("enter type :");
							System.out.println("RESTURANTS, ELECTRICITY, FOOD, HEALTH, SPORTS, CAMPING, TRAVELLING");
							str = scan.next();
							try {
								coup.setType(CouponType.valueOf(str.toUpperCase()));
							} catch (IllegalArgumentException e) {
								System.out.println("No Tyoe like The One you Entered!");
							}

							System.out.println("enter message:");
							str = scan.next();
							coup.setMessage(str);

							System.out.println("enter price:");
							double price = scan.nextDouble();
							coup.setPrice(price);

							System.out.println("enter image:");
							str = scan.next();
							coup.setImage(str);

							facade2.createCoupon(coup);
						} catch (RuntimeException e) {
							System.out.println("Couldnt Execute Query, Possible Input Mismatch.");
						}

					} else if (str.equals("2")) {
						try {
							Coupon coup = new Coupon();

							System.out.println("enter coupon id:");
							long id = scan.nextLong();

							coup.setId(id);

							facade2.removeCoupon(coup);
						} catch (RuntimeException e) {
							System.out.println("Couldnt Execute Query, Possible Input Mismatch.");
						}
					} else if (str.equals("3")) {
						try {

							Coupon coup = new Coupon();

							System.out.println("enter coupon id:");
							long id = scan.nextLong();

							coup.setId(id);

							System.out.println("enter end date (example: 31/12/2017):");
							String endDate = scan.next();
							SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
							try {
								java.util.Date d = f.parse(endDate);
								long milliseconds = d.getTime();
								java.sql.Date dateSql = new Date(milliseconds);
								coup.setEndDate(dateSql);
							} catch (ParseException e) {
								System.out.println("not a valid date.");
							}

							System.out.println("enter new price:");
							double price = scan.nextDouble();
							coup.setPrice(price);
							facade2.updateCoupon(coup);
						} catch (RuntimeException e) {
							System.out.println("Couldnt Execute Query, Possible Input Mismatch.");
						}

					} else if (str.equals("4")) {
						try {
							System.out.println("enter coupon id:");
							long id = scan.nextLong();
							System.out.println(facade2.getCoupon(id));
						} catch (RuntimeException e) {
							System.out.println("Couldnt Execute Query, Possible Input Mismatch.");
						}
					} else if (str.equals("5")) {
						System.out.println(facade2.getAllCoupons());
					} else if (str.equals("6")) {

						System.out.println("enter type :");
						System.out.println("RESTURANTS, ELECTRICITY, FOOD, HEALTH, SPORTS, CAMPING, TRAVELLING");
						str = scan.next();
						try {
							System.out.println(facade2.getCouponByType(CouponType.valueOf(str.toUpperCase())));
						} catch (IllegalArgumentException e) {
							System.out.println("No Tyoe like The One you Entered!");
						}
					}
					System.out.println("If U want to Quit type Quit (else type one from the options above):");
					str = scan.next();
				}
			}

		} catch (ServerCantLoadException | DataBaseIsCloseException | DatabaseAtMaxCapacityException
				| WorrgPasswordException e) {
			System.out.println(e.getMessage());
		} catch (CantUpdateException e) {
			System.out.println(e.getMessage());
		} catch (CreateBeanException e) {
			System.out.println(e.getMessage());
		} catch (CouldntGetFromDataBase e) {
			System.out.println(e.getMessage());
		} catch (GeneralCouponSystemException e) {
			System.out.println(e.getMessage());
		} finally {
			scan.close();
		}

	}
}
