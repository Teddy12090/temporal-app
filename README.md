Temporal Web UI: http://localhost:8233/

```shell
docker run --rm temporalio/temporal workflow start \
--type CreateOrderWorkflow \
--task-queue order \
--address host.docker.internal:7233 \
--input '{"customerId": "123", "productId": "A123", "quantity": 3}'
```

```shell
docker run --rm temporalio/temporal workflow show \
--address host.docker.internal:7233 \
--workflow-id "1ead8b19-3282-4679-ac5a-7e6d4d9d9c71"
```
