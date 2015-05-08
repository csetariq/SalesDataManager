public class SalesPerson {

    private String  name;
    private int     sales;
    private double  commission;
    
    public SalesPerson(String name, int sales, double commission) {
        this.name = name;
        this.sales = sales;
        this.commission = commission;
    }

    public String getName() {
        return name;
    }

    public int getSales() {
        return sales;
    }

    public double getCommission() {
        return commission;
    }
}

