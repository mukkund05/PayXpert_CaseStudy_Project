package com.hexaware.main;

import com.hexaware.dao.EmployeeService;
import com.hexaware.dao.FinancialRecordService;
import com.hexaware.dao.IPayrollService;
import com.hexaware.dao.IEmployeeService;
import com.hexaware.dao.IFinancialRecordService;
import com.hexaware.dao.ITaxService;
import com.hexaware.dao.PayrollService;
import com.hexaware.dao.ReportGenerator;
import com.hexaware.dao.TaxService;
import com.hexaware.entity.Employee;
import com.hexaware.entity.FinancialRecord;
import com.hexaware.entity.Payroll;
import com.hexaware.entity.Tax;
import com.hexaware.exception.*;

import java.sql.Date;
import java.util.List;
import java.util.Scanner;

public class MainModule {
    private static IEmployeeService employeeService;
    private static IPayrollService payrollService;
    private static ITaxService taxService;
    private static IFinancialRecordService financialRecordService;
    private static ReportGenerator reportGenerator;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        
        try {
            employeeService = new EmployeeService();
            payrollService = new PayrollService();
            taxService = new TaxService();
            financialRecordService = new FinancialRecordService();
            reportGenerator = new ReportGenerator();
        } catch (DatabaseConnectionException e) {
            System.err.println("Failed to initialize application: Database connection error - " + e.getMessage());
            System.exit(1);
        }

