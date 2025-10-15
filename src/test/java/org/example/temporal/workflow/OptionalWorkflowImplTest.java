package org.example.temporal.workflow;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.client.WorkflowStub;
import io.temporal.testing.TestWorkflowExtension;
import io.temporal.worker.Worker;
import org.example.temporal.model.FooRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(OutputCaptureExtension.class)
@Timeout(value = 5, unit = TimeUnit.SECONDS)
class OptionalWorkflowImplTest {

    private static ObjectMapper mapper = new ObjectMapper();

    @RegisterExtension
    private final TestWorkflowExtension extension = TestWorkflowExtension.newBuilder().registerWorkflowImplementationTypes(OptionalWorkflowImpl.class).build();

    @Test
    void allFields(OptionalWorkflow workflow, CapturedOutput capturedOutput) throws Exception {
        // given
        FooRequest request = FooRequest.builder()
                .id(1)
                .name("Alice")
                .email("alice@example.com") // not null
                .phone("0912345678") // not null
                .address(FooRequest.Address // not null
                        .builder()
                        .street("123 Main St")
                        .city("Taipei")
                        .postalCode("100") // not null
                        .build()).build();

        // when
        workflow.foo(request);

        // then
        requestLogShouldBe(capturedOutput, "FooRequest(id=1, name=Alice, email=Optional[alice@example.com], phone=Optional[0912345678], address=Optional[FooRequest.Address(street=123 Main St, city=Taipei, postalCode=Optional[100])])");
        assertThat(mapper.writeValueAsString(request)).isEqualTo("{\"id\":1,\"name\":\"Alice\",\"email\":\"alice@example.com\",\"phone\":\"0912345678\",\"address\":{\"street\":\"123 Main St\",\"city\":\"Taipei\",\"postalCode\":\"100\"}}");
    }

    @Test
    void nullAddress(OptionalWorkflow workflow, CapturedOutput capturedOutput) throws Exception {
        // given (address: null)
        FooRequest request = FooRequest.builder().id(1).name("Alice").email("alice@example.com").phone("0912345678").address(null).build();

        // when
        workflow.foo(request);

        // then
        requestLogShouldBe(capturedOutput, "FooRequest(id=1, name=Alice, email=Optional[alice@example.com], phone=Optional[0912345678], address=Optional.empty)");
        assertThat(mapper.writeValueAsString(request)).isEqualTo("{\"id\":1,\"name\":\"Alice\",\"email\":\"alice@example.com\",\"phone\":\"0912345678\",\"address\":null}");
    }

    @Test
    void partialNull(OptionalWorkflow workflow, CapturedOutput capturedOutput) throws Exception {
        // given (email: null, phone: null, postalCode: null)
        FooRequest request = FooRequest.builder().id(1).name("Alice").email(null).phone(null).address(FooRequest.Address.builder().street("123 Main St").city("Taipei").postalCode(null).build()).build();

        // when
        workflow.foo(request);

        // then
        requestLogShouldBe(capturedOutput, "FooRequest(id=1, name=Alice, email=Optional.empty, phone=Optional.empty, address=Optional[FooRequest.Address(street=123 Main St, city=Taipei, postalCode=Optional.empty)])");
        assertThat(mapper.writeValueAsString(request)).isEqualTo("{\"id\":1,\"name\":\"Alice\",\"email\":null,\"phone\":null,\"address\":{\"street\":\"123 Main St\",\"city\":\"Taipei\",\"postalCode\":null}}");
    }

    @Test
    void partialNullUsingJsonString(WorkflowClient workflowClient, Worker worker, CapturedOutput capturedOutput) throws Exception {
        // given (email: null, phone: null, postalCode: null)
        Object request = mapper.readValue("{\"id\":1,\"name\":\"Alice\",\"email\":null,\"phone\":null,\"address\":{\"street\":\"123 Main St\",\"city\":\"Taipei\",\"postalCode\":null}}", Map.class);
        WorkflowStub workflowStub = workflowClient.newUntypedWorkflowStub(OptionalWorkflow.class.getSimpleName(), WorkflowOptions.newBuilder().setTaskQueue(worker.getTaskQueue()).build());

        // when
        workflowStub.start(request);

        // then
        assertThat(workflowStub.getResult(Object.class)).isNotNull();
        requestLogShouldBe(capturedOutput, "FooRequest(id=1, name=Alice, email=Optional.empty, phone=Optional.empty, address=Optional[FooRequest.Address(street=123 Main St, city=Taipei, postalCode=Optional.empty)])");
        assertThat(mapper.writeValueAsString(request)).isEqualTo("{\"id\":1,\"name\":\"Alice\",\"email\":null,\"phone\":null,\"address\":{\"street\":\"123 Main St\",\"city\":\"Taipei\",\"postalCode\":null}}");
    }

    @Test
    void setNonNullFieldToNull(WorkflowClient workflowClient, Worker worker, CapturedOutput capturedOutput) throws Exception {
        // given
        Object request = mapper.readValue("{\"id\":1,\"name\":null,\"email\":\"alice@example.com\",\"phone\":\"0912345678\",\"address\":{\"street\":\"123 Main St\",\"city\":\"Taipei\",\"postalCode\":\"100\"}}", Map.class);
        WorkflowStub workflowStub = workflowClient.newUntypedWorkflowStub(OptionalWorkflow.class.getSimpleName(), WorkflowOptions.newBuilder().setTaskQueue(worker.getTaskQueue()).build());

        // when
        workflowStub.start(request);

        // then
        assertThat(workflowStub.getResult(Object.class)).isNotNull();
        Optional<String> requestLog = extractRequestLog(capturedOutput.getOut());
        assertThat(requestLog.orElseThrow()).doesNotContain("name=null"); // failed
        /**
         * java.lang.AssertionError:
         * Expecting actual:
         *   "FooRequest(id=1, name=null, email=Optional[alice@example.com], phone=Optional[0912345678], address=Optional[FooRequest.Address(street=123 Main St, city=Taipei, postalCode=Optional[100])])"
         * not to contain:
         *   "name=null"
         * */
    }

    private void requestLogShouldBe(CapturedOutput capturedOutput, String expected) {
        Optional<String> requestLog = extractRequestLog(capturedOutput.getOut());
        assertThat(requestLog.orElseThrow()).isEqualTo(expected);
    }

    private Optional<String> extractRequestLog(String log) {
        Matcher matcher = Pattern.compile("FooRequest\\(.*$").matcher(log);
        if (matcher.find())
            return Optional.ofNullable(matcher.group());
        return Optional.empty();
    }
}
