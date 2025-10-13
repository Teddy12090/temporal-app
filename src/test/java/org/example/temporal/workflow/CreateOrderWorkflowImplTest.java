package org.example.temporal.workflow;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.testing.TestWorkflowEnvironment;
import io.temporal.testing.TestWorkflowExtension;
import io.temporal.worker.Worker;
import org.example.temporal.activity.CreateOrderActivities;
import org.example.temporal.activity.CreateOrderActivitiesImpl;
import org.example.temporal.model.CreateOrderRequest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.InOrder;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(OutputCaptureExtension.class)
@Timeout(value = 3, unit = TimeUnit.SECONDS)
class CreateOrderWorkflowImplTest {
    @Nested
    class Environment {
        private TestWorkflowEnvironment testEnv;
        private WorkflowClient client;

        @BeforeEach
        void setUp() {
            testEnv = TestWorkflowEnvironment.newInstance();
            Worker worker = testEnv.newWorker("order");
            worker.registerWorkflowImplementationTypes(CreateOrderWorkflowImpl.class);
            worker.registerActivitiesImplementations(new CreateOrderActivitiesImpl());

            client = testEnv.getWorkflowClient();
        }

        @AfterEach
        void tearDown() {
            testEnv.close();
        }

        @Test
        void createOrder(CapturedOutput output) {
            testEnv.start();
            CreateOrderWorkflow workflow = client.newWorkflowStub(
                    CreateOrderWorkflow.class,
                    WorkflowOptions.newBuilder().setTaskQueue("order").build()
            );

            // when
            String orderId = workflow.createOrder(CreateOrderRequest
                    .builder()
                    .customerId("richer")
                    .productId("i17")
                    .quantity(1)
                    .build());

            // then
            assertThat(orderId).matches("ORD-\\d+");
            assertThat(output.getOut()).containsSubsequence(
                    "creatingOrder", "authorizePayment", "reserveInventory", "confirmOrder", "sendConfirmation");
        }
    }

    @Nested
    class Extension {

        @RegisterExtension
        private static final TestWorkflowExtension extension = TestWorkflowExtension.newBuilder()
                .registerWorkflowImplementationTypes(CreateOrderWorkflowImpl.class)
                .setActivityImplementations(new CreateOrderActivitiesImpl())
                .build();

        @Test
        void createOrder(CreateOrderWorkflow workflow, CapturedOutput output) {
            String orderId = workflow.createOrder(CreateOrderRequest
                    .builder()
                    .customerId("richer")
                    .productId("i17")
                    .quantity(1)
                    .build());

            assertThat(orderId).matches("ORD-\\d+");
            assertThat(output.getOut()).containsSubsequence(
                    "creatingOrder", "authorizePayment", "reserveInventory", "confirmOrder", "sendConfirmation");
        }
    }

    @Nested
    class Mock {
        @RegisterExtension
        private static final TestWorkflowExtension extension = TestWorkflowExtension.newBuilder()
                .registerWorkflowImplementationTypes(CreateOrderWorkflowImpl.class)
                .setDoNotStart(true)
                .build();

        @Test
        void createOrder(TestWorkflowEnvironment testEnv, Worker worker, CreateOrderWorkflow workflow, CapturedOutput output) {
            CreateOrderActivities activity = mock(CreateOrderActivities.class, withSettings().withoutAnnotations());
            worker.registerActivitiesImplementations(activity);
            testEnv.start();

            CreateOrderRequest request = CreateOrderRequest
                    .builder()
                    .customerId("richer")
                    .productId("i17")
                    .quantity(1)
                    .build();
            String orderId = workflow.createOrder(request);

            assertThat(orderId).matches("ORD-\\d+");
            InOrder inOrder = inOrder(activity);
            inOrder.verify(activity, times(1)).authorizePayment(request);
            inOrder.verify(activity, times(1)).reserveInventory(request);
            inOrder.verify(activity, times(1)).confirmOrder(request);
            inOrder.verify(activity, times(1)).sendConfirmation(request);
            verifyNoMoreInteractions(activity);
        }
    }
}
