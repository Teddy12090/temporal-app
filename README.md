Temporal Web UI: http://localhost:8233/

```shell
docker run --rm temporalio/temporal workflow start \
--type FilterWorkflow \
--task-queue filter \
--address host.docker.internal:7233 \
--input '{"data1": "a", "data2": null, "data3": 3}'
```
