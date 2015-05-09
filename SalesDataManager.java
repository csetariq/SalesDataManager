import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Pattern;

public class SalesDataManager {
    
    private static final int        N           = 4;
    private static final char       NEWLINE     = '\n';
    private static final int        SLAB        = 25000;
    private static final int        SALES_UNIT  = 1000;
    private static final int        SALES_MIN   = 10;
    private static final int        SALES_MAX   = 100;
    private static final double[]   PERCENT     = { 5.0,
                                                    6.0,
                                                    8.0,
                                                    10.0};
    
    private static Scanner in = new Scanner(System.in);

    private SalesPerson[] masterList;
    private SalesPerson[] sortedList;

    public SalesDataManager() {
        masterList = new SalesPerson[N];
    }

    public void displayMenu() {
        System.out.println(
            NEWLINE +
            "1. Input and Validate data"    + NEWLINE +
            "2. Display"                    + NEWLINE +
            "3. Sort by name"               + NEWLINE +
            "4. Sort by sales"              + NEWLINE +
            "5. Search by name"             + NEWLINE +
            "6. Search by sales"            + NEWLINE +
            "7. Display statistics"         + NEWLINE +
            "8. Exit"                       + NEWLINE
        );
    }

    public void inputData() {
        
        for (int i = 0; i < N; i++) {
            String  name        = null;
            int     sales       = 0;
            double  commission  = 0.0;

            while (true) {
                System.out.printf("Enter customer name %d: ", i + 1);
                name = in.nextLine().trim();
                
                if (isValidName(name))
                    break;
                else
                    System.out.println("\n\tInvalid name\n");
            }
            
            while (true) {
                System.out.printf("Enter sales %d: ", i + 1);
                String temp = in.nextLine().trim();

                if (!temp.isEmpty()) {
                    sales = Integer.parseInt(temp);
                
                    if (isValidSales(sales)) { 
                        break;
                    }
                }

                System.out.println("\n\tInvalid sales. Must be within [10 - 100]\n");
            }

            commission = calculateCommission(sales);

            SalesPerson currentPerson = new SalesPerson(name, sales, commission);
            masterList[i] = currentPerson;
        }
        sortedList = Arrays.copyOf(masterList, masterList.length);
    }

    public void sortByName() {
        if (!isListEmpty(masterList)) {
            SalesPerson temp = null;
            int i = 0;
            for(int j = 1; j < sortedList.length; j++) {
                temp = sortedList[j];
                for(i = j; 
                    i > 0 && temp.getName().compareToIgnoreCase(sortedList[i-1].getName()) < 0;
                    i--) {
                    sortedList[i] = sortedList[i-1];
                }
                sortedList[i] = temp;
            }
        }
    }

    public void sortBySales() {
        if (!isListEmpty(masterList)) {
            SalesPerson temp = null;
            int i = 0;
            for(int j = 1; j < sortedList.length; j++) {
                temp = sortedList[j];
                for(i = j; i > 0 && temp.getSales() < sortedList[i-1].getSales(); i--)
                    sortedList[i] = sortedList[i-1];
                sortedList[i] = temp;
            }
        }
    }

    public void searchByName(String name) {
        if (!isListEmpty(masterList)) {
            for (int i = 0; i < masterList.length; i++) {
                SalesPerson person = masterList[i];
    
                if (name.equalsIgnoreCase(person.getName())) {
                    System.out.println("\n\t--------------------------------------------------------------");
                    System.out.printf("\t%s - Sales amount: $%d; Commission: $%.1f %n",
                                        person.getName(),
                                        person.getSales(),
                                        person.getCommission());
                    System.out.println("\t--------------------------------------------------------------\n");
                    return;
                }
            }
            System.out.println("\n\tNo match found\n");
        } else {
            System.out.println("\n\tNothing to search\n");
        }
    }

    public void searchBySales(int sales) {
        if (!isListEmpty(masterList)) {
            boolean foundRecords = false;
            sortBySales();
            sales *= SALES_UNIT;
            System.out.printf("\n\tThe following sales person has less than $%d %n%n", sales);
            for (int i = 0; i < sortedList.length; i++) {
                SalesPerson person = sortedList[i];
    
                if (person.getSales() < sales) {
                    foundRecords = true;
                    System.out.printf("\t%-20s $%-6d %n",
                                        person.getName(),
                                        person.getSales());
                } else {
                    System.out.println();
                    return;
                }
            }
            if (!foundRecords)
                System.out.println("\n\tNo records found\n");
        } else {
            System.out.println("\n\tNothing to search\n");
        }
    }

