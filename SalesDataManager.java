import java.util.*;
import java.util.regex.*;

public class SalesDataManager {
    
    private static final int        N       = 4;
    private static final char       NEWLINE = '\n';
    private static final int        SLAB    = 25000;
    private static final double[]   PERCENT = { 5.0,
                                                6.0,
                                                8.0,
                                                10.0};

    private SalesPerson[] masterList;
    private SalesPerson[] nameSorted;
    private SalesPerson[] salesSorted;

    public SalesDataManager() {
        masterList = new SalesPerson[N];
    }

    public void displayMenu() {
        System.out.println(
            "1. Input and Validate data"    + NEWLINE +
            "2. Display"                    + NEWLINE +
            "3. Sort by name"               + NEWLINE +
            "4. Sort by sales"              + NEWLINE +
            "5. Search by name"             + NEWLINE +
            "6. Search by sales"            + NEWLINE +
            "7. Display statistics"         + NEWLINE +
            "8. Exit"
        );
    }

    public void inputData() {
        Scanner in = new Scanner(System.in);
        
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
                    System.out.println("Invalid name");
            }
            
            while (true) {
                System.out.printf("Enter sales %d: ", i + 1);
                sales = Integer.parseInt(in.nextLine().trim());
                
                if (isValidSales(sales)) { 
                    sales *= 1000;
                    break;
                } else {
                    System.out.println("Invalid sales. Must be within [10 - 100]");
                }
            }

            commission = calculateCommission(sales);

            SalesPerson currentPerson = new SalesPerson(name, sales, commission);
            masterList[i] = currentPerson;
        }
    }

    public void sortByName() {
        if (masterList[0] != null) {
            nameSorted = Arrays.copyOf(masterList, masterList.length);
            SalesPerson temp = null;
            int i = 0;
    		for(int j = 1; j < nameSorted.length; j++) {
    			temp = nameSorted[j];
    			for(i = j; 
                    i > 0 && temp.getName().compareTo(nameSorted[i-1].getName()) < 0;
                    i--) {
    				nameSorted[i] = nameSorted[i-1];
                }
    			nameSorted[i] = temp;
    		}
            display(nameSorted);
        }
    }

    public void sortBySales() {
        if (masterList[0] != null) {
            salesSorted = Arrays.copyOf(masterList, masterList.length);
            SalesPerson temp = null;
            int i = 0;
    		for(int j = 1; j < salesSorted.length; j++) {
    			temp = salesSorted[j];
    			for(i = j; i > 0 && temp.getSales() < salesSorted[i-1].getSales(); i--)
    				salesSorted[i] = salesSorted[i-1];
    			salesSorted[i] = temp;
    		}
            display(salesSorted);
        }
    }

    public void searchByName(String name) {
        for (int i = 0; i < masterList.length; i++) {
            SalesPerson person = masterList[i];

            if (name.equalsIgnoreCase(person.getName())) {
                System.out.printf("%s - Sales amount: $%d; Commission: $%.1f %n",
                                    person.getName(),
                                    person.getSales(),
                                    person.getCommission());
                return;
            }
        }
        System.out.println("No match found");
    }

    public void searchBySales(int sales) {
        sales = sales * 1000;
        System.out.printf("The following sales person has less than $%d %n%n", sales);
        for (int i = 0; i < salesSorted.length; i++) {
            SalesPerson person = salesSorted[i];

            if (person.getSales() < sales) {
                System.out.printf("%20s $%-6d %n",
                                    person.getName(),
                                    person.getSales());
            } else {
                return;
            }
        }
        System.out.println("No records found");
    }

    public void displayStatistics() {
        SalesPerson lowest = salesSorted[0];
        SalesPerson highest = salesSorted[salesSorted.length - 1];

        int median = (lowest.getSales() + highest.getSales()) / 2;

        System.out.printf("Sales person with lowest sales amount is %s, $%d%n", 
                            lowest.getName(),
                            lowest.getSales());
        System.out.printf("Sales person with highest sales amount is %s, $%d%n", 
                            highest.getName(),
                            highest.getSales());
        System.out.printf("The median sales amount is $%d%n", median);
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
        return sales >= 10 && sales <= 100;
    }

    public double calculateCommission(int salesAmount) {
        int level = (salesAmount - 1) / SLAB;

        return salesAmount * (PERCENT[level] / 100);
    }

    public void display() {
        display(masterList);
    }

    private void display(SalesPerson[] list) {
       for (int i = 0; i < list.length; ++i) {
            SalesPerson person = list[i];

            System.out.printf("%-20s $%-10d $%-.1f %n",
                                person.getName(),
                                person.getSales(),
                                person.getCommission());
       }
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        SalesDataManager manager = new SalesDataManager();
        int ans = -1;
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
                break;
            case 4:
                manager.sortBySales();
                break;
            case 5:
                String name = null;
                while (true) {
                    System.out.print("Enter a name to search: ");
                    name = in.nextLine().trim();

                    if (!name.isEmpty())
                        break;
                    else
                        System.out.println("Name cannot be empty!");
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
                        System.out.println("Sales cannot be empty!");
                    }
                }
                manager.searchBySales(sales);
                break;
            case 7:
                manager.displayStatistics();
                break;
            case 8:
                System.exit(0);
            default:
                System.out.printf("Invalid option!");
            }
        } while (true);
    }
}
