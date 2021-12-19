package application.doa;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import application.entity.Customer;
import application.entity.Feedback;
import application.entity.ServicesOffered;
import application.entity.Vehicle;
import application.entity.Workshop;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CustomerDOAImp implements CustomerDOA {
	
	private SessionFactory sf;
	
	public CustomerDOAImp(){
		Configuration con = new Configuration();
    	con.configure().addAnnotatedClass(Customer.class);
    	con.configure().addAnnotatedClass(Vehicle.class);
    	con.configure().addAnnotatedClass(Workshop.class);
     	con.configure().addAnnotatedClass(Feedback.class);
     	con.configure().addAnnotatedClass(ServicesOffered.class);
    	sf = con.buildSessionFactory();	
	}
	
	@Override
	public void registerCustomer(Object object) {
		Session session = sf.openSession();
		Transaction trans = session.beginTransaction();
		session.saveOrUpdate(object);
		trans.commit();
		session.close();
	}

	@Override
	public void addVehicle(Vehicle vehicle,Customer c1) {
		Session session = sf.openSession();
		Transaction trans = session.beginTransaction();
//		List customers= session.createQuery("from Customer where username = :user").setParameter("user", customer.getUsername()).list();
		
		Customer c=session.get(Customer.class, c1.getCustomerID());
//		
//		c.getVehicles().add(vehicle);
//		for (int i = 0; i < c.getVehicles().size(); i++) {
//			System.out.println(c.getVehicles().get(i).getName());			
//		}
		vehicle.setCustomer(c);
		c.getVehicles().add(vehicle);
//		vehicle.setCustomer(customer);
//		
		session.saveOrUpdate(vehicle.getCustomer());
		session.saveOrUpdate(vehicle);
		trans.commit();
		session.close();
		
	}

	@Override
	public void removeVehicle(String vehicle, Customer customer) {
		Session session = sf.openSession();
		Transaction trans = session.beginTransaction();
		session.createQuery("delete from Vehicle where customer_customerID = :user and plateNumber = :name").setParameter("user", customer.getCustomerID()).setParameter("name", vehicle).executeUpdate();
		
		trans.commit();
		session.close();
		
	}

	@Override
	public void displayVehciles(String username) {
//		Session session = sf.openSession();
//		Set<Vehicle> vehicles = session.createQuery("from vehicle where hostelLogin = :user").setParameter("user", hostelLogin).list();
//		
	}

	@Override
	public ArrayList<Customer> getCustomers() {
		Session session =  sf.openSession(); 
		Transaction trans = session.beginTransaction();
		List customers=session.createQuery("from Customer").list();
		ArrayList<Customer> customer=new ArrayList<>();
		for (Iterator iterator = customers.iterator(); iterator.hasNext();) {
			customer.add((Customer) iterator.next());
		}
		trans.commit();
		session.close();
		return customer;
	}

	@Override
	public ObservableList<Vehicle> getVehiclesbyUsername(String username, Customer customer) {
		Session session = sf.openSession();
		Transaction trans = session.beginTransaction();
		
		List vehicles= session.createQuery("from Vehicle where customer_customerID = :user").setParameter("user", customer.getCustomerID()).list();
		
		trans.commit();
		session.close();
		ObservableList<Vehicle> vehicle=FXCollections.observableArrayList();
		for (Iterator iterator = vehicles.iterator(); iterator.hasNext();) {
			vehicle.add((Vehicle) iterator.next());
		}
		return vehicle;
	}

}
