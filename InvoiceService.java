import java.util.ArrayList;
import java.util.List;

public class InvoiceService 
{
    private List<Invoice> invoices = new ArrayList<>();

    public void threeSecondsTimer()
    {
        try {
            Thread.sleep(3000); // întârziere de 3 secunde (3000 milisecunde)
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Invoice generateInvoice(Project project) throws Exception
    {
        if (project.getProgress() != 100)
            throw new Exception("The project is not finished yet. Cannot generate invoice.");
        Invoice invoice = new Invoice(project);
        threeSecondsTimer();
        invoice.setReleased(true);
        invoices.add(invoice);
        System.out.println("Invoice generated for project " + project.getName() + " with ID: " + invoice.getId() + ", amount: " + invoice.getAmount() + ", release date: " + invoice.getReleaseDate() + ".");
        return invoice;

    }
    public void showAllInvoices()
    {
        for (Invoice invoice : invoices) {
            System.out.println("Invoice ID: " + invoice.getId() + ", Project: " + invoice.getProject().getName() + ", Amount: " + invoice.getAmount() + ", Release Date: " + invoice.getReleaseDate() + ", Released: " + invoice.isReleased());
        }
    }

    public void deleteInvoice(Invoice invoice) 
    {
        if (invoices.contains(invoice)) {
            invoices.remove(invoice);
            System.out.println("Invoice with ID: " + invoice.getId() + " has been deleted.");
        } else {
            System.out.println("Invoice not found.");
        }
    }
}
