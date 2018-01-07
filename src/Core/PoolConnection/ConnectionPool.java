package Core.PoolConnection;

/*
 * 
 * 
 * class that represent the limited connections to the Database. in other words a connectionPool class.
 * This class is a SingleTon class - means there is only one instance from this class (to keep track after the connections).
 * 
 * 
 * 
 * 
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import Core.DataBase.DBStartUp;
import SystemExceptionHandling.ConnectionClosedException;
import SystemExceptionHandling.DataBaseIsCloseException;
import SystemExceptionHandling.DatabaseAtMaxCapacityException;
import SystemExceptionHandling.ServerCantLoadException;
import SystemExceptionHandling.TransactionCoulntFinishException;

public class ConnectionPool {

	// the limited connections possible.
	private final static int MAX_CONNECTIONS = 50;
	// a set to hold all the connections
	private static HashSet<Connection> connections = new HashSet<>();
	// a set to hold all the connection in use.
	private static HashSet<Connection> usedConnection = new HashSet<>();
	// the singleton instance.
	private static ConnectionPool instance = null;
	// a instance boolean value to hold if the server is closed or not.
	private static boolean serverNotClose = true;

	private ConnectionPool() {
		super();
	}

	/*
	 * a method to initialize the pool for the first time.
	 */
	private synchronized static void initializeConnections() throws ServerCantLoadException {
		// runs up to the limit of connection defined.
		for (int i = 0; i < MAX_CONNECTIONS; i++) {

			try {
				// adds the connection to the collection (pool)
				connections.add(DriverManager.getConnection(DBStartUp.URL));
			} catch (SQLException e) {
				ServerCantLoadException loadException = new ServerCantLoadException();
				throw loadException;
			}
		}

	}

	/*
	 * method to get the singleton instance.
	 */

	public static ConnectionPool getInstance() throws ServerCantLoadException, DataBaseIsCloseException {
		// checks that the server is not closed
		if (serverNotClose) {
			// initializing the pool and return the singleton instance to the
			// user.
			if (instance == null) {
				instance = new ConnectionPool();
				initializeConnections();
				return instance;
			} else {
				return instance;
			}
		} else { // else throws a DataBase closed exception.
			DataBaseIsCloseException closed = new DataBaseIsCloseException();
			throw closed;
		}
	}

	/*
	 * this method returns a connection from the pool to the user. (this method
	 * will be used before every method that execute an action in the database).
	 */

	public synchronized Connection getConnection() throws DataBaseIsCloseException, DatabaseAtMaxCapacityException {
		// checks that the server is not closed.
		if (serverNotClose == false) {
			throw new DataBaseIsCloseException();
		} else if (connections.isEmpty()) { // if the connection pool is empty
											// the user will wait a minute and
											// try to get a connection.
			try {
				wait(1000 * 60);
			} catch (InterruptedException e) {
				throw new DatabaseAtMaxCapacityException();
			}
		} else { // else returns a connection from the pool to the user (not
					// before creating a reference to that connection)
			Iterator<Connection> it = connections.iterator();
			Connection SingleConnection = it.next();
			usedConnection.add(SingleConnection);
			return SingleConnection;

		}
		throw new DatabaseAtMaxCapacityException();
	}

	/*
	 * method to return back a connection to the pool . (this method used after
	 * action from the user ends).
	 * 
	 */

	public synchronized void returnConnection(Connection connnection) {
		/*
		 * checks that the connection is truly from the used Connection (for
		 * safety check, for some possibility that the connection is not from
		 * the pool). if it is from the connection pool then returns it to the
		 * unused collection.
		 */
		Iterator<Connection> it = usedConnection.iterator();
		while (it.hasNext()) {
			Connection nextElement = it.next();
			if (nextElement.equals(connnection)) {
				connections.add(connnection);
				usedConnection.remove(nextElement);
				notify();
			}
		}

	}
	
	/*
	 * closing all the pool connection. runs with iterator on both sets and closes each connection.
	 * 
	 */
	public static synchronized void closeAllConnections()
			throws ConnectionClosedException, TransactionCoulntFinishException {
		if (serverNotClose) {
			Iterator<Connection> it = connections.iterator();
			Iterator<Connection> Copyit = usedConnection.iterator();
			while (it.hasNext()) {
				try {
					it.next().close();
				} catch (SQLException e) {
					throw new ConnectionClosedException();
				}
			}
			while (Copyit.hasNext()) {
				try {
					Copyit.next().close();
				} catch (SQLException e) {
					throw new TransactionCoulntFinishException();
				}
				serverNotClose = false;
			}

		} else {
			throw new ConnectionClosedException();
		}
	}

}