        while (true) {
            displayMenu();
            int choice = getUserChoice();

            try {
                switch (choice) {
                    case 1:
                        addEmployee();
                        break;
                    case 2:
                        viewEmployeeById();
                        break;
                    case 3:
                        viewAllEmployees();
                        break;
                    case 4:
                        updateEmployee();
                        break;
                    case 5:
                        removeEmployee();
                        break;
                    case 6:
                        generatePayroll();
                        break;
                    case 7:
                        viewPayrollById();
                        break;
                    case 8:
                        viewPayrollsForEmployee();
                        break;
                    case 9:
                        viewPayrollsForPeriod();
                        break;
                    case 10:
                        calculateTax();
                        break;
                    case 11:
                        viewTaxById();
                        break;
                    case 12:
                        viewTaxesForEmployee();
                        break;
                    case 13:
                        viewTaxesForYear();
                        break;
                    case 14:
                        addFinancialRecord();
                        break;
                    case 15:
                        viewFinancialRecordById();
                        break;
                    case 16:
                        viewFinancialRecordsForEmployee();
                        break;
                    case 17:
                        viewFinancialRecordsForDate();
                        break;
                    case 18:
                        generateIncomeStatement();
                        break;
                    case 19:
                        generateTaxSummary();
                        break;
                    case 20:
                        generateFinancialReport();
                        break;
                    case 21:
                        System.out.println("Exiting application. Goodbye!");
                        scanner.close();
                        System.exit(0);
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void displayMenu() {
        System.out.println("\n=== PayXpert Payroll Management System ===");
        System.out.println("1. Add Employee");
        System.out.println("2. View Employee by ID");
        System.out.println("3. View All Employees");
        System.out.println("4. Update Employee");
        System.out.println("5. Remove Employee");
        System.out.println("-----------------------------");
        System.out.println("6. Generate Payroll");
        System.out.println("7. View Payroll by ID");
        System.out.println("8. View Payrolls for Employee");
        System.out.println("9. View Payrolls for Period");
        System.out.println("-----------------------------");
        System.out.println("10. Calculate Tax");
        System.out.println("11. View Tax by ID");
        System.out.println("12. View Taxes for Employee");
        System.out.println("13. View Taxes for Year");
        System.out.println("-----------------------------");
        System.out.println("14. Add Financial Record");
        System.out.println("15. View Financial Record by ID");
        System.out.println("16. View Financial Records for Employee");
        System.out.println("17. View Financial Records for Date");
        System.out.println("-----------------------------");
        System.out.println("18. Generate Income Statement");
        System.out.println("19. Generate Tax Summary");
        System.out.println("20. Generate Financial Report");
        System.out.println("21. Exit");
        System.out.print("Enter your choice: ");
    }

    private static int getUserChoice() {
        int choice = scanner.nextInt();
        scanner.nextLine(); 
        return choice;
    }

    private static void addEmployee() throws InvalidInputException {
        System.out.print("Enter First Name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter Last Name: ");
        String lastName = scanner.nextLine();
        System.out.print("Enter Date of Birth (yyyy-mm-dd): ");
        Date dateOfBirth = Date.valueOf(scanner.nextLine());
        System.out.print("Enter Gender: ");
        String gender = scanner.nextLine();
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();
        System.out.print("Enter Phone Number: ");
        String phoneNumber = scanner.nextLine();
        System.out.print("Enter Address: ");
        String address = scanner.nextLine();
        System.out.print("Enter Position: ");
        String position = scanner.nextLine();
        System.out.print("Enter Joining Date (yyyy-mm-dd): ");
        Date joiningDate = Date.valueOf(scanner.nextLine());
        Date terminationDate = null;

        Employee employee = new Employee(0, firstName, lastName, dateOfBirth, gender, email, phoneNumber, address, position, joiningDate, terminationDate);
        boolean success = employeeService.addEmployee(employee);
        System.out.println(success ? "Employee added successfully!" : "Failed to add employee.");
    }

    private static void viewEmployeeById() throws EmployeeNotFoundException {
        System.out.print("Enter Employee ID: ");
        int employeeID = scanner.nextInt();
        scanner.nextLine();
        Employee employee = employeeService.getEmployeeById(employeeID);
        System.out.println("Employee: " + employee.getFirstName() + " " + employee.getLastName());
    }

    private static void viewAllEmployees() throws EmployeeNotFoundException {
        List<Employee> employees = employeeService.getAllEmployees();
        for (Employee e : employees) {
            System.out.println(e.getEmployeeID() + ": " + e.getFirstName() + " " + e.getLastName());
        }
    }

    private static void updateEmployee() throws EmployeeNotFoundException, InvalidInputException {
        System.out.print("Enter Employee ID to update: ");
        int employeeID = scanner.nextInt();
        scanner.nextLine();
        Employee employee = employeeService.getEmployeeById(employeeID);

       
        Employee updatedEmployee = new Employee(
            employee.getEmployeeID(),
            employee.getFirstName(),
            employee.getLastName(),
            employee.getDateOfBirth(),
            employee.getGender(),
            employee.getEmail(),
            employee.getPhoneNumber(),
            employee.getAddress(),
            employee.getPosition(),
            employee.getJoiningDate(),
            employee.getTerminationDate()
        );

        
        java.util.function.Function<String, String> getUpdatedValue = (currentValue) -> {
            System.out.print("Enter new " + currentValue.getClass().getSimpleName() + " (" + currentValue + "): ");
            String input = scanner.nextLine().trim();
            return input.isEmpty() ? currentValue : input;
        };

        
        String newFirstName = getUpdatedValue.apply(employee.getFirstName());
        String newLastName = getUpdatedValue.apply(employee.getLastName());
        Date newDateOfBirth = promptForDate("Date of Birth", employee.getDateOfBirth());
        String newGender = getUpdatedValue.apply(employee.getGender());
        String newEmail = getUpdatedValue.apply(employee.getEmail());
        String newPhoneNumber = getUpdatedValue.apply(employee.getPhoneNumber());
        String newAddress = getUpdatedValue.apply(employee.getAddress());
        String newPosition = getUpdatedValue.apply(employee.getPosition());

        
        if (!isValidEmail(newEmail)) {
            throw new InvalidInputException("Invalid email format");
        }
        if (!isValidPhoneNumber(newPhoneNumber)) {
            throw new InvalidInputException("Invalid phone number format (expecting 10 digits)");
        }

        
        updatedEmployee.setFirstName(newFirstName);
        updatedEmployee.setLastName(newLastName);
        updatedEmployee.setDateOfBirth(newDateOfBirth);
        updatedEmployee.setGender(newGender);
        updatedEmployee.setEmail(newEmail);
        updatedEmployee.setPhoneNumber(newPhoneNumber);
        updatedEmployee.setAddress(newAddress);
        updatedEmployee.setPosition(newPosition);

        
        boolean success = employeeService.updateEmployee(updatedEmployee);
        System.out.println(success ? "Employee updated successfully!" : "Failed to update employee.");
    }

    private static Date promptForDate(String fieldName, Date defaultDate) throws InvalidInputException {
        System.out.print("Enter new " + fieldName + " (" + defaultDate + "): ");
        String input = scanner.nextLine().trim();
        try {
            return input.isEmpty() ? defaultDate : Date.valueOf(input);
        } catch (IllegalArgumentException e) {
            throw new InvalidInputException("Invalid " + fieldName + " format. Use yyyy-mm-dd.");
        }
    }

    private static boolean isValidEmail(String email) {
        
        return email != null && email.contains("@") && email.contains(".");
    }

    private static boolean isValidPhoneNumber(String phoneNumber) {
        
        return phoneNumber != null && phoneNumber.matches("\\d{10}");
    }

    private static void removeEmployee() throws EmployeeNotFoundException {
        System.out.print("Enter Employee ID to remove: ");
        int employeeID = scanner.nextInt();
        scanner.nextLine();
        boolean success = employeeService.removeEmployee(employeeID);
        System.out.println(success ? "Employee removed successfully!" : "Failed to remove employee.");
    }

    private static void generatePayroll() throws PayrollGenerationException {
        System.out.print("Enter Employee ID: ");
        int employeeID = scanner.nextInt();
        scanner.nextLine();

        try {
            employeeService.getEmployeeById(employeeID);
        } catch (EmployeeNotFoundException e) {
            System.out.println("Error: Employee with ID " + employeeID + " does not exist. Please add the employee first.");
            return;
        }

        System.out.print("Enter Pay Period Start Date (yyyy-mm-dd): ");
        Date startDate = Date.valueOf(scanner.nextLine());
        System.out.print("Enter Pay Period End Date (yyyy-mm-dd): ");
        Date endDate = Date.valueOf(scanner.nextLine());
        boolean success = payrollService.generatePayroll(employeeID, startDate, endDate);
        System.out.println(success ? "Payroll generated successfully!" : "Failed to generate payroll.");
    }

    private static void viewPayrollById() throws PayrollGenerationException {
        System.out.print("Enter Payroll ID: ");
        int payrollID = scanner.nextInt();
        scanner.nextLine();
        Payroll payroll = payrollService.getPayrollById(payrollID);
        System.out.println("Payroll ID: " + payroll.getPayrollID() + ", Net Salary: " + payroll.getNetSalary());
    }

    private static void viewPayrollsForEmployee() throws PayrollGenerationException {
        System.out.print("Enter Employee ID: ");
        int employeeID = scanner.nextInt();
        scanner.nextLine();
        List<Payroll> payrolls = payrollService.getPayrollsForEmployee(employeeID);
        for (Payroll p : payrolls) {
            System.out.println("Payroll ID: " + p.getPayrollID() + ", Net Salary: " + p.getNetSalary());
        }
    }

    private static void viewPayrollsForPeriod() throws PayrollGenerationException {
        System.out.print("Enter Start Date (yyyy-mm-dd): ");
        Date startDate = Date.valueOf(scanner.nextLine());
        System.out.print("Enter End Date (yyyy-mm-dd): ");
        Date endDate = Date.valueOf(scanner.nextLine());
        List<Payroll> payrolls = payrollService.getPayrollsForPeriod(startDate, endDate);
        for (Payroll p : payrolls) {
            System.out.println("Payroll ID: " + p.getPayrollID() + ", Net Salary: " + p.getNetSalary());
        }
    }

    private static void calculateTax() throws TaxCalculationException {
        System.out.print("Enter Employee ID: ");
        int employeeID = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter Tax Year: ");
        int taxYear = scanner.nextInt();
        scanner.nextLine();
        double tax = taxService.calculateTax(employeeID, taxYear);
        System.out.println("Tax Amount: " + tax);
    }

    private static void viewTaxById() throws TaxCalculationException {
        System.out.print("Enter Tax ID: ");
        int taxID = scanner.nextInt();
        scanner.nextLine();
        Tax tax = taxService.getTaxById(taxID);
        System.out.println("Tax ID: " + tax.getTaxID() + ", Amount: " + tax.getTaxAmount());
    }

    private static void viewTaxesForEmployee() throws TaxCalculationException {
        System.out.print("Enter Employee ID: ");
        int employeeID = scanner.nextInt();
        scanner.nextLine();
        List<Tax> taxes = taxService.getTaxesForEmployee(employeeID);
        for (Tax t : taxes) {
            System.out.println("Tax ID: " + t.getTaxID() + ", Amount: " + t.getTaxAmount());
        }
    }

    private static void viewTaxesForYear() throws TaxCalculationException {
        System.out.print("Enter Tax Year: ");
        int taxYear = scanner.nextInt();
        scanner.nextLine();
        List<Tax> taxes = taxService.getTaxesForYear(taxYear);
        for (Tax t : taxes) {
            System.out.println("Tax ID: " + t.getTaxID() + ", Amount: " + t.getTaxAmount());
        }
    }

    private static void addFinancialRecord() throws FinancialRecordException {
        System.out.print("Enter Employee ID: ");
        int employeeID = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter Record Date (yyyy-mm-dd): ");
        Date recordDate = Date.valueOf(scanner.nextLine());
        System.out.print("Enter Description: ");
        String description = scanner.nextLine();
        System.out.print("Enter Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter Record Type: ");
        String recordType = scanner.nextLine();
        FinancialRecord record = new FinancialRecord(0, employeeID, recordDate, description, amount, recordType);
        boolean success = financialRecordService.addFinancialRecord(record);
        System.out.println(success ? "Financial record added successfully!" : "Failed to add financial record.");
    }

    private static void viewFinancialRecordById() throws FinancialRecordException {
        System.out.print("Enter Record ID: ");
        int recordID = scanner.nextInt();
        scanner.nextLine();
        FinancialRecord record = financialRecordService.getFinancialRecordById(recordID);
        System.out.println("Record ID: " + record.getRecordID() + ", Amount: " + record.getAmount());
    }

    private static void viewFinancialRecordsForEmployee() throws FinancialRecordException {
        System.out.print("Enter Employee ID: ");
        int employeeID = scanner.nextInt();
        scanner.nextLine();
        List<FinancialRecord> records = financialRecordService.getFinancialRecordsForEmployee(employeeID);
        for (FinancialRecord r : records) {
            System.out.println("Record ID: " + r.getRecordID() + ", Amount: " + r.getAmount());
        }
    }

    private static void viewFinancialRecordsForDate() throws FinancialRecordException {
        System.out.print("Enter Record Date (yyyy-mm-dd): ");
        Date recordDate = Date.valueOf(scanner.nextLine());
        List<FinancialRecord> records = financialRecordService.getFinancialRecordsForDate(recordDate);
        for (FinancialRecord r : records) {
            System.out.println("Record ID: " + r.getRecordID() + ", Amount: " + r.getAmount());
        }
    }

    private static void generateIncomeStatement() throws PayrollGenerationException {
        System.out.print("Enter Employee ID: ");
        int employeeID = scanner.nextInt();
        scanner.nextLine();
        List<Payroll> payrolls = payrollService.getPayrollsForEmployee(employeeID);
        System.out.println(reportGenerator.generateIncomeStatement(payrolls));
    }

    private static void generateTaxSummary() throws TaxCalculationException {
        System.out.print("Enter Employee ID: ");
        int employeeID = scanner.nextInt();
        scanner.nextLine();
        List<Tax> taxes = taxService.getTaxesForEmployee(employeeID);
        System.out.println(reportGenerator.generateTaxSummary(taxes));
    }

    private static void generateFinancialReport() throws FinancialRecordException {
        System.out.print("Enter Employee ID: ");
        int employeeID = scanner.nextInt();
        scanner.nextLine();
        List<FinancialRecord> records = financialRecordService.getFinancialRecordsForEmployee(employeeID);
        System.out.println(reportGenerator.generateFinancialReport(records));
    }
}