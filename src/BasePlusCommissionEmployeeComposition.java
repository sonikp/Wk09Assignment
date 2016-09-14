/*
 * BasePlusCommissionEmployeeComposition.java
 * 
 * Michael Floerchinger
 * Chapter 9.3
 */

public class BasePlusCommissionEmployeeComposition 

{
    
    private double baseSalary; // base salary per week
    // Demonstrate composition of (as opposed to inheritance from) a CommissionEmployee object
    
    // "Composition" (has-a) of a CommissionEmployee object
    private CommissionEmployee employee;
    
    
    // six-argument constructor
    public BasePlusCommissionEmployeeComposition( String first, String last, 
            String ssn, double sales, double rate, double salary) 
    {
    	
    	
    	employee = new CommissionEmployee( first, last, ssn,  sales,  rate);
    	
    	if (salary < 0.0)
    		throw new IllegalArgumentException(
    				"Salary must be > 0");
    	
    	this.baseSalary = salary;
    	
    	
    }
    
    
    
    // Implement accessors and mutators for all six attributes (stubs appear below)
    // As per the text (demonstrated in its BasePlusCommissionEmployee implementation), throw an IllegalArgumentException if the salary argument is negative
    public void setBaseSalary( double salary ) 
    {
    
    	if (salary < 0.0)
    		throw new IllegalArgumentException(
    				"Salary must be > 0");
    	
    	this.baseSalary = salary;
    }    

    public double getBaseSalary() 
    {
    	
    	return baseSalary;
    }
    
    public String getFirstName() 
    {
    	return employee.getFirstName();
    }
    
    public String getLastName() 
    {
    	return employee.getLastName();
    }
	
	
    public String getSocialSecurityNumber() 
    {
    	return employee.getSocialSecurityNumber();
    }
	
    public double getGrossSales() 
    {
    	return employee.getGrossSales();	
    }

    public void setGrossSales(double sales) 
    {
    	
    	if (sales < 0.0)
    		throw new IllegalArgumentException(
    				"Sales amount must be > 0");
    	
    	this.employee.setGrossSales(sales);
    	
    }

    public double getCommissionRate() 
    {
    	return employee.getCommissionRate();
    }

    public void setCommissionRate(double rate) 
    {
    	if (rate < 0.0)
    		throw new IllegalArgumentException(
    				"Rate amount must be > 0");
    	
    	this.employee.setCommissionRate(rate);
    }    
    
    
    public double earnings() 
    {

    	return getBaseSalary() + (getCommissionRate() * getGrossSales());
    }
    
    
    @Override
    public String toString() 
    {        

    	
    	return String.format("%s: %s %s%n%s: %s%n%s: %.2f%n%s: %.2f%n%s: %.2f", 
				"base-salaried commission employee", getFirstName(), getLastName() ,
				"social security number", getSocialSecurityNumber(),
				"gross sales: $", getGrossSales(),
				"commission rate: % ", getCommissionRate(), 
				"base salary: $", baseSalary);
				
    	
    	 
    }
}
