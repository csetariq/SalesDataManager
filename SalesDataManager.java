import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Pattern;

public class SalesDataManager {
    
    private static final int        N           = 4;
    private static final char       NEWLINE     = '\n';
    
    /**
    * The commission level varies for every 25000
    */
    private static final int        SLAB        = 25000;
    
    /**
    * Sales are in G's
    */
    private static final int        SALES_UNIT  = 1000;
    private static final int        SALES_MIN   = 10;
    private static final int        SALES_MAX   = 100;
    
    /**
    * List of commission percentile in the order of sale unit of 25000
    *   
    *   0 - 25000       5%
    *   25001 - 50000   6%
    *   50001 - 75000   8%
    *   75001 - 100000  10%
    */
    private static final double[]   PERCENT     = { 5.0,
                                                    6.0,
                                                    8.0,
                                                    10.0};
    
    private static Scanner in = new Scanner(System.in);

    /**
    * Master list of sales person in the order of input
    */
    private String[]    masterNames;
    private int[]       masterSales;
    private double[]    masterCommissions;
    
    /**
    * Copy of Master list to be used while sorting
    *
    * These 3 arrays are considered as a single entity
    *
    * While sorting one array, all other arrays must also be sorted in parallel
    * based on the array being sorted
    */
    private String[]    copyNames;
    private int[]       copySales;
    private double[]    copyCommissions;

