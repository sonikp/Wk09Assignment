import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

import junit.framework.TestCase;


public class BasePlusCommissionEmployeeComposition_test extends TestCase {

	final String expectedFirst = "John";
	final String expectedLast = "Doe";
	final String expectedSSN = "111-22-3333";
	final double expectedSales = 444.55;
	final double expectedRate = 0.1;
	final double expectedSalary = 200;
	BasePlusCommissionEmployeeComposition emp;
	CommissionEmployee commissionEmployeeReference;
	
	protected void setUp() throws Exception {
		super.setUp();
		emp = new BasePlusCommissionEmployeeComposition(
				expectedFirst,
				expectedLast,
				expectedSSN,
				expectedSales,
				expectedRate,
				expectedSalary);
		
		// Initialize the commissionEmployeeReference to the first Field of type CommissionEmployee encountered (there should only be one... this will be verified in a separate test)
		for(Field currentField : emp.getClass().getDeclaredFields()) {
			if(currentField.getType() == CommissionEmployee.class) {
				currentField.setAccessible(true);
				commissionEmployeeReference = (CommissionEmployee)currentField.get(emp);
				break;
			}
		}
	}

	/**
	 * Ensure composition
	 */
	public void testComposition() {
		// Ensure that we BasePlusCommissionEmployee holds a single reference to a CommissionEmployee object
		int commissionEmployeeReferenceCounter = 0;
		for(Field currentField : emp.getClass().getDeclaredFields()) {
			if(currentField.getType() == CommissionEmployee.class) {
				commissionEmployeeReferenceCounter++;
			}
		}
		assertEquals("BasePlusCommissionEmployee does not hold a SINGLE reference to a CommissionEmployee object in support of composition", 1, commissionEmployeeReferenceCounter);
	}
	
	/**
	 * Ensure delegation
	 */
	public void testDelegation() {
		String[] expectedMethodNames = {
				"getFirstName",
				"getLastName",
				"getSocialSecurityNumber",
				"getGrossSales",
				"setGrossSales",
				"getCommissionRate",
				"setCommissionRate",
				"getBaseSalary",
				"setBaseSalary",
				"earnings",
				"toString"
		};
		ArrayList<String> actualMethodNames = new ArrayList<String>();
		for (Method currentMethod : emp.getClass().getDeclaredMethods()) {
			actualMethodNames.add(currentMethod.getName());
		}
		for (String currentMethodName : expectedMethodNames) {
			assertTrue("Expected method (" + currentMethodName + ") not provided in support of delegation", actualMethodNames.contains(currentMethodName));
		}
	}
	
	/**
	 * Ensure that negative base-salary values are rejected by the constructor; in that case the base should be set to 0.0)
	 */
	public void testBasePlusCommissionEmployeeCompositionNegativeBase() {
		try {
			emp = new BasePlusCommissionEmployeeComposition(
					expectedFirst,
					expectedLast,
					expectedSSN,
					expectedSales,
					expectedRate,
					-200);			// invalid negative salary
									// Expecting an IllegalArgumentException
			fail ("Negative base-salary values should be rejected by setBaseSalary and result in an IllegalArgumentException");
		} catch (IllegalArgumentException ex) {
			// This is the nominal case
		} catch (RuntimeException ex) {
			assertThrowableTestFailure(ex);
		}
	}

	public void testSetBaseSalaryPositive() {
		final double altBaseSalary = 999.99;
		emp.setBaseSalary(altBaseSalary);
		try {
			Field fBase;
			fBase = emp.getClass().getDeclaredField("baseSalary");
			fBase.setAccessible(true);
			assertEquals("setBaseSalary did not set the value as expected", altBaseSalary, fBase.getDouble(emp));
		} catch (Exception ex) {
			assertThrowableTestFailure(ex);
		}	
	}
	
	public void testSetBaseSalaryNegative() {
		final double altBaseSalary = -999.99;
		try {
			emp.setBaseSalary(altBaseSalary); // Expecting an IllegalArgumentException
			fail ("Negative base-salary values should be rejected by setBaseSalary and result in an IllegalArgumentException");
		} catch (IllegalArgumentException ex) {
			// This is the nominal case
		} catch (RuntimeException ex) {
			assertThrowableTestFailure(ex);
		}
	}

	public void testGetBaseSalary() {
		assertEquals("getBaseSalary did not perform as expected", expectedSalary, emp.getBaseSalary());
	}

	public void testGetFirstName() {
		assertEquals("getFirstName did not perform as expected", expectedFirst, emp.getFirstName());
	}

	public void testGetLastName() {
		assertEquals("getLastName did not perform as expected", expectedLast, emp.getLastName());
	}

	public void testGetSocialSecurityNumber() {
		assertEquals("getSocialSecurityNumber did not perform as expected", expectedSSN, emp.getSocialSecurityNumber());
	}

	public void testGetGrossSales() {
		assertEquals("getGrossSales did not perform as expected", expectedSales, emp.getGrossSales());
	}

	public void testSetGrossSales() {
		// Only checking the nominal case (this method should be delegated to an already-tested CommissionEmployee anyway...)
		final double altGrossSales = 42;
		emp.setGrossSales(altGrossSales);
		try {
			assertEquals("setGrossSales did not set the value as expected", altGrossSales, commissionEmployeeReference.getGrossSales());
		} catch (Exception ex) {
			assertThrowableTestFailure(ex);
		}	
	}

	public void testGetCommissionRate() {
		assertEquals("getCommissionRate did not perform as expected", expectedRate, emp.getCommissionRate());
	}

	public void testSetCommissionRate() {
		// Only checking the nominal case (this method should be delegated to an already-tested CommissionEmployee anyway...)
		final double altCommissionRate = 0.5;
		emp.setCommissionRate(altCommissionRate);
		try {
			assertEquals("setCommissionRate did not set the value as expected", altCommissionRate, commissionEmployeeReference.getCommissionRate());
		} catch (Exception ex) {
			assertThrowableTestFailure(ex);
		}	
	}

	public void testEarnings() {
		try {
			Field fBase;
			fBase = emp.getClass().getDeclaredField("baseSalary");
			fBase.setAccessible(true);
			assertEquals("earnings() should return the value of CommissionEmployee.earnings() + baseSalary", commissionEmployeeReference.earnings() + fBase.getDouble(emp), emp.earnings());
		} catch (Exception ex) {
			assertThrowableTestFailure(ex);
		}	
	}

	public void testToString() {
		assertTrue("toString output for BasePlusCommissionEmployee should contain the baseSalary", emp.toString().contains(Double.toString(expectedSalary)));
	}

	/* method name retrieval code courtesy of: http://dev.kanngard.net/Permalinks/ID_20030114224837.html
	 */
	private void assertThrowableTestFailure(Throwable thrown) {
		StackTraceElement stackTraceElements[] =
            (new Throwable()).getStackTrace();
		fail(thrown.getClass().getName() + " encountered! Unable to successfully execute test: " + stackTraceElements[1].toString());
	}
}
