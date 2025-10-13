package org.example.temporal.activity;

import io.temporal.testing.TestActivityEnvironment;
import io.temporal.testing.TestActivityExtension;
import org.example.temporal.model.CreateOrderRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(OutputCaptureExtension.class)
class CreateOrderActivitiesImplTest {

    @Nested
    class Environment {

        private TestActivityEnvironment testEnv;
        private CreateOrderActivities activity;

        @BeforeEach
        void setUp() {
            testEnv = TestActivityEnvironment.newInstance();
            testEnv.registerActivitiesImplementations(new CreateOrderActivitiesImpl());
            activity = testEnv.newActivityStub(CreateOrderActivities.class);
        }


        @Test
        void authorizePayment(CapturedOutput output) {
            activity.authorizePayment(CreateOrderRequest.builder().build());

            assertThat(output.getOut()).contains("authorizePayment");
        }

        @Test
        void reserveInventory(CapturedOutput output) {
            activity.reserveInventory(CreateOrderRequest.builder().build());

            assertThat(output.getOut()).contains("reserveInventory");
        }

        @Test
        void confirmOrder(CapturedOutput output) {
            activity.confirmOrder(CreateOrderRequest.builder().build());

            assertThat(output.getOut()).contains("confirmOrder");
        }

        @Test
        void sendConfirmation(CapturedOutput output) {
            activity.sendConfirmation(CreateOrderRequest.builder().build());

            assertThat(output.getOut()).contains("sendConfirmation");
        }
    }

    @Nested
    class Extension {
        @RegisterExtension
        private TestActivityExtension extension = TestActivityExtension
                .newBuilder()
                .setActivityImplementations(new CreateOrderActivitiesImpl())
                .build();

        @Test
        void authorizePayment(CreateOrderActivities activity, CapturedOutput output) {
            activity.authorizePayment(CreateOrderRequest.builder().build());

            assertThat(output.getOut()).contains("authorizePayment");
        }

        @Test
        void reserveInventory(CreateOrderActivities activity, CapturedOutput output) {
            activity.reserveInventory(CreateOrderRequest.builder().build());

            assertThat(output.getOut()).contains("reserveInventory");
        }

        @Test
        void confirmOrder(CreateOrderActivities activity, CapturedOutput output) {
            activity.confirmOrder(CreateOrderRequest.builder().build());

            assertThat(output.getOut()).contains("confirmOrder");
        }

        @Test
        void sendConfirmation(CreateOrderActivities activity, CapturedOutput output) {
            activity.sendConfirmation(CreateOrderRequest.builder().build());

            assertThat(output.getOut()).contains("sendConfirmation");
        }
    }
}
