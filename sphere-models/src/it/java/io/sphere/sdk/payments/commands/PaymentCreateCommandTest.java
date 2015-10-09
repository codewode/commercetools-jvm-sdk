package io.sphere.sdk.payments.commands;

import io.sphere.sdk.customers.CustomerFixtures;
import io.sphere.sdk.payments.*;
import io.sphere.sdk.states.State;
import io.sphere.sdk.states.StateFixtures;
import io.sphere.sdk.states.StateType;
import io.sphere.sdk.test.IntegrationTest;
import org.junit.Test;

import javax.money.MonetaryAmount;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

import static io.sphere.sdk.customers.CustomerFixtures.withCustomer;
import static io.sphere.sdk.customers.CustomerFixtures.withUpdateableCustomer;
import static io.sphere.sdk.states.StateFixtures.withStateByBuilder;
import static io.sphere.sdk.states.StateType.PAYMENT_STATE;
import static org.assertj.core.api.Assertions.*;
import static io.sphere.sdk.test.SphereTestUtils.*;

public class PaymentCreateCommandTest extends IntegrationTest {
    @Test
    public void payingPerCreditCart() {
        //TODO cart with line item
        CustomerFixtures.withCustomerAndCart(client(), ((customer, cart) -> {
            final MonetaryAmount totalAmount = cart.getTotalPrice();
            final PaymentMethodInfo paymentMethodInfo = PaymentMethodInfoBuilder.of()
                    .paymentInterface("STRIPE")
                    .method("CREDIT_CARD")
                    .build();
            final PaymentDraftBuilder paymentDraftBuilder = PaymentDraftBuilder.of(totalAmount)
                    .customer(customer)
                    .paymentMethodInfo(paymentMethodInfo)
                    ;
            final Payment payment = execute(PaymentCreateCommand.of(paymentDraftBuilder.build()));

            assertThat(payment.getCustomer()).isEqualTo(payment.getCustomer());
            assertThat(payment.getPaymentMethodInfo()).isEqualTo(paymentMethodInfo);
            assertThat(payment.getAmountPlanned()).isEqualTo(totalAmount);

            execute(PaymentDeleteCommand.of(payment));
        }));
    }

    @Test
    public void invoice() {
        withUpdateableCustomer(client(), customer -> {
            withStateByBuilder(client(), stateBuilder -> stateBuilder.initial(true).type(PAYMENT_STATE), paidState -> {
                final MonetaryAmount totalAmount = EURO_30;
                final PaymentMethodInfo paymentMethodInfo = PaymentMethodInfoBuilder.of()
                        .method("INVOICE")
                        .name(en("invoice payment"))
                        .build();
                final PaymentStatus paymentStatus = PaymentStatusBuilder.of()
                        .interfaceCode("300")
                        .interfaceText("received invoice")
                        .state(paidState).build();
                final String interfaceId = randomKey();
                final String interactionId = randomKey();
                final List<Transaction> transactions = Collections.singletonList(TransactionBuilder
                        .of(TransactionType.CHARGE, totalAmount, ZonedDateTime.now())
                        .timestamp(ZonedDateTime.now())
                        .interactionId(interactionId)
                        .build());
                final PaymentDraftBuilder paymentDraftBuilder = PaymentDraftBuilder.of(totalAmount)
                        .interfaceId(interfaceId)
                        .amountPaid(totalAmount)
                        .customer(customer)
                        .paymentMethodInfo(paymentMethodInfo)
                        .paymentStatus(paymentStatus)
                        .transactions(transactions);
                final Payment payment = execute(PaymentCreateCommand.of(paymentDraftBuilder.build()));

                assertThat(payment.getCustomer()).isEqualTo(payment.getCustomer());
                assertThat(payment.getPaymentMethodInfo()).isEqualTo(paymentMethodInfo);
                assertThat(payment.getAmountPlanned()).isEqualTo(totalAmount);
                assertThat(payment.getAmountPaid()).isEqualTo(totalAmount);
                assertThat(payment.getPaymentStatus()).isEqualTo(paymentStatus);
                assertThat(payment.getInterfaceId()).isEqualTo(interfaceId);
                assertThat(payment.getTransactions()).isEqualTo(transactions);

                execute(PaymentDeleteCommand.of(payment));
            });
            return customer;
        });
    }

    @Test
    public void fullTest() {
        //TODO add more fields
        //TODO cart with line item
        CustomerFixtures.withCustomerAndCart(client(), ((customer, cart) -> {
            final MonetaryAmount totalAmount = cart.getTotalPrice();
            final PaymentMethodInfo paymentMethodInfo = PaymentMethodInfoBuilder.of()
                    .paymentInterface("STRIPE")
                    .method("CREDIT_CARD")
                    .build();
            final PaymentDraftBuilder paymentDraftBuilder = PaymentDraftBuilder.of(totalAmount)
                    .customer(customer)
                    .paymentMethodInfo(paymentMethodInfo)
                    ;
            final Payment payment = execute(PaymentCreateCommand.of(paymentDraftBuilder.build()));

            assertThat(payment.getCustomer()).isEqualTo(payment.getCustomer());
            assertThat(payment.getPaymentMethodInfo()).isEqualTo(paymentMethodInfo);
            assertThat(payment.getAmountPlanned()).isEqualTo(totalAmount);

            execute(PaymentDeleteCommand.of(payment));
        }));

    }

    //TODO test with state
}