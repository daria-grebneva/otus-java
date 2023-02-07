package homework;


import java.util.*;

public class CustomerService {

    private final TreeMap<Customer, String> customerStringTreeMap = new TreeMap<>(Comparator.comparingLong(Customer::getScores));

    public Map.Entry<Customer, String> getSmallest() {
        return new AbstractMap.SimpleEntry<>(getClonedCustomer(customerStringTreeMap.firstKey()),
                customerStringTreeMap.firstEntry().getValue());
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        Customer tempCustomer = customerStringTreeMap.higherKey(customer);
        return (tempCustomer != null)
                ? new AbstractMap.SimpleEntry<>(getClonedCustomer(tempCustomer), customerStringTreeMap.higherEntry(customer).getValue())
                : null;
    }

    public void add(Customer customer, String data) {
        customerStringTreeMap.put(customer, data);
    }

    private Customer getClonedCustomer(Customer customer) {
        return new Customer(customer.getId(), customer.getName(), customer.getScores());
    }
}
