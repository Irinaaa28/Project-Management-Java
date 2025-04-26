public class Customer extends User 
{
    private final String customerID;

    public Customer(String name, String email, String customerID) 
    {
        super(name, email);
        this.customerID = customerID;
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