    public void displayStatistics() {
        if (!isListEmpty(masterList)) {
            sortBySales();
            SalesPerson lowest = sortedList[0];
            SalesPerson highest = sortedList[sortedList.length - 1];
    
            int median = (lowest.getSales() + highest.getSales()) / 2;
            
            System.out.printf("\n\t=======================================================%n");
            System.out.printf("\t%-25s %-20s  %-11s%n", "Statistics", "Sales person", "Amount");
            System.out.printf("\t-------------------------------------------------------%n");
            System.out.printf("\t%-25s %-20s  $%-10d%n",
                                "Lowest grosser",
                                lowest.getName(),
                                lowest.getSales());
            System.out.printf("\t%-25s %-20s  $%-10d%n",
                                "Highest grosser",
                                highest.getName(),
                                highest.getSales());
            System.out.printf("\t%-25s %-20s  $%-10d%n",
                                "Median",
                                "-",
                                median);
            System.out.printf("\t-------------------------------------------------------%n");
        } else {
            System.out.println("\n\tNothing to display\n");
        }
    }

    public void display() {
        display(masterList);
    }

    private void display(SalesPerson[] list) {
        if (!isListEmpty(list)) {
            System.out.println("\n\tSales and commission");
            System.out.println("\t===========================================");
            System.out.printf("\t%-20s %-11s %-11s %n", "Sales person", "Amount", "Commission");
            System.out.println("\t-------------------------------------------");
            for (int i = 0; i < list.length; ++i) {
                SalesPerson person = list[i];

                System.out.printf("\t%-20s $%-10d $%-10.1f %n",
                                    person.getName(),
                                    person.getSales(),
                                    person.getCommission());
           }
           System.out.println("\t-------------------------------------------");
           System.out.printf("\tTotal entries: %d %n", list.length);
           System.out.println("\t-------------------------------------------\n");
        } else {
            System.out.println("\n\tNothing to display\n");
        }
    }

    public boolean isValidName(String name) {
        /*
        boolean isAlpha = true;

        for (char c : name.toCharArray())
            if (!Character.isLetter(c) && !Character.isWhiteSpace(c))
                isAlpha = false;

        return isAlpha && name.indexOf(' ') == name.lastIndexOf(' ');
        */
        return Pattern.matches("[a-zA-Z]+[ ][a-zA-Z]+", name);
    }

    public boolean isValidSales(int sales) {
        return sales >= SALES_MIN && sales <= SALES_MAX;
    }

    public double calculateCommission(int salesAmount) {
        salesAmount *= SALES_UNIT;
        int level = (salesAmount - 1) / SLAB;

        return salesAmount * (PERCENT[level] / 100);
    }

    private static boolean isListEmpty(SalesPerson[] list) {
        if (list != null)
            if (list[0] != null)
                return false;
        return true;
    }
   
    public static void main(String[] args) {
        SalesDataManager manager = new SalesDataManager();
        int ans = -1;
        
        System.out.println("\n\tSales and commission data manager");
        System.out.println("\t---------------------------------");
        
        do {
            manager.displayMenu();

            while (true) {
                System.out.print("Enter an option [1-8]: ");
                String temp = in.nextLine().trim();
                if (!temp.isEmpty()) {
                    ans = Integer.parseInt(temp);
                    break;
                }
            }
            
            switch (ans) {
            case 1:
                manager.inputData();
                break;
            case 2:
                manager.display();
                break;
            case 3:
                manager.sortByName();
                manager.display(manager.sortedList);
                break;
            case 4:
                manager.sortBySales();
                manager.display(manager.sortedList);
                break;
            case 5:
                String name = null;
                while (true) {
                    System.out.print("Enter a name to search: ");
                    name = in.nextLine().trim();
                    
                    if (!name.isEmpty())
                        break;
                    else
                        System.out.println("\n\tName cannot be empty\n");
                }
                manager.searchByName(name);
                break;
            case 6:
                int sales = 0;
                while (true) {
                    System.out.print("Enter a sales amount: ");
                    String salesString = in.nextLine().trim();

                    if (!salesString.isEmpty()) {
                        sales = Integer.parseInt(salesString);
                        break;
                    } else {
                        System.out.println("\n\tSales cannot be empty\n");
                    }
                }
                manager.searchBySales(sales);
                break;
            case 7:
                manager.displayStatistics();
                break;
            case 8:
                System.out.println("\n\tThanks for using !!!\n");
                in.close();
                System.exit(0);
            default:
                System.out.println("\n\tInvalid option\n");
            }
        } while (true);
    }
}

class SalesPerson {

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
