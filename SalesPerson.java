public class SalesPerson {

    private static final int SALES_UNIT = 1000;
    
    private String  name;
    private int     sales;
    private double  commission;
    
    public SalesPerson(String name, int sales, double commission) {
        this.name = name;
        this.sales = sales * SALES_UNIT;
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

