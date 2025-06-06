public final class Customer extends User 
{
    private final String customerID;

    public Customer(int id, String name, String email, String customerID) 
    {
        super(id, name, email);
        this.customerID = customerID;
    }

    public Customer(Customer cust)
    {
        super(cust);
        this.customerID = cust.customerID;
    }
    public String getCustomerID() 
    {
        return customerID;
    }
    @Override
    public String getRole() 
    {
        return "Customer";
    }
    
}
