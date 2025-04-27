import java.util.*;
import java.io.*;
import java.text.*;
// Currency class - holds conversion rates and logic
class CurrencyConverter {
 private Map<String, Double> rates;
 private List<String> conversionHistory;
 public CurrencyConverter() {
 rates = new HashMap<>();
 conversionHistory = new ArrayList<>();
 // Base currency: USD
 rates.put("USD", 1.0);
 rates.put("EUR", 0.85);
 rates.put("INR", 83.20);
 rates.put("GBP", 0.74);
 rates.put("JPY", 155.5);
 rates.put("AUD", 1.52);
 rates.put("CAD", 1.35);
 rates.put("CHF", 0.91);
 rates.put("CNY", 7.24);
 rates.put("AED", 3.67);
 rates.put("SGD", 1.36);
 }
 public boolean isSupported(String currency) {
 return rates.containsKey(currency.toUpperCase());
 }
 public double convert(String fromCurrency, String toCurrency,
double amount) {
 fromCurrency = fromCurrency.toUpperCase();
 toCurrency = toCurrency.toUpperCase();
 if (!isSupported(fromCurrency) || !isSupported(toCurrency))
{
 throw new IllegalArgumentException("Unsupported
currency.");
 }
 double fromRate = rates.get(fromCurrency);
 double toRate = rates.get(toCurrency);
 double result = (amount / fromRate) * toRate;
 String timestamp = new SimpleDateFormat("yyyy-MM-dd
HH:mm:ss").format(new Date());
 conversionHistory.add(String.format("[%s] %.2f %s = %.2f
%s", timestamp, amount, fromCurrency, result, toCurrency));
 return result;
 }
 public Set<String> getSupportedCurrencies() {
 return rates.keySet();
 }
 public void updateRate(String currency, double rate) {
 rates.put(currency.toUpperCase(), rate);
 }
 public List<String> getConversionHistory() {
 return conversionHistory;
 }
 public void clearHistory() {
 conversionHistory.clear();
 }
 public void addCustomCurrency(String currency, double rate)
{
 rates.put(currency.toUpperCase(), rate);
 }
 public void convertToAllCurrencies(double amount, String
fromCurrency) {
 for (String toCurrency : rates.keySet()) {
 if (!toCurrency.equals(fromCurrency.toUpperCase())) {
 convert(fromCurrency, toCurrency, amount);
 }
 }
 }
 public void saveConversionHistoryToFile() {
 try (BufferedWriter writer = new BufferedWriter(new
FileWriter("conversion_history.txt", true))) {
 for (String record : conversionHistory) {
 writer.write(record + "\n");
 }
 System.out.println("Conversion history saved.");
 } catch (IOException e) {
 System.out.println("Error saving history: " +
e.getMessage());
 }
 }
 public void loadConversionHistoryFromFile() {
 try (BufferedReader reader = new BufferedReader(new
FileReader("conversion_history.txt"))) {
 String line;
 while ((line = reader.readLine()) != null) {
 conversionHistory.add(line);
 }
 } catch (IOException e) {
 System.out.println("Error loading history: " +
e.getMessage());
 }
 }
 public void showConversionStatistics() {
 Map<String, Integer> currencyCounts = new HashMap<>();
 for (String record : conversionHistory) {
 String[] parts = record.split(" ");
 String fromCurrency = parts[2];
 String toCurrency = parts[5];
 currencyCounts.put(fromCurrency,
currencyCounts.getOrDefault(fromCurrency, 0) + 1);
 currencyCounts.put(toCurrency,
currencyCounts.getOrDefault(toCurrency, 0) + 1);
 }
 System.out.println("Currency Conversion Statistics:");
 for (Map.Entry<String, Integer> entry :
currencyCounts.entrySet()) {
 System.out.println(entry.getKey() + ": " + entry.getValue()
+ " conversions");
 }
 }
}
// User Interface class - handles user interaction
class CurrencyConverterApp {
 private Scanner scanner;
 private CurrencyConverter converter;
 public CurrencyConverterApp() {
 scanner = new Scanner(System.in);
 converter = new CurrencyConverter();
 converter.loadConversionHistoryFromFile(); // Load history
from file on startup
 }
 public void run() {
 System.out.println("=== Welcome to the Advanced
Currency Converter ===");
 while (true) {
 System.out.println("\nMenu:");
 System.out.println("1. Convert Currency");
 System.out.println("2. Show Supported Currencies");
 System.out.println("3. Update Exchange Rate");
 System.out.println("4. View Conversion History");
 System.out.println("5. Clear Conversion History");
 System.out.println("6. Add Custom Currency");
 System.out.println("7. Convert to All Currencies");
 System.out.println("8. Save Conversion History to File");
 System.out.println("9. View Conversion Statistics");
 System.out.println("10. Exit");
 System.out.print("Choose an option: ");
 String choice = scanner.nextLine();
 switch (choice) {
 case "1":
 convertCurrency();
 break;
 case "2":
 showSupportedCurrencies();
 break;
 case "3":
 updateExchangeRate();
 break;
 case "4":
 viewConversionHistory();
 break;
 case "5":
 clearConversionHistory();
 break;
 case "6":
 addCustomCurrency();
 break;
 case "7":
 convertToAllCurrencies();
 break;
 case "8":
 saveConversionHistory();
 break;
 case "9":
 viewConversionStatistics();
 break;
 case "10":
 confirmExit();
 return;
 default:
 System.out.println("Invalid option. Please try
again.");
 }
 }
 }
 private void convertCurrency() {
 try {
 System.out.print("Enter amount: ");
 double amount =
Double.parseDouble(scanner.nextLine());
 System.out.print("From currency (e.g., USD): ");
 String from = scanner.nextLine().toUpperCase();
 System.out.print("To currency (e.g., INR): ");
 String to = scanner.nextLine().toUpperCase();
 if (!converter.isSupported(from) ||
!converter.isSupported(to)) {
 System.out.println("One or both currencies are not
supported.");
 return;
 }
 double result = converter.convert(from, to, amount);
 System.out.printf("%.2f %s = %.2f %s%n", amount,
from, result, to);
 } catch (NumberFormatException e) {
 System.out.println("Invalid amount. Please enter a
numeric value.");
 } catch (Exception e) {
 System.out.println("Error: " + e.getMessage());
 }
 }
 private void showSupportedCurrencies() {
 System.out.println("Supported currencies:");
 for (String currency : converter.getSupportedCurrencies()) {
 System.out.println("- " + currency);
 }
 }
 private void updateExchangeRate() {
 System.out.print("Enter currency code to update (e.g.,
USD): ");
 String currency = scanner.nextLine().toUpperCase();
 try {
 System.out.print("Enter new rate compared to USD: ");
 double newRate =
Double.parseDouble(scanner.nextLine());
 converter.updateRate(currency, newRate);
 System.out.println("Exchange rate for " + currency + "
updated to " + newRate);
 } catch (NumberFormatException e) {
 System.out.println("Invalid rate. Please enter a numeric
value.");
 }
 }
 private void viewConversionHistory() {
 List<String> history = converter.getConversionHistory();
 if (history.isEmpty()) {
 System.out.println("No conversions made yet.");
 } else {
 System.out.println("Conversion History:");
 for (String record : history) {
 System.out.println(record);
 }
 }
 }
 private void clearConversionHistory() {
 converter.clearHistory();
 System.out.println("Conversion history cleared.");
 }
 private void addCustomCurrency() {
 System.out.print("Enter custom currency code: ");
 String currency = scanner.nextLine().toUpperCase();
 System.out.print("Enter the exchange rate compared to
USD: ");
 double rate = Double.parseDouble(scanner.nextLine());
 converter.addCustomCurrency(currency, rate);
 System.out.println("Custom currency added: " + currency);
 }
 private void convertToAllCurrencies() {
 System.out.print("Enter amount: ");
 double amount = Double.parseDouble(scanner.nextLine());
 System.out.print("From currency (e.g., USD): ");
 String from = scanner.nextLine().toUpperCase();
 converter.convertToAllCurrencies(amount, from);
 System.out.println("Conversion completed for all
currencies.");
 }
 private void saveConversionHistory() {
 converter.saveConversionHistoryToFile();
 }
 private void viewConversionStatistics() {
 converter.showConversionStatistics();
 }
 private void confirmExit() {
 System.out.print("Are you sure you want to exit? (yes/no):
");
 String confirmation = scanner.nextLine().toLowerCase();
 if (confirmation.equals("yes")) {
 System.out.println("Goodbye!");
 } else {
 System.out.println("Continuing...");
 }
 }
}
// Main class
public class Main {
 public static void main(String[] args) {
 CurrencyConverterApp app = new
CurrencyConverterApp();
 app.run();
 }
} 