    public SalesDataManager() {
        masterNames         = new String[N];
        masterSales         = new int[N];
        masterCommissions   = new double[N];
        
        copyNames           = new String[N];
        copySales           = new int[N];
        copyCommissions     = new double[N];
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

            masterNames[i]          = name;
            masterSales[i]          = sales * SALES_UNIT;
            masterCommissions[i]    = commission;
        }
        // Copy the master array contents to copies
        System.arraycopy(masterNames, 0, copyNames, 0, masterNames.length);
        System.arraycopy(masterSales, 0, copySales, 0, masterSales.length);
        System.arraycopy(masterCommissions, 0, copyCommissions, 0, masterCommissions.length);
    }

    public void sortByName() {
        if (!isListEmpty(masterNames)) {
            // Insertion sort begins
            String  inHandName          = null;
            int     inHandSale          = 0;
            double  inHandCommission    = 0.0;
            
            int i = 0;
            for(int j = 1; j < copyNames.length; j++) {
                inHandName          = copyNames[j];
                inHandSale          = copySales[j];
                inHandCommission    = copyCommissions[j];
                
                /**
                * Compare the current person with previous person based on name (String)
                * 
                * Using 
                *       name.compareToIgnoreCase(anotherName)
                * Returns
                *       -1, if name lexicographically appears before anotherName
                *       +1, if name lexicographically appears after anotherName
                *       0,  if name and anotherName are equal
                */
                for(i = j; 
                    i > 0 && inHandName.compareToIgnoreCase(copyNames[i-1]) < 0;
                    i--) {
                    copyNames[i]        = copyNames[i-1];
                    copySales[i]        = copySales[i-1];
                    copyCommissions[i]  = copyCommissions[i-1];
                }
                copyNames[i]        = inHandName;
                copySales[i]        = inHandSale;
                copyCommissions[i]  = inHandCommission;
            }
            // Insertion sort ends
        }
    }

    public void sortBySales() {
        if (!isListEmpty(masterNames)) {
            // Insertion sort begins
            String  inHandName          = null;
            int     inHandSale          = 0;
            double  inHandCommission    = 0.0;
            
            int i = 0;
            for(int j = 1; j < copySales.length; j++) {
                inHandName          = copyNames[j];
                inHandSale          = copySales[j];
                inHandCommission    = copyCommissions[j];
                /**
                * Compare the current person with previous person based on sales
                */
                for(i = j; i > 0 && inHandSale < copySales[i-1]; i--) {
                    copyNames[i]        = copyNames[i-1];
                    copySales[i]        = copySales[i-1];
                    copyCommissions[i]  = copyCommissions[i-1];
                }
                copyNames[i]        = inHandName;
                copySales[i]        = inHandSale;
                copyCommissions[i]  = inHandCommission;
            }
            // Insertion sort ends
        }
    }

    public void searchByName(String name) {
        if (!isListEmpty(masterNames)) {
            /**
            * Linear Search
            *
            * Iterate through the list, an element is found, if there is a match before 
            * reaching the end of the array
            */
            for (int i = 0; i < masterNames.length; i++) {
    
                if (name.equalsIgnoreCase(masterNames[i])) {
                    System.out.println("\n\t--------------------------------------------------------------");
                    System.out.printf("\t%s - Sales amount: $%d; Commission: $%.1f %n",
                                        masterNames[i],
                                        masterSales[i],
                                        masterCommissions[i]);
                    System.out.println("\t--------------------------------------------------------------\n");
                    return;
                }
            }
            // If a match was found, the control would have returned to caller
            // If the control reaches here, then no match found
            System.out.println("\n\tNo match found\n");
        } else {
            System.out.println("\n\tNothing to search\n");
        }
    }

    public void searchBySales(int sales) {
        if (!isListEmpty(masterNames)) {
            // flag to see if atleast one record was found
            boolean foundRecords = false;
            sortBySales();
            /**
            *   copySales is now sorted
            *   
            *   Linear Search
            *   -------------
            *   Iterate through the sorted list and print the details until,
            *   we find a entry which has sales greater than the given sales
            */
            sales *= SALES_UNIT;
            System.out.printf("\n\tThe following sales person has less than $%d %n%n", sales);
            for (int i = 0; i < copySales.length; i++) {
    
                if (copySales[i] < sales) {
                    foundRecords = true;
                    System.out.printf("\t%-20s $%-6d %n",
                                        copyNames[i],
                                        copySales[i]);
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
        if (!isListEmpty(masterNames)) {
            sortBySales();
            /**
            *   copySales is now sorted based on sales
            *   
            *   First element will have have the lowest sales
            *   Last element will have the highest sales
            */
    
            int median = (copySales[0] + copySales[copySales.length - 1]) / 2;
            
            System.out.printf("\n\t=======================================================%n");
            System.out.printf("\t%-25s %-20s  %-11s%n", "Statistics", "Sales person", "Amount");
            System.out.printf("\t-------------------------------------------------------%n");
            System.out.printf("\t%-25s %-20s  $%-10d%n",
                                "Lowest grosser",
                                copyNames[0],
                                copySales[0]);
            System.out.printf("\t%-25s %-20s  $%-10d%n",
                                "Highest grosser",
                                copyNames[copyNames.length - 1],
                                copySales[copySales.length - 1]);
            System.out.printf("\t%-25s %-20s  $%-10d%n",
                                "Median",
                                "-",
                                median);
            System.out.printf("\t-------------------------------------------------------%n");
        } else {
            System.out.println("\n\tNothing to display\n");
        }
    }

    public void displayMasterList() {
        display(masterNames, masterSales, masterCommissions);
    }
    
    public void displayWorkingList() {
        display(copyNames, copySales, copyCommissions);
    }

    private void display(String[] names, int[] sales, double[] commissions) {
        if (!isListEmpty(names)) {
            System.out.println("\n\tSales and commission");
            System.out.println("\t===========================================");
            System.out.printf("\t%-20s %-11s %-11s %n", "Sales person", "Amount", "Commission");
            System.out.println("\t-------------------------------------------");
            for (int i = 0; i < names.length; ++i) {

                System.out.printf("\t%-20s $%-10d $%-10.1f %n",
                                    names[i],
                                    sales[i],
                                    commissions[i]);
           }
           System.out.println("\t-------------------------------------------");
           System.out.printf("\tTotal entries: %d %n", names.length);
           System.out.println("\t-------------------------------------------\n");
        } else {
            System.out.println("\n\tNothing to display\n");
        }
    }

    public boolean isValidName(String name) {
        /*
        boolean isAllowed = true;

        for (char c : name.toCharArray())
            if (!Character.isLetter(c) && !Character.isWhiteSpace(c))
                isAllowed = false;

        return isAllowed && name.indexOf(' ') >= 0 && name.indexOf(' ') == name.lastIndexOf(' ');
        */
        
        /**
        *   Name should meet the following requirements
        *   
        *       i)      Should contain First name and Last name
        *       ii)     First name and Last name should be separated by a space
        *       iii)    First name and Last name can only have alphabetic characters
        */
        return Pattern.matches("[a-zA-Z]+[ ][a-zA-Z]+", name);
    }

    public boolean isValidSales(int sales) {
        return sales >= SALES_MIN && sales <= SALES_MAX;
    }

    public double calculateCommission(int salesAmount) {
        salesAmount *= SALES_UNIT;
        
        /**
        *   Find the level of salesAmount on a SLAB of 25000
        *   
        *       Sales Amount        Level
        *       ------------        -----
        *       0 - 25000           0
        *       25001 - 50000       1
        *       50001 - 75000       2
        *       75001 - 100000      3
        *   
        *   this can be achieved by simply dividing the salesAmount by SLAB
        *
        *   With the calculated Level, the percentile can be fetched from PERCENT array
        */
        int level = (salesAmount - 1) / SLAB;

        return salesAmount * (PERCENT[level] / 100);
    }

    private static boolean isListEmpty(String[] list) {
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
                manager.displayMasterList();
                break;
            case 3:
                manager.sortByName();
                manager.displayWorkingList();
                break;
            case 4:
                manager.sortBySales();
                manager.displayWorkingList();
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
