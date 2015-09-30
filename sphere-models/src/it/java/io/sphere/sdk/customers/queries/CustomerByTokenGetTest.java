package io.sphere.sdk.customers.queries;

import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.customers.CustomerToken;
import io.sphere.sdk.customers.commands.CustomerCreatePasswordTokenCommand;
import io.sphere.sdk.test.IntegrationTest;
import org.junit.Test;

import static io.sphere.sdk.customers.CustomerFixtures.withCustomer;
import static org.assertj.core.api.Assertions.assertThat;


public class CustomerByTokenGetTest extends IntegrationTest {
    @Test
    public void execution() throws Exception {
        withCustomer(client(), customer -> {
            final CustomerToken token = execute(CustomerCreatePasswordTokenCommand.of(customer.getEmail()));
            final Customer fetchedCustomer = execute(CustomerByTokenGet.of(token));
            assertThat(fetchedCustomer.getId()).isEqualTo(customer.getId());
        });
    }
}